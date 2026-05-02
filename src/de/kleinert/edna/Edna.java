package de.kleinert.edna;

import de.kleinert.edna.pprint.EdnaWriter;
import de.kleinert.edna.pprint.EdnaWriterException;
import de.kleinert.edna.reader.CodePointIterator;
import de.kleinert.edna.reader.EdnaReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Edna {
    public static @NotNull EdnaOptions defaultOptions() {
        return EdnaOptions.defaultOptions();
    }

    public static @NotNull EdnaOptions extendedOptions() {
        return EdnaOptions.extendedOptions();
    }

    public static <T> @Nullable T read(final @NotNull String s,
                                       final @Nullable EdnaOptions options,
                                       final @NotNull Class<T> castClass) {
        final @NotNull var cpi = new CodePointIterator(s.codePoints());
        return EdnaReader.read(cpi, optsOrDefault(options), castClass);
    }

    public static @Nullable Object read(final @NotNull String s,
                                        final @Nullable EdnaOptions options) {
        return read(s, options, Object.class);
    }

    public static Object read(final @NotNull String s) {
        return read(s, null, Object.class);
    }

    /**
     * Parse EDN from a {@link File}. The file is assumed to exist and be a non-directory.
     */
    public static <T> @Nullable T read(
            final @NotNull File file,
            final @Nullable EdnaOptions options,
            final @NotNull Class<T> castClass) throws FileNotFoundException {
        T result;
        try (CodePointIterator cpi = new CodePointIterator(new FileReader(file))) {
            result = EdnaReader.read(cpi, optsOrDefault(options), castClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static @Nullable Object read(final @NotNull File file,
                                        final @Nullable EdnaOptions options)
            throws FileNotFoundException {
        return read(file, options, Object.class);
    }

    public static @Nullable Object read(final @NotNull File file)
            throws FileNotFoundException {
        return read(file, null, Object.class);
    }

    public static <T> @Nullable T read(final @NotNull InputStream inputStream,
                                       final @Nullable EdnaOptions options,
                                       final @NotNull Class<T> castClass) {
        final @NotNull var cpi = new CodePointIterator(inputStream);
        return EdnaReader.read(cpi, optsOrDefault(options), castClass);
    }

    public static @Nullable Object read(final @NotNull InputStream inputStream,
                                        final @Nullable EdnaOptions options) {
        return read(inputStream, options, Object.class);
    }

    public static @Nullable Object read(final @NotNull InputStream inputStream) {
        return read(inputStream, null, Object.class);
    }

    public static <T> @Nullable T read(final @NotNull Reader reader,
                                       final @Nullable EdnaOptions options,
                                       final @NotNull Class<T> castClass) {
        var cpi = new CodePointIterator(reader);
        return EdnaReader.read(cpi, optsOrDefault(options), castClass);
    }

    public static @Nullable Object read(final @NotNull Reader reader,
                                        final @Nullable EdnaOptions options) {
        return read(reader, options, Object.class);
    }

    public static @Nullable Object read(final @NotNull Reader reader) {
        return read(reader, null, Object.class);
    }

    public static void pprint(final @NotNull Object obj,
                              final @NotNull File file,
                              final @Nullable EdnaOptions options) {
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            EdnaWriter.pprint(obj, optsOrDefault(options), fw);
        } catch (Exception e) {
            throw new EdnaWriterException(e);
        }
    }

    public static void pprint(final @Nullable Object obj,
                              final @Nullable Appendable w,
                              final @Nullable EdnaOptions options) {
        try {
            if (w == null) {
                EdnaWriter.pprint(obj, optsOrDefault(options), new PrintWriter(System.out));
            } else {
                EdnaWriter.pprint(obj, options == null ? defaultOptions() : options, w);
            }
        } catch (Exception e) {
            throw new EdnaWriterException(e);
        }
    }

    public static void pprint(final @Nullable Object obj,
                              final @Nullable Appendable w) {
        pprint(obj, w, defaultOptions());
    }

    public static void pprintln(final @Nullable Object obj,
                                final @Nullable Appendable w,
                                final @Nullable EdnaOptions options) {
        try {
            EdnaWriter.pprintln(
                    obj,
                    optsOrDefault(options),
                    Objects.requireNonNullElseGet(w, () -> new PrintWriter(System.out)));
        } catch (Exception e) {
            throw new EdnaWriterException(e);
        }
    }

    public static void pprintln(final @Nullable Object obj,
                                final @Nullable Appendable w) {
        pprintln(obj, w, defaultOptions());
    }

    public static @NotNull String pprintToString(
            final @Nullable Object obj,
            final @Nullable EdnaOptions options) {
        try {
            final @NotNull StringBuilder sb = new StringBuilder();
            EdnaWriter.pprint(obj, optsOrDefault(options), sb);
            return sb.toString();
        } catch (Exception e) {
            throw new EdnaWriterException(e);
        }
    }

    public static @NotNull String pprintToString(final @Nullable Object obj) {
        return pprintToString(obj, defaultOptions());
    }

    private static @NotNull EdnaOptions optsOrDefault(
            final @Nullable EdnaOptions options) {
        return options == null ? defaultOptions() : options;
    }
}
