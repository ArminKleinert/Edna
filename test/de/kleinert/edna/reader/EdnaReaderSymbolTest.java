package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaOptions;
import de.kleinert.edna.data.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EdnaReaderSymbolTest {
    @Test
    public void parseSymbolBasicTest() {
        {
            var text = "ab";
            var it = Edna.read(text, null, Symbol.class);
            var symbol = Symbol.parse(text);
            Assertions.assertInstanceOf(Symbol.class, it);

            Assertions.assertEquals(symbol, it);
            Assertions.assertNotNull(symbol);
            Assertions.assertEquals(symbol.getNamespace(), it.getNamespace());
            Assertions.assertEquals(symbol.getName(), it.getName());
        }
        {
            var text = "a1";
            var it = Edna.read(text, null, Symbol.class);
            var symbol = Symbol.parse(text);
            Assertions.assertInstanceOf(Symbol.class, it);

            Assertions.assertEquals(symbol, it);
            Assertions.assertNotNull(symbol);
            Assertions.assertEquals(symbol.getNamespace(), it.getNamespace());
            Assertions.assertEquals(symbol.getName(), it.getName());
        }
    }

    @Test
    public void parseSymbolWithNamespaceTest() {
        {
            var text = "a/b";
            var it = Edna.read(text, null, Symbol.class);
            var symbol = Symbol.parse(text);
            Assertions.assertInstanceOf(Symbol.class, it);

            Assertions.assertEquals(symbol, it);
            Assertions.assertNotNull(symbol);
            Assertions.assertEquals(symbol.getNamespace(), it.getNamespace());
            Assertions.assertEquals(symbol.getName(), it.getName());
        }
    }

    @Test
    public void parseSymbolSymbolsTest() {
        {
            var text = "+";
            var it = Edna.read(text, null, Symbol.class);
            var symbol = Symbol.parse(text);
            Assertions.assertInstanceOf(Symbol.class, it);

            Assertions.assertEquals(symbol, it);
            Assertions.assertNotNull(symbol);
            Assertions.assertEquals(symbol.getNamespace(), it.getNamespace());
            Assertions.assertEquals(symbol.getName(), it.getName());
        }
        {
            var text = "+-";
            var it = Edna.read(text, null, Symbol.class);
            var symbol = Symbol.parse(text);
            Assertions.assertInstanceOf(Symbol.class, it);

            Assertions.assertEquals(symbol, it);
            Assertions.assertNotNull(symbol);
            Assertions.assertEquals(symbol.getNamespace(), it.getNamespace());
            Assertions.assertEquals(symbol.getName(), it.getName());
        }
        {
            var text = "->";
            var it = Edna.read(text, null, Symbol.class);
            var symbol = Symbol.parse(text);
            Assertions.assertInstanceOf(Symbol.class, it);

            Assertions.assertEquals(symbol, it);
            Assertions.assertNotNull(symbol);
            Assertions.assertEquals(symbol.getNamespace(), it.getNamespace());
            Assertions.assertEquals(symbol.getName(), it.getName());
        }
        {
            var text = "===";
            var it = Edna.read(text, null, Symbol.class);
            var symbol = Symbol.parse(text);
            Assertions.assertInstanceOf(Symbol.class, it);

            Assertions.assertEquals(symbol, it);
            Assertions.assertNotNull(symbol);
            Assertions.assertEquals(symbol.getNamespace(), it.getNamespace());
            Assertions.assertEquals(symbol.getName(), it.getName());
        }
    }

    @Test
    public void parseSymbolSymbolsMixTest() {
        {
            var text = "a+";
            var it = Edna.read(text, null, Symbol.class);
            var symbol = Symbol.parse(text);
            Assertions.assertInstanceOf(Symbol.class, it);

            Assertions.assertEquals(symbol, it);
            Assertions.assertNotNull(symbol);
            Assertions.assertEquals(symbol.getNamespace(), it.getNamespace());
            Assertions.assertEquals(symbol.getName(), it.getName());
        }
        {
            var text = "-a";
            var it = Edna.read(text, null, Symbol.class);
            var symbol = Symbol.parse(text);
            Assertions.assertInstanceOf(Symbol.class, it);

            Assertions.assertEquals(symbol, it);
            Assertions.assertNotNull(symbol);
            Assertions.assertEquals(symbol.getNamespace(), it.getNamespace());
            Assertions.assertEquals(symbol.getName(), it.getName());
        }
    }

    @Test
    public void parseSymbolUTFTest() {
        { // 'λ' fits into simple chars.
            var text = "λ";
            var it = Edna.read(text, EdnaOptions.extendedOptions(), Symbol.class);
            var symbol = Symbol.parse(text, true);
            Assertions.assertInstanceOf(Symbol.class, it);

            Assertions.assertEquals(symbol, it);
            Assertions.assertNotNull(symbol);
            Assertions.assertEquals(symbol.getNamespace(), it.getNamespace());
            Assertions.assertEquals(symbol.getName(), it.getName());
        }
        { // '🎁' does not fit into simple chars, requiring options.allowUTFSymbols.
            var text = "🎁";
            var it = Edna.read(text, EdnaOptions.extendedOptions(), Symbol.class);
            var symbol = Symbol.parse(text, true);
            Assertions.assertInstanceOf(Symbol.class, it);

            Assertions.assertEquals(symbol, it);
            Assertions.assertNotNull(symbol);
            Assertions.assertEquals(symbol.getNamespace(), it.getNamespace());
            Assertions.assertEquals(symbol.getName(), it.getName());
        }
    }

    @Test
    public void parseInvalidSymbolTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("\uD83C\uDF81")); // UTF-16 only valid with extension.
    }
}
