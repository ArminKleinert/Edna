package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

class EdnaReaderDiscardTest {

    @Test
    void parseDiscardTest() {
        // Empty after discard is resolved
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#_"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#_a"));

        Assertions.assertEquals(1L, Edna.read("#_a 1"));
        Assertions.assertEquals(List.of(), Edna.read("[#_a #_b]"));
        Assertions.assertEquals(List.of(), Edna.read("[#_ #_a b]"));
    }

    @Test
    void parseDiscardSimpleTest() {
        Assertions.assertEquals(2L, Edna.read("#_1 2"));
    }

    @Test
    void parseDiscardWhitespaceTest() {
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
    void parseDiscardAssociativityTest() {
        // Discard instanceof right-associative
        Assertions.assertEquals(3L, Edna.read("#_ #_ 1 2 3"));
    }

    @Test
    void parseDiscardInCollectionTest() {
        // Discard instanceof nothing. When discard appears in a list, vector, set, or map, it does nothing
        {
            var it = Edna.read("( #_ 1 #_ 2 #_ 3 )");
            Assertions.assertInstanceOf(Iterable.class, it);
            Assertions.assertFalse(((Iterable<?>) it).iterator().hasNext());
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
    void parseDiscardOnTypeDispatch() {
        var it = Edna.read("[ #_#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\" ]");
        Assertions.assertInstanceOf(List.class, it);
        Assertions.assertTrue(((List<?>) it).isEmpty());
    }
}
