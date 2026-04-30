package de.kleinert.edna.pprint;

import de.kleinert.edna.EdnaOptions;
import de.kleinert.edna.data.Char32;
import de.kleinert.edna.data.EdnaCollections;
import de.kleinert.edna.data.Keyword;
import de.kleinert.edna.data.Symbol;

import java.io.Flushable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ListFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class EdnaWriter {
    private final EdnaOptions options;

    private EdnaWriter(EdnaOptions options) {
        this.options = options;
    }

    public static String pprintToString(Object obj, EdnaOptions options) {
        var writer = new StringBuilder();
        try {
            new EdnaWriter(options).encode(obj, writer, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    public static String pprintToString(Object obj) {
        return pprintToString(obj, EdnaOptions.defaultOptions());
    }

    public static void pprint(Object obj, EdnaOptions options, Appendable writer) throws IOException {
        new EdnaWriter(options).encode(obj, writer, 0);
        if (writer instanceof Flushable) ((Flushable) writer).flush();
    }

    public static void pprintln(Object obj, EdnaOptions options, Appendable writer) throws IOException {
        new EdnaWriter(options).encode(obj, writer, 0);
        writer.append('\n');
        if (writer instanceof Flushable) ((Flushable) writer).flush();
    }

    private Appendable appendIfPrettyEnabled(Appendable writer, CharSequence cs) throws IOException {
        if (options.encoderPrettyPrint()) writer.append(cs);
        return writer;
    }

    private boolean tryEncoder(Object obj, Appendable writer, int indent) throws IOException {
        indent = options.encoderPrettyPrint() ? indent : 0;
        AtomicReference<Function<Object, Map.Entry<String, ?>>> encoder = new AtomicReference<>(null);
        options.ednClassEncoders().forEach((jClass, enc) -> {
            if (encoder.get() == null && jClass.isInstance(obj)) {
                encoder.set(enc);
            }
        });
        if (encoder.get() == null)
            return false;

        var r = encoder.get().apply(obj);
        if (r == null)
            return false;

        var prefix = r.getKey();
        var output = r.getValue();
        if (prefix != null && !prefix.isBlank()) writer.append('#').append(prefix).append(' ');
        this.encode(output, writer, indent);
        return true;
    }

    private void encode(Object obj, Appendable writer, int indent) throws IOException {
        indent = options.encoderPrettyPrint() ? indent : 0;
        int finalIndent = indent;

        switch (obj) {
            case null -> encodeNull(writer);
            case Boolean b -> encodeBool(b, writer);
            case String s -> encodeString(s, writer);

            case Keyword k -> {
                if (!tryEncoder(k, writer, indent))
                    encodeKeyword(k, writer);
            }
            case Symbol s -> {
                if (!tryEncoder(s, writer, indent))
                    encodeSymbol(s, writer);
            }
            case EdnaCollections.EdnaList<?> l -> {
                if (!tryEncoder(l, writer, indent))
                    encodePersistentList(l, writer, indent);
            }
            case List<?> l -> {
                if (!tryEncoder(l, writer, indent))
                    encodeVector(l, writer, indent);
            }
            case Set<?> s -> {
                if (!tryEncoder(s, writer, indent))
                    encodeSet(s, writer, indent);
            }
            case Map<?, ?> m -> {
                if (!tryEncoder(m, writer, indent))
                    encodeMap(m, writer, indent);
            }
            case Iterable<?> i -> {
                if (!tryEncoder(i, writer, indent))
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

            case EdnaCollections.IObj o -> encodeIObj(o, writer, finalIndent);
            case UUID u -> encodeUuid(u, writer, finalIndent);
            case Instant i -> encodeInstant(i, writer, finalIndent);

            case byte[] a -> {
                if (!tryEncoder(a, writer, indent))
                    encode(Arrays.stream(byteArrayToLongArray(a)).boxed().toList(), writer, finalIndent);
            }
            case short[] a -> {
                if (!tryEncoder(a, writer, indent))
                    encode(Arrays.stream(shortArrayToLongArray(a)).boxed().toList(), writer, finalIndent);
            }
            case int[] a -> {
                if (!tryEncoder(a, writer, indent))
                    encode(Arrays.stream(a).boxed().toList(), writer, finalIndent);
            }
            case long[] a -> {
                if (!tryEncoder(a, writer, indent))
                    encode(Arrays.stream(a).boxed().toList(), writer, finalIndent);
            }
            case float[] a -> {
                if (!tryEncoder(a, writer, indent))
                    encode(Arrays.stream(floatArrayToDoubleArray(a)).boxed().toList(), writer, finalIndent);
            }
            case double[] a -> {
                if (!tryEncoder(a, writer, indent))
                    encode(Arrays.stream(a).boxed().toList(), writer, finalIndent);
            }
            default -> {
                if (!tryEncoder(obj, writer, indent))
                    encodeAnything(obj.toString(), writer);
            }
        }
    }

    private void encodeIObj(EdnaCollections.IObj obj, Appendable writer, int indent) throws IOException {
        writer.append('^');
        encode(obj.meta(), writer, indent);
        writer.append(' ');
        encode(obj.obj(), writer, indent);
    }

    private long[] byteArrayToLongArray(byte[] a) {
        return IntStream.range(0, a.length).mapToLong(i -> a[i]).toArray();
    }

    private long[] shortArrayToLongArray(short[] a) {
        return IntStream.range(0, a.length).mapToLong(i -> a[i]).toArray();
    }

    private double[] floatArrayToDoubleArray(float[] a) {
        return IntStream.range(0, a.length).mapToDouble(i -> a[i]).toArray();
    }

    private void encodeAnything(String string, Appendable writer) throws IOException {
         writerAppend(writer, string);
    }

    private void encodeInstant(Instant i, Appendable writer, int indent)throws IOException {
        writerAppend(writer,"#inst \"" + i + '"');
    }

    private void encodeUuid(UUID u, Appendable writer, int indent)throws IOException {
        writerAppend(writer,"#uuid \"" + u + '"');
    }

    private void encodeDouble(Double n, Appendable writer)throws IOException {
        if (n.isNaN()) writerAppend(writer,"##NaN");
        else if (n < 0.0 && n.isInfinite()) writerAppend(writer,"##-INF");
        else if (n.isInfinite()) writerAppend(writer,"##INF");
        else writerAppend(writer,n.toString());
    }

    private void encodeFloat(Float n, Appendable writer)throws IOException {
        if (n.isNaN()) writerAppend(writer,"##NaN");
        else if (n < 0.0 && n.isInfinite()) writerAppend(writer,"##-INF");
        else if (n.isInfinite()) writerAppend(writer,"##INF");
        else writerAppend(writer,n.toString());
    }

    private void encodePredefinedNumberType(Number n, Appendable writer)throws IOException {
        StringBuilder sb = new StringBuilder();
        switch(n) {
            case Byte ignored -> sb.append(n.toString());
            case Short ignored -> sb.append(n.toString());
            case Integer ignored -> sb.append(n.toString());
            case Long ignored -> sb.append(n.toString());
            case BigInteger ignored -> {
                sb.append(n.toString());
                sb.append('N');
            }
            case BigDecimal ignored -> {
                sb.append(n.toString());
                sb.append('M');
            }
            default -> sb.append(n.toString());}
        if (options.allowNumericSuffixes()) {
            switch(n) {
                case Byte  b->sb.append("_i8");
                case Short ignored->sb.append("_i16");
                case Integer ignored->sb.append("_i32");
                default -> {}
            }
        }
        writerAppend(writer, sb.toString());
    }

    private void encodeChar32(Char32 c, Appendable writer) throws IOException{
        if (!options.allowDispatchChars()) {
            writerAppend(writer, '"' + c.toString() + '"');
        }
        int code = c.code();
        String s = switch (code) {
            case '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4',
                 '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '\\', '^',
                 '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '|', '~', '§', '°', '´', '€' -> "#\\"+ code;
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

    private void encodeChar(Character code, Appendable writer)throws IOException {
        String s = switch (code) {
            case '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4',
                 '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '\\', '^',
                 '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '|', '~', '§', '°', '´', '€' -> "\\"+ code;
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

    private void encodeOtherIterable(Iterable<?> i, Appendable writer, int indent) throws IOException{
        var list = new ArrayList<>();
        for (var e : i)
            list.add(i);
        formatCollectionTo(list, "(", ")", writer, indent);
    }

    private void encodeMap(Map<?, ?> m, Appendable writer, int indent)throws IOException {
        formatCollectionTo(m.entrySet().stream().toList(), "{", "}", writer, indent, true);
    }

    private void encodeSet(Set<?> s, Appendable writer, int indent)throws IOException {
        formatCollectionTo(s.stream().toList(), "#{", "}", writer, indent);
    }

    private void encodeVector(List<?> l, Appendable writer, int indent)throws IOException {
        formatCollectionTo(l, "[", "]", writer, indent);
    }

    private void encodePersistentList(EdnaCollections.EdnaList<?> l, Appendable writer, int indent)throws IOException {
        formatCollectionTo(l, "(", ")", writer, indent);
    }

    private void encodeSymbol(Symbol s, Appendable writer) throws IOException {
        writerAppend(writer, s.toString());
    }

    private void encodeKeyword(Keyword k, Appendable writer) throws IOException {
        writerAppend(writer, k.toString());
    }

    private void encodeString(String s, Appendable writer) throws IOException {
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

    private void orIfNullDo(Object v, Supplier<Object> lazy) {
        if (v == null) lazy.get();
    }

    private void encodeNull(Appendable writer) throws IOException {
         writerAppend(writer, "nil");
    }

    private void encodeBool(boolean b, Appendable writer) throws IOException {
         writerAppend(writer, String.valueOf(b));
    }

    private static void writerAppend(Appendable writer, String s) throws IOException {
        writer.append(s);
    }

    private void formatCollectionTo(List<?> l, String open, String close, Appendable writer, int indent, boolean isMap) throws IOException {
        indent = options.encoderPrettyPrint() ? indent : 0;

        // Try inline first (dry-run)
        var tmp = new StringBuilder();
        tmp.append(open);
        if (isMap) {
            var entryIterator = l.iterator();
            for (int i = 0; i < l.size(); i++) {
                var e = (Map.Entry<?,?>) entryIterator.next();
                encode(e.getKey(), tmp, indent+1);
                tmp.append(' ');
                encode(e.getValue(), tmp, indent+1);
                if (i != l.size()) tmp.append(options.encodingSequenceSeparator());
            }
        } else {
            var entryIterator = l.iterator();
            for (int i = 0; i < l.size(); i++) {
                var e = entryIterator.next();
                encode(e, tmp, indent+1);
                if (i != l.size()) tmp.append(options.encodingSequenceSeparator());
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
        var childIndent = indent +1 ;
        var pad = options.encoderLineIndent().repeat(childIndent);
        if (isMap) {
            var entryIterator = l.iterator();
            for (int i = 0; i < l.size(); i++) {
                var e = (Map.Entry<?,?>) entryIterator.next();
                appendIfPrettyEnabled(writer, pad);
                encode(e.getKey(), tmp, indent+1);
                tmp.append(' ');
                encode(e.getValue(), tmp, indent+1);
                if (i != l.size()) {
                    tmp.append(options.encodingSequenceSeparator());
                    appendIfPrettyEnabled(writer, "\n");
                }
            }
        } else {
            var entryIterator = l.iterator();
            for (int i = 0; i < l.size(); i++) {
                var e = entryIterator.next();
                appendIfPrettyEnabled(writer, pad);
                encode(e, tmp, indent+1);
                if (i != l.size()) {
                    tmp.append(options.encodingSequenceSeparator());
                    appendIfPrettyEnabled(writer, "\n");
                }
            }
        }
        appendIfPrettyEnabled(writer, "\n");
        appendIfPrettyEnabled(writer, options.encoderLineIndent().repeat(indent)).append(close);
    }

    private void formatCollectionTo(List<?> l, String open, String close, Appendable writer, int indent) throws IOException { formatCollectionTo(l,open,close, writer, indent, false);
    }
}

/*
    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodePersistentList(obj: EdnList<*>, writer: Appendable, indent: Int) {
        formatCollectionTo(obj, "(", ")", writer, indent)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeVector(obj: List<*>, writer: Appendable, indent: Int) {
        formatCollectionTo(obj, "[", "]", writer, indent)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeSet(obj: Set<*>, writer: Appendable, indent: Int) {
        formatCollectionTo(obj.toList(), "#{", "}", writer, indent)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeSequence(obj: Sequence<*>, writer: Appendable, indent: Int) {
        formatCollectionTo(obj.toList(), "(", ")", writer, indent)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeOtherIterable(obj: Iterable<*>, writer: Appendable, indent: Int) {
        formatCollectionTo(obj.toList(), "(", ")", writer, indent)
    }

//    private fun join(elements:Iterable<*>,
//                     buffer:Appendable,
//                     separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", indent:Int=0): Appendable {
//        buffer.append(prefix)
//        var count = 0
//        for (element in elements) {
//            if (++count > 1) buffer.append(separator)
//            if (limit < 0 || count <= limit) {
//                if (element is Map.Entry<*,*>) {
//                    encode(element.key, buffer, indent + 1)
//                    buffer.append(' ')
//                    encode(element.value, buffer, indent + 1)
//                }
//                else {
//                    encode(element, buffer, indent + 1)
//                }
//            } else break
//        }
//        if (limit in 0..<count) buffer.append(truncated)
//        buffer.append(postfix)
//        return buffer
//    }

    private fun formatCollectionTo(
        elements: List<*>, open: String, close: String, writer: Appendable, indent: Int, isMap: Boolean = false
    ) {
        val indent =
            if (options.encoderPrettyPrint) indent else 0
        // Try inline first (dry-run)
        val tmp = StringBuilder()
        tmp.append(open)
        if (isMap) {
            for ((i, entry) in elements.withIndex()) {
                val e = entry as Map.Entry<*, *>
                encode(e.key, tmp, indent + 1)
                tmp.append(' ')
                encode(e.value, tmp, indent + 1)
                if (i != elements.lastIndex) tmp.append(options.encodingSequenceSeparator)
            }
        } else {
            for ((i, e) in elements.withIndex()) {
                encode(e, tmp, indent + 1)
                if (i != elements.lastIndex) tmp.append(options.encodingSequenceSeparator)
            }
        }
        tmp.append(close)

        if (tmp.length + indent <= options.encoderMaxColumn) {
            writer.append(tmp)
            return
        }

        // Multi-line formatting
        writer.append(open)
        appendIfPrettyEnabled(writer, "\n")
        val childIndent = indent + 1
        val pad = options.encoderLineIndent.repeat(childIndent)
        if (isMap) {
            for ((i, entry) in elements.withIndex()) {
                val e = entry as Map.Entry<*, *>
                appendIfPrettyEnabled(writer, pad)
                encode(e.key, writer, childIndent)
                writer.append(' ')
                encode(e.value, writer, childIndent)
                if (i != elements.lastIndex) {
                    writer.append(options.encodingSequenceSeparator)
                    appendIfPrettyEnabled(writer, "\n")
                }
            }
        } else {
            for ((i, e) in elements.withIndex()) {
                appendIfPrettyEnabled(writer, pad)
                encode(e, writer, childIndent)
                if (i != elements.lastIndex) {
                    writer.append(options.encodingSequenceSeparator)
                    appendIfPrettyEnabled(writer, "\n")
                }
            }
        }
        appendIfPrettyEnabled(writer, "\n")
        appendIfPrettyEnabled(writer, options.encoderLineIndent.repeat(indent)).append(close)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeMap(obj: Map<*, *>, writer: Appendable, indent: Int) =
        formatCollectionTo(obj.entries.toList(), "{", "}", writer, indent, true)

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeNull(): CharSequence = "nil"

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeBool(b: Boolean): CharSequence = if (b) "true" else "false"

    private fun encodeString(obj: String): CharSequence {
        val writer = StringBuilder()
        writer.append('"')
        for (code in obj) {
            when (code) {
                '\t' -> writer.append("\\t")
                '\b' -> writer.append("\\b")
                '\n' -> writer.append("\\n")
                '\r' -> writer.append("\\r")
                '\"' -> writer.append("\\\"")
                '\\' -> writer.append("\\\\")
                else -> writer.append(code)
            }
        }
        writer.append('"')
        return writer
    }

    private fun encodeIObj(obj: IObj<*>, writer: Appendable, indent: Int) {
        writer.append('^')
        encode(obj.meta, writer, indent)
        writer.append(' ')
        encode(obj.obj, writer, indent)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeKeyword(obj: Keyword): CharSequence = obj.toString()

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeSymbol(obj: Symbol): CharSequence = obj.toString()

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeUuid(obj: UUID): CharSequence = "#uuid \"$obj\""

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeInstant(obj: Instant): CharSequence = "#inst \"$obj\""

    private fun encodeChar(obj: Char): CharSequence = when (obj) {
        '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        '\\', '^', '_', '`',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '|', '~', '§', '°', '´', '€' -> "\\$obj"

        '\n' -> "\\newline"
        ' ' -> "\\space"
        '\t' -> "\\tab"
        '\b' -> "\\backspace"
        12.toChar() -> "\\formfeed"
        '\r' -> "\\return"

        else -> String.format("\\u%04x", obj.code)
    }

    private fun encodeChar32(obj: Char32): CharSequence {
        if (!options.allowDispatchChars) {
            return "\"$obj\""
        }

        return when (obj.code) {
            '!'.code, '"'.code, '#'.code, '$'.code, '%'.code, '&'.code, '\''.code, '('.code, ')'.code, '*'.code, '+'.code,
            ','.code, '-'.code, '.'.code, '/'.code,
            '0'.code, '1'.code, '2'.code, '3'.code, '4'.code, '5'.code, '6'.code, '7'.code, '8'.code, '9'.code,
            ':'.code, ';'.code, '<'.code, '='.code, '>'.code, '?'.code, '@'.code,
            'A'.code, 'B'.code, 'C'.code, 'D'.code, 'E'.code, 'F'.code, 'G'.code, 'H'.code, 'I'.code, 'J'.code, 'K'.code,
            'L'.code, 'M'.code, 'N'.code, 'O'.code, 'P'.code, 'Q'.code, 'R'.code, 'S'.code, 'T'.code, 'U'.code, 'V'.code,
            'W'.code, 'X'.code, 'Y'.code, 'Z'.code,
            '\\'.code, '^'.code, '_'.code, '`'.code,
            'a'.code, 'b'.code, 'c'.code, 'd'.code, 'e'.code, 'f'.code, 'g'.code, 'h'.code, 'i'.code, 'j'.code, 'k'.code,
            'l'.code, 'm'.code, 'n'.code, 'o'.code, 'p'.code, 'q'.code, 'r'.code, 's'.code, 't'.code, 'u'.code, 'v'.code,
            'w'.code, 'x'.code, 'y'.code, 'z'.code,
            '|'.code, '~'.code, '§'.code, '°'.code, '´'.code, '€'.code
                -> "#\\$obj"

            '\n'.code -> "#\\newline"
            ' '.code -> "#\\space"
            '\t'.code -> "#\\tab"
            '\b'.code -> "#\\backspace"
            12 -> "#\\formfeed"
            '\r'.code -> "#\\return"

            else -> String.format("#\\u%08x", obj.code)
        }
    }

    private fun encodePredefinedNumberType(obj: Number): CharSequence {
        val writer = StringBuilder()
        when (obj) {
            is Byte, is Short, is Int, is Long, is Ratio -> writer.append(obj.toString())
            is BigInteger -> return "${obj}N"
            is BigDecimal -> return "${obj}M"
            else -> writer.append(obj.toString())
        }
        if (options.allowNumericSuffixes) {
            when (obj) {
                is Byte -> writer.append("_i8")
                is Short -> writer.append("_i16")
                is Int -> writer.append("_i32")
            }
        }
        return writer
    }

    private fun encodeFloat(obj: Float): CharSequence =
        if (obj.isNaN()) "##NaN"
        else if (obj == Float.POSITIVE_INFINITY) "##INF"
        else if (obj == Float.NEGATIVE_INFINITY) "##-INF"
        else obj.toString()

    private fun encodeDouble(obj: Double): CharSequence =
        if (obj.isNaN()) "##NaN"
        else if (obj == Double.POSITIVE_INFINITY) "##INF"
        else if (obj == Double.NEGATIVE_INFINITY) "##-INF"
        else obj.toString()
}
 */
