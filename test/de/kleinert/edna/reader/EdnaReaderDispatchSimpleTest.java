package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaOptions;
import de.kleinert.edna.data.Char32;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class EdnaReaderDispatchSimpleTest {
    @Test
    public void parseSymbolicTest() {
        Assertions.assertTrue(((Double)Edna.read("##NaN")  ).isNaN());
        Assertions.assertTrue(((Double)Edna.read("##-NaN") ).isNaN());
        Assertions.assertEquals(Double.POSITIVE_INFINITY, Edna.read("##INF"));
        Assertions.assertEquals(Double.NEGATIVE_INFINITY, Edna.read("##-INF"));
    }

    @Test
    public void parseDispatchChar() {
        // Only possible with extensions
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#\\a"));
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#\\o41") );
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#\\u03BB") );
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#\\u000003bb"));
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#\\x000003bb"));

        // Simple dispatch "char" (string)
        Assertions.assertEquals(new Char32('a'), Edna.read("#\\a", EdnaOptions.extendedOptions()));

        // Octal dispatch "char" (string)
        Assertions.assertEquals(new Char32('!'), Edna.read("#\\o41", EdnaOptions.extendedOptions()));
        Assertions.assertEquals(new Char32('!'), Edna.read("#\\o041", EdnaOptions.extendedOptions()));
        Assertions.assertEquals(new Char32('ǂ'), Edna.read("#\\o702", EdnaOptions.extendedOptions()));

        // Hexadecimal dispatch "char" (string)
        Assertions.assertEquals(new Char32('λ'), Edna.read("#\\u03BB", EdnaOptions.extendedOptions()));
        Assertions.assertEquals(new Char32('λ'), Edna.read("#\\u000003bb", EdnaOptions.extendedOptions()));
        Assertions.assertEquals(new Char32('λ'), Edna.read("#\\x000003bb", EdnaOptions.extendedOptions()));
        Assertions.assertEquals(new Char32('ῷ'), Edna.read("#\\u1ff7", EdnaOptions.extendedOptions()));
        Assertions.assertEquals(new Char32('ῷ'), Edna.read("#\\u00001ff7", EdnaOptions.extendedOptions()));
        Assertions.assertEquals(new Char32('ῷ'), Edna.read("#\\x00001ff7", EdnaOptions.extendedOptions()));

        // Hexadecimal dispatch "char" (string) over 2^16
        Assertions.assertEquals(
                Char32.valueOf("\uD83C\uDF81"),
                Edna.read("#\\u0001F381", EdnaOptions.extendedOptions())
        ) ;// Wrapped present 🎁
        Assertions.assertEquals(
                Char32.valueOf("\uD83C\uDF81"),
                Edna.read("#\\x0001F381", EdnaOptions.extendedOptions())
        ); // Wrapped present 🎁
        Assertions.assertEquals(
                Char32.valueOf("\uD83E\uDFF0"),
                Edna.read("#\\u0001FBF0", EdnaOptions.extendedOptions())
        ); // Segmented Digit Zero
        Assertions.assertEquals(
                Char32.valueOf("\uD83E\uDFF0"),
                Edna.read("#\\x0001FBF0", EdnaOptions.extendedOptions())
        ); // Segmented Digit Zero
    }

    @Test
    public void parseDiscardTest() {
        // Empty after discard is resolved
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#_"));
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#_a"));

        Assertions.assertEquals(1L, Edna.read("#_a 1"));
        Assertions.assertEquals(List.of(), Edna.read("[#_a #_b]"));
        Assertions.assertEquals(List.of(), Edna.read("[#_ #_a b]"));
    }

    @Test
    public void parseUuidTest() {
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#uuid")); // Empty
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#uuid 1")); // Wrong type (need string)
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#uuid \"\"")); // Invalid format

        Assertions.assertEquals(
                UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6"),
                Edna.read("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"")
        );
    }

    @Test
    public void parseInstTest() {
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#inst")); // Empty
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#inst 1")); // Wrong type (need string)
        Assertions.assertThrows(EdnaReaderException.class,()-> Edna.read("#inst \"\"")); // Invalid format

        Assertions.assertEquals(
                Instant.parse("1985-04-12T23:20:50.520Z"),
                Edna.read("#inst \"1985-04-12T23:20:50.52Z\"")
        );
    }
}
