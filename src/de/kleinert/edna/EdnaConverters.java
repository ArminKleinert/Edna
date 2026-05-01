package de.kleinert.edna;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EdnaConverters {
    private EdnaConverters() {
    }

    @SafeVarargs
    public static <K, V> Map<K, V> merge(Map<K, V>... maps) {
        Map<K, V> res = new LinkedHashMap<>();
        for (Map<K, V> map : maps) {
            res.putAll(map);
        }
        return res;
    }

    public static Map<String, Function<Object, Object>> arrayConverters() {
        Map<String, Function<Object, Object>> res = new HashMap<>();
        res.put("edna/bytearray", EdnaConverters::bytearray);
        res.put("edna/shortarray", EdnaConverters::shortarray);
        res.put("edna/intarray", EdnaConverters::intarray);
        res.put("edna/longarray", EdnaConverters::longarray);
        res.put("edna/floatarray", EdnaConverters::floatarray);
        res.put("edna/doublearray", EdnaConverters::doublearray);
        res.put("edna/stringarray", EdnaConverters::stringarray);
        res.put("edna/array", EdnaConverters::array);
        res.put("edna/array2d", EdnaConverters::array2d);
        res.put("edna/bigintarray", EdnaConverters::bigintarray);
        res.put("edna/bigdecimalarray", EdnaConverters::bigdecimalarray);
        return res;
    }

    public static Map<String, Function<Object, Object>> numberConverters() {
        Map<String, Function<Object, Object>> res = new HashMap<>();
        res.put("edna/byte", EdnaConverters::numberToByte);
        res.put("edna/short", EdnaConverters::numberToShort);
        res.put("edna/int", EdnaConverters::numberToInt);
        res.put("edna/long", EdnaConverters::numberToLong);
        res.put("edna/float", EdnaConverters::numberToFloat);
        res.put("edna/double", EdnaConverters::numberToDouble);
        res.put("edna/bigint", EdnaConverters::numberToBigInt);
        res.put("edna/bigdec", EdnaConverters::numberToBigDec);
        return res;
    }

    public static Object[] array2d(Object o) {
        if (!(o instanceof List<?> obj))
            throw new IllegalArgumentException();
        return obj.stream().map(e -> {
            if (e instanceof List<?>) return ((List<?>) e).toArray();
            else throw new IllegalArgumentException("Expected list, got " + e);
        }).toArray();
    }

    public static Object[] array(Object o) {
        if (!(o instanceof List<?> obj))
            throw new IllegalArgumentException();
        return obj.toArray();
    }

    public static byte[] bytearray(Object o) {
        if (!(o instanceof List<?> obj))
            throw new IllegalArgumentException();
        byte[] res = new byte[obj.size()];
        for (byte i = 0; i < obj.size(); i++) {
            var e = obj.get(i);
            if (!(e instanceof Number))
                throw new IllegalArgumentException();
            res[i] = ((Number) e).byteValue();
        }
        return res;
    }

    public static short[] shortarray(Object o) {
        if (!(o instanceof List<?> obj))
            throw new IllegalArgumentException();
        short[] res = new short[obj.size()];
        for (short i = 0; i < obj.size(); i++) {
            var e = obj.get(i);
            if (!(e instanceof Number))
                throw new IllegalArgumentException();
            res[i] = ((Number) e).shortValue();
        }
        return res;
    }

    public static int[] intarray(Object o) {
        if (!(o instanceof List<?> obj))
            throw new IllegalArgumentException();
        int[] res = new int[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            var e = obj.get(i);
            if (!(e instanceof Number))
                throw new IllegalArgumentException();
            res[i] = ((Number) e).intValue();
        }
        return res;
    }

    public static long[] longarray(Object o) {
        if (!(o instanceof List<?> obj))
            throw new IllegalArgumentException();
        long[] res = new long[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            var e = obj.get(i);
            if (!(e instanceof Number))
                throw new IllegalArgumentException();
            res[i] = ((Number) e).longValue();
        }
        return res;
    }

    public static float[] floatarray(Object o) {
        if (!(o instanceof List<?> obj))
            throw new IllegalArgumentException();
        float[] res = new float[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            var e = obj.get(i);
            if (!(e instanceof Number))
                throw new IllegalArgumentException();
            res[i] = ((Number) e).floatValue();
        }
        return res;
    }

    public static double[] doublearray(Object o) {
        if (!(o instanceof List<?> obj))
            throw new IllegalArgumentException();
        double[] res = new double[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            var e = obj.get(i);
            if (!(e instanceof Number))
                throw new IllegalArgumentException();
            res[i] = ((Number) e).doubleValue();
        }
        return res;
    }

    public static String[] stringarray(Object o) {
        if (!(o instanceof List<?> obj))
            throw new IllegalArgumentException();
        String[] res = new String[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            var e = obj.get(i);
            if (!(e instanceof String))
                throw new IllegalArgumentException();
            res[i] = (String) e;
        }
        return res;
    }

    public static BigInteger[] bigintarray(Object o) {
        if (!(o instanceof List<?> obj))
            throw new IllegalArgumentException();
        BigInteger[] res = new BigInteger[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            var e = obj.get(i);
            switch (e) {
                case BigInteger bigInteger -> res[i] = bigInteger;
                case BigDecimal bigDecimal -> res[i] = bigDecimal.toBigInteger();
                case Number number -> res[i] = BigInteger.valueOf(number.longValue());
                case null, default -> throw new IllegalArgumentException();
            }
        }
        return res;
    }

    public static BigDecimal[] bigdecimalarray(Object o) {
        if (!(o instanceof List<?> obj))
            throw new IllegalArgumentException();
        BigDecimal[] res = new BigDecimal[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            var e = obj.get(i);
            switch (e) {
                case BigInteger bigInteger -> res[i] = new BigDecimal(bigInteger);
                case BigDecimal bigDecimal -> res[i] = bigDecimal;
                case Number number -> res[i] = BigDecimal.valueOf(number.doubleValue());
                case null, default -> throw new IllegalArgumentException();
            }
        }
        return res;
    }

    public static byte numberToByte(Object o) {
        return numberTo(o, Number::byteValue).byteValue();
    }

    public static short numberToShort(Object o) {
        return numberTo(o, Number::shortValue).shortValue();
    }

    public static int numberToInt(Object o) {
        return numberTo(o, Number::intValue).intValue();
    }

    // this method is only here for completeness.
    public static long numberToLong(Object o) {
        return numberTo(o, Number::longValue).longValue();
    }

    public static float numberToFloat(Object o) {
        return numberTo(o, Number::floatValue).floatValue();
    }

    public static double numberToDouble(Object o) {
        return numberTo(o, Number::doubleValue).doubleValue();
    }

    public static BigInteger numberToBigInt(Object obj) {
        return obj instanceof BigInteger ? (BigInteger) obj : BigInteger.valueOf(numberToLong(obj));
    }

    public static BigDecimal numberToBigDec(Object obj) {
        return (BigDecimal) numberTo(obj, (o) -> switch (o) {
            case Float n -> BigDecimal.valueOf(n);
            case Double n->BigDecimal.valueOf(n);
            case Byte n->BigDecimal.valueOf(n);
            case Short n->BigDecimal.valueOf(n);
            case Integer n->BigDecimal.valueOf(n);
            case Long n-> BigDecimal.valueOf(n);
            case BigInteger n->new BigDecimal(n);
            case BigDecimal n->n;
            default -> throw new IllegalArgumentException();
        });
    }

    private static Number numberTo(Object obj, Function<Number, Number> conv) {
        return switch (obj) {
            case Float n -> conv.apply(n);
            case Double n->conv.apply(n);
            case Byte n->conv.apply(n);
            case Short n->conv.apply(n);
            case Integer n->conv.apply(n);
            case Long n->conv.apply(n);
            case BigInteger n->conv.apply(n);
            case BigDecimal n->conv.apply(n);
            default -> throw new IllegalArgumentException();
        };
    }
}
