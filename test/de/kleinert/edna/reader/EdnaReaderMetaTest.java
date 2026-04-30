package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaOptions;

import de.kleinert.edna.data.EdnaCollections;
import de.kleinert.edna.data.Keyword;
import de.kleinert.edna.data.Symbol;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.util.Map;

class EdnaReaderMetaTest {
    private final EdnaOptions opts = Edna.defaultOptions().copy(b -> b.allowMetaData(true));

    @Test
    void parseMetaErrors() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("^", opts));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("^abc", opts));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("^[]", opts));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("^[] abc", opts));
    }

    @Test
    void parseDispatchMetaErrors() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^abc"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^[]"));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^[] abc"));

        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^", opts));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^abc", opts));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^[]", opts));
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^[] abc", opts));
    }

    @Test
    void parseMetaSymbolTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("^a b"));

        {
            var it = Edna.read("^a b", opts);
            Assertions.assertInstanceOf(EdnaCollections.IObj.class, it);
            Assertions.assertEquals(Map.of(Keyword.get("tag"), Symbol.symbol("a")), ((EdnaCollections.IObj) it).meta());
            Assertions.assertEquals(Symbol.symbol("b"), ((EdnaCollections.IObj) it).obj());
        }
    }

    @Test
    void parseMetaStringTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("^\"a\" b"));

        {
            var it = Edna.read("^\"a\" b", opts);
            Assertions.assertInstanceOf(EdnaCollections.IObj.class, it);
            Assertions.assertEquals(Map.of(Keyword.get("tag"), "a"), ((EdnaCollections.IObj) it).meta());
            Assertions.assertEquals(Symbol.symbol("b"), ((EdnaCollections.IObj) it).obj());
        }
    }

    @Test
    void parseMetaKeywordTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("^:a b"));

        {
            var it = Edna.read("^:a b", opts);
            Assertions.assertInstanceOf(EdnaCollections.IObj.class, it);
            Assertions.assertEquals(Map.of(Keyword.get("a"), true), ((EdnaCollections.IObj) it).meta());
            Assertions.assertEquals(Symbol.symbol("b"), ((EdnaCollections.IObj) it).obj());
        }
    }

    @Test
    void parseMetaMapTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("^{\"a\" \"c\"} b"));

        {
            var it = Edna.read("^{\"a\" \"c\"} b", opts);
            Assertions.assertInstanceOf(EdnaCollections.IObj.class, it);
            Assertions.assertEquals(Map.of("a", "c"), ((EdnaCollections.IObj) it).meta());
            Assertions.assertEquals(Symbol.symbol("b"), ((EdnaCollections.IObj) it).obj());
        }
    }

    @Test
    void parseDispatchMetaSymbolTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^a b"));

        {
            var it = Edna.read("#^a b", opts);
            Assertions.assertInstanceOf(EdnaCollections.IObj.class, it);
            Assertions.assertEquals(Map.of(Keyword.get("tag"), Symbol.symbol("a")), ((EdnaCollections.IObj) it).meta());
            Assertions.assertEquals(Symbol.symbol("b"), ((EdnaCollections.IObj) it).obj());
        }
    }

    @Test
    void parseDispatchMetaStringTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^\"a\" b"));

        {
            var it = Edna.read("#^\"a\" b", opts);
            Assertions.assertInstanceOf(EdnaCollections.IObj.class, it);
            Assertions.assertEquals(Map.of(Keyword.get("tag"), "a"), ((EdnaCollections.IObj) it).meta());
            Assertions.assertEquals(Symbol.symbol("b"), ((EdnaCollections.IObj) it).obj());
        }
    }

    @Test
    void parseDispatchMetaKeywordTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^:a b"));

        {
            var it = Edna.read("#^:a b", opts);
            Assertions.assertInstanceOf(EdnaCollections.IObj.class, it);
            Assertions.assertEquals(Map.of(Keyword.get("a"), true), ((EdnaCollections.IObj) it).meta());
            Assertions.assertEquals(Symbol.symbol("b"), ((EdnaCollections.IObj) it).obj());
        }
    }

    @Test
    void parseDispatchMetaMapTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("#^{\"a\" \"c\"} b"));

        {
            var it = Edna.read("#^{\"a\" \"c\"} b", opts);
            Assertions.assertInstanceOf(EdnaCollections.IObj.class, it);
            Assertions.assertEquals(Map.of("a", "c"), ((EdnaCollections.IObj) it).meta());
            Assertions.assertEquals(Symbol.symbol("b"), ((EdnaCollections.IObj) it).obj());
        }
    }
}