package de.kleinert.edna.pprint;

import de.kleinert.edna.EdnaOptions;
import de.kleinert.edna.data.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Flushable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.IntStream;

public class EdnaWriter {
    private final @NotNull EdnaOptions options;

    private EdnaWriter(final @NotNull EdnaOptions options) {
        this.options = options;
    }

    public static void pprint(final @Nullable Object obj, final @NotNull EdnaOptions options, final @NotNull Appendable writer) throws IOException {
        new EdnaWriter(options).encode(obj, writer, 0);
        if (writer instanceof Flushable) ((Flushable) writer).flush();
    }

    public static void pprintln(final @Nullable Object obj, final @NotNull EdnaOptions options, final @NotNull Appendable writer) throws IOException {
        new EdnaWriter(options).encode(obj, writer, 0);
        writer.append('\n');
        if (writer instanceof Flushable) ((Flushable) writer).flush();
    }

    private Appendable appendIfPrettyEnabled(final @NotNull Appendable writer, final @NotNull CharSequence cs) throws IOException {
        if (options.encoderPrettyPrint()) writer.append(cs);
        return writer;
    }

    private boolean tryEncoder(final @NotNull Object obj, final @NotNull Appendable writer, int indent) throws IOException {
        indent = options.encoderPrettyPrint() ? indent : 0;
        AtomicReference<Function<Object, Map.Entry<String, ?>>> encoder = new AtomicReference<>(null);
        options.taggedElementEncoders().forEach((jClass, enc) -> {
            if (encoder.get() == null && jClass.isInstance(obj)) {
                encoder.set(enc);
            }
        });
        if (encoder.get() == null)
            return true;

        var r = encoder.get().apply(obj);
        if (r == null)
            return true;

        var prefix = r.getKey();
        var output = r.getValue();
        if (prefix != null && !prefix.isBlank()) writer.append('#').append(prefix).append(' ');
        this.encode(output, writer, indent);
        return false;
    }

    private void encode(final Object obj, final @NotNull Appendable writer, int indent) throws IOException {
        indent = options.encoderPrettyPrint() ? indent : 0;
        int finalIndent = indent;

        switch (obj) {
            case null -> encodeNull(writer);
            case Boolean b -> encodeBool(b, writer);
            case String s -> encodeString(s, writer);

            case Keyword k -> {
                if (tryEncoder(k, writer, indent))
                    encodeKeyword(k, writer);
            }
            case Symbol s -> {
                if (tryEncoder(s, writer, indent))
                    encodeSymbol(s, writer);
            }
            case EdnaList<?> l -> {
                if (tryEncoder(l, writer, indent))
                    encodePersistentList(l, writer, indent);
            }
            case List<?> l -> {
                if (tryEncoder(l, writer, indent))
                    encodeVector(l, writer, indent);
            }
            case Object[] l -> {
                if (tryEncoder(l, writer, indent))
                    encodeVector(Arrays.stream(l).toList(), writer, indent);
            }
            case Set<?> s -> {
                if (tryEncoder(s, writer, indent))
                    encodeSet(s, writer, indent);
            }
            case Map<?, ?> m -> {
                if (tryEncoder(m, writer, indent))
                    encodeMap(m, writer, indent);
            }
            case Iterable<?> i -> {
                if (tryEncoder(i, writer, indent))
                    encodeOtherIterable(i, writer, indent);
            }

            case Character c -> encodeChar(c, writer);
            case Char32 c -> encodeChar32(c, writer);
            case Byte n -> encodePredefinedNumberType(n, writer);
            case Short n -> encodePredefinedNumberType(n, writer);
            case Integer n -> encodePredefinedNumberType(n, writer);
            case Long n -> encodePredefinedNumberType(n, writer);
            case Float n -> encodeFloat(n, writer);
            case Double n -> encodeDouble(n, writer);
            case BigInteger n -> encodePredefinedNumberType(n, writer);
            case BigDecimal n -> encodePredefinedNumberType(n, writer);

            case IObj o -> encodeIObj(o, writer, finalIndent);
            case UUID u -> encodeUuid(u, writer);
            case Instant i -> encodeInstant(i, writer);

            case byte[] a -> {
                if (tryEncoder(a, writer, indent))
                    encode(Arrays.stream(byteArrayToLongArray(a)).boxed().toList(), writer, finalIndent);
            }
            case short[] a -> {
                if (tryEncoder(a, writer, indent))
                    encode(Arrays.stream(shortArrayToLongArray(a)).boxed().toList(), writer, finalIndent);
            }
            case int[] a -> {
                if (tryEncoder(a, writer, indent))
                    encode(Arrays.stream(a).boxed().toList(), writer, finalIndent);
            }
            case long[] a -> {
                if (tryEncoder(a, writer, indent))
                    encode(Arrays.stream(a).boxed().toList(), writer, finalIndent);
            }
            case float[] a -> {
                if (tryEncoder(a, writer, indent))
                    encode(Arrays.stream(floatArrayToDoubleArray(a)).boxed().toList(), writer, finalIndent);
            }
            case double[] a -> {
                if (tryEncoder(a, writer, indent))
                    encode(Arrays.stream(a).boxed().toList(), writer, finalIndent);
            }
            default -> {
                if (tryEncoder(obj, writer, indent))
                    encodeAnything(obj.toString(), writer);
            }
        }
    }

