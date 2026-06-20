package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.data.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.*;

class EdnaReaderMultiTest {
    @Test
    void parseMultiEmptyWhitespaceTest() {
        Assertions.assertEquals(List.of(), Edna.readMulti("").toList());
        Assertions.assertEquals(List.of(), Edna.readMulti("                      ").toList());
        Assertions.assertEquals(List.of(), Edna.readMulti(" \n ").toList());
        Assertions.assertEquals(List.of(), Edna.readMulti("\n ").toList());
        Assertions.assertEquals(List.of(), Edna.readMulti(", ").toList());
    }

    @Test
    void parseMultiEmptyCommentTest() {
        Assertions.assertEquals(List.of(), Edna.readMulti("; comment").toList());
        Assertions.assertEquals(List.of(), Edna.readMulti("#_discard").toList());
        Assertions.assertEquals(List.of(), Edna.readMulti("#_discard #_again").toList());
    }

    @Test
    void parseBasicSingleMultiTest() {
        Assertions.assertEquals(List.of(Symbol.symbol("abc")), Edna.readMulti("abc").toList());
        Assertions.assertEquals(List.of(123L), Edna.readMulti("123").toList());
    }

    @Test
    void parseBasicMultiTest() {
        Assertions.assertEquals(List.of(Symbol.symbol("abc"), Symbol.symbol("def")), Edna.readMulti("abc def").toList());
        Assertions.assertEquals(List.of("abc", "def"), Edna.readMulti("\"abc\" \"def\"").toList());
        Assertions.assertEquals(List.of(123L, "def"), Edna.readMulti("123 \"def\"").toList());

        Assertions.assertEquals(List.of(Symbol.symbol("abc"), Symbol.symbol("def")), Edna.readMulti(new StringReader("abc def"), null).toList());
        Assertions.assertEquals(List.of("abc", "def"), Edna.readMulti(new StringReader("\"abc\" \"def\""), null).toList());
        Assertions.assertEquals(List.of(123L, "def"), Edna.readMulti(new StringReader("123 \"def\""), null).toList());
    }

    @Test
    void parseErrorMultiTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.readMulti("/abc def").toList());
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.readMulti("ab \"").toList());

        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.readMulti(new StringReader("/abc def"), null).toList());
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.readMulti(new StringReader("ab \""), null).toList());
    }
}