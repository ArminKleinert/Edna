package de.kleinert.edna.reader;

import de.kleinert.edna.EdnaOptions;
import de.kleinert.edna.data.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;

public class EdnaReader {
    private final @NotNull EdnaOptions options;
    private final @NotNull CodePointIterator cpi;

    EdnaReader(final @NotNull EdnaOptions options,
               final @NotNull CodePointIterator cpi) {
        @NotNull var newOptions = options;
        if (options.allowSymbolicValues()) {
            final @NotNull Map<@NotNull Symbol, Object> symbolics = new HashMap<>(EdnaOptions.defaultSymbolicValues());
            symbolics.putAll(options.symbolicValues());
            newOptions = options.copy(b -> b.symbolicValues(symbolics));
        }
        this.options = newOptions;
        this.cpi = cpi;
        ensureValidDecoderNames();
    }

    private void ensureValidDecoderNames() {
        for (final @NotNull String key : options.taggedElementDecoders().keySet()) {
            var name = Symbol.parse(key);
            if (name == null) {
                final @NotNull var message = "Decoder name \"" + key + "\" is not a valid symbol.";
                throw new EdnaReaderException(cpi.getLineIdx(), cpi.getTextIndex(), message);
            }
            if (!options.allowMoreEncoderDecoderNames() && name.namespace() == null) {
                final @NotNull var message = "Decoder without namespace: " + name;
                throw new EdnaReaderException(cpi.getLineIdx(), cpi.getTextIndex(), message);
            }
            if (key.equals("inst") || key.equals("uuid") || key.equals("ref")) {
                final @NotNull var message = "Decoder name " + name + " is not allowed.";
                throw new EdnaReaderException(cpi.getLineIdx(), cpi.getTextIndex(), message);
            }
        }
    }

    private final @NotNull Object NOTHING = new Object();

    public static <T> @Nullable T read(final @NotNull CodePointIterator cpi,
                                       final @NotNull EdnaOptions options,
                                       final @NotNull Class<T> castClass) {
        final var temp = new EdnaReader(options, cpi).readString(false);
        if (temp == null) return null;
        return castClass.cast(temp);
    }

    public static @NotNull List<Object> readMulti(
            final @NotNull CodePointIterator cpi,
            final @NotNull EdnaOptions options) {
        //noinspection unchecked
        return Collections.unmodifiableList((List<Object>) (Objects.requireNonNull(new EdnaReader(options, cpi).readString(true))));
    }

    private @Nullable Object readString(final boolean readMulti) {
        var data = (List<?>) readForm(0, false);

        if (readMulti)
            return data;

        if (data.size() != 1) {
            throw new EdnaReaderException(
                    cpi.getLineIdx(), cpi.getTextIndex(),
                    "The input should only contain one expression, but there are " + data.size() + ".");
        }
        return data.getFirst();
    }


    private Object readForm(final int level, final boolean stopAfterOne) {
        @NotNull List<@Nullable Object> res = new ArrayList<>();
        int linePos = 0;
        int codePosIndex = 0;

        while (true) {
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
                        throw new EdnaReaderException(
                                linePos, codePosIndex,
                                "Unexpected character " + (char) codePoint);
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

            if (stopAfterOne) {
                break;
            }
        }

        if (stopAfterOne) {
            if (res.size() != 1) {
                //noinspection ConstantValue
                final var message = "Reader requires exactly one expression, but got " + res.size() + ".";
                throw new EdnaReaderException(linePos, codePosIndex, message);
            }
            return res.getFirst();
        }

        return res;
    }

