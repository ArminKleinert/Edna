package de.kleinert.edna.options;

import de.kleinert.edna.Edna;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EdnaOptionsTest {
    @Test
    public void testBasicBuilder() {
        var a = Edna.defaultOptions();
        var aAttr = a.allowSchemeUTF32Codes();

        var b = a.copy((builder -> builder.allowSchemeUTF32Codes(!aAttr)));
        var bAttr = b.allowSchemeUTF32Codes();

        Assertions.assertEquals(aAttr, !bAttr);
    }
}
