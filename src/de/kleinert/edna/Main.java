import de.kleinert.edna.EdnOptions;
import de.kleinert.edna.reader.CodePointIterator;
import de.kleinert.edna.reader.EdnaReader;

void main() {

    var o = EdnaReader.read(new CodePointIterator(new StringReader("+ggg")), new EdnOptions().allowNumericSuffixes(true), Object.class);
    IO.println(o);
    IO.println(o.getClass());
}
