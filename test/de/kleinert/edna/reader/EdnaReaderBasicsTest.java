package de.kleinert.edna.reader;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.util.*;

class EdnaReaderBasicsTest {
    @Test
    public void parseStringBasicTest() {
        Assertions.assertInstanceOf(String.class, EdnaReader.read("\"\""));
        Assertions.assertEquals("", EdnaReader.read("\"\""));
        Assertions.assertEquals("abc", EdnaReader.read("\"abc\""));
    }

    @Test
    public void parseStringEscapeSequenceTest() {
        Assertions.assertEquals("\n", EdnaReader.read("\"\\n\""));
        Assertions.assertEquals(List.of("", ""), Arrays.asList(((String) EdnaReader.read("\"\\n\"")).split("\n", -1)));
        Assertions.assertEquals("\t", EdnaReader.read("\"\\t\""));

        Assertions.assertEquals("\t", EdnaReader.read("\"\\t\""));
        Assertions.assertEquals("\b", EdnaReader.read("\"\\b\""));
        Assertions.assertEquals("\r", EdnaReader.read("\"\\r\""));
        Assertions.assertEquals("\"", EdnaReader.read("\"\\\"\""));

        Assertions.assertEquals("\\", EdnaReader.read("\"\\\\\""));
        Assertions.assertEquals("\\\\", EdnaReader.read("\"\\\\\\\\\""));

        {
            var it = EdnaReader.read("\"\\\\\"");
            Assertions.assertEquals("\\", it);
        }
        {
            var it = EdnaReader.read("\"\\\\\\\\\"");
            Assertions.assertEquals("\\\\", it);
        }

        Assertions.assertEquals("\t\t", EdnaReader.read("\"\\t\\t\""));
    }

    @Test
    public void parseStringUnicodeSequenceTest() {
        Assertions.assertEquals("🎁", EdnaReader.read("\"🎁\""));
        Assertions.assertEquals("🎁", EdnaReader.read("\"\\uD83C\\uDF81\""));
        Assertions.assertEquals("🎁", EdnaReader.read("\"\\uD83C\\uDF81\""));
        Assertions.assertEquals("🎁", EdnaReader.read("\"\\x0001F381\""));
    }

    @Test
    public void parseStringUnclosedTest() {
        Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read("\""));
        Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read("\"abc"));
        Assertions.assertThrows(EdnReaderException.class, () -> EdnaReader.read("\"\"\""));
    }

    @Test
    public void parseDiscardSimpleTest() {
        Assertions.assertEquals(2L, EdnaReader.read("#_1 2"));
    }

    @Test
    public void parseDiscardTest() {
        Assertions.assertEquals(2L, EdnaReader.read("#_1 2"));

        // Discard tags ignore spaces.
        Assertions.assertEquals(2L, EdnaReader.read("#_ 1 2"));
        Assertions.assertEquals(2L, EdnaReader.read("#_      1 2"));

        // Discard ignores newlines
        Assertions.assertEquals(2L, EdnaReader.read("#_\n1 2"));

        // Discard ignores comments
        Assertions.assertEquals(2L, EdnaReader.read("#_ ; abc\n 1 2"));

        // Discard in strings does nothing
        Assertions.assertEquals("#_", EdnaReader.read("\"#_\""));
        Assertions.assertEquals("#_1", EdnaReader.read("\"#_1\""));
    }

    @Test
    public void parseDiscardAssociativityTest() {
        // Discard is right-associative
        Assertions.assertEquals(3L, EdnaReader.read("#_ #_ 1 2 3"));
    }

    @Test
    public void parseDiscardInCollectionTest() {
        // Discard is nothing. When discard appears in a list, vector, set, or map, it does nothing
        {
            var it = EdnaReader.read("( #_ 1 #_ 2 #_ 3 )");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertTrue(iterableToList((Iterable<?>) it).isEmpty());
        }
        {
            var it = EdnaReader.read("[#_1 #_2 #_3]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertTrue(((List<?>) it).isEmpty());
        }
        {
            var it = EdnaReader.read("#{#_1 #_1 #_1}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertTrue(((Set<?>) it).isEmpty());
        }
        {
            var it = EdnaReader.read("{#_1 #_1 #_1}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertTrue(((Map<?, ?>) it).isEmpty());
        }
    }

    @Test
    public void parseEmptySet() {
        // Normal
        {
            var it = EdnaReader.read("#{}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertTrue(((Set<?>) it).isEmpty());
        }

        // Whitespace does not matter
        {
            var it = EdnaReader.read("#{  }");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertTrue(((Set<?>) it).isEmpty());
        }
        {
            var it = EdnaReader.read("#{\t \n}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertTrue(((Set<?>) it).isEmpty());
        }
        {
            var it = EdnaReader.read("#{\n}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertTrue(((Set<?>) it).isEmpty());
        }
    }

    @Test
    public void parseEmptyMap() {
        // Normal
        {
            var it = EdnaReader.read("{}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertTrue(((Map<?, ?>) it).isEmpty());
        }

        // Whitespace does not matter
        {
            var it = EdnaReader.read("{  }");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertTrue(((Map<?, ?>) it).isEmpty());
        }
        {
            var it = EdnaReader.read("{\t \n}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertTrue(((Map<?, ?>) it).isEmpty());
        }
        {
            var it = EdnaReader.read("{\n}");
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
            var it = EdnaReader.read("(\\a)");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of('a'), iterableToList((Iterable<?>) it));
        }
        {
            var it = EdnaReader.read("(\\a \\b)");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of('a', 'b'), iterableToList((Iterable<?>) it));
        }
        {
            var it = EdnaReader.read("(())");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of(List.of()), iterableToList((Iterable<?>) it));
        }
    }

    @Test
    public void parseVectorSimple() {
        {
            var it = EdnaReader.read("[\\a]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of('a'), it);
        }
        {
            var it = EdnaReader.read("[\\a \\b]");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of('a', 'b'), ((List<?>) it));
        }
        {
            var it = EdnaReader.read("[[]]");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertEquals(List.of(List.of()), ((List<?>) it));
        }
    }

    @Test
    public void parseSetSimple() {
        {
            var it = EdnaReader.read("#{\\a \\b}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertEquals(Set.of('a', 'b'), ((Set<?>) it));
        }
        {
            var it = EdnaReader.read("#{ \\a }");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertEquals(Set.of('a'), ((Set<?>) it));
        }
        {
            var it = EdnaReader.read("#{\\a #{}}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertEquals(Set.of('a', Set.of()), ((Set<?>) it));
        }
    }

    @Test
    public void parseMapSimple() {
        {
            var it = EdnaReader.read("{\\a \\b}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertEquals(Map.of('a', 'b').entrySet(), ((Map<?, ?>) it).entrySet());
        }
        {
            var it = EdnaReader.read("{\\a {}}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertEquals(Map.of('a', Map.of()).entrySet(), ((Map<?, ?>) it).entrySet());
        }
        {
            var it = EdnaReader.read("{{} {}}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertEquals(Map.of(Map.of(), Map.of()), it);
        }
    }
}