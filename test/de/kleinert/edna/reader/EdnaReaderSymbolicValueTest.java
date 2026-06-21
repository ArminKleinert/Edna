package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.data.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;

class EdnaReaderSymbolicValueTest {
    @Test
    void parseStandardSymbolicTest() {
        var optionsWithSymbolics = Edna.defaultOptions().copy(b -> b.allowSymbolicValues(true));
        Assertions.assertTrue(((Double) Objects.requireNonNull(Edna.read("##NaN", optionsWithSymbolics))).isNaN());
        Assertions.assertEquals(Double.POSITIVE_INFINITY, Edna.read("##Inf", optionsWithSymbolics));
        Assertions.assertEquals(Double.NEGATIVE_INFINITY, Edna.read("##-Inf", optionsWithSymbolics));
    }

    @Test
    void parseUserDefSymbolicTest() {
        var o = Integer.valueOf(1);
        var sv = Map.of(Symbol.symbol("abc"), o);
        var optionsWithSymbolics = Edna.defaultOptions().copy(b -> b
                .allowSymbolicValues(true)
                .symbolicValues(sv));
        Assertions.assertSame(o, Edna.read("##abc", optionsWithSymbolics));
    }
}
