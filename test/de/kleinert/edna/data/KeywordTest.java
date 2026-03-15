package de.kleinert.edna.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

class KeywordTest {

    @Test
    void getFullyQualified() {
        Char32 c;
        Assertions.assertFalse(Keyword.keyword(null, "abc").isFullyQualified());
        Assertions.assertFalse(Keyword.keyword("abc").isFullyQualified());
        Assertions.assertTrue(Keyword.keyword("ns", "abc").isFullyQualified());

        Assertions.assertFalse(Objects.requireNonNull(Symbol.parse("abc")).isFullyQualified());
        Assertions.assertTrue(Objects.requireNonNull(Symbol.parse("ns/abc")).isFullyQualified());

        Assertions.assertTrue(Keyword.keyword("", "abc").isFullyQualified());

        Assertions.assertFalse(Keyword.keyword("/").isFullyQualified());
    }

    @Test
    void getNamespace() {
        {
            var it = Keyword.keyword(null, "abc");
            Assertions.assertNull(it.getNamespace());
        }
        {
            var it = Keyword.keyword("abc");
            Assertions.assertNull(it.getNamespace());
        }
        {
            var it = Keyword.keyword("ns", "abc");
            Assertions.assertEquals("ns", it.getNamespace());
        }
        {
            var it = Keyword.keyword("", "abc");
            Assertions.assertEquals("", it.getNamespace());
        }
        {
            var it = Keyword.keyword("", "");
            Assertions.assertEquals("", it.getNamespace());
        }
    }

    @Test
    void getName() {
        {
            var it = Keyword.keyword(null, "abc");
            Assertions.assertEquals("abc", it.getName());
        }
        {
            var it = Keyword.keyword("abc");
            Assertions.assertEquals("abc", it.getName());
        }
        {
            var it = Keyword.keyword("ns", "abc");
            Assertions.assertEquals("abc", it.getName());
        }
        {
            var it = Keyword.keyword("");
            Assertions.assertEquals("", it.getName());
        }
        {
            var it = Keyword.keyword("", "");
            Assertions.assertEquals("", it.getName());
        }
    }


    @Test
    void sym() {
        var xs = List.of(
                "", " ", "abc", "123", "#", "~", ".", "*", "+", "!",
                "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁"
        );
        for (var string : xs) {
            Assertions.assertEquals(Symbol.symbol(string), Keyword.keyword(Symbol.symbol(string)).getSym());

            Assertions.assertEquals(
                    Symbol.symbol("a" + string + "b", "abc"),
                    Keyword.keyword(Symbol.symbol("a" + string + "b", "abc")).getSym()
            );
        }
    }

    @Test
    void parseNamespaceAndName() {
        Assertions.assertEquals(Keyword.keyword("ns", "abc"), Keyword.parse(":ns/abc"));
    }

    @Test
    void parseName() {
        Assertions.assertEquals(Keyword.keyword("abc"), Keyword.parse(":abc"));
    }

    @Test
    void parseInvalid() {
        Assertions.assertNull(Keyword.parse(":")); //not valid
        Assertions.assertNull(Keyword.parse(":/abc")); // empty namespace
        Assertions.assertNull(Keyword.parse(":+1"));// invalid first char
        Assertions.assertNull(Keyword.parse(":-1"));// invalid first char
        Assertions.assertNull(Keyword.parse(":.1"));// invalid first char
        Assertions.assertNull(Keyword.parse(":🎁"));
        Assertions.assertNull(Keyword.parse(":#"));
        Assertions.assertNull(Keyword.parse(":~"));
        Assertions.assertNull(Keyword.parse(":#a"));
        Assertions.assertNull(Keyword.parse(":~a"));
        Assertions.assertNull(Keyword.parse(":a#"));
        Assertions.assertNull(Keyword.parse(":a~"));
    }

    @Test
    void parseInvalidWithNs() {
        Assertions.assertNull(Keyword.parse(":ns/"));// no name
        Assertions.assertNull(Keyword.parse(":ns/+1"));// invalid first char
        Assertions.assertNull(Keyword.parse(":ns/-1"));// invalid first char
        Assertions.assertNull(Keyword.parse(":ns/.1"));// invalid first char
        Assertions.assertNull(Keyword.parse(":ns/🎁"));
        Assertions.assertNull(Keyword.parse(":ns/#"));
        Assertions.assertNull(Keyword.parse(":ns/~"));
        Assertions.assertNull(Keyword.parse(":ns/#a"));
        Assertions.assertNull(Keyword.parse(":ns/~a"));
        Assertions.assertNull(Keyword.parse(":ns/a#"));
        Assertions.assertNull(Keyword.parse(":ns/a~"));
    }

