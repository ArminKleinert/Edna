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
        Assertions.assertEquals(List.of(), Edna.stream("").toList());
        Assertions.assertEquals(List.of(), Edna.stream("                      ").toList());
        Assertions.assertEquals(List.of(), Edna.stream(" \n ").toList());
        Assertions.assertEquals(List.of(), Edna.stream("\n ").toList());
        Assertions.assertEquals(List.of(), Edna.stream(", ").toList());
    }

    @Test
    void parseBasicSingleMultiTest() {
        Assertions.assertEquals(List.of(Symbol.symbol("abc")), Edna.stream("abc").toList());
        Assertions.assertEquals(List.of(123L), Edna.stream("123").toList());
    }

    @Test
    void parseBasicMultiTest() {
        Assertions.assertEquals(List.of(Symbol.symbol("abc"), Symbol.symbol("def")), Edna.stream("abc def").toList());
        Assertions.assertEquals(List.of("abc", "def"), Edna.stream("\"abc\" \"def\"").toList());
        Assertions.assertEquals(List.of(123L, "def"), Edna.stream("123 \"def\"").toList());

        Assertions.assertEquals(List.of(Symbol.symbol("abc"), Symbol.symbol("def")), Edna.stream(new StringReader("abc def"), null).toList());
        Assertions.assertEquals(List.of("abc", "def"), Edna.stream(new StringReader("\"abc\" \"def\""), null).toList());
        Assertions.assertEquals(List.of(123L, "def"), Edna.stream(new StringReader("123 \"def\""), null).toList());
    }

    @Test
    void parseMultiEmptyCommentTest() {
        Assertions.assertEquals(List.of(), Edna.stream("; comment").toList());
        Assertions.assertEquals(List.of(), Edna.stream("#_discard").toList());
        Assertions.assertEquals(List.of(), Edna.stream("#_discard #_again").toList());
    }

    @Test
    void parseErrorMultiTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.stream("/abc def").toList());
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.stream("ab \"").toList());

        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.stream(new StringReader("/abc def"), null).toList());
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.stream(new StringReader("ab \""), null).toList());
    }

    @Test
    void useParseMultiAsStream() {
        var contents = Edna.stream("0 1 2 3 4 5")
                .mapToInt(it -> Math.toIntExact((Long) it))
                .filter(it -> it % 2 == 0)
                .map(it -> it + 65)
                .mapToObj(it -> String.valueOf((char) it))
                .toList();
        Assertions.assertEquals(List.of("A", "C", "E"), contents);
    }
}