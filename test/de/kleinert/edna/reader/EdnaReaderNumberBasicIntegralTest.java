package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class EdnaReaderNumberBasicIntegralTest {
    private final EdnaOptions allowMoreNumberPrefixes = Edna.defaultOptions().copy((b) -> b.moreNumberPrefixes(true));
    private final EdnaOptions allowZeros = Edna.defaultOptions().copy((b) -> b.allowZeroPrefix(true));

    @Test
    public void parseIntegerDecimal() {
        {
            var it = Edna.read("0");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("00", allowZeros);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("+0");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("-0");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }

        {
            var it = Edna.read("1");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(1L, it);
        }
        {
            var it = Edna.read("+1");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(1L, it);
        }
        {
            var it = Edna.read("-1");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-1L, it);
        }

        {
            var it = Edna.read("128");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(128L, it);
        }
        {
            var it = Edna.read("+128");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(128L, it);
        }
        {
            var it = Edna.read("-128");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-128L, it);
        }

        {
            var it = Edna.read("255");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(255L, it);
        }
        {
            var it = Edna.read("+255");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(255L, it);
        }
        {
            var it = Edna.read("-255");
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-255L, it);
        }
    }

    @Test
    public void parseIntegerOctal() {
        {
            var it = Edna.read("0o0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("+0o0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("-0o0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }

        {
            var it = Edna.read("0o1", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(1L, it);
        }
        {
            var it = Edna.read("+0o1", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(1L, it);
        }
        {
            var it = Edna.read("-0o1", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-1L, it);
        }

        {
            var it = Edna.read("0o200", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(128L, it);
        }
        {
            var it = Edna.read("+0o200", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(128L, it);
        }
        {
            var it = Edna.read("-0o200", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-128L, it);
        }

        {
            var it = Edna.read("0o377", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(255L, it);
        }
        {
            var it = Edna.read("+0o377", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(255L, it);
        }
        {
            var it = Edna.read("-0o377", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-255L, it);
        }
    }

    @Test
    public void parseIntegerBinary() {
        {
            var it = Edna.read("0b0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("+0b0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("-0b0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }

        {
            var it = Edna.read("0b1", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(1L, it);
        }
        {
            var it = Edna.read("+0b1", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(1L, it);
        }
        {
            var it = Edna.read("-0b1", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-1L, it);
        }

        {
            var it = Edna.read("0b10000000", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(128L, it);
        }
        {
            var it = Edna.read("+0b10000000", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(128L, it);
        }
        {
            var it = Edna.read("-0b10000000", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-128L, it);
        }

        {
            var it = Edna.read("0b11111111", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(255L, it);
        }
        {
            var it = Edna.read("+0b11111111", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(255L, it);
        }
        {
            var it = Edna.read("-0b11111111", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-255L, it);
        }
    }

    @Test
    public void parseIntegerHex() {
        {
            var it = Edna.read("0x0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("+0x0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("-0x0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }

        {
            var it = Edna.read("0x1", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(1L, it);
        }
        {
            var it = Edna.read("+0x1", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(1L, it);
        }
        {
            var it = Edna.read("-0x1", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-1L, it);
        }

        {
            var it = Edna.read("0x80", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(128L, it);
        }
        {
            var it = Edna.read("+0x80", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(128L, it);
        }
        {
            var it = Edna.read("-0x80", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-128L, it);
        }

        {
            var it = Edna.read("0xFF", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(255L, it);
        }
        {
            var it = Edna.read("+0xFF", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(255L, it);
        }
        {
            var it = Edna.read("-0xFF", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-255L, it);
        }
    }

    @Test
    public void parseBigInt() {
        {
            var it = Edna.read("0N");
            Assertions.assertInstanceOf(BigInteger.class, it);
            Assertions.assertEquals(BigInteger.ZERO, it);
        }
        {
            var it = Edna.read("+0N");
            Assertions.assertInstanceOf(BigInteger.class, it);
            Assertions.assertEquals(BigInteger.ZERO, it);
        }
        {
            var it = Edna.read("-0N");
            Assertions.assertInstanceOf(BigInteger.class, it);
            Assertions.assertEquals(BigInteger.ZERO, it);
        }

        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("00N"));
        {
            var it = Edna.read("00N", allowZeros);
            Assertions.assertInstanceOf(BigInteger.class, it);
            Assertions.assertEquals(BigInteger.ZERO, it);
        }
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("+00N"));
        {
            var it = Edna.read("+00N", allowZeros);
            Assertions.assertInstanceOf(BigInteger.class, it);
            Assertions.assertEquals(BigInteger.ZERO, it);
        }
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("-00N"));
    }

    {
        var it = Edna.read("-00N", allowZeros);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ZERO, it);
    }

    {
        var it = Edna.read("0o0N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ZERO, it);
    }

    {
        var it = Edna.read("+0o0N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ZERO, it);
    }

    {
        var it = Edna.read("-0o0N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ZERO, it);
    }

    {
        var it = Edna.read("0b0N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ZERO, it);
    }

    {
        var it = Edna.read("+0b0N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ZERO, it);
    }

    {
        var it = Edna.read("-0b0N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ZERO, it);
    }

    {
        var it = Edna.read("0x0N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ZERO, it);
    }

    {
        var it = Edna.read("+0x0N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ZERO, it);
    }

    {
        var it = Edna.read("-0x0N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ZERO, it);
    }

    {
        var it = Edna.read("1N");
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE, it);
    }

    {
        var it = Edna.read("+1N");
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE, it);
    }

    {
        var it = Edna.read("-1N");
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE.negate(), it);
    }

    {
        var it = Edna.read("01N", allowZeros);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE, it);
    }

    {
        var it = Edna.read("+01N", allowZeros);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE, it);
    }

    {
        var it = Edna.read("-01N", allowZeros);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE.negate(), it);
    }

    {
        var it = Edna.read("0o1N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE, it);
    }

    {
        var it = Edna.read("+0o1N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE, it);
    }

    {
        var it = Edna.read("-0o1N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE.negate(), it);
    }

    {
        var it = Edna.read("0b1N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE, it);
    }

    {
        var it = Edna.read("+0b1N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE, it);
    }

    {
        var it = Edna.read("-0b1N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE.negate(), it);
    }

    {
        var it = Edna.read("0x1N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE, it);
    }

    {
        var it = Edna.read("+0x1N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE, it);
    }

    {
        var it = Edna.read("-0x1N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.ONE.negate(), it);
    }

    BigInteger bigint128 = BigInteger.valueOf(128L);

    {
        var it = Edna.read("128N");
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128, it);
    }

    {
        var it = Edna.read("+128N");
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128, it);
    }

    {
        var it = Edna.read("-128N");
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128.negate(), it);
    }

    {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("0200N"));
    }

    {
        var it = Edna.read("0200N", allowZeros);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.valueOf(200L), it);
    }

    {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("+0200N"));
    }

    {
        var it = Edna.read("+0200N", allowZeros);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.valueOf(200L), it);
    }

    {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("-0200N"));
    }

    {
        var it = Edna.read("-0200N", allowZeros);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(BigInteger.valueOf(200L).negate(), it);
    }

    {
        var it = Edna.read("0o200N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128, it);
    }

    {
        var it = Edna.read("+0o200N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128, it);
    }

    {
        var it = Edna.read("-0o200N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128.negate(), it);
    }

    {
        var it = Edna.read("0b10000000N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128, it);
    }

    {
        var it = Edna.read("+0b10000000N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128, it);
    }

    {
        var it = Edna.read("-0b10000000N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128.negate(), it);
    }

    {
        var it = Edna.read("0x80N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128, it);
    }

    {
        var it = Edna.read("+0x80N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128, it);
    }

    {
        var it = Edna.read("-0x80N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint128.negate(), it);
    }

    BigInteger bigint255 = BigInteger.valueOf(255L);

    {
        var it = Edna.read("255N");
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255, it);
    }

    {
        var it = Edna.read("+255N");
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255, it);
    }

    {
        var it = Edna.read("-255N");
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255.negate(), it);
    }

    {
        var it = Edna.read("0o377N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255, it);
    }

    {
        var it = Edna.read("+0o377N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255, it);
    }

    {
        var it = Edna.read("-0o377N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255.negate(), it);
    }

    {
        var it = Edna.read("0b11111111N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255, it);
    }

    {
        var it = Edna.read("+0b11111111N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255, it);
    }

    {
        var it = Edna.read("-0b11111111N", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255.negate(), it);
    }

    {
        var it = Edna.read("0xFFN", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255, it);
    }

    {
        var it = Edna.read("+0xFFN", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255, it);
    }

    {
        var it = Edna.read("-0xFFN", allowMoreNumberPrefixes);
        Assertions.assertInstanceOf(BigInteger.class, it);
        Assertions.assertEquals(bigint255.negate(), it);
    }
}

