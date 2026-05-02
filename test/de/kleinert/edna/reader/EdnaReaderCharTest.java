package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.data.Char32;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

public class EdnaReaderCharTest {
    @Test
    public void parseSlashWhitespaceIsInvalid() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read(""));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\\"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#\\"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\\ "));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#\\ "));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\\\n "));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#\\\n "));

        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("[\\ ]"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("[#\\ ]"));
    }

    @Test
    public void parseSpecialCharsTest() {
        Assertions.assertEquals('\n', Edna.read("\\newline"));
        Assertions.assertEquals(' ', Edna.read("\\space"));
        Assertions.assertEquals('\t', Edna.read("\\tab"));
        Assertions.assertEquals('\b', Edna.read("\\backspace"));
        Assertions.assertEquals((char) 12, Edna.read("\\formfeed"));
        Assertions.assertEquals('\r', Edna.read("\\return"));
    }

    @Test
    public void parseSimpleCharsTest() {
        var str = "abcdefghijklmnopqrstuvwxyz";
        str += str.toUpperCase();
        str += "0123456789";
        for (int i = 0; i < str.length(); i++) {
            Assertions.assertEquals(str.charAt(i), Edna.read("\\" + str.charAt(i)));
        }
    }

    @Test
    public void parseSymbolCharsTest() {
        var chars = "!\"§$%&/()=?*+~^°'#-_.:,<>|€@`´λ";
        for (int i = 0; i < chars.length(); i++) {
            var chr = chars.charAt(i);
            Assertions.assertEquals(chr, Edna.read("\\" + chr));
        }
    }

    @Test
    public void parseOctCharsTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\\o41"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\\o041"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\\o702"));

        // Octal dispatch "char" (string)
        Assertions.assertEquals('!', Edna.read("\\o41", Edna.extendedOptions()));
        Assertions.assertEquals('!', Edna.read("\\o041", Edna.extendedOptions()));
        Assertions.assertEquals('ǂ', Edna.read("\\o702", Edna.extendedOptions()));
    }

    @Test
    public void parseUniCharsTest() {
        Assertions.assertEquals('λ', Edna.read("\\u03BB"));
        Assertions.assertEquals('λ', Edna.read("\\u03bb"));
        Assertions.assertEquals('ῷ', Edna.read("\\u1ff7"));
        Assertions.assertEquals('\u8183', Edna.read("\\u8183"));

        Assertions.assertEquals('\u2626', Edna.read("\\u2626")); // Orthodox cross
        Assertions.assertEquals('\u271D', Edna.read("\\u271D")); // Latin cross

        var options = Edna.extendedOptions();
        Assertions.assertEquals(new Char32('\n'), Edna.read("#\\newline", options));
        Assertions.assertEquals(new Char32(' '), Edna.read("#\\space", options));
        Assertions.assertEquals(new Char32('\t'), Edna.read("#\\tab", options));
        Assertions.assertEquals(new Char32('\b'), Edna.read("#\\backspace", options));
        Assertions.assertEquals(new Char32(12), Edna.read("#\\formfeed", options));
        Assertions.assertEquals(new Char32('\r'), Edna.read("#\\return", options));

        Assertions.assertEquals(
                Char32.valueOf("\uD83D\uDD46"),
                Edna.read("#\\u0001F546", options)
        ); // White latin cross
        Assertions.assertEquals(
                Char32.valueOf("\uD83D\uDD47"),
                Edna.read("#\\u0001F547", options)
        ); // Heavy latin cross
    }
}
