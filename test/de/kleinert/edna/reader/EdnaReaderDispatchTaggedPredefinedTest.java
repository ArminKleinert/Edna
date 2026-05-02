package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaConverters;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.function.Function;

class EdnaReaderDispatchTaggedPredefinedTest {
    private Object parse(String s) {
        @NotNull Map<String, Function<Object, Object>> decoders = EdnaConverters.merge(
                        EdnaConverters.arrayConverters(),
                        EdnaConverters.numberConverters());
        var opts = Edna.defaultOptions().copy(b -> b.allowMoreEncoderDecoderNames(true).taggedElementDecoders(decoders));
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

    @Test void parseNumberIntegralDecodersTest() {
        Assertions.assertEquals((byte) 0, parse("#edna/byte 0"));
        Assertions.assertEquals((byte) 12, parse("#edna/byte 12"));
        Assertions.assertEquals((byte) -12, parse("#edna/byte -12"));

        Assertions.assertEquals((short) 0, parse("#edna/short 0"));
        Assertions.assertEquals((short) 12, parse("#edna/short 12"));
        Assertions.assertEquals((short) -12, parse("#edna/short -12"));

        Assertions.assertEquals(0, parse("#edna/int 0"));
        Assertions.assertEquals(12, parse("#edna/int 12"));
        Assertions.assertEquals(-12, parse("#edna/int -12"));

        Assertions.assertEquals(0L, parse("#edna/long 0"));
        Assertions.assertEquals(12L, parse("#edna/long 12"));
        Assertions.assertEquals(-12L, parse("#edna/long -12"));
    }

    @Test void parseNumberFloatyDecodersTest() {
        Assertions.assertEquals((float) 0, parse("#edna/float 0"));
        Assertions.assertEquals((float) 12.5, parse("#edna/float 12.5"));
        Assertions.assertEquals((float) -12, parse("#edna/float -12"));

        Assertions.assertEquals((double) 0, parse("#edna/double 0"));
        Assertions.assertEquals((double) 12.5, parse("#edna/double 12.5"));
        Assertions.assertEquals((double) -12, parse("#edna/double -12"));
    }

    @Test void parseNumberBigDecodersTest() {
        Assertions.assertEquals(BigInteger.valueOf(0), parse("#edna/bigint 0"));
        Assertions.assertEquals(BigInteger.valueOf(12), parse("#edna/bigint 12"));
        Assertions.assertEquals(BigInteger.valueOf(-12), parse("#edna/bigint -12"));

        Assertions.assertEquals(BigDecimal.valueOf(0), parse("#edna/bigdec 0"));
        Assertions.assertEquals(BigDecimal.valueOf(12.5), parse("#edna/bigdec 12.5"));
        Assertions.assertEquals(BigDecimal.valueOf(-12), parse("#edna/bigdec -12"));
    }
}