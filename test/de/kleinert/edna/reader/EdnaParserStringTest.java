package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class EdnaParserStringTest {
    @Test
    public void parseStringBasicTest() {
        {
            var it = Edna.read("\"\"", null, String.class);
            Assertions.assertNotNull(it);
        }
        {
            var it = Edna.read("\"\"", null, String.class);
            Assertions.assertEquals("", it);
        }
        {
            var it = Edna.read("\"abc\"", null, String.class);
            Assertions.assertEquals("abc", it);
        }
    }

    @Test
    public void parseStringEscapeSequenceTest() {
        {
            var it = Edna.read("\"\\n\"", null, String.class);
            Assertions.assertEquals("\n", it);
        }
        {
            var it = Edna.read("\"a\\nb\"", null, String.class);
            Assertions.assertNotNull(it);
            Assertions.assertEquals(List.of("a", "b"), it.lines().toList());
        }
        {
            var it = Edna.read("\"\\t\"", null, String.class);
            ;
            Assertions.assertEquals("\t", it);
        }
        {
            var it = Edna.read("\"\\t\"", null, String.class);
            Assertions.assertEquals("\t", it);
        }
        {
            var it = Edna.read("\"\\b\"", null, String.class);
            Assertions.assertEquals("\b", it);
        }
        {
            var it = Edna.read("\"\\r\"", null, String.class);
            Assertions.assertEquals("\r", it);
        }
        {
            var it = Edna.read("\"\\\"\"", null, String.class);
            Assertions.assertEquals("\"", it);
        }

        {
            var it = Edna.read("\"\\\\\"", null, String.class);
            Assertions.assertEquals("\\", it);
        }
        {
            var it = Edna.read("\"\\\\\\\\\"", null, String.class);
            Assertions.assertEquals("\\\\", it);
        }
        {
            var it = Edna.read("\"\\\\\"", null, String.class);
            Assertions.assertEquals("\\", it);
        }
        {
            var it = Edna.read("\"\\\\\\\\\"", null, String.class);
            Assertions.assertEquals("\\\\", it);
        }
        {
            var it = Edna.read("\"\\t\\t\"");
            Assertions.assertEquals("\t\t", it);
        }
    }

    @Test
    public void parseStringUnicodeSequenceTest() {
        {
            var it = Edna.read("\"🎁\"", null, String.class);
            Assertions.assertEquals("🎁", it);
        }
        {
            var it = Edna.read("\"\\uD83C\\uDF81\"", null, String.class);
            Assertions.assertEquals("🎁", it);
        }
        {
            var it = Edna.read("\"\\uD83C\\uDF81\"", null, String.class);
            Assertions.assertEquals("🎁", it);
        }
        {
            var it = Edna.read("\"\\x0001F381\"", null, String.class);
            Assertions.assertEquals("🎁", it);
        }
    }

    @Test
    public void parseStringUnclosedTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\""));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\"abc"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\"\"\""));
    }
}
