package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.data.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

class EdnaParserIteratorTest {
    @Test
    public void parseIteratorEmptyWhitespaceTest() {
        Assertions.assertFalse(Edna.reader("").hasNext());
        Assertions.assertFalse(Edna.reader("                      ").hasNext());
        Assertions.assertFalse(Edna.reader(" \n ").hasNext());
        Assertions.assertFalse(Edna.reader("\n ").hasNext());
        Assertions.assertFalse(Edna.reader(", ").hasNext());
    }

    @Test
    public void parseIteratorEmptyCommentTest() {
        Assertions.assertFalse(Edna.reader("; comment").hasNext());
        Assertions.assertFalse(Edna.reader("#_discard").hasNext());
        Assertions.assertFalse(Edna.reader("#_discard #_again").hasNext());
    }

    @Test
    public void parseIteratorBasicSingleTest() {
        {
            var iter = Edna.reader("abc");
            Assertions.assertTrue(iter.hasNext());
            Assertions.assertEquals(Symbol.symbol("abc"), iter.next());
            Assertions.assertFalse(iter.hasNext());
        }
        {
            var iter = Edna.reader("abc");
            Assertions.assertEquals(Symbol.symbol("abc"), iter.next());
        }
    }

    @Test
    public void parseIteratorBasicSingleOptionsTest() {
        {
            var iter = Edna.reader("abc", null);
            Assertions.assertEquals(Symbol.symbol("abc"), iter.next());
        }
        {
            var iter = Edna.reader("abc", Edna.defaultOptions());
            Assertions.assertEquals(Symbol.symbol("abc"), iter.next());
        }
        {
            var iter = Edna.reader("abc", Edna.extendedOptions());
            Assertions.assertEquals(Symbol.symbol("abc"), iter.next());
        }
        {
            var iter = Edna.reader("0xFF", Edna.defaultOptions());
            Assertions.assertThrows(EdnaReaderException.class, () -> iter.next());
        }
        {
            var iter = Edna.reader("0xFF", Edna.defaultOptions().copy(b -> b.moreNumberPrefixes(true)));
            Assertions.assertEquals(255L, iter.next());
        }
    }

    @Test
    public void parseIteratorBasicMultiTest() {
        {
            var iter = Edna.reader("0xFF 0x1", Edna.defaultOptions().copy(b -> b.moreNumberPrefixes(true)));
            Assertions.assertEquals(255L, iter.next());
            Assertions.assertEquals(1L, iter.next());
        }
    }

    @Test
    public void parseIteratorMultiWithDiscardTest() {
        {
            var iter = Edna.reader("0xFF #_discarded 0x1", Edna.defaultOptions().copy(b -> b.moreNumberPrefixes(true)));
            Assertions.assertEquals(255L, iter.next());
            Assertions.assertEquals(1L, iter.next());
        }
        {
            var iter = Edna.reader(
                    "#_discarded 0xFF #_again 0x1 #_andonemoretime",
                    Edna.defaultOptions().copy(b -> b.moreNumberPrefixes(true)));
            Assertions.assertEquals(255L, iter.next());
            Assertions.assertEquals(1L, iter.next());
        }
    }
}