package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaOptions;
import de.kleinert.edna.data.Keyword;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EdnaReaderKeywordTest {
    @Test
    public void parseKeywordBasicTest() {
        {
            var text = ":ab";
            var it = Edna.read(text);
            var keyword = Keyword.parse(text);
            Assertions.assertInstanceOf(Keyword.class, it);
            var itKeyword = (Keyword) it;
            Assertions.assertSame(keyword, it);
            Assertions.assertNotNull(keyword);
            Assertions.assertEquals(keyword.getNamespace(), itKeyword.getNamespace());
            Assertions.assertEquals(keyword.getName(), itKeyword.getName());
        }
        {
            var text = ":a1";
            var it = Edna.read(text);
            var keyword = Keyword.parse(text);
            Assertions.assertInstanceOf(Keyword.class, it);
            var itKeyword = (Keyword) it;
            Assertions.assertSame(keyword, it);
            Assertions.assertNotNull(keyword);
            Assertions.assertEquals(keyword.getNamespace(), itKeyword.getNamespace());
            Assertions.assertEquals(keyword.getName(), itKeyword.getName());
        }
    }

    @Test
    public void parseKeywordWithNamespaceTest() {
        {
            var text = ":a/b";
            var it = Edna.read(text);
            var keyword = Keyword.parse(text);
            Assertions.assertInstanceOf(Keyword.class, it);
            var itKeyword = (Keyword) it;
            Assertions.assertSame(keyword, it);
            Assertions.assertNotNull(keyword);
            Assertions.assertEquals(keyword.getNamespace(), itKeyword.getNamespace());
            Assertions.assertEquals(keyword.getName(), itKeyword.getName());
        }
    }

    @Test
    public void parseKeywordSymbolsTest() {
        {
            var text = ":+";
            var it = Edna.read(text);
            var keyword = Keyword.parse(text);
            Assertions.assertInstanceOf(Keyword.class, it);
            var itKeyword = (Keyword) it;
            Assertions.assertSame(keyword, it);
            Assertions.assertNotNull(keyword);
            Assertions.assertEquals(keyword.getNamespace(), itKeyword.getNamespace());
            Assertions.assertEquals(keyword.getName(), itKeyword.getName());
        }
        {
            var text = ":+-";
            var it = Edna.read(text);
            var keyword = Keyword.parse(text);
            Assertions.assertInstanceOf(Keyword.class, it);
            var itKeyword = (Keyword) it;
            Assertions.assertSame(keyword, it);
            Assertions.assertNotNull(keyword);
            Assertions.assertEquals(keyword.getNamespace(), itKeyword.getNamespace());
            Assertions.assertEquals(keyword.getName(), itKeyword.getName());
        }
        {
            var text = ":->";
            var it = Edna.read(text);
            var keyword = Keyword.parse(text);
            Assertions.assertInstanceOf(Keyword.class, it);
            var itKeyword = (Keyword) it;
            Assertions.assertSame(keyword, it);
            Assertions.assertNotNull(keyword);
            Assertions.assertEquals(keyword.getNamespace(), itKeyword.getNamespace());
            Assertions.assertEquals(keyword.getName(), itKeyword.getName());
        }
        {
            var text = ":===";
            var it = Edna.read(text);
            var keyword = Keyword.parse(text);
            Assertions.assertInstanceOf(Keyword.class, it);
            var itKeyword = (Keyword) it;
            Assertions.assertSame(keyword, it);
            Assertions.assertNotNull(keyword);
            Assertions.assertEquals(keyword.getNamespace(), itKeyword.getNamespace());
            Assertions.assertEquals(keyword.getName(), itKeyword.getName());
        }
    }

    @Test
    public void parseKeywordSymbolsMixTest() {
        {
            var it = Edna.read(":a+");
            Assertions.assertInstanceOf(Keyword.class, it);
            Assertions.assertSame(Keyword.parse(":a+"), it);
        }
        {
            var it = Edna.read(":-a");
            Assertions.assertInstanceOf(Keyword.class, it);
            Assertions.assertSame(Keyword.parse(":-a"), it);
        }
        {
            var text = ":a+";
            var it = Edna.read(text);
            var keyword = Keyword.parse(text);
            Assertions.assertInstanceOf(Keyword.class, it);
            var itKeyword = (Keyword) it;
            Assertions.assertSame(keyword, it);
            Assertions.assertNotNull(keyword);
            Assertions.assertEquals(keyword.getNamespace(), itKeyword.getNamespace());
            Assertions.assertEquals(keyword.getName(), itKeyword.getName());
        }
        {
            var text = ":-a";
            var it = Edna.read(text);
            var keyword = Keyword.parse(text);
            Assertions.assertInstanceOf(Keyword.class, it);
            var itKeyword = (Keyword) it;
            Assertions.assertSame(keyword, it);
            Assertions.assertNotNull(keyword);
            Assertions.assertEquals(keyword.getNamespace(), itKeyword.getNamespace());
            Assertions.assertEquals(keyword.getName(), itKeyword.getName());
        }
    }

    @Test
    public void parseKeywordUTFTest() {
        { // 'λ' fits into simple chars.
            var text = ":λ";
            var it = Edna.read(text, EdnaOptions.extendedOptions());
            var keyword = Keyword.parse(text, true);
            Assertions.assertInstanceOf(Keyword.class, it);
            var itKeyword = (Keyword) it;
            Assertions.assertSame(keyword, it);
            Assertions.assertNotNull(keyword);
            Assertions.assertEquals(keyword.getNamespace(), itKeyword.getNamespace());
            Assertions.assertEquals(keyword.getName(), itKeyword.getName());
        }
        { // '🎁' does not fit into simple chars, requiring options.allowUTFSymbols.
            var text = ":🎁";
            var it = Edna.read(text, EdnaOptions.extendedOptions());
            var keyword = Keyword.parse(text, true);
            Assertions.assertInstanceOf(Keyword.class, it);
            var itKeyword = (Keyword) it;
            Assertions.assertSame(keyword, it);
            Assertions.assertNotNull(keyword);
            Assertions.assertEquals(keyword.getNamespace(), itKeyword.getNamespace());
            Assertions.assertEquals(keyword.getName(), itKeyword.getName());
        }
    }

    @Test
    public void parseInvalidKeywordTest() {
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read(":")); // Only colon instanceof invalid.
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read(":/")); // Colon+slash instanceof invalid.

        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("::")); // Double colon instanceof invalid.
        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("::abc")); // Double colon instanceof invalid.

        Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read(":\uD83C\uDF81")); // UTF-8 only valid with extension.
    }
}