    @Test
    void parseInvalidWitInvalidNs() {
        Assertions.assertNull(Keyword.parse(":/abc")); // empty namespace
        Assertions.assertNull(Keyword.parse(":+1/abc"));// invalid first char
        Assertions.assertNull(Keyword.parse(":-1/abc"));// invalid first char
        Assertions.assertNull(Keyword.parse(":.1/abc"));// invalid first char
        Assertions.assertNull(Keyword.parse(":🎁/abc"));
        Assertions.assertNull(Keyword.parse(":#/abc"));
        Assertions.assertNull(Keyword.parse(":~/abc"));
        Assertions.assertNull(Keyword.parse(":#a/abc"));
        Assertions.assertNull(Keyword.parse(":~a/abc"));
        Assertions.assertNull(Keyword.parse(":a#/abc"));
        Assertions.assertNull(Keyword.parse(":a~/abc"));
    }

    @Test
    void parseChecked() {
        Assertions.assertEquals(Keyword.keyword("ns", "abc"), Keyword.parseChecked("ns/abc"));
        Assertions.assertEquals(Keyword.keyword("abc"), Keyword.parseChecked("abc"));
        Assertions.assertEquals(Keyword.keyword("🎁"), Keyword.parseChecked("🎁", true));

        Assertions.assertThrows(IllegalArgumentException.class, () -> Keyword.parseChecked("")); //not valid
        Assertions.assertThrows(IllegalArgumentException.class, () -> Keyword.parseChecked("🎁"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Keyword.parseChecked("a/+1"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Keyword.parseChecked("🎁/a"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Keyword.parseChecked("a~/abc"));
    }

    @Test
    void keywordNamespaceAndName() {
        var xs = List.of(
                "", " ", "abc", "123", "#", "~", ".", "*", "+", "!",
                "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁");
        for (var string : xs) {
            var it = Keyword.keyword(string, "abc");
            Assertions.assertEquals(string, it.getNamespace());
            Assertions.assertEquals("abc", it.getName());
        }
        for (var string : xs) {
            var it = Keyword.keyword("a" + string, "abc");
            Assertions.assertEquals("a" + string, it.getNamespace());
            Assertions.assertEquals("abc", it.getName());
        }
        for (var string : xs) {
            var it = Keyword.keyword(string + "a", "abc");
            Assertions.assertEquals(string + "a", it.getNamespace());
            Assertions.assertEquals("abc", it.getName());
        }
        for (var string : xs) {
            var it = Keyword.keyword("a" + string + "b", "abc");
            Assertions.assertEquals("a" + string + "b", it.getNamespace());
            Assertions.assertEquals("abc", it.getName());
        }
    }

    @Test
    void keywordOnlyName() {
        var xs = List.of(
                "", " ", "abc", "123", "#", "~", ".", "*", "+", "!",
                "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁");
        for (var string : xs) {
            var it = Keyword.keyword(string);
            Assertions.assertNull(it.getNamespace());
            Assertions.assertEquals(string, it.getName());
        }
        for (var string : xs) {
            var it = Keyword.keyword("a" + string);
            Assertions.assertNull(it.getNamespace());
            Assertions.assertEquals("a" + string, it.getName());
        }
        for (var string : xs) {
            var it = Keyword.keyword(string + "a");
            Assertions.assertNull(it.getNamespace());
            Assertions.assertEquals(string + "a", it.getName());
        }
        for (var string : xs) {
            var it = Keyword.keyword("a" + string + "b");
            Assertions.assertNull(it.getNamespace());
            Assertions.assertEquals("a" + string + "b", it.getName());
        }
    }

    @Test
    void keywordSymbol() {
        var xs = List.of(
                "", " ", "abc", "123", "#", "~", ".", "*", "+", "!",
                "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁");
        for (var string : xs) {
            var it = Keyword.keyword(Symbol.symbol(string));
            Assertions.assertNull(it.getNamespace());
            Assertions.assertEquals(string, it.getName());
        }
        for (var string : xs) {
            var it = Keyword.keyword(Symbol.symbol("a" + string + "b"));
            Assertions.assertNull(it.getNamespace());
            Assertions.assertEquals("a" + string + "b", it.getName());
        }
    }

    @Test
    void internOnlyName() {
        var xs = List.of(
                "", " ", "abc", "123", "#", "~", ".", "*", "+", "!",
                "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁");
        for (var string : xs) {
            {
                var it = Keyword.intern(Symbol.symbol(string));
                Assertions.assertNull(it.getNamespace());
                Assertions.assertEquals(string, it.getName());
            }
            {
                var it = Keyword.intern(Symbol.symbol("a" + string + "b", "abc"));
                Assertions.assertEquals("a" + string + "b", it.getNamespace());
                Assertions.assertEquals("abc", it.getName());
            }
        }
    }

    @Test
    void intern() {
        var xs = List.of(
                "", " ", "abc", "123", "#", "~", ".", "*", "+", "!",
                "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁");
        for (var string : xs) {
            {
                var it = Keyword.intern(Symbol.symbol(string));
                Assertions.assertNull(it.getNamespace());
                Assertions.assertEquals(string, it.getName());
            }
            {
                var it = Keyword.intern(Symbol.symbol("a" + string + "b", "abc"));
                Assertions.assertEquals("a" + string + "b", it.getNamespace());
                Assertions.assertEquals("abc", it.getName());
            }
        }
    }

    @Test
    void get() {
        Assertions.assertEquals(Keyword.keyword(null, "abc"), Keyword.get("abc"));
        Assertions.assertEquals(Keyword.keyword("ns", "+"), Keyword.get("ns/+"));
        Assertions.assertEquals(Keyword.keyword("ns", "abc"), Keyword.get("ns/abc"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Keyword.parseChecked("ns/🎁"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Keyword.parseChecked("ns/"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Keyword.parseChecked("/abc"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Keyword.parseChecked("+1"));
    }


    @Test
    void getSymbol() {
        Assertions.assertSame(Keyword.intern(Symbol.symbol("abc")), Keyword.get(Symbol.symbol("abc")));
        Assertions.assertSame(Keyword.intern(Symbol.symbol("ns", "+")), Keyword.get(Symbol.symbol("ns", "+")));
        Assertions.assertSame(Keyword.intern(Symbol.symbol("ns", "abc")), Keyword.get(Symbol.symbol("ns", "abc")));
    }

    @Test
    void findSymbol() {
        var random = new Random(0xCAFEC0FFEEL);
       var it = Symbol.symbol(""+random.nextDouble()); // A hopefully random name
            Assertions.assertNull(Keyword.find(it)); // Did not exist.
            Assertions.assertNull(Keyword.find(it)); // Was not interned by the first call.

            var k = Keyword.intern(it); // Create and intern
            Assertions.assertEquals(k, Keyword.find(it));
    }

    @Test
    void findString() {
        var random = new Random(0xDEADBEEFL);
        var it = ""+random.nextDouble(); // A hopefully random name

        Assertions.assertNull(Keyword.find(it)); // Did not exist.
        Assertions.assertNull(Keyword.find(it)); // Was not interned by the first call.

        var k = Keyword.get(it); // Create and intern
        Assertions.assertEquals(k, Keyword.find(it));
    }


    @Test
    void compareTo() {
        var ks = List.of(Keyword.keyword(null, ""), Keyword.keyword("a", "b"),
                Keyword.keyword(null, "b"), Keyword.keyword("a", "🎁"));
        var res = ks.stream().flatMap((k) -> ks.stream().map((b) -> Stream.of(k, b).sorted().toList())).toList();

        var expect = List.of(
                List.of(Keyword.keyword(null, ""), Keyword.keyword(null, "")),
                List.of(Keyword.keyword(null, ""), Keyword.keyword("a", "b")),
                List.of(Keyword.keyword(null, ""), Keyword.keyword(null, "b")),
                List.of(Keyword.keyword(null, ""), Keyword.keyword("a", "🎁")),
                List.of(Keyword.keyword(null, ""), Keyword.keyword("a", "b")),
                List.of(Keyword.keyword("a", "b"), Keyword.keyword("a", "b")),
                List.of(Keyword.keyword(null, "b"), Keyword.keyword("a", "b")),
                List.of(Keyword.keyword("a", "b"), Keyword.keyword("a", "🎁")),
                List.of(Keyword.keyword(null, ""), Keyword.keyword(null, "b")),
                List.of(Keyword.keyword(null, "b"), Keyword.keyword("a", "b")),
                List.of(Keyword.keyword(null, "b"), Keyword.keyword(null, "b")),
                List.of(Keyword.keyword(null, "b"), Keyword.keyword("a", "🎁")),
                List.of(Keyword.keyword(null, ""), Keyword.keyword("a", "🎁")),
                List.of(Keyword.keyword("a", "b"), Keyword.keyword("a", "🎁")),
                List.of(Keyword.keyword(null, "b"), Keyword.keyword("a", "🎁")),
                List.of(Keyword.keyword("a", "🎁"), Keyword.keyword("a", "🎁"))
        );
        Assertions.assertEquals(expect, res);
    }

    @Test
    void equals() {
        Assertions.assertEquals(Keyword.keyword(null, "abc"), Keyword.keyword(null, "abc"));
        Assertions.assertEquals(Keyword.keyword("ns", "+"), Keyword.keyword("ns", "+"));
        Assertions.assertEquals(Keyword.keyword("ns", "abc"), Keyword.keyword("ns", "abc"));

        Assertions.assertNotEquals(Keyword.keyword(null, "abc"), Keyword.keyword("ns", "+"));
    }

    @Test
    void equalsBecauseInterned() {
        Assertions.assertSame(Keyword.keyword(null, "abc"), Keyword.keyword(null, "abc"));
        Assertions.assertSame(Keyword.keyword("ns", "+"), Keyword.keyword("ns", "+"));
        Assertions.assertSame(Keyword.keyword("ns", "abc"), Keyword.keyword("ns", "abc"));

        Assertions.assertNotSame(Keyword.keyword(null, "abc"), Keyword.keyword("ns", "+"));
    }
}