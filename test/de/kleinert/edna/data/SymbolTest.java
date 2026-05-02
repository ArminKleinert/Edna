package de.kleinert.edna.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

class SymbolTest {
    @Test
    void getFullyQualified() {
        Assertions.assertFalse(Symbol.symbol(null, "abc").isFullyQualified());
        Assertions.assertFalse(Symbol.symbol("abc").isFullyQualified());
        Assertions.assertTrue(Symbol.symbol("ns", "abc").isFullyQualified());

        Assertions.assertFalse(Objects.requireNonNull(Symbol.parse("abc")).isFullyQualified());
        Assertions.assertTrue(Objects.requireNonNull(Symbol.parse("ns/abc")).isFullyQualified());

        Assertions.assertTrue(Symbol.symbol("", "abc").isFullyQualified());

        Assertions.assertFalse(Symbol.symbol("/").isFullyQualified());
    }

    @Test
    void getNamespace() {
        {
            var it = Symbol.symbol(null, "abc");
            Assertions.assertNull(it.getNamespace());
        }
        {
            var it = Symbol.symbol("abc");
            Assertions.assertNull(it.getNamespace());
        }
        {
            var it = Symbol.symbol("ns", "abc");
            Assertions.assertEquals("ns", it.getNamespace());
        }
        {
            var it = Symbol.symbol("", "abc");
            Assertions.assertEquals("", it.getNamespace());
        }
        {
            var it = Symbol.symbol("", "");
            Assertions.assertEquals("", it.getNamespace());
        }
    }

    @Test
    void getName() {
        {
            var it = Symbol.symbol(null, "abc");
            Assertions.assertEquals("abc", it.getName());
        }
        {
            var it = Symbol.symbol("abc");
            Assertions.assertEquals("abc", it.getName());
        }
        {
            var it = Symbol.symbol("ns", "abc");
            Assertions.assertEquals("abc", it.getName());
        }
        {
            var it = Symbol.symbol("");
            Assertions.assertEquals("", it.getName());
        }
        {
            var it = Symbol.symbol("", "");
            Assertions.assertEquals("", it.getName());
        }
    }

    @Test
    void isValidSymbolSimple() {
        Assertions.assertTrue(Symbol.isValidSymbol("abc"));
        Assertions.assertTrue(Symbol.isValidSymbol("ns/abc"));
        Assertions.assertTrue(Symbol.isValidSymbol("/"));
        Assertions.assertTrue(Symbol.isValidSymbol("+"));
        Assertions.assertTrue(Symbol.isValidSymbol("-"));
        Assertions.assertTrue(Symbol.isValidSymbol("."));
    }

    @Test
    void isValidSymbolInvalidFormat() {
        Assertions.assertFalse(Symbol.isValidSymbol("")); //not valid
        Assertions.assertFalse(Symbol.isValidSymbol("/abc")); // empty namespace
        Assertions.assertFalse(Symbol.isValidSymbol("ns/"));// no name
        Assertions.assertFalse(Symbol.isValidSymbol("+1"));// invalid tag char
        Assertions.assertFalse(Symbol.isValidSymbol("-1"));// invalid tag char
        Assertions.assertFalse(Symbol.isValidSymbol(".1"));// invalid tag char
        Assertions.assertFalse(Symbol.isValidSymbol("+1"));// invalid tag char
    }

    @Test
    void isValidSymbolInvalidChar() {
        Assertions.assertFalse(Symbol.isValidSymbol("🎁"));
        Assertions.assertFalse(Symbol.isValidSymbol("#"));
        Assertions.assertFalse(Symbol.isValidSymbol("~"));
        Assertions.assertFalse(Symbol.isValidSymbol("#a"));
        Assertions.assertFalse(Symbol.isValidSymbol("~a"));
        Assertions.assertFalse(Symbol.isValidSymbol("a#"));
        Assertions.assertFalse(Symbol.isValidSymbol("a~"));
    }

