package de.kleinert.edna.reader;

import de.kleinert.edna.EdnOptions;
import de.kleinert.edna.data.Char32;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;
public class EdnaReaderCharTest {    @Test
public void parseSlashWhitespaceIsInvalid() {
    Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read(""));
    Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read("\\") );
    Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read("#\\"));
    Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read("\\ ") );
    Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read("#\\ ") );
    Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read("\\\n "));
    Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read("#\\\n "));

    Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read("[\\ ]"));
    Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read("[#\\ ]"));
}

    @Test
    public void parseSpecialCharsTest() {
        Assertions.assertEquals('\n', EdnaReader.read("\\newline"));
        Assertions.assertEquals(' ', EdnaReader.read("\\space"));
        Assertions.assertEquals('\t', EdnaReader.read("\\tab"));
        Assertions.assertEquals('\b', EdnaReader.read("\\backspace"));
        Assertions.assertEquals((char)12, EdnaReader.read("\\formfeed"));
        Assertions.assertEquals('\r', EdnaReader.read("\\return"));
    }

    @Test
    public void parseSimpleCharsTest() {
    var str = "abcdefghijklmnopqrstuvwxyz";
    str += str.toUpperCase();
    str += "0123456789";
        for (int i = 0; i < str.length(); i++) {
            Assertions.assertEquals(str.charAt(i), EdnaReader.read("\\"+str.charAt(i)));
        }
    }

    @Test
    public void parseSymbolCharsTest() {
    var chars =  "!\"§$%&/()=?*+~^°'#-_.:,<>|€@`´λ";
        for (int i = 0; i < chars.length(); i++) {
            var chr = chars.charAt(i);
            Assertions.assertEquals(chr, EdnaReader.read("\\"+chr));
        }
    }

    @Test
    public void parseOctCharsTest() {
        Assertions.assertEquals('!', EdnaReader.read("\\o41"));
        Assertions.assertEquals('!', EdnaReader.read("\\o041"));
        Assertions.assertEquals('ǂ', EdnaReader.read("\\o702"));
    }

    @Test
    public void parseUniCharsTest() {
        Assertions.assertEquals('λ', EdnaReader.read("\\u03BB"));
        Assertions.assertEquals('λ', EdnaReader.read("\\u03bb"));
        Assertions.assertEquals('ῷ', EdnaReader.read("\\u1ff7"));
        Assertions.assertEquals('\u8183', EdnaReader.read("\\u8183"));

        Assertions.assertEquals('\u2626', EdnaReader.read("\\u2626")); // Orthodox cross
        Assertions.assertEquals('\u271D', EdnaReader.read("\\u271D")); // Latin cross

        var options = EdnOptions.extendedOptions();
        Assertions.assertEquals(new Char32('\n'), EdnaReader.read("#\\newline", options));
        Assertions.assertEquals(new Char32(' '), EdnaReader.read("#\\space", options));
        Assertions.assertEquals(new Char32('\t'), EdnaReader.read("#\\tab", options));
        Assertions.assertEquals(new Char32('\b'), EdnaReader.read("#\\backspace", options));
        Assertions.assertEquals(new Char32(12), EdnaReader.read("#\\formfeed", options));
        Assertions.assertEquals(new Char32('\r'), EdnaReader.read("#\\return", options));

        Assertions.assertEquals(
                Char32.valueOf("\uD83D\uDD46"),
                EdnaReader.read("#\\u0001F546", options)
        ); // White latin cross
        Assertions.assertEquals(
                Char32.valueOf("\uD83D\uDD47"),
                EdnaReader.read("#\\u0001F547", options)
        ); // Heavy latin cross
    }
}
