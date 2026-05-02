package de.kleinert.edna;

import de.kleinert.edna.data.EdnaMap;
import de.kleinert.edna.data.IObj;
import de.kleinert.edna.reader.CodePointIterator;
import de.kleinert.edna.reader.EdnaParser;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.LongStream;

public class Main {
    private static void stdExamples() {
        System.out.println(Edna.read("symbol")); // Symbol without namespace
        System.out.println(Edna.read("namespace/symbol")); // Symbol
        System.out.println(Edna.read(":keyword")); // Keyword without namespace
        System.out.println(Edna.read(":namespace/keyword")); // Keyword
        System.out.println(Edna.read("\"string\"")); // String
        System.out.println(Edna.read("\\c")); // Character

        System.out.println(Edna.read("(list elements)")); // List
        System.out.println(Edna.read("[vector elements]")); // Vector
        System.out.println(Edna.read("#{set elements}")); // Set
        System.out.println(Edna.read("{map-key map-value}")); // Map

        System.out.println(Edna.read("12648430")); // Long
        System.out.println(Edna.read("12648430N")); // The same as BigInt
        System.out.println(Edna.read("5.0")); // Double
        System.out.println(Edna.read("5.0M")); // BigDecimal

        System.out.println(Edna.read("nil")); // null
        System.out.println(Edna.read("false")); // false
        System.out.println(Edna.read("true")); // true

        System.out.println(Edna.read("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"")); // UUID
        System.out.println(Edna.read("#inst \"1985-04-12T23:20:50.52Z\"")); // Instant

        var decoders = Map.<String, Function<Object, Object>>of(
                "list/repeat", (e) -> LongStream
                        .range((Long) ((List) e).get(0), (Long) ((List) e).get(1))
                        .boxed().toList());
        var opts = Edna.defaultOptions().copy(b -> b.taggedElementDecoders(decoders));
        System.out.println(Edna.read("#list/repeat [2 7]", opts));

        System.out.println(Edna.read("0xC0FFEE", Edna.extendedOptions())); // Long, hex prefix requires additional setting
        System.out.println(Edna.read("^:a abc", Edna.extendedOptions())); // Meta

        Edna.pprintln(BigInteger.valueOf(11), null);
        Edna.pprintln(BigInteger.valueOf(11), null);
    }
    public static void main(String[] args) {
        {
            var text = "1 2 3";
            var cpi = new CodePointIterator(text.codePoints());
            var pars = EdnaParser.read1(cpi, Edna.defaultOptions(), Object.class);
            System.out.println(pars);
            pars = EdnaParser.read1(cpi, Edna.defaultOptions(), Object.class);
            System.out.println(pars);
            pars = EdnaParser.read1(cpi, Edna.defaultOptions(), Object.class);
            System.out.println(pars);
            pars = EdnaParser.read1(cpi, Edna.defaultOptions(), Object.class);
            System.out.println(pars);
        }

        System.exit(0);

        {
            var temp = Edna.read("^abc", Edna.defaultOptions().copy(b -> b.allowMetaData(true)));
            System.out.println(temp);
        }

        System.exit(0);

        {
            var iobj = ((IObj) Edna.read("^:a :b", Edna.defaultOptions().copy(b -> b.allowMetaData(true))));
            System.out.println(iobj.meta() + " " + iobj.obj());

            var r = EdnaParser.readMulti(new CodePointIterator("".codePoints()), EdnaOptions.defaultOptions());
            System.out.println(r);
        }

        System.exit(0);

        stdExamples();

        {
            var options = Edna.defaultOptions().copy((b) -> b.allowMetaData(true));
            var o = (IObj.Wrapper<?>) EdnaParser.read(new CodePointIterator(new StringReader("^a 166")), options, Object.class);
            System.out.println(o.meta());
            System.out.println(o.obj());
            System.out.println(o.getClass());
        }
        {
            var options = Edna.defaultOptions();
            var o = EdnaParser.read(new CodePointIterator(new StringReader("{2 3 :a 6}")), options, Object.class);
            System.out.println(o);
            System.out.println(o.getClass());
        }
        {
            var o = new EdnaMap<>(Map.of("r", 1, "a", 3).entrySet().stream().toList());
            System.out.println(o);
            System.out.println(o.getClass());
        }
    }
}