    @Test
    void isValidSymbol() {
        Assertions.assertTrue(Symbol.isValidSymbol("."));
        Assertions.assertTrue(Symbol.isValidSymbol("*"));
        Assertions.assertTrue(Symbol.isValidSymbol("+"));
        Assertions.assertTrue(Symbol.isValidSymbol("!"));
        Assertions.assertTrue(Symbol.isValidSymbol("-"));
        Assertions.assertTrue(Symbol.isValidSymbol("_"));
        Assertions.assertTrue(Symbol.isValidSymbol("?"));
        Assertions.assertTrue(Symbol.isValidSymbol("$"));
        Assertions.assertTrue(Symbol.isValidSymbol("%"));
        Assertions.assertTrue(Symbol.isValidSymbol("&"));
        Assertions.assertTrue(Symbol.isValidSymbol("="));
        Assertions.assertTrue(Symbol.isValidSymbol("<"));
        Assertions.assertTrue(Symbol.isValidSymbol(">"));
        Assertions.assertTrue(Symbol.isValidSymbol("🎁", true));
        Assertions.assertTrue(Symbol.isValidSymbol("a.b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a*b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a+b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a!b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a-b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a_b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a?b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a$b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a%b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a&b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a=b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a<b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a>b"));
        Assertions.assertTrue(Symbol.isValidSymbol("a🎁b", true));
    }

    @Test
    void parseNamespaceAndName() {
        Assertions.assertEquals(Symbol.symbol("ns", "abc"), Symbol.parse("ns/abc"));
    }

    @Test
    void parseName() {
        Assertions.assertEquals(Symbol.symbol("abc"), Symbol.parse("abc"));
    }

    @Test
    void parseInvalid() {
        Assertions.assertNull(Symbol.parse("")); //not valid
        Assertions.assertNull(Symbol.parse("/abc")); // empty namespace
        Assertions.assertNull(Symbol.parse("+1"));// invalid tag char
        Assertions.assertNull(Symbol.parse("-1"));// invalid tag char
        Assertions.assertNull(Symbol.parse(".1"));// invalid tag char
        Assertions.assertNull(Symbol.parse("🎁"));
        Assertions.assertNull(Symbol.parse("#"));
        Assertions.assertNull(Symbol.parse("~"));
        Assertions.assertNull(Symbol.parse("#a"));
        Assertions.assertNull(Symbol.parse("~a"));
        Assertions.assertNull(Symbol.parse("a#"));
        Assertions.assertNull(Symbol.parse("a~"));
    }

    @Test
    void parseInvalidWithNs() {
        Assertions.assertNull(Symbol.parse("ns/"));// no name
        Assertions.assertNull(Symbol.parse("ns/+1"));// invalid tag char
        Assertions.assertNull(Symbol.parse("ns/-1"));// invalid tag char
        Assertions.assertNull(Symbol.parse("ns/.1"));// invalid tag char
        Assertions.assertNull(Symbol.parse("ns/🎁"));
        Assertions.assertNull(Symbol.parse("ns/#"));
        Assertions.assertNull(Symbol.parse("ns/~"));
        Assertions.assertNull(Symbol.parse("ns/#a"));
        Assertions.assertNull(Symbol.parse("ns/~a"));
        Assertions.assertNull(Symbol.parse("ns/a#"));
        Assertions.assertNull(Symbol.parse("ns/a~"));
    }

    @Test
    void parseInvalidWitInvalidNs() {
        Assertions.assertNull(Symbol.parse("/abc")); // empty namespace
        Assertions.assertNull(Symbol.parse("+1/abc"));// invalid tag char
        Assertions.assertNull(Symbol.parse("-1/abc"));// invalid tag char
        Assertions.assertNull(Symbol.parse(".1/abc"));// invalid tag char
        Assertions.assertNull(Symbol.parse("🎁/abc"));
        Assertions.assertNull(Symbol.parse("#/abc"));
        Assertions.assertNull(Symbol.parse("~/abc"));
        Assertions.assertNull(Symbol.parse("#a/abc"));
        Assertions.assertNull(Symbol.parse("~a/abc"));
        Assertions.assertNull(Symbol.parse("a#/abc"));
        Assertions.assertNull(Symbol.parse("a~/abc"));
    }

