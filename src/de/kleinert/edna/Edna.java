package de.kleinert.edna;

import de.kleinert.edna.reader.CodePointIterator;
import de.kleinert.edna.reader.EdnaReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class Edna {
    public static <T> @Nullable T read(final @NotNull String s,
                                       final @Nullable EdnOptions options,
                                       final @NotNull Class<T> castClass) {
        var cpi = new CodePointIterator(s.codePoints());
        return EdnaReader.read(cpi, options == null ? EdnOptions.defaultOptions() : options, castClass);
    }

    public static @Nullable Object read(final @NotNull String s,
                                        final @Nullable EdnOptions options) {
        return read(s, options, Object.class);
    }

    public static Object read(final @NotNull String s) {
        return read(s, null, Object.class);
    }

    /**
     * Parse EDN from a [File]. The file is assumed to exist and be a non-directory.
     */
    public static <T> @Nullable T read(final @NotNull File file,
                                       final @Nullable EdnOptions options,
                                       final @NotNull Class<T> castClass) throws FileNotFoundException {
        var cpi = new CodePointIterator(new FileReader(file));
        return EdnaReader.read(cpi, options == null ? EdnOptions.defaultOptions() : options, castClass);
    }

    public static @Nullable Object read(final @NotNull File reader,
                                        final @Nullable EdnOptions options) throws FileNotFoundException {
        return read(reader, options, Object.class);
    }

    public static @Nullable Object read(final @NotNull File reader) throws FileNotFoundException {
        return read(reader, null, Object.class);
    }


    public static <T> @Nullable T read(final @NotNull InputStream reader,
                                       final @Nullable EdnOptions options,
                                       final @NotNull Class<T> castClass) {
        var cpi = new CodePointIterator(reader);
        return EdnaReader.read(cpi, options == null ? EdnOptions.defaultOptions() : options, castClass);
    }

    public static @Nullable Object read(final @NotNull InputStream reader,
                                        final @Nullable EdnOptions options) {
        return read(reader, options, Object.class);
    }

    public static @Nullable Object read(final @NotNull InputStream reader) {
        return read(reader, null, Object.class);
    }

    public static <T> @Nullable T read(final @NotNull Reader reader,
                                       final @Nullable EdnOptions options,
                                       final @NotNull Class<T> castClass) {
        var cpi = new CodePointIterator(reader);
        return EdnaReader.read(cpi, options == null ? EdnOptions.defaultOptions() : options, castClass);
    }

    public static @Nullable Object read(final @NotNull Reader reader,
                                        final @Nullable EdnOptions options) {
        return read(reader, options, Object.class);
    }

    public static @Nullable Object read(final @NotNull Reader reader) {
        return read(reader, null, Object.class);
    }
}
