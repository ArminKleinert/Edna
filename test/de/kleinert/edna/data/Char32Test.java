package de.kleinert.edna.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Char32Test {
    @Test
    void testConstructor() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Char32(Char32.MIN_VALUE.code() - 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Char32(Char32.MAX_VALUE.code() + 1));

        Assertions.assertEquals(new Char32(0), new Char32(0));
        Assertions.assertEquals(new Char32('A'), Char32.valueOf('A'));
        Assertions.assertEquals(new Char32(0x0001F546), Char32.valueOf("\uD83D\uDD46"));
    }

    @Test
    void dec() {
        Assertions.assertThrows(IllegalArgumentException.class, Char32.MIN_VALUE::dec);
        Assertions.assertEquals(new Char32('A'), Char32.valueOf('B').dec());
        Assertions.assertEquals(new Char32('A'), new Char32('B').dec());
    }

    @Test
    void inc() {
        Assertions.assertThrows(IllegalArgumentException.class, Char32.MAX_VALUE::inc);
        Assertions.assertEquals(new Char32('B'), Char32.valueOf('A').inc());
        Assertions.assertEquals(new Char32('B'), new Char32('A').inc());
    }

    @Test
    void minus() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Char32(Char32.MIN_VALUE.code()).minus(1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Char32(Char32.MIN_VALUE.code()).minus('A'));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Char32(Char32.MIN_VALUE.code()).minus(new Char32(1)));
        Assertions.assertEquals(new Char32(0), new Char32('A').minus(65));
        Assertions.assertEquals(new Char32(0), new Char32('A').minus('A'));
        Assertions.assertEquals(new Char32(0), new Char32('A').minus(new Char32(65)));
    }

    @Test
    void plus() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Char32(Char32.MAX_VALUE.code()).plus(1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Char32(Char32.MAX_VALUE.code()).plus('A'));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Char32(Char32.MAX_VALUE.code()).plus(new Char32(1)));

        Assertions.assertEquals(new Char32('A'), new Char32(0).plus(65));
        Assertions.assertEquals(new Char32('A'), new Char32(0).plus('A'));
        Assertions.assertEquals(new Char32('A'), new Char32(0).plus(new Char32(65)));
    }

    @Test
    void toChar() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Char32(Character.MAX_VALUE + 1).toChar());
        Assertions.assertEquals('A', Char32.valueOf('A').toChar());
        Assertions.assertEquals('A', new Char32('A').toChar());
    }

    @Test
    void toByte() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Char32(Byte.MAX_VALUE + 1).toByte());
        Assertions.assertEquals((byte) 65, Char32.valueOf('A').toByte());
        Assertions.assertEquals((byte) 65, new Char32('A').toByte());
    }

    @Test
    void toShort() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Char32(Short.MAX_VALUE + 1).toShort());
        Assertions.assertEquals((short) 65, Char32.valueOf('A').toShort());
        Assertions.assertEquals((short) 65, new Char32('A').toShort());
    }

    @Test
    void toInt() {
        Assertions.assertEquals(65, Char32.valueOf('A').toInt());
        Assertions.assertEquals(65, new Char32('A').toInt());
    }

    @Test
    void toLong() {
        Assertions.assertEquals(65L, Char32.valueOf('A').toLong());
        Assertions.assertEquals(65L, new Char32('A').toLong());
    }

    @Test
    void toFloat() {
        Assertions.assertEquals(65.0f, Char32.valueOf('A').toFloat());
        Assertions.assertEquals(65.0f, new Char32('A').toFloat());
    }

    @Test
    void toDouble() {
        Assertions.assertEquals(65.0, Char32.valueOf('A').toDouble());
        Assertions.assertEquals(65.0, new Char32('A').toDouble());
    }

    @Test
    void testToString() {
        Assertions.assertEquals("A", Char32.valueOf('A').toString());
        Assertions.assertEquals("\uD83D\uDD46", new Char32(0x0001F546).toString()); // White Latin cross
        Assertions.assertEquals("\uD83C\uDF81", new Char32(0x0001F381).toString()); // Wrapped present 🎁
    }

    @Test
    void compareTo() {
        Assertions.assertTrue(Char32.valueOf('A').compareTo(Char32.valueOf('B')) < 0);
        Assertions.assertEquals(0, Char32.valueOf('A').compareTo(Char32.valueOf('A')));
        Assertions.assertTrue(Char32.valueOf('B').compareTo(Char32.valueOf('A')) > 0);
    }

    @Test
    void getCode() {
        Assertions.assertEquals(65, new Char32('A').code());
    }

    @Test
    void charToChar32() {
        Assertions.assertEquals(new Char32('A'), Char32.valueOf('A'));
    }

    @Test
    void byteToChar32() {
        Assertions.assertEquals(new Char32('A'), Char32.valueOf((byte) 65));
    }

    @Test
    void intToChar32() {
        Assertions.assertEquals(new Char32('A'), Char32.valueOf(65));
    }
}