    @Test
    void parseChecked() {
        Assertions.assertEquals(Symbol.symbol("ns", "abc"), Symbol.parseChecked("ns/abc"));
        Assertions.assertEquals(Symbol.symbol("abc"), Symbol.parseChecked("abc"));
        Assertions.assertEquals(Symbol.symbol("🎁"), Symbol.parseChecked("🎁", true));

        Assertions.assertThrows(IllegalArgumentException.class, () -> Symbol.parseChecked("")); //not valid
        Assertions.assertThrows(IllegalArgumentException.class, () -> Symbol.parseChecked("🎁"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Symbol.parseChecked("a/+1"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Symbol.parseChecked("🎁/a"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Symbol.parseChecked("a~/abc"));
    }

    @Test
    void symbolNamespaceAndName() {
        var xs = List.of(
                "", " ", "abc", "123", "#", "~", ".", "*", "+", "!",
                "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁");
        for (var string : xs) {
            var it = Symbol.symbol(string, "abc");
            Assertions.assertEquals(string, it.getNamespace());
            Assertions.assertEquals("abc", it.getName());
        }
        for (var string : xs) {
            var it = Symbol.symbol("a" + string, "abc");
            Assertions.assertEquals("a" + string, it.getNamespace());
            Assertions.assertEquals("abc", it.getName());
        }
        for (var string : xs) {
            var it = Symbol.symbol(string + "a", "abc");
            Assertions.assertEquals(string + "a", it.getNamespace());
            Assertions.assertEquals("abc", it.getName());
        }
        for (var string : xs) {
            var it = Symbol.symbol("a" + string + "b", "abc");
            Assertions.assertEquals("a" + string + "b", it.getNamespace());
            Assertions.assertEquals("abc", it.getName());
        }
    }

    @Test
    void symbolOnlyName() {
        var xs = List.of(
                "", " ", "abc", "123", "#", "~", ".", "*", "+", "!",
                "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁");
        for (var string : xs) {
            var it = Symbol.symbol(string);
            Assertions.assertNull(it.getNamespace());
            Assertions.assertEquals(string, it.getName());
        }
        for (var string : xs) {
            var it = Symbol.symbol("a" + string);
            Assertions.assertNull(it.getNamespace());
            Assertions.assertEquals("a" + string, it.getName());
        }
        for (var string : xs) {
            var it = Symbol.symbol(string + "a");
            Assertions.assertNull(it.getNamespace());
            Assertions.assertEquals(string + "a", it.getName());
        }
        for (var string : xs) {
            var it = Symbol.symbol("a" + string + "b");
            Assertions.assertNull(it.getNamespace());
            Assertions.assertEquals("a" + string + "b", it.getName());
        }
    }

    @Test
    void get() {
        Assertions.assertEquals(Symbol.symbol("ns", "abc"), Symbol.get("ns/abc"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Symbol.get("ns/🎁"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Symbol.get("ns/"));// no name
        Assertions.assertThrows(IllegalArgumentException.class, () -> Symbol.get("/abc")); // empty namespace
        Assertions.assertThrows(IllegalArgumentException.class, () -> Symbol.get("+1"));// invalid tag char
    }

    @Test
    void compareTo() {
        var ks = List.of(Symbol.symbol(null, ""), Symbol.symbol("a", "b"),
                Symbol.symbol(null, "b"), Symbol.symbol("a", "🎁"));
        var res = ks.stream().flatMap((k) -> ks.stream().map((b) -> Stream.of(k, b).sorted().toList())).toList();

        var expect = List.of(
                List.of(Symbol.symbol(null, ""), Symbol.symbol(null, "")),
                List.of(Symbol.symbol(null, ""), Symbol.symbol("a", "b")),
                List.of(Symbol.symbol(null, ""), Symbol.symbol(null, "b")),
                List.of(Symbol.symbol(null, ""), Symbol.symbol("a", "🎁")),
                List.of(Symbol.symbol(null, ""), Symbol.symbol("a", "b")),
                List.of(Symbol.symbol("a", "b"), Symbol.symbol("a", "b")),
                List.of(Symbol.symbol(null, "b"), Symbol.symbol("a", "b")),
                List.of(Symbol.symbol("a", "b"), Symbol.symbol("a", "🎁")),
                List.of(Symbol.symbol(null, ""), Symbol.symbol(null, "b")),
                List.of(Symbol.symbol(null, "b"), Symbol.symbol("a", "b")),
                List.of(Symbol.symbol(null, "b"), Symbol.symbol(null, "b")),
                List.of(Symbol.symbol(null, "b"), Symbol.symbol("a", "🎁")),
                List.of(Symbol.symbol(null, ""), Symbol.symbol("a", "🎁")),
                List.of(Symbol.symbol("a", "b"), Symbol.symbol("a", "🎁")),
                List.of(Symbol.symbol(null, "b"), Symbol.symbol("a", "🎁")),
                List.of(Symbol.symbol("a", "🎁"), Symbol.symbol("a", "🎁"))
        );
        Assertions.assertEquals(expect, res);
    }

    @Test
    void equals() {
        Assertions.assertEquals(Symbol.symbol(null, "abc"), Symbol.symbol(null, "abc"));
        Assertions.assertEquals(Symbol.symbol("ns", "+"), Symbol.symbol("ns", "+"));
        Assertions.assertEquals(Symbol.symbol("ns", "abc"), Symbol.symbol("ns", "abc"));
        Assertions.assertNotEquals(Symbol.symbol(null, "abc"), Symbol.symbol("ns", "+"));
    }
}