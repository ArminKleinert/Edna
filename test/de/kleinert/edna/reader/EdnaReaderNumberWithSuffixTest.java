package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class EdnaReaderNumberWithSuffixTest {

    @Test
    public void parseIntegerDecimal() {
        var options = EdnaOptions.defaultOptions().copy((b) -> b.allowNumericSuffixes(true));

        Map<Number, String> numbers = Map.of(
                (byte) 0, "0_i8", (short) 0, "0_i16", 0, "0_i32", 0L, "0_i64",
                (byte) 1, "1_i8", (short) 1, "1_i16", 1, "1_i32", 1L, "1_i64"
        );

        for (Map.Entry<Number, String> numberStringEntry : numbers.entrySet()) {
            Assertions.assertEquals(
                    numberStringEntry.getKey(),
                    Edna.read(numberStringEntry.getValue(), options)
            );
        }

        Map<Number, String> numbers2 = Map.of(
                (byte) -1, "-1_i8", (short) -1, "-1_i16", -1, "-1_i32", -1L, "-1_i64",
                (byte) 128, "128_i8", (short) 128, "128_i16", 128, "128_i32", 128L, "128_i64");

        for (Map.Entry<Number, String> numberStringEntry : numbers2.entrySet()) {
            Assertions.assertEquals(
                    numberStringEntry.getKey(),
                    Edna.read(numberStringEntry.getValue(), options)
            );
        }
    }

    @Test
    public void parseIntegerOctal() {
//        val numberPairs = listOf(
//                0.toByte() to "0o0_i8", 0.toShort() to "0o0_i16", 0 to "0o0_i32", 0L to "0o0L", 0L to "0o0_i64",
//                1.toByte() to "0o1_i8", 1.toShort() to "0o1_i16", 1 to "0o1_i32", 1L to "0o1L", 1L to "0o1_i64",
//
//                (-1).toByte() to "-0o1_i8", (-1).toShort() to "-0o1_i16", (-1) to "-0o1_i32",
//                (-1L) to "-0o1L", (-1L) to "-0o1_i64",
//
//                128.toByte() to "0o200_i8", 128.toShort() to "0o200_i16", 128 to "0o200_i32",
//                128L to "0o200L", 128L to "0o200_i64",
//                )
//        for ((num, str) in numberPairs) {
//            Assertions.assertEquals(
//                    num,
//                    EDN.read(str, EdnaOptions.defaultOptions().copy(allowNumericSuffixes = true, moreNumberPrefixes = true))
//            )
//        }
    }

    @Test
    public void parseIntegerBinary() {
//        val numberPairs = listOf(
//                0.toByte() to "0b0_i8", 0.toShort() to "0b0_i16", 0 to "0b0_i32", 0L to "0b0L", 0L to "0b0_i64",
//                1.toByte() to "0b1_i8", 1.toShort() to "0b1_i16", 1 to "0b1_i32", 1L to "0b1L", 1L to "0b1_i64",
//
//                (-1).toByte() to "-0b1_i8", (-1).toShort() to "-0b1_i16", (-1) to "-0b1_i32",
//                (-1L) to "-0b1L", (-1L) to "-0b1_i64",
//
//                128.toByte() to "0b10000000_i8", 128.toShort() to "0b10000000_i16", 128 to "0b10000000_i32",
//                128L to "0b10000000L", 128L to "0b10000000_i64",
//                )
//        for ((num, str) in numberPairs) {
//            Assertions.assertEquals(
//                    num,
//                    EDN.read(str, EdnaOptions.defaultOptions().copy(allowNumericSuffixes = true, moreNumberPrefixes = true))
//            )
//        }
    }

    @Test
    public void parseIntegerHex() {
//        val numberPairs = listOf(
//                0.toByte() to "0x0_i8", 0.toShort() to "0x0_i16", 0 to "0x0_i32", 0L to "0x0L", 0L to "0x0_i64",
//                1.toByte() to "0x1_i8", 1.toShort() to "0x1_i16", 1 to "0x1_i32", 1L to "0x1L", 1L to "0x1_i64",
//
//                (-1).toByte() to "-0x1_i8", (-1).toShort() to "-0x1_i16", (-1) to "-0x1_i32",
//                (-1L) to "-0x1L", (-1L) to "-0x1_i64",
//
//                128.toByte() to "0x80_i8", 128.toShort() to "0x80_i16", 128 to "0x80_i32",
//                128L to "0x80L", 128L to "0x80_i64",
//                )
//        for ((num, str) in numberPairs) {
//            Assertions.assertEquals(
//                    num,
//                    EDN.read(str, EdnaOptions.defaultOptions().copy(moreNumberPrefixes = true, allowNumericSuffixes = true))
//            )
//        }
    }
}
