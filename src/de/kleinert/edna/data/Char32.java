package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;

public record Char32(int code) implements Comparable<Char32> {
    public static final @NotNull Char32 MIN_VALUE = new Char32(0);
    public static final @NotNull Char32 MAX_VALUE = new Char32(0x10ffff);

    public Char32 {
        if (!(code >= 0 && code <= 0x10ffff)) {
            throw new IllegalArgumentException(
                    "Char code must be between 0 and 0x10ffff (inclusive)");
        }
    }

    public static @NotNull Char32 valueOf(final int code) {
        return new Char32(code);
    }

    public static @NotNull Char32 valueOf(final char chr) {
        return new Char32(chr);
    }

    public static @NotNull Char32 valueOf(final @NotNull String s) {
        var codePointIterator = s.codePoints().iterator();
        if (!codePointIterator.hasNext())
            throw new IllegalArgumentException(
                    "Can not get tag char code from empty string.");
        codePointIterator.nextInt();
        if (codePointIterator.hasNext())
            throw new IllegalArgumentException(
                    "Can not get Char32 from string which has more than one code point.");
        return new Char32(s.codePointAt(0));
    }

    public @NotNull Char32 dec() {
        return new Char32(code - 1);
    }

    public @NotNull Char32 inc() {
        return new Char32(code + 1);
    }

    public @NotNull Char32 minus(final @NotNull Char32 other) {
        return new Char32(code - other.code);
    }

    public @NotNull Char32 minus(final char other) {
        return new Char32(code - other);
    }

    public @NotNull Char32 minus(final int other) {
        return new Char32(code - other);
    }

    public @NotNull Char32 plus(final int other) {
        return new Char32(code + other);
    }

    public @NotNull Char32 plus(final char other) {
        return new Char32(code + other);
    }

    public @NotNull Char32 plus(final @NotNull Char32 other) {
        return new Char32(code + other.code);
    }

    public char toChar() {
        if (code > Character.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Cannot convert Char32 to Char because the char code $code is too big.");
        }
        return (char) code;
    }

    public byte toByte() {
        if (code > Byte.MAX_VALUE)
            throw new IllegalArgumentException(
                    "Cannot convert Char32 to Byte because the char code $code is too big.");
        return (byte) code;
    }

    public short toShort() {
        if (code > Short.MAX_VALUE)
            throw new IllegalArgumentException(
                    "Cannot convert Char32 to Short because the char code $code is too big.");
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
