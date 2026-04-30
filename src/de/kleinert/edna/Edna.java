package de.kleinert.edna;

import de.kleinert.edna.pprint.EdnaWriter;
import de.kleinert.edna.pprint.EdnaWriterException;
import de.kleinert.edna.reader.CodePointIterator;
import de.kleinert.edna.reader.EdnaReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;

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
        var cpi = new CodePointIterator(s.codePoints());
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
     * Parse EDN from a [File]. The file is assumed to exist and be a non-directory.
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

    public static @Nullable Object read(final @NotNull File reader,
                                        final @Nullable EdnaOptions options)
            throws FileNotFoundException {
        return read(reader, options, Object.class);
    }

    public static @Nullable Object read(final @NotNull File reader)
            throws FileNotFoundException {
        return read(reader, null, Object.class);
    }

    public static <T> @Nullable T read(final @NotNull InputStream reader,
                                       final @Nullable EdnaOptions options,
                                       final @NotNull Class<T> castClass) {
        var cpi = new CodePointIterator(reader);
        return EdnaReader.read(cpi, optsOrDefault(options), castClass);
    }

    public static @Nullable Object read(final @NotNull InputStream reader,
                                        final @Nullable EdnaOptions options) {
        return read(reader, options, Object.class);
    }

    public static @Nullable Object read(final @NotNull InputStream reader) {
        return read(reader, null, Object.class);
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

    public static void pprint(Object obj, File file, EdnaOptions options) {
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            EdnaWriter.pprint(obj, options, fw);
        } catch (Exception e) {
            throw new EdnaWriterException(e);
        }
    }

    public static void pprint(Object obj, Appendable w, EdnaOptions options) {
        try {
            if (w == null) {
                try (Writer writer = new PrintWriter(System.out)) {
                    EdnaWriter.pprint(obj, options, writer);
                }
            } else {
                EdnaWriter.pprint(obj, options, w);
            }
        } catch (Exception e) {
            throw new EdnaWriterException(e);
        }
    }

    public static void pprintln(Object obj, Appendable w, EdnaOptions options) {
        try {
            if (w == null) {
                try (Writer writer = new PrintWriter(System.out)) {
                    EdnaWriter.pprintln(obj, options, writer);
                }
            } else {
                EdnaWriter.pprintln(obj, options, w);
            }
        } catch (Exception e) {
            throw new EdnaWriterException(e);
        }
    }

    public static void pprintToString(Object obj, EdnaOptions options) {
        try {
            EdnaWriter.pprint(obj, options, new StringBuilder());
        } catch (Exception e) {
            throw new EdnaWriterException(e);
        }
    }


    private static @NotNull EdnaOptions optsOrDefault(
            final @Nullable EdnaOptions options) {
        return options == null ? EdnaOptions.defaultOptions() : options;
    }
}