    private @Nullable Object readOther() {
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
        final @NotNull var token = readToken(this::isValidSymbolChar);

        if (token.length() > 1 && token.codePointAt(0) == ':') {
            final var temp = Keyword.parse(token, options.allowUTFSymbols());
            if (temp == null)
                throw new EdnaReaderException(
                        linePos, codePosIndex,
                        "Token starts with a colon, but is not a valid keyword: " + token);
            return temp;
        }

        if (token.codePointAt(0) == ':') {
            throw new EdnaReaderException(linePos, codePosIndex, "Lonely colon.");
        }

        return switch (token) {
            case "nil" -> null;
            case "true" -> true;
            case "false" -> false;
            default -> {
                final var temp = Symbol.parse(token, options.allowUTFSymbols());
                if (temp == null)
                    throw new EdnaReaderException(linePos, codePosIndex, "Invalid symbol: " + token);
                yield temp;
            }
        };
    }

    private @Nullable Object readDispatch(final int level) {
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
        final @NotNull var token = cpi.takeCodePoints(new StringBuilder(), this::isNotBreakingSymbolOrDispatch);

        if (token.isEmpty()) {
            if (!cpi.hasNext())
                throw new EdnaReaderException(linePos, codePosIndex, "Invalid dispatch expression #" + token);

            final var code = cpi.nextInt();
            switch (code) {
                case '{', '#' -> token.appendCodePoint(code);
                default -> throw new EdnaReaderException(linePos, codePosIndex, "Invalid dispatch expression " + token);
            }
        }

        switch (token.codePointAt(0)) {
            case '\\' -> {
                if (options.allowDispatchChars()) {
                    final var linePos1 = cpi.getLineIdx();
                    final var codePosIndex1 = cpi.getTextIndex();
                    var subToken = token.substring(1);
                    return readDispatchUnicodeChar(linePos1, codePosIndex1, subToken, true);
                }
            }
            case '{' -> {
                return readSet(level + 1, '}');
            }
            case '#' -> {
                if (!options.allowSymbolicValues())
                    throw new EdnaReaderException(linePos, codePosIndex, "Symbolic values are not allowed.");
                token.setLength(0); // Clear token
                cpi.takeCodePoints(token, this::isNotBreakingSymbolOrDispatch);

                var sym = Symbol.symbol(token.toString());
                if (!options.symbolicValues().containsKey(sym))
                    throw new EdnaReaderException(linePos, codePosIndex, "Unknown symbolic value ##" + token);
                return options.symbolicValues().get(Symbol.symbol(token.toString()));
            }
        }

        return readDecode(level + 1, token.toString());
    }

    private @Nullable Object readDecode(final int level, final @NotNull String token) {
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
        final var form = readForm(level + 1, true);
        try {
            if (token.equals("uuid")) {
                if (!(form instanceof CharSequence)) {
                    var msg = "Dispatch decoder $token requires a string as input but got " + form + ".";
                    throw new EdnaReaderException(linePos, codePosIndex, msg);
                }
                return UUID.fromString(form.toString());
            } else if (token.equals("inst")) {
                if (!(form instanceof CharSequence)) {
                    var msg = "Dispatch decoder $token requires a string as input but got " + form + ".";
                    throw new EdnaReaderException(linePos, codePosIndex, msg);
                }
                return Instant.parse((CharSequence) form);
            } else {
                final var decoder = options.taggedElementDecoders().get(token);
                if (decoder != null) return decoder.apply(form);
            }
        } catch (
                @SuppressWarnings("CaughtExceptionImmediatelyRethrown")
                EdnaReaderException.EdnClassConversionError ex) {
            throw ex;
        } catch (IllegalArgumentException ex) {
            // For UUID.fromString
            throw new EdnaReaderException.EdnClassConversionError(linePos, codePosIndex, null, ex);
        } catch (DateTimeParseException ex) {
            // For Instant.parse.
            throw new EdnaReaderException.EdnClassConversionError(linePos, codePosIndex, null, ex);
        }
        throw new EdnaReaderException(linePos, codePosIndex, "Invalid dispatch expression #" + token + ".");
    }

