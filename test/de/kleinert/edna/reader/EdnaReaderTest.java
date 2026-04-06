package de.kleinert.edna.reader;

import de.kleinert.edna.EdnOptions;
import de.kleinert.edna.data.Char32;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

class EdnaReaderTest {
    @Test
    void read() {
        Assertions.assertEquals('a', EdnaReader.read("\\a"));
        Assertions.assertEquals(Char32.valueOf('a'), EdnaReader.read("#\\u0061", new EdnOptions().allowDispatchChars(true)));
        Assertions.assertEquals('a', EdnaReader.read("\\u0061"));
    }
}