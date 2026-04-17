package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaOptions;
import de.kleinert.edna.data.Symbol;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class EdnaReaderRefTest {
    private @NotNull EdnaOptions options() {
        return EdnaOptions.defaultOptions().copy((b) -> b.allowReferences(true));
    }

    private @NotNull EdnaOptions options(Map<Symbol, Object> refTable) {
        return EdnaOptions.defaultOptions().copy((b) ->
                b.allowReferences(true).referenceTable(refTable));
    }

    private @Nullable Object parse(String s) {
        return Edna.read(s, options());
    }

    @Test
    public void parseRefUndefTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref A"));

        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref B"));
    }

    @Test
    public void parseIllegalRefTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref"));
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref :A"));
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref 0.1"));
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref 1"));
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref 1N"));
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref 1M"));
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref \"\""));
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref ()"));
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref []"));
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref {}"));
        Assertions.assertThrows(EdnaReaderException.class, () -> parse("#ref #{}"));
    }

    @Test
    public void parseRefUsingOutsideTest() {
        var options = options();
        var refs = Map.<Symbol, Object>of(Symbol.symbol("A"), 1L, Symbol.symbol("B"), 2L);

        Assertions.assertEquals(1L, Edna.read("#ref A", options(refs)));
        Assertions.assertEquals(2L, Edna.read("#ref B", options(refs)));
    }

    @Test
    public void parseRefUsesReferencesOutsideTest() {
        var options = options();
        var value = List.of(1L, 2L, 3L);
        var refs = Map.<Symbol, Object>of(Symbol.symbol("A"), value);

        Assertions.assertEquals(value, Edna.read("#ref A", options(refs)));

        var temp = Edna.read("[#ref A #ref A]", options(refs), List.class);
        Assertions.assertNotNull(temp);
        Assertions.assertSame(value, temp.getFirst());
        Assertions.assertSame(value, temp.get(1));
    }

    @Test
    public void parseRefIsDynamicTest() {
        var options = options();
        var refs = Map.<Symbol, Object>of(
                Symbol.symbol("A"), 1L,
                Symbol.symbol("B"), Symbol.symbol("A"),
                Symbol.symbol("C"), Symbol.symbol("B"));

        Assertions.assertEquals(Symbol.symbol("A"), Edna.read("#ref B", options(refs)));
        Assertions.assertEquals(Symbol.symbol("B"), Edna.read("#ref C", options(refs)));
        Assertions.assertEquals(Symbol.symbol("A"), Edna.read("#ref #ref C", options(refs)));
        Assertions.assertEquals(1L, Edna.read("#ref #ref B", options(refs)));
        Assertions.assertEquals(1L, Edna.read("#ref #ref #ref C", options(refs)));
    }

    @Test
    public void parseDefRefInSameParseTest() {
        var text = "#ref A";
        Assertions.assertEquals(22L, Edna.read(text, options(Map.of(Symbol.symbol("A"), 22L))));

        var text2 = "#ref A";
        Assertions.assertEquals(List.of(1L, 2L), Edna.read(text2, options(Map.of(Symbol.symbol("A"), List.of(1L, 2L)))));

        var text3 = "#ref A";
        Assertions.assertEquals(List.of(5L, 6L), Edna.read(text3, options(Map.of(Symbol.symbol("A"), List.of(5L, 6L), Symbol.symbol("B"), 44L))));

        var temp = Edna.read("[#ref A #ref A]", options(Map.of(Symbol.symbol("A"), 22L)));
        Assertions.assertEquals(List.of(22L, 22L), temp);
    }

    @Test
    public void refFromConfig() {
        var options = options();

        var configText = "{ A 1, B 2, C 3 }";
        var config = Edna.read(configText, options, Map.class);

        var text = "[#ref A #ref B #ref C]";
        Assertions.assertEquals(List.of(1L, 2L, 3L), Edna.read(text, options(config)));
    }
}