    private @NotNull Set<Object> readSet(
            final int level,
            @SuppressWarnings("SameParameterValue") final int separator) {
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
        final Set<Object> result = new HashSet<>();
        final var lst = readVector(level, separator);
        var i = 0;
        while (i < lst.size()) {
            Object key = lst.get(i);
            if (result.contains(key))
                throw new EdnaReaderException(linePos, codePosIndex, "Illegal set. Duplicate value " + key + ".");
            result.add(key);
            i++;
        }
        return options.listToEdnSetConverter().apply(lst);
    }

    private @NotNull IObj readMeta(final int level) {
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();

        if (!options.allowMetaData())
            throw new EdnaReaderException(
                    linePos, codePosIndex,
                    "Metadata is turned off.");

        final Map<Object, Object> meta = switch (readForm(level, true)) {
            case String v -> EdnaMap.create(List.of(Keyword.keyword("tag"), v));
            case Symbol v -> EdnaMap.create(List.of(Keyword.keyword("tag"), v));
            case Keyword v -> EdnaMap.create(List.of(v, true));
            case Map<?, ?> tempMap ->
                //noinspection unchecked
                    (Map<Object, Object>) tempMap;
            default -> throw new EdnaReaderException(
                    linePos, codePosIndex,
                    "Metadata must be a Symbol, Keyword, String or Map.");
        };

        final var obj = readForm(level, true);
        if (obj == NOTHING) {
            throw new EdnaReaderException(
                    linePos, codePosIndex,
                    "Required object for metadata, but got nothing.");
        }

        return new IObj.Wrapper<>(meta, obj);
    }

    private @NotNull Number readNumber() {
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
        try {
            return readNumberHelper(linePos, codePosIndex);
        } catch (NumberFormatException ex) {
            throw new EdnaReaderException(linePos, codePosIndex, null, ex);
        }
    }

    private Pattern floatyRegex;
    private Pattern intRegex;
    private Pattern expandedIntRegex;
    private Pattern basedIntRegex;

    private @NotNull Number readNumberHelper(final int linePos, final int codePosIndex) {
        final @NotNull var token = readToken(this::isNotBreakingSymbol);

        if (intRegex == null) {
            floatyRegex = Pattern.compile("[+\\-]?[0-9]*\\.?[0-9]+([eE][+\\-][0-9]+)?M?");
            intRegex = Pattern.compile("[+\\-]?[0-9]+N?");
            expandedIntRegex = Pattern.compile("[+\\-]?(0[obx])?[0-9a-fA-F]+N?");
            basedIntRegex = Pattern.compile("[+\\-]?([1-9][0-9]?)r[0-9a-fA-F]+N?");
        }

        final var tokenLen = token.length();
        var base = 10;
        var startIndex = 0;
        byte sign = 1;

        if (options.moreNumberPrefixes() && basedIntRegex.matcher(token).matches()) {
            if (token.codePointAt(0) == '+') {
                startIndex++;
            } else if (token.codePointAt(0) == '-') {
                startIndex++;
                sign = -1;
            }
            var split = token.substring(startIndex).split("r", 2);
            base = Integer.parseInt(split[0]);
            startIndex = split[0].length() + 1 + startIndex;
        } else if ((options.moreNumberPrefixes() && expandedIntRegex.matcher(token).matches())
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
                    throw new EdnaReaderException(
                            linePos, codePosIndex,
                            "Invalid number prefix in number " + token + ".");
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

            if (!options.allowZeroPrefix()
                    && tokenLen > startIndex + 1
                    && token.codePointAt(startIndex) == '0'
                    && token.codePointAt(startIndex + 1) >= '0'
                    && token.codePointAt(startIndex + 1) <= '9') {
                throw new EdnaReaderException(
                        linePos, codePosIndex,
                        "Invalid number format: " + token + " (numbers other than 0 can not start with a 0)");
            }
        } else if (floatyRegex.matcher(token).matches()) {
            if (token.endsWith("M")) {
                return new BigDecimal(token.substring(0, tokenLen - 1));
            }
            return sign * Double.parseDouble(token);
        } else {
            throw new EdnaReaderException(linePos, codePosIndex, "Invalid number format: " + token);
        }

        if (token.endsWith("M"))
            return BigDecimal.valueOf(sign).multiply(new BigDecimal(token.substring(startIndex, tokenLen - 1)));
        if (token.endsWith("N"))
            return BigInteger.valueOf(sign).multiply(new BigInteger(token.substring(startIndex, tokenLen - 1), base));

        final var tokenSubs = token.substring(startIndex, tokenLen);

        return sign * Long.valueOf(tokenSubs, base);
    }

