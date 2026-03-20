package de.kleinert.edna.reader;

import de.kleinert.edna.EdnOptions;
import de.kleinert.edna.data.Char32;
import de.kleinert.edna.data.EdnCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;

public class EdnaReader {
    private final @NotNull EdnOptions options;
    private final @NotNull CodePointIterator cpi;

    EdnaReader(final @NotNull EdnOptions options,
               final @NotNull CodePointIterator cpi) {
        this.options = options;
        this.cpi = cpi;
        ensureValidDecoderNames();
    }

    private void ensureValidDecoderNames() {
        // TODO
    }

    private final @NotNull Object NOTHING = new Object();

    public static <T> T read(final @NotNull CodePointIterator cpi,
                             final @NotNull EdnOptions options,
                             final @NotNull Class<T> castClass) {
        var temp = new EdnaReader(options, cpi).readString();
        return castClass.cast(temp);
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
                    } else if (cpi.hasNext() && options.allowMetaData() && cpi.peek() == '^') {
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
                    if (options.allowMetaData()) {
                        int peeked = cpi.peek();
                        isNumber = peeked >= '0' && peeked <= '9';
                    }
                    cpi.unread(codePoint);
                    res.add(isNumber ? readNumber() : readOther());
                }
                case '^' -> {
                    if (options.allowMetaData()) {
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
        var linePos = cpi.getLineIdx();
        var codePosIndex = cpi.getTextIndex();
        try {
            return readNumberHelper(linePos, codePosIndex);
        } catch (NumberFormatException ex) {
            throw new EdnReaderException(linePos, codePosIndex, null, ex);
        }
    }

    private Pattern floatyRegex;
    private Pattern intRegex;
    private Pattern ratioRegex;
    private Pattern expandedIntRegex;

    private @NotNull Number readNumberHelper(final int linePos, final int codePosIndex) {
        var token = readToken(this::isNotBreakingSymbol);

        if (intRegex == null) {
            floatyRegex = Pattern.compile("[+\\-]?[0-9]*\\.?[0-9]+([eE][+\\-][0-9]+)?M?");
            intRegex = Pattern.compile("[+\\-]?(0[obx])?[0-9a-fA-F]+N?");
            ratioRegex = Pattern.compile("[+\\-]?[0-9]+/[0-9]+?");
            expandedIntRegex = Pattern.compile("[+\\-]?(0[obx])?[0-9a-fA-F]+(N|_i8|_i16|_i32|_i64|L)?");
        }

        var tokenLen = token.length();
        var base = 10;
        var startIndex = 0;
        var sign = 1L;

        if ((options.allowNumericSuffixes() && expandedIntRegex.matcher(token).matches()) || intRegex.matcher(token).matches()) {
            if (token.codePointAt(0) == '+')
            {startIndex++;
            sign=1;}else if (token.codePointAt(0)== '-'){startIndex++;sign=-1;}

            var first = token.codePointAt(startIndex);
            if (first == '0' && token.length()-startIndex>1) {
                startIndex+=2;
                var error = new EdnReaderException(linePos, codePosIndex, "Invalid number prefix in number "+token+".");
                switch(token.codePointAt(startIndex-1)) {
                    case'x':
                        if (!options.moreNumberPrefixes())
                            throw error;
                        base = 16;
                        break;
                    case'o':
                        if (!options.moreNumberPrefixes())
                            throw error;
                        base = 8;
                        break;
                    case'b':
                        if (!options.moreNumberPrefixes())
                            throw error;
                        base = 2;
                        break;
                    default:
                        startIndex-=2;
                        break;
                }
            }
        }
    }

    private final StringBuilder readTokenBuffer = new StringBuilder();

    private @NotNull String readToken(final @NotNull IntPredicate condition) {
        return readToken(Integer.MAX_VALUE, condition);
    }

    private @NotNull String readToken(final int maxCount, final @NotNull IntPredicate condition) {
        cpi.takeCodePoints(readTokenBuffer, condition);
        final var tkn = readTokenBuffer.toString();
        readTokenBuffer.setLength(0);
        return tkn;
    }

    private @Nullable Object readVector(final int level) {
        var it = List.of(); // TODO
        return options.listToEdnVectorConverter().apply(it);
    }

    private @NotNull Character readChar() {
        return (char) -1; // TODO
    }

    private @NotNull Map<?, ?> readMap(final int level) {
        var it = List.<Map.Entry<Object, Object>>of(); // TODO
        return options.listToEdnMapConverter().apply(it);
    }

    private @Nullable List<?> readList(final int level) {
        var it = List.of(); // TODO
        return options.listToEdnListConverter().apply(it);
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

    private boolean isNum(final int codepoint) {
        return switch (codepoint) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true;
            default -> false;
        };
    }

    private boolean isNotBreakingSymbol(final int codepoint) {
        return switch (codepoint) {
            case ' ', '\t', '\n', '\r', '[', ']', '(', ')', '{', '}', '\'', '"', ';', ',' -> false;
            default -> true;
        };
    }


}
