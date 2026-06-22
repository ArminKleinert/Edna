package de.kleinert.edna.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Char32Test {
    @Test
    void testConstructor() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Char32.valueOf(Char32.MIN_VALUE.code() - 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Char32.valueOf(Char32.MAX_VALUE.code() + 1));

        Assertions.assertEquals(Char32.valueOf(0), Char32.valueOf(0));
        Assertions.assertEquals(Char32.valueOf('A'), Char32.valueOf('A'));
        Assertions.assertEquals(Char32.valueOf(0x0001F546), Char32.valueOf("\uD83D\uDD46"));
    }

    @Test
    void dec() {
        Assertions.assertThrows(IllegalArgumentException.class, Char32.MIN_VALUE::dec);
        Assertions.assertEquals(Char32.valueOf('A'), Char32.valueOf('B').dec());
        Assertions.assertEquals(Char32.valueOf('A'), Char32.valueOf('B').dec());
    }

    @Test
    void inc() {
        Assertions.assertThrows(IllegalArgumentException.class, Char32.MAX_VALUE::inc);
        Assertions.assertEquals(Char32.valueOf('B'), Char32.valueOf('A').inc());
        Assertions.assertEquals(Char32.valueOf('B'), Char32.valueOf('A').inc());
    }

    @Test
    void minus() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Char32.valueOf(Char32.MIN_VALUE.code()).minus(1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Char32.valueOf(Char32.MIN_VALUE.code()).minus('A'));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Char32.valueOf(Char32.MIN_VALUE.code()).minus(Char32.valueOf(1)));
        Assertions.assertEquals(Char32.valueOf(0), Char32.valueOf('A').minus(65));
        Assertions.assertEquals(Char32.valueOf(0), Char32.valueOf('A').minus('A'));
        Assertions.assertEquals(Char32.valueOf(0), Char32.valueOf('A').minus(Char32.valueOf(65)));
    }

    @Test
    void plus() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Char32.valueOf(Char32.MAX_VALUE.code()).plus(1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Char32.valueOf(Char32.MAX_VALUE.code()).plus('A'));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Char32.valueOf(Char32.MAX_VALUE.code()).plus(Char32.valueOf(1)));

        Assertions.assertEquals(Char32.valueOf('A'), Char32.valueOf(0).plus(65));
        Assertions.assertEquals(Char32.valueOf('A'), Char32.valueOf(0).plus('A'));
        Assertions.assertEquals(Char32.valueOf('A'), Char32.valueOf(0).plus(Char32.valueOf(65)));
    }

    @Test
    void toChar() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Char32.valueOf(Character.MAX_VALUE + 1).toChar());
        Assertions.assertEquals('A', Char32.valueOf('A').toChar());
        Assertions.assertEquals('A', Char32.valueOf('A').toChar());
    }

    @Test
    void toByte() {
        Assertions.assertEquals((byte) 65, Char32.valueOf('A').toByte());
        Assertions.assertEquals((byte) 65, Char32.valueOf('A').toByte());
    }

    @Test
    void toShort() {
        Assertions.assertEquals((short) 65, Char32.valueOf('A').toShort());
        Assertions.assertEquals((short) 65, Char32.valueOf('A').toShort());
    }

    @Test
    void toInt() {
        Assertions.assertEquals(65, Char32.valueOf('A').toInt());
        Assertions.assertEquals(65, Char32.valueOf('A').toInt());
    }

    @Test
    void toLong() {
        Assertions.assertEquals(65L, Char32.valueOf('A').toLong());
        Assertions.assertEquals(65L, Char32.valueOf('A').toLong());
    }

    @Test
    void toFloat() {
        Assertions.assertEquals(65.0f, Char32.valueOf('A').toFloat());
        Assertions.assertEquals(65.0f, Char32.valueOf('A').toFloat());
    }

    @Test
    void toDouble() {
        Assertions.assertEquals(65.0, Char32.valueOf('A').toDouble());
        Assertions.assertEquals(65.0, Char32.valueOf('A').toDouble());
    }

    @Test
    void testToString() {
        Assertions.assertEquals("A", Char32.valueOf('A').toString());
        Assertions.assertEquals("\uD83D\uDD46", Char32.valueOf(0x0001F546).toString()); // White Latin cross
        Assertions.assertEquals("\uD83C\uDF81", Char32.valueOf(0x0001F381).toString()); // Wrapped present 🎁
    }

    @Test
    void compareTo() {
        Assertions.assertTrue(Char32.valueOf('A').compareTo(Char32.valueOf('B')) < 0);
        Assertions.assertEquals(0, Char32.valueOf('A').compareTo(Char32.valueOf('A')));
        Assertions.assertTrue(Char32.valueOf('B').compareTo(Char32.valueOf('A')) > 0);
    }

    @Test
    void getCode() {
        Assertions.assertEquals(65, Char32.valueOf('A').code());
    }

    @Test
    void charToChar32() {
        Assertions.assertEquals(Char32.valueOf('A'), Char32.valueOf('A'));
    }

    @Test
    void byteToChar32() {
        Assertions.assertEquals(Char32.valueOf('A'), Char32.valueOf((byte) 65));
    }

    @Test
    void intToChar32() {
        Assertions.assertEquals(Char32.valueOf('A'), Char32.valueOf(65));
    }
}