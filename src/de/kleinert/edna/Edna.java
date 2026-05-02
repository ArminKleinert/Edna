package de.kleinert.edna;

import de.kleinert.edna.pprint.EdnaWriter;
import de.kleinert.edna.pprint.EdnaWriterException;
import de.kleinert.edna.reader.CodePointIterator;
import de.kleinert.edna.reader.EdnaReader;
import de.kleinert.edna.reader.EdnaReaderException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class Edna {
    public static @NotNull EdnaOptions defaultOptions() {
        return EdnaOptions.defaultOptions();
    }

    public static @NotNull EdnaOptions extendedOptions() {
        return EdnaOptions.extendedOptions();
    }

    private static <T> @Nullable T readSingle(final @NotNull CodePointIterator cpi,
                                              @Nullable EdnaOptions options,
                                              final @NotNull Class<T> castClass) {
        options = optsOrDefault(options);
        final Iterator<Object> iterator = EdnaReader.reader(cpi, options);
        if (!iterator.hasNext()) throw new EdnaReaderException(0, 0, "Expected exactly one element, but found none.");
        var output = iterator.next();

        final T castedOutput = (output == null) ? null : castClass.cast(output);

        if (iterator.hasNext()) throw new EdnaReaderException(0, 0, "Expected exactly one element, but found more.");
        return castedOutput;
    }

    public static <T> @Nullable T read(final @NotNull String text,
                                       final @Nullable EdnaOptions options,
                                       final @NotNull Class<T> castClass) {
        return readSingle(new CodePointIterator(text.codePoints()), options, castClass);
    }

    public static @Nullable Object read(final @NotNull String text,
                                        final @Nullable EdnaOptions options) {
        return read(text, options, Object.class);
    }

    public static @Nullable Object read(final @NotNull String text) {
        return read(text, null);
    }

    public static <T> @Nullable T read(final @NotNull Reader reader,
                                       final @Nullable EdnaOptions options,
                                       final @NotNull Class<T> castClass) {
        final CodePointIterator cpi = new CodePointIterator(reader);
        return readSingle(cpi, options, castClass);
    }

    public static @Nullable Object read(final @NotNull Reader reader,
                                        final @Nullable EdnaOptions options) {
        return read(reader, options, Object.class);
    }

    public static @Nullable Object read(final @NotNull Reader reader) {
        return read(reader, null);
    }

    private static @NotNull @Unmodifiable List<Object> readMulti(
            final @NotNull CodePointIterator cpi,
            final @Nullable EdnaOptions options) {
        final Iterator<Object> iter = EdnaReader.reader(cpi, optsOrDefault(options));
        final var spliterator = Spliterators.spliteratorUnknownSize(iter, 0);
        return StreamSupport.stream(spliterator, false).toList();
    }

    public static @NotNull @Unmodifiable List<Object> readMulti(final @NotNull String text,
                                                                final @Nullable EdnaOptions options) {
        return readMulti(new CodePointIterator(text.codePoints()), options);
    }

    public static @NotNull @Unmodifiable List<Object> readMulti(final @NotNull String text) {
        return readMulti(new CodePointIterator(text.codePoints()), null);
    }

    public static @NotNull @Unmodifiable List<Object> readMulti(
            final @NotNull Reader reader,
            final @Nullable EdnaOptions options) {
        final CodePointIterator cpi = new CodePointIterator(reader);
        return readMulti(cpi, options);
    }

    public static @NotNull @Unmodifiable List<Object> readMulti(
            final @NotNull Reader reader) {
        return readMulti(reader, null);
    }

    private static @NotNull Iterator<Object> reader(
            final @NotNull CodePointIterator cpi,
            final @Nullable EdnaOptions options) {
        return EdnaReader.reader(cpi, optsOrDefault(options));
    }

    public static @NotNull Iterator<Object> reader(
            final @NotNull String text,
            final @Nullable EdnaOptions options) {
        return reader(new CodePointIterator(text.codePoints()), options);
    }

    public static @NotNull Iterator<Object> reader(
            final @NotNull String text) {
        return reader(text, null);
    }

    public static @NotNull Iterator<Object> reader(
            final @NotNull Reader reader,
            final @Nullable EdnaOptions options) {
        return reader(new CodePointIterator(reader), options);
    }

    public static @NotNull Iterator<Object> reader(
            final @NotNull Reader reader) {
        return reader(reader, null);
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
