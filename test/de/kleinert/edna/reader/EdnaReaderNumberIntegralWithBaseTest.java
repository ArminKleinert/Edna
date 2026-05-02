package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class EdnaReaderNumberIntegralWithBaseTest {
    private final EdnaOptions allowMoreNumberPrefixes = Edna.defaultOptions().copy((b) -> b.moreNumberPrefixes(true));
    private final EdnaOptions allowZeros = Edna.defaultOptions().copy((b) -> b.allowZeroPrefix(true));
    @Test void parseIntegerIllegalNum(){
        Assertions.assertThrows(EdnaReaderException.class, ()-> Edna.read("10rFFFF", allowMoreNumberPrefixes));
        Assertions.assertThrows(EdnaReaderException.class, ()-> Edna.read("2r88", allowMoreNumberPrefixes));
        Assertions.assertThrows(EdnaReaderException.class, ()-> Edna.read("8r88", allowMoreNumberPrefixes));
    }
    @Test void parseIntegerIllegalBase(){
        Assertions.assertThrows(EdnaReaderException.class, ()-> Edna.read("0r12", allowMoreNumberPrefixes));
        Assertions.assertThrows(EdnaReaderException.class, ()-> Edna.read("1r12", allowMoreNumberPrefixes));
        Assertions.assertThrows(EdnaReaderException.class, ()-> Edna.read("88r12", allowMoreNumberPrefixes));
    }
    @Test
    public void parseIntegerDecimal() {
        {
            var it = Edna.read("10r0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("10r88", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(88L, it);
        }
        {
            var it = Edna.read("+10r88", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(88L, it);
        }
    }
    @Test
    public void parseIntegerBinary() {
        {
            var it = Edna.read("2r0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("2r1010", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(10L, it);
        }
        {
            var it = Edna.read("-2r1010", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-10L, it);
        }
        {
            var it = Edna.read("-2r1010N", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(BigInteger.class, it);
            Assertions.assertEquals(BigInteger.valueOf(-10L), it);
        }
    }
    @Test
    public void parseIntegerBase9() {
        {
            var it = Edna.read("9r0", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(0L, it);
        }
        {
            var it = Edna.read("9r11", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(10L, it);
        }
        {
            var it = Edna.read("-9r11", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(Long.class, it);
            Assertions.assertEquals(-10L, it);
        }
        {
            var it = Edna.read("-9r11N", allowMoreNumberPrefixes);
            Assertions.assertInstanceOf(BigInteger.class, it);
            Assertions.assertEquals(BigInteger.valueOf(-10L), it);
        }
    }
}
