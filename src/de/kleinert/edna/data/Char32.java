package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;

/**
 * A Char32 is a representation of a UTF-32 codepoint.
 * @param code The code as an int.
 */
public record Char32(int code) implements Comparable<Char32> {
    /**
     * The minimum unicode codepoint (zero).
     */
    public static final @NotNull Char32 MIN_VALUE = new Char32(0);
    /**
     * The maximum unicode codepoint 0x0010FFFF.
     */
    public static final @NotNull Char32 MAX_VALUE = new Char32(0x10ffff);

    public Char32 {
        if (!(code >= 0 && code <= 0x10ffff)) {
            throw new IllegalArgumentException(
                    "Char code must be between 0 and +" + Integer.toHexString(MAX_VALUE.code()) + " (inclusive)");
        }
    }

    /**
     * Creates a new instance based on the code. The value must be between 0 and {@link #MAX_VALUE} (inclusive). If the value is exactly 0 or the exact maximum, it may be memoized.
     * @param code The codepoint.
     * @return A new instance.
     */
    public static @NotNull Char32 valueOf(final int code) {
        if (code == MIN_VALUE.code()) return MIN_VALUE;
        if (code == MAX_VALUE.code()) return MAX_VALUE;
        return new Char32(code);
    }

    /**
     * Creates a new instance based on the code.
     * @param chr The codepoint.
     * @return A new instance.
     */
    public static @NotNull Char32 valueOf(final char chr) {
        if (chr == MIN_VALUE.code()) return MIN_VALUE;
        return new Char32(chr);
    }

    /**
     * Creates a new instance from a string. The string must have exactly one codepoint.
     * @param s The string (expected to have exactly one codepoint).
     * @return A new instance.
     */
    public static @NotNull Char32 valueOf(final @NotNull String s) {
        var codePointIterator = s.codePoints().iterator();
        if (!codePointIterator.hasNext())
            throw new IllegalArgumentException(
                    "Can not get tag char code from empty string.");
        var code = codePointIterator.nextInt();
        if (codePointIterator.hasNext())
            throw new IllegalArgumentException(
                    "Can not get Char32 from string which has more than one code point.");
        return Char32.valueOf(code);
    }

    public @NotNull Char32 dec() {
        return Char32.valueOf(code - 1);
    }

    public @NotNull Char32 inc() {
        return Char32.valueOf(code + 1);
    }

    public @NotNull Char32 minus(final @NotNull Char32 other) {
        return Char32.valueOf(code - other.code);
    }

    public @NotNull Char32 minus(final char other) {
        return Char32.valueOf(code - other);
    }

    public @NotNull Char32 minus(final int other) {
        return Char32.valueOf(code - other);
    }

    public @NotNull Char32 plus(final int other) {
        return Char32.valueOf(code + other);
    }

    public @NotNull Char32 plus(final char other) {
        return Char32.valueOf(code + other);
    }

    public @NotNull Char32 plus(final @NotNull Char32 other) {
        return Char32.valueOf(code + other.code);
    }

    public char toChar() {
        if (code > Character.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Cannot convert Char32 to Char because the char code " + code + " (hex " + Integer.toHexString(code) + ") is too big.");
        }
        return (char) code;
    }

    public byte toByte() {
        return (byte) code;
    }

    public short toShort() {
        return (short) code;
    }

    public int toInt() {
        return code;
    }

    public long toLong() {
        return code;
    }

    public float toFloat() {
        return code;
    }

    public double toDouble() {
        return code;
    }

    @Override
    public @NotNull String toString() {
        return new String(new int[]{code}, 0, 1);
    }

    @Override
    public int compareTo(final @NotNull Char32 other) {
        return Integer.compare(code, other.code);
    }
}
