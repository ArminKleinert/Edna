package de.kleinert.edna;

import de.kleinert.edna.data.Char32;
import de.kleinert.edna.data.EdnaList;
import de.kleinert.edna.data.EdnaVector;
import de.kleinert.edna.pprint.EdnaTagVal;
import de.kleinert.edna.reader.EdnaReaderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;

class EdnaOptionsTest {

    @Test
    void allowSchemeUTF32Codes() {
        // Only possible with extensions
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\\x000003bb"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#\\x000003bb"));

        var opts = Edna.defaultOptions().copy(b -> b.allowSchemeUTF32Codes(true));

        Assertions.assertEquals('ῷ', Edna.read("\\x00001ff7", opts));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\\x0001F381"));

        opts = opts.copy(b -> b.allowDispatchChars(true));

        Assertions.assertEquals('ῷ', Edna.read("\\x00001ff7", opts));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\\x0001F381"));
        Assertions.assertEquals(Char32.valueOf('ῷ'), Edna.read("#\\x00001ff7", opts));
        Assertions.assertEquals(Char32.valueOf("\uD83C\uDF81"), Edna.read("#\\x0001F381", opts)); // Wrapped present 🎁
    }

    @Test
    void allowDispatchChars() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#\\a"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#\\u1ff7"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#\\u0001F381"));

        var opts = Edna.defaultOptions().copy(b -> b.allowDispatchChars(true));

        Assertions.assertEquals(Char32.valueOf('a'), Edna.read("#\\a", opts));
        Assertions.assertEquals(Char32.valueOf('ῷ'), Edna.read("#\\u1ff7", opts));
        Assertions.assertEquals(Char32.valueOf("\uD83C\uDF81"), Edna.read("#\\u0001F381", opts)); // Wrapped present 🎁
    }

    @Test
    void allowBase8Chars() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\\o41"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\\o041"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#\\o41"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#\\o041"));

        var opts = Edna.defaultOptions().copy(b -> b.allowBase8Chars(true));

        Assertions.assertEquals('!', Edna.read("\\o41", opts));
        Assertions.assertEquals('!', Edna.read("\\o041", opts));

        opts = opts.copy(b -> b.allowDispatchChars(true));

        Assertions.assertEquals(Char32.valueOf('!'), Edna.read("#\\o41", opts));
        Assertions.assertEquals(Char32.valueOf('!'), Edna.read("#\\o041", opts));
    }

    @Test
    void taggedElementDecoders() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#my/int 123"));

        var opts = Edna.defaultOptions().copy(b -> b.taggedElementDecoders(
                Map.of("my/int", (o) -> ((Number) o).intValue())
        ));
        Assertions.assertEquals(123, Edna.read("#my/int 123", opts));
    }

    @Test
    void taggedElementEncoders() {
        var opts = Edna.defaultOptions().copy(b -> b.taggedElementEncoders(new LinkedHashMap<>(Map.of(
                EdnaList.class, (ll) -> new EdnaTagVal(null, EdnaVector.create((List<?>)ll)),
                ArrayList.class, (ll) -> new EdnaTagVal("my/tag", ll)
        ))));
        Assertions.assertEquals("(1)", Edna.pprintToString(EdnaList.of(1)));
        Assertions.assertEquals("[1]", Edna.pprintToString(EdnaList.of(1), opts));
        Assertions.assertEquals("#my/tag [1]", Edna.pprintToString(new ArrayList<>(List.of(1)), opts));
    }

    @Test
    void moreNumberPrefixes() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("0b1000001"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("0o101"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("0x41"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("36r1T"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("0x41N"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("36r1TN"));

        var opts = Edna.defaultOptions().copy(b -> b.moreNumberPrefixes(true));

        Assertions.assertEquals(65L, Edna.read("0b1000001", opts));
        Assertions.assertEquals(65L, Edna.read("0o101", opts));
        Assertions.assertEquals(65L, Edna.read("0x41", opts));
        Assertions.assertEquals(65L, Edna.read("36r1T", opts));
        Assertions.assertEquals(BigInteger.valueOf(65L), Edna.read("0x41N", opts));
        Assertions.assertEquals(BigInteger.valueOf(65L), Edna.read("36r1TN", opts));

        // Some invalid radixes
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("0r1T", opts));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("1r1T", opts));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("37r1T", opts));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("64r1T", opts));
    }

    @Test
    void allowTaggedElementsWithoutNS() {
    }

    @Test
    void encodingSequenceSeparator() {
    }

    @Test
    void listToEdnListConverter() {
    }

    @Test
    void listToEdnVectorConverter() {
    }

    @Test
    void listToEdnSetConverter() {
    }

    @Test
    void listToEdnMapConverter() {
    }

    @Test
    void allowUTFSymbols() {
    }

    @Test
    void encoderSequenceElementLimit() {
    }

    @Test
    void encoderCollectionElementLimit() {
    }

    @Test
    void encoderMaxColumn() {
    }

    @Test
    void encoderLineIndent() {
    }

    @Test
    void encoderPrettyPrint() {
    }

    @Test
    void allowMetaData() {
    }

    @Test
    void allowZeroPrefix() {
    }

    @Test
    void allowSymbolicValues() {
    }

    @Test
    void symbolicValues() {
    }
}