package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class EdnaReaderNumberFloatyTest {
    @Test
   public void parseDouble() {
        {
            var it = Edna.read("0.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("+0.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("-0.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.0, it);
        }

        {
            var it = Edna.read("0.5");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.5, it);
        }
        {
            var it = Edna.read("+0.5");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.5, it);
        }
        {
            var it = Edna.read("-0.5");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.5, it);
        }

        {
            var it = Edna.read("1.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(1.0, it);
        }
        {
            var it = Edna.read("+1.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(1.0, it);
        }
        {
            var it = Edna.read("-1.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-1.0, it);
        }

        {
            var it = Edna.read("128.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(128.0, it);
        }
        {
            var it = Edna.read("+128.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(128.0, it);
        }
        {
            var it = Edna.read("-128.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-128.0, it);
        }

        {
            var it = Edna.read("255.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(255.0, it);
        }
        {
            var it = Edna.read("+255.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(255.0, it);
        }
        {
            var it = Edna.read("-255.0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-255.0, it);
        }

        {
            var it = Edna.read("255.25");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(255.25, it);
        }
        {
            var it = Edna.read("+255.25");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(255.25, it);
        }
        {
            var it = Edna.read("-255.25");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-255.25, it);
        }
    }

    @Test
   public void parseDoubleENotation() {
        {
            var it = Edna.read("0e+0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0e-0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0e+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0e-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("-0e+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.0, it);
        }
        {
            var it = Edna.read("-0e-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.0, it);
        }

        {
            var it = Edna.read("5e+0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(5.0, it);
        }
        {
            var it = Edna.read("5e-0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(5.0, it);
        }
        {
            var it = Edna.read("5e+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(50.0, it);
        }
        {
            var it = Edna.read("+5e+0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(5.0, it);
        }
        {
            var it = Edna.read("+5e-0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(5.0, it);
        }
        {
            var it = Edna.read("+5e+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(50.0, it);
        }
        {
            var it = Edna.read("5e-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.5, it);
        }
        {
            var it = Edna.read("-5e+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-50.0, it);
        }
        {
            var it = Edna.read("-5e-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.5, it);
        }
    }

    @Test
   public void parseDoubleENotationBig() {
        {
            var it = Edna.read("0E+0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0E-0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0E+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0E-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("-0E+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.0, it);
        }
        {
            var it = Edna.read("-0E-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.0, it);
        }

        {
            var it = Edna.read("5E+0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(5.0, it);
        }
        {
            var it = Edna.read("5E-0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(5.0, it);
        }
        {
            var it = Edna.read("5E+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(50.0, it);
        }
        {
            var it = Edna.read("5E-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.5, it);
        }
        {
            var it = Edna.read("-5E+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-50.0, it);
        }
        {
            var it = Edna.read("-5E-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.5, it);
        }
    }

    @Test
   public void parseDoubleENotation2() {
        {
            var it = Edna.read("0.0e+0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0.0e-0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0.0e+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0.0e-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("-0.0e+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.0, it);
        }
        {
            var it = Edna.read("-0.0e-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.0, it);
        }

        {
            var it = Edna.read("0.5e+0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.5, it);
        }
        {
            var it = Edna.read("0.5e-0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.5, it);
        }
        {
            var it = Edna.read("0.5e+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(5.0, it);
        }
        {
            var it = Edna.read("0.5e-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.05, it);
        }
        {
            var it = Edna.read("-0.5e+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-5.0, it);
        }
        {
            var it = Edna.read("-0.5e-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.05, it);
        }
    }

    @Test
   public void parseDoubleENotationBig2() {
        {
            var it = Edna.read("0.0E+0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0.0E-0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0.0E+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("0.0E-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.0, it);
        }
        {
            var it = Edna.read("-0.0E+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.0, it);
        }
        {
            var it = Edna.read("-0.0E-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.0, it);
        }

        {
            var it = Edna.read("0.5E+0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.5, it);
        }
        {
            var it = Edna.read("0.5E-0");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.5, it);
        }
        {
            var it = Edna.read("0.5E+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(5.0, it);
        }
        {
            var it = Edna.read("0.5E-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(0.05, it);
        }
        {
            var it = Edna.read("-0.5E+1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-5.0, it);
        }
        {
            var it = Edna.read("-0.5E-1");
            Assertions.assertInstanceOf(Double.class, it);
            Assertions.assertEquals(-0.05, it);
        }
    }

    @Test
   public void parseBigDecimal() {
        var temp = BigDecimal.valueOf(0.0);
        {
            var it = Edna.read("0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(0), it);
        }
        {
            var it = Edna.read("0.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("+0.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("-0.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp.negate(), it);
        }

        temp = BigDecimal.valueOf(0.5);
        {
            var it = Edna.read("0.5M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("+0.5M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("-0.5M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp.negate(), it);
        }

        temp = BigDecimal.valueOf(1.0);
        {
            var it = Edna.read("1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(1L), it);
        }
        {
            var it = Edna.read("-1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(1L).negate(), it);
        }
        {
            var it = Edna.read("1.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("+1.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("-1.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp.negate(), it);
        }

        temp = BigDecimal.valueOf(128.0);
        {
            var it = Edna.read("128M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(128L), it);
        }
        {
            var it = Edna.read("-128M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(128L).negate(), it);
        }
        {
            var it = Edna.read("128.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("+128.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("-128.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp.negate(), it);
        }

        temp = BigDecimal.valueOf(255.0);
        {
            var it = Edna.read("255M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(255), it);
        }
        {
            var it = Edna.read("-255M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(255).negate(), it);
        }
        {
            var it = Edna.read("255.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("+255.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("-255.0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp.negate(), it);
        }

        temp = BigDecimal.valueOf(255.25);
        {
            var it = Edna.read("255.25M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("+255.25M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp, it);
        }
        {
            var it = Edna.read("-255.25M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(temp.negate(), it);
        }
    }

    @Test
   public void parseBigDecimalENotation() {
        {
            var it = Edna.read("0.0e+0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(Double.parseDouble("0.0e+0")), it);
        }
        {
            var it = Edna.read("0.0e-0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(Double.parseDouble("0.0e-0")), it);
        }
        {
            var it = Edna.read("0.0e+1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("0.0e+1")), it);
        }
        {
            var it = Edna.read("0.0e-1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("0.0e-1")), it);
        }
        {
            var it = Edna.read("-0.0e+1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("-0.0e+1")), it);
        }
        {
            var it = Edna.read("-0.0e-1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("-0.0e-1")), it);
        }

        {
            var it = Edna.read("0.5e+0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("0.5e+0")), it);
        }
        {
            var it = Edna.read("0.5e-0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(Double.parseDouble("0.5e-0")), it);
        }
        {
            var it = Edna.read("0.5e+1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("0.5e+1")), it);
        }
        {
            var it = Edna.read("0.5e-1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("0.5e-1")), it);
        }
        {
            var it = Edna.read("-0.5e+1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("-0.5e+1")), it);
        }
        {
            var it = Edna.read("-0.5e-1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("-0.5e-1")), it);
        }
    }

    @Test
   public void parseBigDecimalENotationBig2() {
        {
            var it = Edna.read("0.0E+0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("0.0e+0")), it);
        }
        {
            var it = Edna.read("0.0E-0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("0.0e-0")), it);
        }
        {
            var it = Edna.read("0.0E+1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("0.0e+1")), it);
        }
        {
            var it = Edna.read("0.0E-1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("0.0e-1")), it);
        }
        {
            var it = Edna.read("-0.0E+1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("-0.0e+1")), it);
        }
        {
            var it = Edna.read("-0.0E-1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("-0.0e-1")), it);
        }

        {
            var it = Edna.read("0.5E+0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(Double.parseDouble("0.5e+0")), it);
        }
        {
            var it = Edna.read("0.5E-0M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(BigDecimal.valueOf(Double.parseDouble("0.5e-0")), it);
        }
        {
            var it = Edna.read("0.5E+1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("0.5e+1")), it);
        }
        {
            var it = Edna.read("0.5E-1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("0.5e-1")), it);
        }
        {
            var it = Edna.read("-0.5E+1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("-0.5e+1")), it);
        }
        {
            var it = Edna.read("-0.5E-1M");
            Assertions.assertInstanceOf(BigDecimal.class, it);
            Assertions.assertEquals(new BigDecimal(("-0.5e-1")), it);
        }
    }
}

