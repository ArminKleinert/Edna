package de.kleinert.edna.reader;

import de.kleinert.edna.EdnOptions;
import de.kleinert.edna.data.Char32;
import de.kleinert.edna.data.EdnCollections;
import de.kleinert.edna.data.Keyword;
import de.kleinert.edna.data.Symbol;
import jdk.jshell.spi.ExecutionControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
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
                    if (cpi.hasNext()) {
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
                default -> {
                    cpi.unread(codePoint);
                    res.add(readOther());
                }
            }

            res.remove(NOTHING);

            if (stopAfterOne && !res.isEmpty()) {
                return res.getFirst();
            }
        } while (true);
        return res;
    }

    private @Nullable Object readOther() {
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
        final@NotNull var token = readToken(this::isValidSymbolChar);

        if (token.length() > 1 && token.codePointAt(0) == ':') {
            final var temp = Keyword.parse(token, options.allowUTFSymbols());
            if (temp == null)
                throw new EdnReaderException(linePos, codePosIndex, "Token starts with a colon, but is not a valid keyword: " + token);
            return temp;
        }

        if (token.codePointAt(0) == ':') {
            throw new EdnReaderException(linePos, codePosIndex, "Lonely colon.");
        }

        return switch (token) {
            case "nil" -> null;
            case "true" -> true;
            case "false" -> false;
            default -> {
                final var temp = Symbol.parse(token, options.allowUTFSymbols());
                if (temp == null)
                    throw new EdnReaderException(linePos, codePosIndex, "Invalid symbol: " + token);
                yield temp;
            }
        };
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
        byte sign = 1;

        if ((options.allowNumericSuffixes() && expandedIntRegex.matcher(token).matches())
                || intRegex.matcher(token).matches()) {
            if (token.codePointAt(0) == '+') {
                startIndex++;
            } else if (token.codePointAt(0) == '-') {
                startIndex++;
                sign = -1;
            }

            var first = token.codePointAt(startIndex);
            if (first == '0' && token.length() - startIndex > 1) {
                startIndex += 2;
                var baseC = token.codePointAt(startIndex - 1);
                if (!options.moreNumberPrefixes() && (baseC == 'x' || baseC == 'o' || baseC == 'b'))
                    throw new EdnReaderException(linePos, codePosIndex, "Invalid number prefix in number " + token + ".");
                base = switch (token.codePointAt(startIndex - 1)) {
                    case 'x' -> 16;
                    case 'o' -> 8;
                    case 'b' -> 2;
                    default -> {
                        startIndex -= 2;
                        yield base;
                    }
                };
            }

            if (!options.allowNumericSuffixes()
                    && tokenLen > startIndex + 1
                    && token.codePointAt(startIndex) == '0'
                    && token.codePointAt(startIndex + 1) >= '0'
                    && token.codePointAt(startIndex + 1) <= '9') {
                throw new EdnReaderException(linePos, codePosIndex, "Invalid number format: " + token + " (numbers other than 0 can not start with a 0)");
            }
        } else if (floatyRegex.matcher(token).matches()) {
            if (token.endsWith("M")) {
                return new BigDecimal(token.substring(0, tokenLen - 1));
            }
            return sign * Double.parseDouble(token);
        } else if (options.allowRatios()) {
            throw new UnsupportedOperationException(); // TODO?
        } else {
            throw new EdnReaderException(linePos, codePosIndex, "Invalid number format: " + token);
        }

        if (token.endsWith("M"))
            return BigDecimal.valueOf(sign).multiply(new BigDecimal(token.substring(startIndex, tokenLen - 1)));
        if (token.endsWith("N"))
            return BigInteger.valueOf(sign).multiply(new BigInteger(token.substring(startIndex, tokenLen - 1), base));


        final int byteNum, offset;

        if (!options.allowNumericSuffixes()) {
            byteNum = 8;
            offset = 0;
        } else if (token.endsWith("_i8")) {
            byteNum = 1;
            offset = 3;
        } else if (token.endsWith("_i16")) {
            byteNum = 2;
            offset = 4;
        } else if (token.endsWith("_i32")) {
            byteNum = 4;
            offset = 4;
        } else if (token.endsWith("_i64")) {
            byteNum = 8;
            offset = 4;
        } else if (token.endsWith("L")) {
            byteNum = 8;
            offset = 1;
        } else {
            byteNum = 8;
            offset = 0;
        }

        final var tokenSubs = token.substring(startIndex, tokenLen - offset);
        final long temp = sign * Long.valueOf(tokenSubs, base);

        return switch (byteNum) {
            case 1 -> (byte) temp;
            case 2 -> (short) temp;
            case 4 -> (int) temp;
            case 8 -> temp;
            default -> throw new IllegalStateException();
        };
    }

    private final StringBuilder readTokenBuffer = new StringBuilder();

    private @NotNull String readToken(final @NotNull IntPredicate condition) {
        return readToken(Integer.MAX_VALUE, condition);
    }

    private @NotNull String readToken(final int maxCount, final @NotNull IntPredicate condition) {
        cpi.takeCodePoints(readTokenBuffer, condition);
        final @NotNull var tkn = readTokenBuffer.toString();
        readTokenBuffer.setLength(0);
        return tkn;
    }

    private @NotNull Object readVector(final int level) {
        final @NotNull var it = readVector(level, ']');
        return options.listToEdnVectorConverter().apply(it);
    }

    private @NotNull List<Object> readVector(final int level, final int separator) {
        final var linePos = cpi.getLineIdx();
        final var acc = new ArrayList<>();
        do {
            cpi.skipWhile(this::isWhitespace);

            if (!cpi.hasNext()) {
                throw new EdnReaderException(cpi.getLineIdx(), cpi.getTextIndex(), "Unclosed list started in line " + linePos + ". Expected '" + (char) separator + "', got EOF.");
            }

            if (cpi.peek() == separator) {
                cpi.nextInt();
                break;
            }

            final var elem = readForm(level + 1, true);
            if (elem != NOTHING) acc.add(elem);
        } while (true);
        return acc;
    }

    private @NotNull Character readChar() {
        return (char) -1; // TODO
    }

    private @NotNull Map<?, ?> readMap(final int level) {
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
        var kvs = readVector(level, '}');
        var gatheredKeys = new HashSet<>();
        var res = new ArrayList<Map.Entry<Object,Object>>();

        for (int i = 0; i < kvs.size(); i += 2) {
            var key = kvs.get(i);
            if (gatheredKeys.contains(key)) {
                throw new EdnReaderException(linePos, codePosIndex, "Illegal map. Duplicate key "+key+".");
            }
            gatheredKeys.add(key);
            if (i+1 >= kvs.size()) {
                throw new EdnReaderException(linePos, codePosIndex, "Odd number of elements in map. Last key was "+key+".");
            }
            var value = kvs.get(i+1);
            res.add(new AbstractMap.SimpleImmutableEntry<>(key,value));
        }

        return options.listToEdnMapConverter().apply(res);
    }

    private @NotNull List<?> readList(final int level) {
        final@NotNull var it = readVector(level, ')');
        return options.listToEdnListConverter().apply(it);
    }

    private @NotNull String readEdnString() {
        final @NotNull var currentToken = new StringBuilder();
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
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
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
        final @NotNull StringBuilder token = cpi.takeCodePoints(new StringBuilder(), maxLength, this::isHexNum);
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

    private boolean isValidSymbolChar(final int codepoint) {
        return switch (codepoint) {
            case ' ', '\t', '\n', '\r', '[', ']', '(', ')', '{', '}',
                 '"' -> false;
            default -> true;
        };
    }
}
