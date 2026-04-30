package de.kleinert.edna.pprint;

import de.kleinert.edna.Edna;
import de.kleinert.edna.data.Char32;
import de.kleinert.edna.data.EdnaCollections;
import de.kleinert.edna.data.Keyword;
import de.kleinert.edna.data.Symbol;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class EdnaWriterTest {

    @Test
    void encodeNull() {
        Assertions.assertEquals("nil", Edna.pprintToString(null));

        var appendable = new StringBuilder();
        Edna.pprint(null, appendable);
        Assertions.assertEquals("nil", appendable.toString());

        var appendable2 = new StringBuilder();
        Edna.pprintln(null, appendable2);
        Assertions.assertEquals("nil\n", appendable2.toString());
    }

    @Test
    void encodeTrue() {
        Assertions.assertEquals("true", Edna.pprintToString(true));
    }

    @Test
    void encodeFalse() {
        Assertions.assertEquals("false", Edna.pprintToString(false));
    }

    @Test
    void encodeString() {
        Assertions.assertEquals("\"\"", Edna.pprintToString(""));
        Assertions.assertEquals("\"abc\"", Edna.pprintToString("abc"));
    }

    @Test
    void encodeChar() {
        Assertions.assertEquals("\\a", Edna.pprintToString('a'));
    }

    @Test
    void encodeCharSpecial() {
        Assertions.assertEquals("\\newline", Edna.pprintToString('\n'));
        Assertions.assertEquals("\\space", Edna.pprintToString(' '));
        Assertions.assertEquals("\\tab", Edna.pprintToString('\t'));
        Assertions.assertEquals("\\backspace", Edna.pprintToString('\b'));
        Assertions.assertEquals("\\formfeed", Edna.pprintToString((char) 12));
        Assertions.assertEquals("\\return", Edna.pprintToString('\r'));
    }

    @Test
    void encodeCharCode() {
        Assertions.assertEquals("\\u271d", Edna.pprintToString('\u271D'));
    }

    @Test
    void encodeChar32() {
        Assertions.assertEquals("#\\a", Edna.pprintToString(Char32.valueOf('a'), Edna.extendedOptions()));

        Assertions.assertEquals("#\\newline", Edna.pprintToString(Char32.valueOf('\n'), Edna.extendedOptions()));
        Assertions.assertEquals("#\\space", Edna.pprintToString(Char32.valueOf(' '), Edna.extendedOptions()));
        Assertions.assertEquals("#\\tab", Edna.pprintToString(Char32.valueOf('\t'), Edna.extendedOptions()));
        Assertions.assertEquals("#\\backspace", Edna.pprintToString(Char32.valueOf('\b'), Edna.extendedOptions()));
        Assertions.assertEquals("#\\formfeed", Edna.pprintToString(Char32.valueOf(12), Edna.extendedOptions()));
        Assertions.assertEquals("#\\return", Edna.pprintToString(Char32.valueOf('\r'), Edna.extendedOptions()));

        Assertions.assertEquals("#\\u0000271d", Edna.pprintToString(Char32.valueOf('\u271D'), Edna.extendedOptions()));
        Assertions.assertEquals("#\\u0001f546", Edna.pprintToString(Char32.valueOf(0x0001F546), Edna.extendedOptions()));
    }

    @Test
    void encodeChar32AsString() {
        Assertions.assertEquals("\"a\"", Edna.pprintToString(Char32.valueOf('a')));

        Assertions.assertEquals("\"\n\"", Edna.pprintToString(Char32.valueOf('\n')));
        Assertions.assertEquals("\" \"", Edna.pprintToString(Char32.valueOf(' ')));
        Assertions.assertEquals("\"\t\"", Edna.pprintToString(Char32.valueOf('\t')));
        Assertions.assertEquals("\"\b\"", Edna.pprintToString(Char32.valueOf('\b')));
        Assertions.assertEquals("\"\u000C\"", Edna.pprintToString(Char32.valueOf(12)));
        Assertions.assertEquals("\"\r\"", Edna.pprintToString(Char32.valueOf('\r')));

        Assertions.assertEquals("\"✝\"", Edna.pprintToString(Char32.valueOf('\u271D')));
        Assertions.assertEquals("\"\ud83d\udd46\"", Edna.pprintToString(Char32.valueOf(0x0001F546)));
    }

    @Test
    void encodeByte() {
        Assertions.assertEquals("0", Edna.pprintToString((byte) 0));
        Assertions.assertEquals("1", Edna.pprintToString((byte) 1));
        Assertions.assertEquals("-1", Edna.pprintToString((byte) (-1)));

        var options = Edna.defaultOptions().copy(b -> b.allowNumericSuffixes(true));
        Assertions.assertEquals("0_i8", Edna.pprintToString((byte) 0, options));
        Assertions.assertEquals("1_i8", Edna.pprintToString((byte) 1, options));
        Assertions.assertEquals("-1_i8", Edna.pprintToString((byte) (-1), options));
    }

    @Test
    void encodeShort() {
        Assertions.assertEquals("0", Edna.pprintToString((short) 0));
        Assertions.assertEquals("1", Edna.pprintToString((short) 1));
        Assertions.assertEquals("-1", Edna.pprintToString((short) (-1)));

        var options = Edna.defaultOptions().copy(b -> b.allowNumericSuffixes(true));
        Assertions.assertEquals("0_i16", Edna.pprintToString((short) 0, options));
        Assertions.assertEquals("1_i16", Edna.pprintToString((short) 1, options));
        Assertions.assertEquals("-1_i16", Edna.pprintToString((short) (-1), options));
    }

    @Test
    void encodeInt() {
        Assertions.assertEquals("0", Edna.pprintToString(0));
        Assertions.assertEquals("1", Edna.pprintToString(1));
        Assertions.assertEquals("-1", Edna.pprintToString(-1));

        var options = Edna.defaultOptions().copy(b -> b.allowNumericSuffixes(true));
        Assertions.assertEquals("0_i32", Edna.pprintToString(0, options));
        Assertions.assertEquals("1_i32", Edna.pprintToString(1, options));
        Assertions.assertEquals("-1_i32", Edna.pprintToString((-1), options));
    }

    @Test
    void encodeLong() {
        Assertions.assertEquals("0", Edna.pprintToString(0L));
        Assertions.assertEquals("1", Edna.pprintToString(1L));
        Assertions.assertEquals("-1", Edna.pprintToString(-1L));
    }

    @Test
    void encodeFloat() {
        Assertions.assertEquals("0.0", Edna.pprintToString(0.0));
        Assertions.assertEquals("1.0", Edna.pprintToString(1.0));
        Assertions.assertEquals("1.5", Edna.pprintToString(1.5));
        Assertions.assertEquals("-1.5", Edna.pprintToString(-1.5));
    }

    @Test
    void encodeDouble() {
        Assertions.assertEquals("0.0", Edna.pprintToString(0.0));
        Assertions.assertEquals("1.0", Edna.pprintToString(1.0));
        Assertions.assertEquals("1.5", Edna.pprintToString(1.5));
        Assertions.assertEquals("-1.5", Edna.pprintToString(-1.5));
    }

    @Test
    void encodeBigInteger() {
        Assertions.assertEquals("123N", Edna.pprintToString(BigInteger.valueOf(123L)));
    }

    @Test
    void encodeBigDecimal() {
        Assertions.assertEquals("123M", Edna.pprintToString(BigDecimal.valueOf(123L)));
    }

    @Test
    void encodeIObj() {
        Assertions.assertEquals("^{:tag \"abc\"} :a", Edna.pprintToString(EdnaCollections.IObj.of("abc", Keyword.get("a"))));
        Assertions.assertEquals("^{:abc true} :a", Edna.pprintToString(EdnaCollections.IObj.of(Keyword.get("abc"), Keyword.get("a"))));
        Assertions.assertEquals("^{:tag abc} :a", Edna.pprintToString(EdnaCollections.IObj.of(Symbol.symbol("abc"), Keyword.get("a"))));
        Assertions.assertEquals("^{\"b\" \"c\"} :a", Edna.pprintToString(EdnaCollections.IObj.of(Map.of("b", "c"), Keyword.get("a"))));
    }

    @Test
    void encodePersistentList() {
        Assertions.assertEquals("()", Edna.pprintToString(EdnaCollections.EdnaList.<Keyword>of()));
        Assertions.assertEquals("(:a, :b)", Edna.pprintToString(EdnaCollections.EdnaList.of(Keyword.get("a"), Keyword.get("b"))));
    }

    private List<Byte> bytesToList(byte[] bytes) {
        return IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).toList();
    }

    private List<Short> shortsToList(short[] bytes) {
        return IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).toList();
    }

    private List<Float> floatsToList(float[] bytes) {
        return IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).toList();
    }

    @Test
    void encodeByteArray() {
        Assertions.assertEquals("[]", Edna.pprintToString(new byte[0]));
        Assertions.assertEquals("[0, 0]", Edna.pprintToString(new byte[2]));

        var encoders = new LinkedHashMap<Class<?>, Function<Object, Map.Entry<String, ?>>>(Map.of(
                byte[].class, (it) -> new AbstractMap.SimpleEntry<>("bytearray", bytesToList((byte[]) it))
        ));
        var options = Edna.defaultOptions().copy(b -> b.ednClassEncoders(encoders));
        Assertions.assertEquals("#bytearray [0, 0]", Edna.pprintToString(new byte[2], options));
    }

    @Test
    void encodeShortArray() {
        Assertions.assertEquals("[]", Edna.pprintToString(new short[0]));
        Assertions.assertEquals("[0, 0]", Edna.pprintToString(new short[2]));

        var encoders = new LinkedHashMap<Class<?>, Function<Object, Map.Entry<String, ?>>>(Map.of(
                short[].class, (it) -> new AbstractMap.SimpleEntry<>("shortarray", shortsToList((short[]) it))
        ));
        var options = Edna.defaultOptions().copy(b -> b.ednClassEncoders(encoders));
        Assertions.assertEquals("#shortarray [0, 0]", Edna.pprintToString(new short[2], options));
    }

    @Test
    void encodeIntArray() {
        Assertions.assertEquals("[]", Edna.pprintToString(new int[0]));
        Assertions.assertEquals("[0, 0]", Edna.pprintToString(new int[2]));

        var encoders = new LinkedHashMap<Class<?>, Function<Object, Map.Entry<String, ?>>>(Map.of(
                int[].class, (it) -> new AbstractMap.SimpleEntry<>("intarray", Arrays.stream(((int[]) it)).boxed().toList())
        ));
        var options = Edna.defaultOptions().copy(b -> b.ednClassEncoders(encoders));
        Assertions.assertEquals("#intarray [0, 0]", Edna.pprintToString(new int[2], options));
    }

    @Test
    void encodeLongArray() {
        Assertions.assertEquals("[]", Edna.pprintToString(new long[0]));
        Assertions.assertEquals("[0, 0]", Edna.pprintToString(new long[2]));

        var encoders = new LinkedHashMap<Class<?>, Function<Object, Map.Entry<String, ?>>>(Map.of(
                long[].class, (it) -> new AbstractMap.SimpleEntry<>("longarray", Arrays.stream(((long[]) it)).boxed().toList())
        ));
        var options = Edna.defaultOptions().copy(b -> b.ednClassEncoders(encoders));
        Assertions.assertEquals("#longarray [0, 0]", Edna.pprintToString(new long[2], options));
    }

    @Test
    void encodeFloatArray() {
        Assertions.assertEquals("[]", Edna.pprintToString(new float[0]));
        Assertions.assertEquals("[0.0, 0.0]", Edna.pprintToString(new float[2]));

        var encoders = new LinkedHashMap<Class<?>, Function<Object, Map.Entry<String, ?>>>(Map.of(
                float[].class, (it) -> new AbstractMap.SimpleEntry<>("floatarray", floatsToList((float[]) it))
        ));
        var options = Edna.defaultOptions().copy(b -> b.ednClassEncoders(encoders));
        Assertions.assertEquals("#floatarray [0.0, 0.0]", Edna.pprintToString(new float[2], options));
    }

    @Test
    void encodeDoubleArray() {
        Assertions.assertEquals("[]", Edna.pprintToString(new double[0]));
        Assertions.assertEquals("[0.0, 0.0]", Edna.pprintToString(new double[2]));

        var encoders = new LinkedHashMap<Class<?>, Function<Object, Map.Entry<String, ?>>>(Map.of(
                double[].class, (it) -> new AbstractMap.SimpleEntry<>("doublearray", Arrays.stream(((double[]) it)).boxed().toList())
        ));
        var options = Edna.defaultOptions().copy(b -> b.ednClassEncoders(encoders));
        Assertions.assertEquals("#doublearray [0.0, 0.0]", Edna.pprintToString(new double[2], options));
    }

    @Test
    void encodeArray() {
        Assertions.assertEquals("[]", Edna.pprintToString(new Keyword[0]));
        Assertions.assertEquals("[:a, :b]", Edna.pprintToString(new Keyword[]{Keyword.get("a"), Keyword.get("b")}));

        var encoders = new LinkedHashMap<Class<?>, Function<Object, Map.Entry<String, ?>>>(Map.of(
                Keyword[].class, (it) -> new AbstractMap.SimpleEntry<>("array", Arrays.stream(((Keyword[]) it)).toList())
        ));
        var options = Edna.defaultOptions().copy(b -> b.ednClassEncoders(encoders));
        Assertions.assertEquals("#array [:a, :b]", Edna.pprintToString(new Keyword[]{Keyword.get("a"), Keyword.get("b")}, options));
    }

    @Test
    void encodeVector() {
        Assertions.assertEquals("[]", Edna.pprintToString(EdnaCollections.EdnaVector.of()));
        Assertions.assertEquals("[:a, :b]", Edna.pprintToString(EdnaCollections.EdnaVector.of(Keyword.get("a"), Keyword.get("b"))));
        Assertions.assertEquals("[:a, :b]", Edna.pprintToString(List.of(Keyword.get("a"), Keyword.get("b"))));
    }

    @Test
    void encodeSet() {
        Assertions.assertEquals("#{}", Edna.pprintToString(EdnaCollections.EdnaSet.of()));
        Assertions.assertEquals("#{:a, :b}", Edna.pprintToString(EdnaCollections.EdnaSet.of(Keyword.get("a"), Keyword.get("b"))));

        var encoders = new LinkedHashMap<Class<?>, Function<Object, Map.Entry<String, ?>>>(Map.of(
                Set.class, (it) -> new AbstractMap.SimpleEntry<>("set", ((Set<?>) it).stream().toList())
        ));
        var options = Edna.defaultOptions().copy(b -> b.ednClassEncoders(encoders));
        Assertions.assertEquals(
                "#set [:a, :b]",
                Edna.pprintToString(EdnaCollections.EdnaSet.of(Keyword.get("a"), Keyword.get("b")), options)
        );
    }

    @Test
    void encodeMap() {
        Assertions.assertEquals("{}", Edna.pprintToString(EdnaCollections.EdnaMap.of()));
        Assertions.assertEquals(
                "{:a :b, :c :d}",
                Edna.pprintToString(EdnaCollections.EdnaMap.of(Keyword.get("a"), Keyword.get("b"), Keyword.get("c"), Keyword.get("d")))
        );
    }

    private record TestIterableNotList(int @NotNull [] arr) implements Iterable<Integer> {
        @Override
        public @NotNull Iterator<Integer> iterator() {
            return Arrays.stream(arr).boxed().iterator();
        }
    }

    @Test
    void encodeIterable() {
        Assertions.assertEquals("()", Edna.pprintToString(new TestIterableNotList(new int[0])));
        Assertions.assertEquals("(0, 1)", Edna.pprintToString(new TestIterableNotList(new int[]{0, 1})));
    }

    @Test
    void encodeUUID() {
        Assertions.assertEquals(
                "#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"",
                Edna.pprintToString(UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6"))
        );
    }

    @Test
    void encodeInstant() {
        Assertions.assertEquals(
                "#inst \"1985-04-12T23:20:50.520Z\"",
                Edna.pprintToString(Instant.parse("1985-04-12T23:20:50.520Z"))
        );
    }

    @Test
    void encodeNaN() {
        Assertions.assertEquals("##NaN", Edna.pprintToString(Double.NaN));
        Assertions.assertEquals("##NaN", Edna.pprintToString(-Double.NaN));
    }

    @Test
    void encodeInfinity() {
        Assertions.assertEquals("##INF", Edna.pprintToString(Double.POSITIVE_INFINITY));
        Assertions.assertEquals("##-INF", Edna.pprintToString(Double.NEGATIVE_INFINITY));
    }

    @Test
    void encodeKeyword() {
        Assertions.assertEquals(":abc", Edna.pprintToString(Keyword.get("abc")));
    }

    @Test
    void encodeSymbol() {
        Assertions.assertEquals("abc", Edna.pprintToString(Symbol.symbol("abc")));
    }

    @Test
    void encodeCustom() {
        var encoders = new LinkedHashMap<Class<?>, Function<Object, Map.Entry<String, ?>>>(Map.of(
                File.class, (it) -> new AbstractMap.SimpleEntry<>("file", ((File) it).getName())
        ));
        var options = Edna.defaultOptions().copy(b -> b.ednClassEncoders(encoders));
        Assertions.assertEquals(
                "#file \"filename.here\"",
                Edna.pprintToString(new File("filename.here"), options)
        );
    }
}
