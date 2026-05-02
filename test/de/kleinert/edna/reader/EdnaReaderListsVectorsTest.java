package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class EdnaReaderListsVectorsTest {

    @Test
    public void parseEmptyList() {
        // Normal
        {
            var it = Edna.read("()");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertTrue(((List<?>) it).isEmpty());
        }
        // Normal
        {
            var it = Edna.read("[]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertTrue(((List<?>) it).isEmpty());
        }

        // Whitespace does not matter
        {
            var it = Edna.read("(  )");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertTrue(((List<?>) it).isEmpty());
        }
        {
            var it = Edna.read("(\t \n)");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertTrue(((List<?>) it).isEmpty());
        }
        {
            var it = Edna.read("(\n)");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertTrue(((List<?>) it).isEmpty());
        }

        // Whitespace does not matter
        {
            var it = Edna.read("[  ]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertTrue(((List<?>) it).isEmpty());
        }
        {
            var it = Edna.read("[\t \n]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertTrue(((List<?>) it).isEmpty());
        }
        {
            var it = Edna.read("[\n]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertTrue(((List<?>) it).isEmpty());
        }
    }

    @Test
    public void parseBasicList() {
        {
            var it = Edna.read("(1)");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of(1L), it);
        }
        {
            var it = Edna.read("[1]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of(1L), it);
        }
        {
            var it = Edna.read("(1 2 3)");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of(1L, 2L, 3L), it);
        }
        {
            var it = Edna.read("[1 2 3]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of(1L, 2L, 3L), it);
        }
    }

    @Test
    public void parseNestedList() {
        {
            var it = Edna.read("((1))");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of(List.of(1L)), it);
        }
        {
            var it = Edna.read("([1])");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of(List.of(1L)), it);
        }
        {
            var it = Edna.read("[(1)]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of(List.of(1L)), it);
        }
        {
            var it = Edna.read("[(1)]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of(List.of(1L)), it);
        }
        {
            var it = Edna.read("(1 (2 3))");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of(1L, List.of(2L, 3L)), it);
        }
        {
            var it = Edna.read("[1 (2 3)]");
            Assertions.assertInstanceOf(List.class, it);
            Assertions.assertEquals(List.of(1L, List.of(2L, 3L)), it);
        }
    }

    @Test
    public void parseWithConverter() {
        {
            var options = Edna.defaultOptions().copy((builder -> builder.listToEdnListConverter(ArrayList::new)));
            var parsed = Edna.read("(1 2)", options);
            Assertions.assertInstanceOf(ArrayList.class, parsed);
            Assertions.assertEquals(List.of(1L, 2L), parsed);
        }
        {
            var options = Edna.defaultOptions().copy((builder -> builder.listToEdnVectorConverter(ArrayList::new)));
            var parsed = Edna.read("[1 2]", options);
            Assertions.assertInstanceOf(ArrayList.class, parsed);
            Assertions.assertEquals(List.of(1L, 2L), parsed);
        }
    }

    @Test
    public void invalidTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("("));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("["));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read(")"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("]"));
    }
}
