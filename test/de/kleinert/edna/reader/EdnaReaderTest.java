package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.util.*;

class EdnaReaderTest {
    @Test
    void parseStringBasicTest() {
        Assertions.assertInstanceOf(String.class, Edna.read("\"\""));
        Assertions.assertEquals("", Edna.read("\"\""));
        Assertions.assertEquals("abc", Edna.read("\"abc\""));
    }

    @Test
    void parseStringEscapeSequenceTest() {
        Assertions.assertEquals("\n", Edna.read("\"\\n\""));

        var temp = ((String) Objects.requireNonNull(Edna.read("\"\n\""))).split("\n", -1);
        Assertions.assertEquals(List.of("", ""), Arrays.asList(temp));

        Assertions.assertEquals("\t", Edna.read("\"\\t\""));

        Assertions.assertEquals("\t", Edna.read("\"\\t\""));
        Assertions.assertEquals("\b", Edna.read("\"\\b\""));
        Assertions.assertEquals("\r", Edna.read("\"\\r\""));
        Assertions.assertEquals("\"", Edna.read("\"\\\"\""));

        Assertions.assertEquals("\\", Edna.read("\"\\\\\""));
        Assertions.assertEquals("\\\\", Edna.read("\"\\\\\\\\\""));
        {
            var it = Edna.read(
                    """
                                 "\\\\"
                            """);
            Assertions.assertEquals("\\", it);
        }

        {
            var it = Edna.read(
                    """
                                "\\\\\\\\"
                            """);
            Assertions.assertEquals("\\\\", it);
        }

        Assertions.assertEquals("\t\t", Edna.read("\"\\t\\t\""));
    }

    @Test
    void parseStringUnicodeSequenceTest() {
        Assertions.assertEquals("🎁", Edna.read("\"🎁\""));
        Assertions.assertEquals("🎁", Edna.read("\"\\uD83C\\uDF81\""));
        Assertions.assertEquals("🎁", Edna.read("\"\\uD83C\\uDF81\""));
        Assertions.assertEquals("🎁", Edna.read("\"\\x0001F381\""));
    }

    @Test
    void parseStringUnclosedTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\""));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\"abc"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\"\"\""));
    }

    @Test
    void parseEmptySet() {
        // Normal
        {
            var it = Edna.read("#{}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertTrue(((Set<?>) it).isEmpty());
        }

        // Whitespace does not matter
        {
            var it = Edna.read("#{  }");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertTrue(((Set<?>) it).isEmpty());
        }
        {
            var it = Edna.read("#{\t \n}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertTrue(((Set<?>) it).isEmpty());
        }
        {
            var it = Edna.read("#{\n}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertTrue(((Set<?>) it).isEmpty());
        }
    }

    @Test
    void parseEmptyMap() {
        // Normal
        {
            var it = Edna.read("{}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertTrue(((Map<?, ?>) it).isEmpty());
        }

        // Whitespace does not matter
        {
            var it = Edna.read("{  }");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertTrue(((Map<?, ?>) it).isEmpty());
        }
        {
            var it = Edna.read("{\t \n}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertTrue(((Map<?, ?>) it).isEmpty());
        }
        {
            var it = Edna.read("{\n}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertTrue(((Map<?, ?>) it).isEmpty());
        }
    }

    @Test
    void parseListSimple() {
        {
            var it = Edna.read("(\\a)");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of('a'), it);
        }
        {
            var it = Edna.read("(\\a \\b)");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of('a', 'b'), it);
        }
        {
            var it = Edna.read("(())");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of(List.of()), it);
        }
    }

    @Test
    void parseVectorSimple() {
        {
            var it = Edna.read("[\\a]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of('a'), it);
        }
        {
            var it = Edna.read("[\\a \\b]");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of('a', 'b'), it);
        }
        {
            var it = Edna.read("[[]]");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of(List.of()), it);
        }
    }

    @Test
    void parseSetSimple() {
        {
            var it = Edna.read("#{\\a \\b}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertEquals(Set.of('a', 'b'), it);
        }
        {
            var it = Edna.read("#{ \\a }");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertEquals(Set.of('a'), it);
        }
        {
            var it = Edna.read("#{\\a #{}}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertEquals(Set.of('a', Set.of()), it);
        }
    }

    @Test
    void parseMapSimple() {
        {
            var it = Edna.read("{\\a \\b}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertEquals(Map.of('a', 'b').entrySet(), ((Map<?, ?>) it).entrySet());
        }
        {
            var it = Edna.read("{\\a {}}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertEquals(Map.of('a', Map.of()).entrySet(), ((Map<?, ?>) it).entrySet());
        }
        {
            var it = Edna.read("{{} {}}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertEquals(Map.of(Map.of(), Map.of()), it);
        }
    }
}