package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EdnaReaderConstantsTest {
    @Test
    public void parseDirectConstantsTest() {
        Assertions.assertEquals(false, Edna.read("false"));
        Assertions.assertEquals(true, Edna.read("true"));
        Assertions.assertNull(Edna.read("nil"));
    }
}