package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaOptions;
import de.kleinert.edna.data.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class EdnaReaderSetsMapsTest {
    @Test
    public void parseEmpty() {
        {
            var it = Edna.read("{}", null, Map.class);
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertTrue(it.isEmpty());
        }
        {
            var it = Edna.read("#{}", null, Set.class);
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertTrue(it.isEmpty());
        }
    }

    @Test
    public void parseBasicList() {
        {
            var it = Edna.read("{a b c d}");
            Assertions.assertInstanceOf(Map.class, it);
            Assertions.assertEquals(
                    it,
                    Map.of(Symbol.symbol("a"), Symbol.symbol("b"), Symbol.symbol("c"), Symbol.symbol("d"))
            );
        }
        {
            var it = Edna.read("{1 2 3 4}");
            Assertions.assertEquals(Map.of(1L, 2L, 3L, 4L), it);
        }
        {
            var it = Edna.read("#{1 2 3 4}");
            Assertions.assertInstanceOf(Set.class, it);
            Assertions.assertEquals(Set.of(1L, 2L, 3L, 4L), it);
        }
    }

    @Test
    public void parseWithConverter() {
        {
            var options = Edna.defaultOptions().copy((b) -> b
                    .listToEdnMapConverter((it) -> new IdentityHashMap<>(it.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
            ));
            var parsed = Edna.read("{1 2}", options);
            Assertions.assertInstanceOf(IdentityHashMap.class, parsed);
            Assertions.assertEquals(Map.of(1L, 2L), parsed);
        }
        {
            var options = Edna.defaultOptions().copy((b) -> b.listToEdnSetConverter((it) -> new TreeSet<Long>()));
            var parsed = Edna.read("#{1 2}", options);
            Assertions.assertInstanceOf(TreeSet.class, parsed);
            Assertions.assertEquals(Set.<Long>of(), parsed);
        }
    }

    @Test
    public void invalidTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("{"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#{"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("}"));

        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("{1 2 3}")); // Odd number of elements
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("{1 2 1 3}"));// Duplicate key
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#{1 2 1}"));// Duplicate element
    }
}