    private void encodeIObj(final @NotNull IObj obj, final @NotNull Appendable writer, final int indent) throws IOException {
        writer.append('^');
        encode(obj.meta(), writer, indent);
        writer.append(' ');
        encode(obj.obj(), writer, indent);
    }

    private long[] byteArrayToLongArray(final byte @NotNull [] a) {
        return IntStream.range(0, a.length).mapToLong(i -> a[i]).toArray();
    }

    private long[] shortArrayToLongArray(final short @NotNull [] a) {
        return IntStream.range(0, a.length).mapToLong(i -> a[i]).toArray();
    }

    private double[] floatArrayToDoubleArray(final float @NotNull [] a) {
        return IntStream.range(0, a.length).mapToDouble(i -> a[i]).toArray();
    }

    private void encodeAnything(final @NotNull String string, final @NotNull Appendable writer) throws IOException {
        writerAppend(writer, string);
    }

    private void encodeInstant(final @NotNull Instant i, final @NotNull Appendable writer) throws IOException {
        writerAppend(writer, "#inst \"" + i + '"');
    }

    private void encodeUuid(final @NotNull UUID u, final @NotNull Appendable writer) throws IOException {
        writerAppend(writer, "#uuid \"" + u + '"');
    }

    private void encodeDouble(final @NotNull Double n, final @NotNull Appendable writer) throws IOException {
        if (n.isNaN()) writerAppend(writer, "##NaN");
        else if (n < 0.0 && n.isInfinite()) writerAppend(writer, "##-INF");
        else if (n.isInfinite()) writerAppend(writer, "##INF");
        else writerAppend(writer, n.toString());
    }

    private void encodeFloat(final @NotNull Float n, final @NotNull Appendable writer) throws IOException {
        if (n.isNaN()) writerAppend(writer, "##NaN");
        else if (n < 0.0 && n.isInfinite()) writerAppend(writer, "##-INF");
        else if (n.isInfinite()) writerAppend(writer, "##INF");
        else writerAppend(writer, n.toString());
    }

    private void encodePredefinedNumberType(final @NotNull Number n, final @NotNull Appendable writer) throws IOException {
        StringBuilder sb = new StringBuilder();
        switch (n) {
            case Byte ignored -> sb.append(n.byteValue());
            case Short ignored -> sb.append(n.shortValue());
            case Integer ignored -> sb.append(n.intValue());
            case Long ignored -> sb.append(n.longValue());
            case BigInteger ignored -> {
                sb.append(n);
                sb.append('N');
            }
            case BigDecimal ignored -> {
                sb.append(n);
                sb.append('M');
            }
            default -> sb.append(n);
        }
        writerAppend(writer, sb.toString());
    }

    private void encodeChar32(final @NotNull Char32 c, final @NotNull Appendable writer) throws IOException {
        if (!options.allowDispatchChars()) {
            writerAppend(writer, '"' + c.toString() + '"');
            return;
        }
        int code = c.code();
        String s = switch (code) {
            case '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4',
                 '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '\\', '^',
                 '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '|', '~', '§', '°', '´', '€' ->
                    (new StringBuilder("#\\")).appendCodePoint(code).toString();
            case '\n' -> "#\\newline";
            case ' ' -> "#\\space";
            case '\t' -> "#\\tab";
            case '\b' -> "#\\backspace";
            case 12 -> "#\\formfeed";
            case '\r' -> "#\\return";
            default -> String.format("#\\u%08x", code);
        };
        writerAppend(writer, s);
    }

    private void encodeChar(final @NotNull Character code, final @NotNull Appendable writer) throws IOException {
        String s = switch (code) {
            case '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4',
                 '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '\\', '^',
                 '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '|', '~', '§', '°', '´', '€' -> "\\" + code;
            case '\n' -> "\\newline";
            case ' ' -> "\\space";
            case '\t' -> "\\tab";
            case '\b' -> "\\backspace";
            case 12 -> "\\formfeed";
            case '\r' -> "\\return";
            default -> String.format("\\u%04x", (int) code);
        };
        writerAppend(writer, s);
    }

    private void encodeOtherIterable(final @NotNull Iterable<?> i, final @NotNull Appendable writer, final int indent) throws IOException {
        var list = new ArrayList<>();
        for (var e : i)
            list.add(e);
        formatCollectionTo(list, "(", ")", writer, indent);
    }

