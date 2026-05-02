package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.data.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.*;

class EdnaReaderMultiTest {
    @Test public void parseMultiEmptyTest() {
        Assertions.assertEquals(List.of(), Edna.readMulti(""));
        Assertions.assertEquals(List.of(), Edna.readMulti("                      "));
        Assertions.assertEquals(List.of(), Edna.readMulti(" \n "));
        Assertions.assertEquals(List.of(), Edna.readMulti("\n "));
        Assertions.assertEquals(List.of(), Edna.readMulti(", "));
    }
    @Test
    public void parseBasicSingleMultiTest() {
        Assertions.assertEquals(List.of(Symbol.symbol("abc")), Edna.readMulti("abc"));
        Assertions.assertEquals(List.of(123L), Edna.readMulti("123"));
    }
    @Test
    public void parseBasicMultiTest() {
        Assertions.assertEquals(List.of(Symbol.symbol("abc"), Symbol.symbol("def")), Edna.readMulti("abc def"));
        Assertions.assertEquals(List.of("abc", "def"), Edna.readMulti("\"abc\" \"def\""));
        Assertions.assertEquals(List.of(123L, "def"), Edna.readMulti("123 \"def\""));

        Assertions.assertEquals(List.of(Symbol.symbol("abc"), Symbol.symbol("def")), Edna.readMulti(new StringReader("abc def"), null));
        Assertions.assertEquals(List.of("abc", "def"), Edna.readMulti(new StringReader("\"abc\" \"def\""), null));
        Assertions.assertEquals(List.of(123L, "def"), Edna.readMulti(new StringReader("123 \"def\""), null));
    }
    @Test
    public void parseErrorMultiTest() {
        Assertions.assertThrows(EdnaReaderException.class, ()->Edna.readMulti("/abc def"));
        Assertions.assertThrows(EdnaReaderException.class, ()->Edna.readMulti("ab \""));

        Assertions.assertThrows(EdnaReaderException.class, ()->Edna.readMulti(new StringReader("/abc def"), null));
        Assertions.assertThrows(EdnaReaderException.class, ()->Edna.readMulti(new StringReader("ab \""), null));
    }
}