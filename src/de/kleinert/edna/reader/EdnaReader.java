package de.kleinert.edna.reader;

import de.kleinert.edna.EdnOptions;
import de.kleinert.edna.data.Char32;
import de.kleinert.edna.data.EdnCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EdnaReader {
    private final @NotNull EdnOptions options;
    private final @NotNull CodePointIterator cpi;

    private EdnaReader(final @NotNull EdnOptions options,
                       final @NotNull CodePointIterator cpi) {
        this.options = options;
        this.cpi = cpi;
        ensureValidDecoderNames();
    }

    private void ensureValidDecoderNames() {
        // TODO
    }

    private final @NotNull Object NOTHING = new Object();

    static <T> T read(final @NotNull CodePointIterator cpi,
                      final @NotNull EdnOptions options,
                      final @NotNull Class<T> castClass) {
        return castClass.cast(new EdnaReader(options, cpi).readString());
    }

    private @Nullable Object readString() {
        var data = (List<?>) readForm(0, false);
        if (data.size() != 1)
            throw new EdnReaderException(cpi.getLineIdx(), cpi.getTextIndex(), "The input should only contain one expression, but there are none or multiple.");
        return data.getFirst();
    }


    private Object readForm(final int level, final boolean stopAfterOne) {
        @NotNull List<@Nullable Object> res = new ArrayList<>();
        int linePos;
        int codePosIndex;

        do {
            cpi.skipWhile(this::isWhitespace);

            if (!cpi.hasNext())
                break;

            linePos = cpi.getLineIdx();
            codePosIndex = cpi.getTextIndex();

            res.remove(NOTHING);

            int codePoint = cpi.nextInt();
            switch (codePoint) {
                case ';' -> {
                    readComment();
                    continue;
                }
                case '"' -> res.add(readEdnString());
                case '(' -> res.add(readList(level + 1));
                case '[' -> res.add(readVector(level + 1));
                case '{' -> res.add(readMap(level + 1));
                case '\\' -> res.add(readChar());
                case '#' -> {
                    if (cpi.hasNext() && cpi.peek() == '_') {
                        cpi.nextInt();

                        Object temp;
                        do {
                            temp = readForm(level + 1, true);
                        } while (temp == NOTHING);
                        if (stopAfterOne) return NOTHING;
                    } else if (cpi.hasNext() && options.isAllowMetaData() && cpi.peek() == '^') {
                        cpi.nextInt();
                        res.add(readMeta(level));
                    } else {
                        res.add(readDispatch(level + 1));
                    }
                }
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    cpi.unread(codePoint);
                    res.add(readNumber());
                }
                case ')', ']', '}' -> {
                    if (level == 0)
                        throw new EdnReaderException(linePos, codePosIndex, "Unexpected character " + (char) codePoint);
                    cpi.unread(codePoint);
                    return NOTHING;
                }
                case '+', '-' -> {
                    boolean isNumber = false;
                    if (options.isAllowMetaData()) {
                        int peeked = cpi.peek();
                        isNumber = peeked >= '0' && peeked <= '9';
                    }
                    cpi.unread(codePoint);
                    res.add(isNumber ? readNumber() : readOther());
                }
                case '^' -> {
                    if (options.isAllowMetaData()) {
                        res.add(readMeta(level));
                    } else {
                        cpi.unread(codePoint);
                        res.add(readOther());
                    }
                }
            }

            if (stopAfterOne && !res.isEmpty()) {
                return res.getFirst();
            }
        } while (true);
        return res;
    }

    private @Nullable Object readOther() {
        return null; // TODO
    }

    private @Nullable Object readDispatch(final int level) {
        return null; // TODO
    }

    private @NotNull EdnCollections.IObj readMeta(final int level) {
        return new EdnCollections.IObj.Wrapper<>(Map.of(), null); // TODO
    }

    private @NotNull Number readNumber() {
        return 0; // TODO
    }

    private @Nullable Object readVector(final int level) {
        var it = List.of(); // TODO
        return options.getListToEdnVectorConverter().apply(it);
    }

    private @NotNull Character readChar() {
        return (char) -1; // TODO
    }

    private @NotNull Map<?, ?> readMap(final int level) {
        var it = List.<Map.Entry<Object, Object>>of(); // TODO
        return options.getMapToEdnMapConverter().apply(it);
    }

    private @Nullable List<?> readList(final int level) {
        var it = List.of(); // TODO
        return options.getListToEdnListConverter().apply(it);
    }

    private @NotNull String readEdnString() {
        var currentToken = new StringBuilder();
        var linePos = cpi.getLineIdx();
        var codePosIndex = cpi.getTextIndex();
        while (cpi.hasNext()) {
            var codePoint = cpi.nextInt();
            if (codePoint == '"') {
                // Closing the string;
                return currentToken.toString();
            } else if (codePoint == '\\') { // Escape-sequence or Unicode char.
                if (!cpi.hasNext())
                    break;
                var escapeCodePt = cpi.nextInt();
                int temp = switch (escapeCodePt) {
                    case 't' -> '\t';
                    case 'b' -> '\b';
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case '"' -> '\"';
                    case '\\' -> '\\';
                    case 'u' -> readUnicodeChar(4, 4, 'u').toChar(); // UTF-16 code
                    case 'x' -> readUnicodeChar(8, 8, 'x').code(); // UTF-32 code
                    default -> {
                        var message = "Invalid escape sequence: \\${escapeCodePt.toChar()} in string $currentToken";
                        throw new EdnReaderException(cpi.getLineIdx(), cpi.getTextIndex(), message);
                    }
                };
                currentToken.appendCodePoint(temp);
            } else { // Just a codepoint.
                currentToken.appendCodePoint(codePoint);
            }
        }
        throw new EdnReaderException(linePos, codePosIndex, "Unclosed String literal " + currentToken + ".");
    }

    private @NotNull Char32 readUnicodeChar(final int minLength, final int maxLength, final char initChar) {
        var linePos = cpi.getLineIdx();
        var codePosIndex = cpi.getTextIndex();
        StringBuilder token = cpi.takeCodePoints(new StringBuilder(), maxLength, this::isHexNum);
        if (token.length() < minLength && token.length() > maxLength)
            throw new EdnReaderException(linePos, codePosIndex, "Invalid unicode sequence \\" + initChar + token);
        return readUnicodeChar(token, 16, initChar);
    }

    private @NotNull Char32 readUnicodeChar(final @NotNull CharSequence token,
                                            final int base,
                                            final char initChar) {
        return Char32.valueOf(0); // TODO
    }

    private void readComment() {
        cpi.skipLine();
    }

    private boolean isWhitespace(final int codepoint) {
        return Character.isWhitespace(codepoint) || codepoint == ',';
    }

    private boolean isHexNum(final int codepoint) {
        return switch (codepoint) {
            case 'a', 'b', 'c', 'd', 'e', 'f',
                 'A', 'B', 'C', 'D', 'E', 'F',
                 '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true;
            default -> false;
        };
    }
}
