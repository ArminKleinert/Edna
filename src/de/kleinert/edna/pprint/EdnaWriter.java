package de.kleinert.edna.pprint;

import de.kleinert.edna.EdnaOptions;
import de.kleinert.edna.data.Char32;
import de.kleinert.edna.data.EdnaCollections;
import de.kleinert.edna.data.Keyword;
import de.kleinert.edna.data.Symbol;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

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
    }

    public static void pprint(Object obj, EdnaOptions options) throws IOException {
        pprint(obj, options, System.out);
    }

    public static void pprint(Object obj) throws IOException {
        pprint(obj, EdnaOptions.defaultOptions());
    }

    private Appendable appendIfPrettyEnabled(CharSequence cs, Appendable writer) throws IOException {
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

        
        if (obj == null)
            encodeNull(writer);
        else if (obj instanceof Boolean b)
            encodeBool(b, writer);
        else if (obj instanceof String s)
            encodeString(s, writer);
        else if (obj instanceof Keyword k) {
            if (!tryEncoder(k, writer, indent))
                encodeKeyword(k, writer);
        } else if (obj instanceof Symbol s) {
            if (!tryEncoder(s, writer, indent))
                encodeSymbol(s, writer);
        } else if (obj instanceof EdnaCollections.EdnaList<?> l) {
            if (!tryEncoder(l, writer, indent))
                encodePersistentList(l, writer);
        } else if (obj instanceof List<?> l) {
            if (!tryEncoder(l, writer, indent))
                encodeVector(l, writer);
        } else if (obj instanceof Set<?> s) {
            if (!tryEncoder(s, writer, indent))
                encodeSet(s, writer);
        } else if (obj instanceof Map<?, ?> m) {
            if (!tryEncoder(m, writer, indent))
                encodeMap(m, writer);
        } else if (obj instanceof Iterable<?> i) {
            if (!tryEncoder(i, writer, indent))
                encodeOtherIterable(i, writer);
        } else if (obj instanceof Character c)
            encodeChar(c, writer);
        else if (obj instanceof Char32 c)
            encodeChar32(c, writer);
        else if (obj instanceof Byte n)
            encodePredefinedNumberType(n, writer);
        else if (obj instanceof Short n)
            encodePredefinedNumberType(n, writer);
        else if (obj instanceof Integer n)
            encodePredefinedNumberType(n, writer);
        else if (obj instanceof Long n)
            encodePredefinedNumberType(n, writer);
        else if (obj instanceof Float n)
            encodeFloat(n, writer);
        else if (obj instanceof Double n)
            encodeDouble(n, writer);
        else if (obj instanceof BigInteger n)
            encodePredefinedNumberType(n, writer);
        else if (obj instanceof BigDecimal n)
            encodePredefinedNumberType(n, writer);

        else if (obj instanceof UUID u)
            encodeUuid(u, writer);
        else if (obj instanceof Instant i)
            encodeInstant(i, writer);

        else if (obj instanceof byte[] a) {
            if (!tryEncoder(a, writer, indent))
                encode(Arrays.stream(byteArrayToLongArray(a)).boxed().toList(), writer, finalIndent);
        } else if (obj instanceof short[] a) {
            if (!tryEncoder(a, writer, indent))
                encode(Arrays.stream(shortArrayToLongArray(a)).boxed().toList(), writer, finalIndent);
        } else if (obj instanceof int[] a) {
            if (!tryEncoder(a, writer, indent)) encode(Arrays.stream(a).boxed().toList(), writer, finalIndent);
        } else if (obj instanceof long[] a) {
            if (!tryEncoder(a, writer, indent)) encode(Arrays.stream(a).boxed().toList(), writer, finalIndent);
        } else if (obj instanceof float[] a) {
            if (!tryEncoder(a, writer, indent))
                encode(Arrays.stream(floatArrayToDoubleArray(a)).boxed().toList(), writer, finalIndent);
        } else if (obj instanceof double[] a) {
            if (!tryEncoder(a, writer, indent)) encode(Arrays.stream(a).boxed().toList(), writer, finalIndent);
        } else if (!tryEncoder(obj, writer, indent))
            encodeAnything(obj.toString(), writer);
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

    private Object encodeAnything(String string, Appendable writer) {
        return writerAppend(writer, string);
    }

    private String encodeInstant(Instant i, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeUuid(UUID u, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeDouble(Double n, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeFloat(Float n, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodePredefinedNumberType(Number n, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeChar32(Char32 c, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeChar(Character c, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeOtherIterable(Iterable<?> i, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeMap(Map<?, ?> m, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeSet(Set<?> s, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeVector(List<?> l, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodePersistentList(EdnaCollections.EdnaList<?> l, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeSymbol(Symbol s, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeKeyword(Keyword k, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private String encodeString(String s, Appendable writer) {
        throw new UnsupportedOperationException();
        //var res =       ;
        //return writerAppend(writer, res);
    }

    private void orIfNullDo(Object v, Supplier<Object> lazy) {
        if (v == null) lazy.get();
    }

    private String encodeNull(Appendable writer) {
        return writerAppend(writer, "null");
    }

    private String encodeBool(boolean b, Appendable writer) throws IOException {
        return writerAppend(writer, String.valueOf(b));
    }

    private static String writerAppend(Appendable writer, String s) {
        try {
            writer.append(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return s;
    }
}

/*
    fun encode(
        obj: Any?,
        writer: Appendable,
        indent: Int = 0
    ) {
        val indent =
            if (options.encoderPrettyPrint) indent else 0
        when (obj) {
            null -> encodeNull())
            true -> encodeBool(true))
            false -> encodeBool(false))

            is String -> encodeString(obj))
            is Keyword -> tryEncoder(obj, writer, indent) ?: encodeKeyword(obj))
            is Symbol -> tryEncoder(obj, writer, indent) ?: encodeSymbol(obj))

            is EdnList<*> ->
                tryEncoder(obj, writer, indent) ?: encodePersistentList(obj, writer, indent) // List, not vector

            is List<*> -> tryEncoder(obj, writer, indent) ?: encodeVector(obj, writer, indent) // Vector
            is Array<*> -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector

            is Set<*> -> tryEncoder(obj, writer, indent) ?: encodeSet(obj, writer, indent)
            is Map<*, *> -> tryEncoder(obj, writer, indent) ?: encodeMap(obj, writer, indent)

            is Iterable<*> -> tryEncoder(obj, writer, indent) ?: encodeOtherIterable(obj, writer, indent)
            is Sequence<*> -> tryEncoder(obj, writer, indent) ?: encodeSequence(obj, writer, indent)

            is Char -> encodeChar(obj))
            is Char32 -> encodeChar32(obj))
            is Byte, is Short, is Int, is Long, is Ratio -> encodePredefinedNumberType(obj as Number))
            is Float -> encodeFloat(obj))
            is Double -> encodeDouble(obj))
            is BigInteger, is BigDecimal -> encodePredefinedNumberType(obj as Number))

            is IObj<*> -> encodeIObj(obj, writer, indent)

            is UUID -> encodeUuid(obj))
            is Instant -> encodeInstant(obj))

            is ByteArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is ShortArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is IntArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is LongArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is FloatArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is DoubleArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector

            else -> tryEncoder(obj, writer, indent) ?: writer.append(obj.toString())
        }
    }

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