    private void encodeMap(final @NotNull Map<?, ?> m, final @NotNull Appendable writer, final int indent) throws IOException {
        formatCollectionTo(m.entrySet().stream().toList(), "{", "}", writer, indent, true);
    }

    private void encodeSet(final @NotNull Set<?> s, final @NotNull Appendable writer, final int indent) throws IOException {
        formatCollectionTo(s.stream().toList(), "#{", "}", writer, indent);
    }

    private void encodeVector(final @NotNull List<?> l, final @NotNull Appendable writer, final int indent) throws IOException {
        formatCollectionTo(l, "[", "]", writer, indent);
    }

    private void encodePersistentList(final @NotNull EdnaList<?> l, final @NotNull Appendable writer, final int indent) throws IOException {
        formatCollectionTo(l, "(", ")", writer, indent);
    }

    private void encodeSymbol(final @NotNull Symbol s, final @NotNull Appendable writer) throws IOException {
        writerAppend(writer, s.toString());
    }

    private void encodeKeyword(final @NotNull Keyword k, final @NotNull Appendable writer) throws IOException {
        writerAppend(writer, k.toString());
    }

    private void encodeString(final @NotNull String s, final @NotNull Appendable writer) throws IOException {
        var sb = new StringBuilder();
        sb.append('"');
        for (int code : s.chars().toArray()) {
            switch (code) {
                case '\t' -> sb.append("\\t");
                case '\b' -> sb.append("\\b");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                default -> sb.appendCodePoint(code);
            }
        }

        sb.append('"');
        var newString = sb.toString();
        writerAppend(writer, newString);
    }

    private void encodeNull(final @NotNull Appendable writer) throws IOException {
        writerAppend(writer, "nil");
    }

    private void encodeBool(final boolean b, final @NotNull Appendable writer) throws IOException {
        writerAppend(writer, String.valueOf(b));
    }

    private static void writerAppend(final @NotNull Appendable writer, final @NotNull String s) throws IOException {
        writer.append(s);
    }

    private void formatCollectionTo(final @NotNull List<?> l, final @NotNull String open, final @NotNull String close, final @NotNull Appendable writer, int indent, final boolean isMap) throws IOException {
        indent = options.encoderPrettyPrint() ? indent : 0;

        // Try inline first (dry-run)
        var tmp = new StringBuilder();
        tmp.append(open);
        var entryIteratorDry = l.iterator();
        if (isMap) {
            for (int i = 0; i < l.size(); i++) {
                var e = (Map.Entry<?, ?>) entryIteratorDry.next();
                encode(e.getKey(), tmp, indent + 1);
                tmp.append(' ');
                encode(e.getValue(), tmp, indent + 1);
                if (i != l.size() - 1) tmp.append(options.encodingSequenceSeparator());
            }
        } else {
            for (int i = 0; i < l.size(); i++) {
                var e = entryIteratorDry.next();
                encode(e, tmp, indent + 1);
                if (i != l.size() - 1) tmp.append(options.encodingSequenceSeparator());
            }
        }
        tmp.append(close);

        if (tmp.length() + indent <= options.encoderMaxColumn()) {
            writer.append(tmp);
            return;
        }

        // Multi-line formatting
        writer.append(open);
        appendIfPrettyEnabled(writer, "\n");
        var childIndent = indent + 1;
        var pad = options.encoderLineIndent().repeat(childIndent);
        var entryIteratorML = l.iterator();
        if (isMap) {
            for (int i = 0; i < l.size(); i++) {
                var e = (Map.Entry<?, ?>) entryIteratorML.next();
                appendIfPrettyEnabled(writer, pad);
                encode(e.getKey(), tmp, indent + 1);
                tmp.append(' ');
                encode(e.getValue(), tmp, indent + 1);
                if (i != l.size()) {
                    tmp.append(options.encodingSequenceSeparator());
                    appendIfPrettyEnabled(writer, "\n");
                }
            }
        } else {
            for (int i = 0; i < l.size(); i++) {
                var e = entryIteratorML.next();
                appendIfPrettyEnabled(writer, pad);
                encode(e, tmp, indent + 1);
                if (i != l.size()) {
                    tmp.append(options.encodingSequenceSeparator());
                    appendIfPrettyEnabled(writer, "\n");
                }
            }
        }
        appendIfPrettyEnabled(writer, "\n");
        appendIfPrettyEnabled(writer, options.encoderLineIndent().repeat(indent)).append(close);
    }

    private void formatCollectionTo(final @NotNull List<?> l, final @NotNull String open, final @NotNull String close, final @NotNull Appendable writer, final int indent) throws IOException {
        formatCollectionTo(l, open, close, writer, indent, false);
    }
}