    private final StringBuilder readTokenBuffer = new StringBuilder();

    private @NotNull String readToken(final @NotNull IntPredicate condition) {
        return readToken(Integer.MAX_VALUE, condition);
    }

    private @NotNull String readToken(final int maxCount, final @NotNull IntPredicate condition) {
        cpi.takeCodePoints(readTokenBuffer, maxCount, condition);
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
                throw new EdnaReaderException(
                        cpi.getLineIdx(), cpi.getTextIndex(),
                        "Unclosed list started in line " + linePos
                                + ". Expected '" + (char) separator + "', got EOF.");
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
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
        final var token = readToken(this::isNotBreakingSymbol);
        return readDispatchUnicodeChar(linePos, codePosIndex, token, false).toChar();
    }

    private Char32 readDispatchUnicodeChar(final int linePos,
                                           final int codePosIndex,
                                           final @NotNull String initialToken,
                                           final boolean isDispatch) {
        final @NotNull var token = (initialToken.isEmpty() && cpi.hasNext())
                ? readToken(1, this::isValidCharSingle).trim()
                : initialToken.trim();

        if (token.isEmpty())
            throw new EdnaReaderException(
                    linePos, codePosIndex,
                    "Invalid char literal. Single backslash or or ");

        if (token.length() == 1)
            return Char32.valueOf(token.codePointAt(0));

        final int maybeReturn = switch (token) {
            case "newline" -> '\n';
            case "space" -> ' ';
            case "tab" -> '\t';
            case "backspace" -> '\b';
            case "formfeed" -> '\f';
            case "return" -> '\r';
            default -> 0;
        };

        if (maybeReturn != 0) return Char32.valueOf(maybeReturn);

        final var reducedToken = token.subSequence(1, token.length());
        final var errorTokenText = (isDispatch ? "#" : "") + '\\' + token;
        final var c0 = token.codePointAt(0);
        if (c0 == 'o') {
            if (reducedToken.length() < 2 || reducedToken.length() > 3)
                throw new EdnaReaderException(
                        linePos, codePosIndex,
                        "Invalid length of unicode sequence " + reducedToken.length() + " in char literal "
                                + errorTokenText + " (should be 2 or 3).");
            return readUnicodeChar(reducedToken, 8, 'o');
        }
        if (c0 == 'u') { // UTF-16 or UTF-32 code
            if (reducedToken.length() == 4 || (isDispatch && reducedToken.length() == 8))
                return readUnicodeChar(reducedToken, 16, 'u');
            throw new EdnaReaderException(
                    linePos, codePosIndex,
                    "Invalid length of unicode sequence " + reducedToken.length()
                            + " in char literal " + errorTokenText
                            + " (should be 4 or 8 if used in a dispatch literal).");
        }
        if (c0 == 'x') { // UTF-32 code
            if (!options.allowSchemeUTF32Codes())
                throw new EdnaReaderException(
                        linePos, codePosIndex,
                        "Invalid char literal: " + errorTokenText);
            if (reducedToken.length() == 8)
                return readUnicodeChar(reducedToken, 16, 'x');
            throw new EdnaReaderException(
                    linePos, codePosIndex,
                    "Invalid length of unicode sequence " + reducedToken.length()
                            + " in char literal " + errorTokenText + " (should be 8).");
        } else {
            throw new EdnaReaderException(linePos, codePosIndex, "Invalid char literal " + errorTokenText);
        }
    }

