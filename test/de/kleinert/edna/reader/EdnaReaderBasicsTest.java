package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.util.*;

class EdnaReaderBasicsTest {
    @Test
    void parseBasicExampleTest() {
        Assertions.assertEquals(List.of("abc"), Edna.read("[\"abc\"]"));

        var opts = Edna.defaultOptions().copy(b -> b.allowSymbolicValues(true));
        Assertions.assertTrue(Objects.requireNonNull(Edna.read("##Inf", opts, Double.class)).isInfinite());

        Assertions.assertEquals(List.of(1L, 2L, 3L), Edna.stream("1 2 3").toList());

        Edna.reader("1 2 3").forEachRemaining(System.out::println);
    }

    @Test
    void parseStringBasicTest() {
        Assertions.assertInstanceOf(String.class, Edna.read("\"\""));
        Assertions.assertEquals("", Edna.read("\"\""));
        Assertions.assertEquals("abc", Edna.read("\"abc\""));
    }

    @Test
    void parseStringEscapeSequenceTest() {
        Assertions.assertEquals("\n", Edna.read("\"\\n\""));
        Assertions.assertEquals(List.of("", ""), Arrays.asList(((String) Objects.requireNonNull(Edna.read("\"\\n\""))).split("\n", -1)));
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

    private <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> output = new ArrayList<>();
        for (T t : iterable) {
            output.add(t);
        }
        return output;
    }

    @Test
    void parseListSimple() {
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
    void parseVectorSimple() {
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
    void parseSetSimple() {
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