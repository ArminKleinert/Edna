import de.kleinert.edna.Edna;
import de.kleinert.edna.EdnaOptions;
import de.kleinert.edna.data.EdnaMap;
import de.kleinert.edna.data.IObj;
import de.kleinert.edna.reader.CodePointIterator;
import de.kleinert.edna.reader.EdnaReader;

void stdExamples() {
    IO.println(Edna.read("symbol")); // Symbol without namespace
    IO.println(Edna.read("namespace/symbol")); // Symbol
    IO.println(Edna.read(":keyword")); // Keyword without namespace
    IO.println(Edna.read(":namespace/keyword")); // Keyword
    IO.println(Edna.read("\"string\"")); // String
    IO.println(Edna.read("\\c")); // Character

    IO.println(Edna.read("(list elements)")); // List
    IO.println(Edna.read("[vector elements]")); // Vector
    IO.println(Edna.read("#{set elements}")); // Set
    IO.println(Edna.read("{map-key map-value}")); // Map

    IO.println(Edna.read("12648430")); // Long
    IO.println(Edna.read("0xC0FFEE", Edna.extendedOptions())); // Long, hex prefix requires additional setting
    IO.println(Edna.read("12648430N")); // The same as BigInt
    IO.println(Edna.read("5.0")); // Double
    IO.println(Edna.read("5.0M")); // BigDecimal

    IO.println(Edna.read("nil")); // null
    IO.println(Edna.read("false")); // false
    IO.println(Edna.read("true")); // true

    IO.println(Edna.read("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"")); // UUID
    IO.println(Edna.read("#inst \"1985-04-12T23:20:50.52Z\"")); // Instant

    IO.println(Edna.read("^:a abc", Edna.extendedOptions())); // Meta
}

void main() {
    stdExamples();
    
    System.exit(0);
    
    {
        var options = EdnaOptions.defaultOptions().copy((b)->b.allowMetaData(true));
        var o = (IObj.Wrapper<?>)EdnaReader.read(new CodePointIterator(new StringReader("^a 166")), options, Object.class);
        IO.println(o.meta());
        IO.println(o.obj());
        IO.println(o.getClass());
    }
    {
        var options = EdnaOptions.defaultOptions().copy((b)->b.allowNumericSuffixes(true));
        var o = EdnaReader.read(new CodePointIterator(new StringReader("{2 3 :a 6}")), options, Object.class);
        IO.println(o);
        IO.println(o.getClass());
    }
    {
        var o = new EdnaMap<>(Map.of("r", 1, "a", 3).entrySet().stream().toList());
        IO.println(o);
        IO.println(o.getClass());
    }
}
