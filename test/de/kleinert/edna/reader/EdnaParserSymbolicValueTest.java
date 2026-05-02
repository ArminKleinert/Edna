package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.data.Char32;
import de.kleinert.edna.data.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EdnaParserSymbolicValueTest {
    @Test
    public void parseStandardSymbolicTest() {
        var optionsWithSymbolics = Edna.defaultOptions().copy(b -> b.allowSymbolicValues(true));
        Assertions.assertTrue(((Double) Edna.read("##NaN", optionsWithSymbolics)).isNaN());
        Assertions.assertEquals(Double.POSITIVE_INFINITY, Edna.read("##Inf", optionsWithSymbolics));
        Assertions.assertEquals(Double.NEGATIVE_INFINITY, Edna.read("##-Inf", optionsWithSymbolics));
    }

    @Test
    public void parseUserDefSymbolicTest() {
        var o = new Object();
        var sv = Map.of(Symbol.symbol("abc"), o);
        var optionsWithSymbolics = Edna.defaultOptions().copy(b -> b
                .allowSymbolicValues(true)
                .symbolicValues(sv));
        Assertions.assertSame(o, Edna.read("##abc", optionsWithSymbolics));
    }
}