    private @NotNull Map<?, ?> readMap(final int level) {
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
        final var kvs = readVector(level, '}');
        final var gatheredKeys = new HashSet<>();
        final var res = new ArrayList<Map.Entry<Object, Object>>();

        for (int i = 0; i < kvs.size(); i += 2) {
            var key = kvs.get(i);
            if (gatheredKeys.contains(key)) {
                throw new EdnaReaderException(
                        linePos, codePosIndex,
                        "Illegal map. Duplicate key " + key + ".");
            }
            gatheredKeys.add(key);
            if (i + 1 >= kvs.size()) {
                throw new EdnaReaderException(
                        linePos, codePosIndex,
                        "Odd number of elements in map. Last key was " + key + ".");
            }
            final var value = kvs.get(i + 1);
            res.add(new AbstractMap.SimpleImmutableEntry<>(key, value));
        }

        return options.listToEdnMapConverter().apply(res);
    }

    private @NotNull List<?> readList(final int level) {
        final @NotNull var it = readVector(level, ')');
        return options.listToEdnListConverter().apply(it);
    }

    private @NotNull String readEdnString() {
        final var currentToken = new StringBuilder();
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
                        var message =
                                "Invalid escape sequence: \\" + escapeCodePt + "in string " + currentToken;
                        throw new EdnaReaderException(cpi.getLineIdx(), cpi.getTextIndex(), message);
                    }
                };
                currentToken.appendCodePoint(temp);
            } else { // Just a codepoint.
                currentToken.appendCodePoint(codePoint);
            }
        }
        throw new EdnaReaderException(linePos, codePosIndex, "Unclosed String literal " + currentToken + ".");
    }

    private @NotNull Char32 readUnicodeChar(final int minLength,
                                            final int maxLength,
                                            final char initChar) {
        final var linePos = cpi.getLineIdx();
        final var codePosIndex = cpi.getTextIndex();
        final @NotNull StringBuilder token =
                cpi.takeCodePoints(new StringBuilder(), maxLength, this::isHexNum);
        if (token.length() < minLength && token.length() > maxLength)
            throw new EdnaReaderException(
                    linePos, codePosIndex,
                    "Invalid unicode sequence \\" + initChar + token);
        return readUnicodeChar(token, 16, initChar);
    }

    private @NotNull Char32 readUnicodeChar(final @NotNull CharSequence token,
                                            final int base,
                                            final char initChar) {
        var linePos = cpi.getLineIdx();
        var codePosIndex = cpi.getTextIndex();
        try {
            var code = token.chars().reduce(0, (res, elem) ->
                    res * base + Character.digit(elem, base)
            );
            return Char32.valueOf(code);
        } catch (NumberFormatException nfe) {
            throw new EdnaReaderException(
                    linePos, codePosIndex,
                    (nfe.getMessage() == null
                            ? nfe.getMessage()
                            : ("Invalid unicode sequence \\" + initChar + token)));
        }
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

    private boolean isNotBreakingSymbol(final int codepoint) {
        return switch (codepoint) {
            case ' ', '\t', '\n', '\r', '[', ']', '(', ')', '{', '}', '\'', '"', ';', ',' -> false;
            default -> true;
        };
    }

    private boolean isNotBreakingSymbolOrDispatch(final int codepoint) {
        return isNotBreakingSymbol(codepoint) && codepoint != '#';
    }

    private boolean isValidSymbolChar(final int codepoint) {
        return switch (codepoint) {
            case ' ', '\t', '\n', '\r', '[', ']', '(', ')', '{', '}',
                 '"' -> false;
            default -> true;
        };
    }

    private boolean isValidCharSingle(final int codepoint) {
        if (codepoint < Character.MIN_VALUE || codepoint > Character.MAX_VALUE)
            return false;
        return !Character.isWhitespace(codepoint);
    }
}
