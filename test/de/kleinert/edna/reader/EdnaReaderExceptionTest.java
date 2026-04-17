package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EdnaReaderExceptionTest {
    @Test
    public void positionStartTest() {
        try {
            Edna.read("[");
        } catch (EdnaReaderException ex ) {
            Assertions.assertEquals(1, ex.getTextIndex());
            Assertions.assertEquals(0, ex.getLineIndex());
        }
        try {
            Edna.read("]");
        } catch (EdnaReaderException ex) {
            Assertions.assertEquals(0, ex.getTextIndex());
            Assertions.assertEquals(0, ex.getLineIndex());
        }
    }

    @Test
    public void positionTest() {
        try {
            Edna.read("   [");
        } catch (EdnaReaderException ex) {
            Assertions.assertEquals(4, ex.getTextIndex());
            Assertions.assertEquals(0, ex.getLineIndex());
        }
        try {
            Edna.read("\n\n   ]");
        } catch (EdnaReaderException ex) {
            Assertions.assertEquals(5, ex.getTextIndex());
            Assertions.assertEquals(2, ex.getLineIndex());
        }
    }
}