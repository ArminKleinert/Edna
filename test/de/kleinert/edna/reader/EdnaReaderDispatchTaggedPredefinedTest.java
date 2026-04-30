package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaConverters;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.util.Map;
import java.util.function.Function;

class EdnaReaderDispatchTaggedPredefinedTest {
    Object parse(String s) {
        Map<String, Function<Object, Object>> decoders = EdnaConverters.arrayConverters();
        var opts = Edna.defaultOptions().copy(b -> b.allowMoreEncoderDecoderNames(true).ednClassDecoders(decoders));
        return Edna.read(s, opts);
    }

    @Test
    void parseArrayDecodersFailureTest() {
        // Wrong type
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, () -> parse("#edna/bytearray \"\""));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, () -> parse("#edna/shortarray \"\""));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, () -> parse("#edna/intarray \"\""));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, () -> parse("#edna/longarray \"\""));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#edna/floatarray \"\""));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#edna/doublearray \"\""));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#edna/bigintarray \"\""));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#edna/bigdecimalarray \"\""));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, () -> parse("#edna/stringarray \"\""));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, () -> parse("#edna/array \"\""));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#edna/array2d \"\""));

        // Wrong type
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, () -> parse("#edna/bytearray [a b]"));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, () -> parse("#edna/shortarray [a b]"));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, () -> parse("#edna/intarray [a b]"));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, () -> parse("#edna/longarray [a b]"));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#edna/floatarray [a b]"));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#edna/doublearray [a b]"));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#edna/bigintarray [a b]"));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#edna/bigdecimalarray [a b]"));
        Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#edna/array2d [a b]"));
    }

    @Test
    void parseNumericArrayDecodersTest() {
        Assertions.assertArrayEquals(new byte[]{0, 1, 2, 3}, (byte[]) parse("#edna/bytearray [0 1 2 3]"));
        Assertions.assertArrayEquals(new short[]{0, 1, 2, 3}, (short[]) parse("#edna/shortarray [0 1 2 3]"));
        Assertions.assertArrayEquals(new int[]{0, 1, 2, 3}, (int[]) parse("#edna/intarray [0 1 2 3]"));
        Assertions.assertArrayEquals(new long[]{0, 1, 2, 3}, (long[]) parse("#edna/longarray [0 1 2 3]"));
        Assertions.assertArrayEquals(new String[]{"0", "1", "2", "3"}, (String[]) parse("#edna/stringarray [\"0\" \"1\" \"2\" \"3\"]"));
        Assertions.assertArrayEquals(new Object[]{0L, "1", 2L, "3"}, (Object[]) parse("#edna/array [0 \"1\" 2 \"3\"]"));
    }
}
/*

class EDNReaderDispatchTaggedPredefinedTest {
    private fun parse(s: String) =
        EDN.read(
            s,
            EDNSoupOptions.defaultOptions.copy(
                allowMoreEncoderDecoderNames = true,
                ednClassDecoders = ExtendedEDNDecoders.arrayDecoders + ExtendedEDNDecoders.listDecoders + ExtendedEDNDecoders.prettyDecoders
            )
        )

@Test
fun parseArrayDecodersFailureTest() {
    // Wrong type
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#bytearray \"\""));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#shortarray \"\""));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#intarray \"\""));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#longarray \"\""));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#floatarray \"\""));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#doublearray \"\""));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#bigintarray \"\""));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#bigdecimalarray \"\""));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#stringarray \"\""));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#array \"\""));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#array2d \"\""));

    // Wrong type
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#bytearray [a b]"));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#shortarray [a b]"));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#intarray [a b]"));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#longarray [a b]"));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#floatarray [a b]"));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#doublearray [a b]"));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#bigintarray [a b]"));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#bigdecimalarray [a b]"));
    Assertions.assertThrows(EdnaReaderException.EdnClassConversionError.class, ()-> parse("#array2d [a b]"));
}

@Test
fun parseNumericArrayDecodersTest() {
    Assertions.assertArrayEquals(ByteArray(4) { it.toByte());, parse("#bytearray [0 1 2 3]") as ByteArray)
    Assertions.assertArrayEquals(ShortArray(4) { it.toShort());, parse("#shortarray [0 1 2 3]") as ShortArray)
    Assertions.assertArrayEquals(IntArray(4) { it }, parse("#intarray [0 1 2 3]") as IntArray)
    Assertions.assertArrayEquals(LongArray(4) { it.toLong());, parse("#longarray [0 1 2 3]") as LongArray)
    Assertions.assertArrayEquals(Array(4) { it.toBigInteger());, parse("#bigintarray [0 1 2 3]") as Array<*>)

    Assertions.assertArrayEquals(FloatArray(4) { it.toFloat());, parse("#floatarray [0 1 2 3]") as FloatArray)
    Assertions.assertArrayEquals(
            FloatArray(4) { it.toFloat());,
    parse("#floatarray [0.0 1.0 2.0 3.0]") as FloatArray
        )
    Assertions.assertArrayEquals(DoubleArray(4) { it.toDouble());, parse("#doublearray [0 1 2 3]") as DoubleArray)
    Assertions.assertArrayEquals(
            DoubleArray(4) { it.toDouble());,
    parse("#doublearray [0.0 1.0 2.0 3.0]") as DoubleArray
        )
    Assertions.assertArrayEquals(
            Array(4) { it.toLong().toBigDecimal());,
    parse("#bigdecimalarray [0 1 2 3]") as Array<*>
        )
    Assertions.assertArrayEquals(
            arrayOf(BigDecimal("0.0"), BigDecimal("1.0"), BigDecimal(2L), BigDecimal(3)),
            parse("#bigdecimalarray [0.0 1.0M 2M 3]") as Array<*>
    )
}

@Test
fun parseArrayDecodersTest() {
    Assertions.assertArrayEquals(
            Array(0) { it.toString());,
    parse("#stringarray []") as Array<*>
        )
    Assertions.assertArrayEquals(
            Array(4) { it.toString());,
    parse("#stringarray [0 1 2 3]") as Array<*>
        )

    Assertions.assertArrayEquals(
            Array(4) { it.toString());,
    parse("#stringarray [\"0\" \"1\" \"2\" \"3\"]") as Array<*>
        )

    Assertions.assertArrayEquals(
            arrayOf<Any?>(),
            parse("#array []") as Array<*>
    )

    Assertions.assertArrayEquals(
            arrayOf(Symbol.symbol("a"), Keyword.parse(":a"), 1L),
            parse("#array [a :a 1]") as Array<*>
    )

    Assertions.assertArrayEquals(
            arrayOf(Symbol.symbol("a"), Keyword.parse(":a"), 1L),
            parse("#array [a :a 1]") as Array<*>
    )

    Assertions.assertArrayEquals(
            arrayOf<Array<Any?>>(),
            parse("#array2d []") as Array<*>
    )

    Assertions.assertArrayEquals(
            arrayOf<Array<Any?>>(arrayOf()),
            parse("#array2d [[]]") as Array<*>
    )

    Assertions.assertArrayEquals(
            arrayOf<Array<Any?>>(arrayOf(1L), arrayOf(2L, 3L), arrayOf(4L, 5L, 6L)),
            parse("#array2d [[1] [2 3] [4 5 6]]") as Array<*>
        )
}
}
 */