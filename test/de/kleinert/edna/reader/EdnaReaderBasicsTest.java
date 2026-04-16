package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.util.*;

class EdnaReaderBasicsTest {
    @Test
    public void parseStringBasicTest() {
        Assertions.assertInstanceOf(String.class, Edna.read("\"\""));
        Assertions.assertEquals("", Edna.read("\"\""));
        Assertions.assertEquals("abc", Edna.read("\"abc\""));
    }

    @Test
    public void parseStringEscapeSequenceTest() {
        Assertions.assertEquals("\n", Edna.read("\"\\n\""));
        Assertions.assertEquals(List.of("", ""), Arrays.asList(((String) Edna.read("\"\\n\"")).split("\n", -1)));
        Assertions.assertEquals("\t", Edna.read("\"\\t\""));

        Assertions.assertEquals("\t", Edna.read("\"\\t\""));
        Assertions.assertEquals("\b", Edna.read("\"\\b\""));
        Assertions.assertEquals("\r", Edna.read("\"\\r\""));
        Assertions.assertEquals("\"", Edna.read("\"\\\"\""));

        Assertions.assertEquals("\\", Edna.read("\"\\\\\""));
        Assertions.assertEquals("\\\\", Edna.read("\"\\\\\\\\\""));

        {
            var it = Edna.read("\"\\\\\"");
            Assertions.assertEquals("\\", it);
        }
        {
            var it = Edna.read("\"\\\\\\\\\"");
            Assertions.assertEquals("\\\\", it);
        }

        Assertions.assertEquals("\t\t", Edna.read("\"\\t\\t\""));
    }

    @Test
    public void parseStringUnicodeSequenceTest() {
        Assertions.assertEquals("🎁", Edna.read("\"🎁\""));
        Assertions.assertEquals("🎁", Edna.read("\"\\uD83C\\uDF81\""));
        Assertions.assertEquals("🎁", Edna.read("\"\\uD83C\\uDF81\""));
        Assertions.assertEquals("🎁", Edna.read("\"\\x0001F381\""));
    }

    @Test
    public void parseStringUnclosedTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\""));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\"abc"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\"\"\""));
    }

    @Test
    public void parseDiscardSimpleTest() {
        Assertions.assertEquals(2L, Edna.read("#_1 2"));
    }

    @Test
    public void parseDiscardTest() {
        Assertions.assertEquals(2L, Edna.read("#_1 2"));

        // Discard tags ignore spaces.
        Assertions.assertEquals(2L, Edna.read("#_ 1 2"));
        Assertions.assertEquals(2L, Edna.read("#_      1 2"));

        // Discard ignores newlines
        Assertions.assertEquals(2L, Edna.read("#_\n1 2"));

        // Discard ignores comments
        Assertions.assertEquals(2L, Edna.read("#_ ; abc\n 1 2"));

        // Discard in strings does nothing
        Assertions.assertEquals("#_", Edna.read("\"#_\""));
        Assertions.assertEquals("#_1", Edna.read("\"#_1\""));
    }

    @Test
    public void parseDiscardAssociativityTest() {
        // Discard is right-associative
        Assertions.assertEquals(3L, Edna.read("#_ #_ 1 2 3"));
    }

    @Test
    public void parseDiscardInCollectionTest() {
        // Discard is nothing. When discard appears in a list, vector, set, or map, it does nothing
        {
            var it = Edna.read("( #_ 1 #_ 2 #_ 3 )");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertTrue(iterableToList((Iterable<?>) it).isEmpty());
        }
        {
            var it = Edna.read("[#_1 #_2 #_3]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertTrue(((List<?>) it).isEmpty());
        }
        {
            var it = Edna.read("#{#_1 #_1 #_1}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertTrue(((Set<?>) it).isEmpty());
        }
        {
            var it = Edna.read("{#_1 #_1 #_1}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertTrue(((Map<?, ?>) it).isEmpty());
        }
    }

    @Test
    public void parseEmptySet() {
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
    public void parseEmptyMap() {
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

    private <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> output = new ArrayList<>();
        for (T t : iterable) {
            output.add(t);
        }
        return output;
    }

    @Test
    public void parseListSimple() {
        {
            var it = Edna.read("(\\a)");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of('a'), iterableToList((Iterable<?>) it));
        }
        {
            var it = Edna.read("(\\a \\b)");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of('a', 'b'), iterableToList((Iterable<?>) it));
        }
        {
            var it = Edna.read("(())");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of(List.of()), iterableToList((Iterable<?>) it));
        }
    }

    @Test
    public void parseVectorSimple() {
        {
            var it = Edna.read("[\\a]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of('a'), it);
        }
        {
            var it = Edna.read("[\\a \\b]");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of('a', 'b'), ((List<?>) it));
        }
        {
            var it = Edna.read("[[]]");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of(List.of()), ((List<?>) it));
        }
    }

    @Test
    public void parseSetSimple() {
        {
            var it = Edna.read("#{\\a \\b}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertEquals(Set.of('a', 'b'), ((Set<?>) it));
        }
        {
            var it = Edna.read("#{ \\a }");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertEquals(Set.of('a'), ((Set<?>) it));
        }
        {
            var it = Edna.read("#{\\a #{}}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertEquals(Set.of('a', Set.of()), ((Set<?>) it));
        }
    }

    @Test
    public void parseMapSimple() {
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