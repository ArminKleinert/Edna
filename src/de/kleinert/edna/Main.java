import de.kleinert.edna.EdnaOptions;
import de.kleinert.edna.data.EdnaCollections;
import de.kleinert.edna.reader.CodePointIterator;
import de.kleinert.edna.reader.EdnaReader;

void main() {
    {
        var options = EdnaOptions.defaultOptions().copy((b)->b.allowMetaData(true));
        var o = (EdnaCollections.IObj.Wrapper<?>)EdnaReader.read(new CodePointIterator(new StringReader("^a 166")), options, Object.class);
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
        var o = new EdnaCollections.EdnaMap<>(Map.of("r", 1, "a", 3).entrySet().stream().toList());
        IO.println(o);
        IO.println(o.getClass());
    }
}
