import de.kleinert.edna.EdnOptions;
import de.kleinert.edna.data.EdnCollections;
import de.kleinert.edna.reader.CodePointIterator;
import de.kleinert.edna.reader.EdnaReader;

void main() {
    {
        var o = (EdnCollections.IObj.Wrapper<?>)EdnaReader.read(new CodePointIterator(new StringReader("^a 166")), new EdnOptions().allowMetaData(true), Object.class);
        IO.println(o.meta());
        IO.println(o.element());
        IO.println(o.getClass());
    }
    {
        var o = EdnaReader.read(new CodePointIterator(new StringReader("{2 3 :a 6}")), new EdnOptions().allowNumericSuffixes(true), Object.class);
        IO.println(o);
        IO.println(o.getClass());
    }
    {
        var o = new EdnCollections.EdnMap<>(Map.of("r", 1, "a", 3).entrySet().stream().toList());
        IO.println(o);
        IO.println(o.getClass());
    }
}
