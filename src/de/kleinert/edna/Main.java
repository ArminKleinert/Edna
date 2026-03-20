import de.kleinert.edna.EdnOptions;
import de.kleinert.edna.reader.CodePointIterator;
import de.kleinert.edna.reader.EdnaReader;

void main() {
    IO.println(EdnaReader.read(new CodePointIterator(new StringReader("55")), new EdnOptions(), Object.class));
}
