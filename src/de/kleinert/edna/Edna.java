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
import java.util.Objects;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The primary interface through which Edna is accessed. Provides methods for reading and writing.
 */
public class Edna {
    private Edna(){}

    /**
     * Default parsing options specified in the EDN file specification.
     * @return Default options.
     * @see EdnaOptions#defaultOptions()
     */
    public static @NotNull EdnaOptions defaultOptions() {
        return EdnaOptions.defaultOptions();
    }

    /**
     * Default parsing options.
     * @return Default options.
     * @see EdnaOptions#defaultOptions()
     */
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

    /**
     * Parse text with the given option and then cast the result to the specified type. This method is theoretically equivalent to {@link #read(Reader, EdnaOptions, Class)} if using a {@link StringReader}. But the actual implementation might vary.
     * @param text Input text.
     * @param options The options used. If null, defaults to {@link #defaultOptions()}.
     * @param castClass The expected result class.
     * @return The parsed result.
     * @param <T> Output type. Specified by the class argument.
     * @throws ClassCastException If the output is not convertable to T.
     */
    public static <T> @Nullable T read(final @NotNull String text,
                                       final @Nullable EdnaOptions options,
                                       final @NotNull Class<T> castClass) {
        return readSingle(new CodePointIterator(text.codePoints()), options, castClass);
    }

    /**
     * Equivalent to {@link #read(String, EdnaOptions, Class)} with the class being set to {@code Object.class}.
     * @param text Input text.
     * @param options The options used. If null, defaults to {@link #defaultOptions()}.
     * @return The parsed result.
     */
    public static @Nullable Object read(final @NotNull String text,
                                        final @Nullable EdnaOptions options) {
        return read(text, options, Object.class);
    }

    /**
     * Equivalent to {@link #read(String, EdnaOptions)} with the options being set to {@link #defaultOptions()}.
     * @param text Input text.
     * @return The parsed result.
     */
    public static @Nullable Object read(final @NotNull String text) {
        return read(text, null);
    }

    /**
     * Equivalent to {@link #read(String, EdnaOptions, Class)} except the input is received lazily from the reader. Note that this method is not thread-safe. Edna will not be held accountable if the reader is used somewhere else.
     * @param reader Input.
     * @param options The options used. If null, defaults to {@link #defaultOptions()}.
     * @param castClass The expected result class.
     * @return The parsed
     * @param <T> Output type. Specified by the class argument.
     * @throws ClassCastException If the output is not convertable to T.
     */
    public static <T> @Nullable T read(final @NotNull Reader reader,
                                       final @Nullable EdnaOptions options,
                                       final @NotNull Class<T> castClass) {
        final CodePointIterator cpi = new CodePointIterator(reader);
        return readSingle(cpi, options, castClass);
    }

    /**
     * Equivalent to {@link #read(Reader, EdnaOptions, Class)} with the class being set to {@code Object.class}.
     * @param reader Input reader.
     * @param options The options used. If null, defaults to {@link #defaultOptions()}.
     * @return The parsed result.
     * @see #read(Reader, EdnaOptions, Class)
     */
    public static @Nullable Object read(final @NotNull Reader reader,
                                        final @Nullable EdnaOptions options) {
        return read(reader, options, Object.class);
    }

    /**
     * Equivalent to {@link #read(Reader, EdnaOptions)} with the options being set to {@link #defaultOptions()}.
     * @param reader Input reader.
     * @return The parsed result.
     * @see #read(Reader, EdnaOptions, Class)
     */
    public static @Nullable Object read(final @NotNull Reader reader) {
        return read(reader, null);
    }

    private static @NotNull @Unmodifiable Stream<Object> stream(
            final @NotNull CodePointIterator cpi,
            final @Nullable EdnaOptions options) {
        final Iterator<Object> iter = EdnaReader.reader(cpi, optsOrDefault(options));
        final var spliterator = Spliterators.spliteratorUnknownSize(iter, 0);
        return StreamSupport.stream(spliterator, false);
    }

    /**
     * Creates a stream of objects parsed from an EDN input. The stream terminates if an error or end-of-input is encountered.
     * @param text Input text.
     * @param options The options used. If null, defaults to {@link #defaultOptions()}.
     * @return The parsed results as a lazy stream.
     */
    public static @NotNull @Unmodifiable Stream<Object> stream(final @NotNull String text,
                                                               final @Nullable EdnaOptions options) {
        return stream(new CodePointIterator(text.codePoints()), options);
    }

    /**
     * Equivalent to {@link #stream(String, EdnaOptions)} with the options being set to {@link #defaultOptions()}.
     * @param text Input text.
     * @return The parsed result.
     */
    public static @NotNull @Unmodifiable Stream<Object> stream(final @NotNull String text) {
        return stream(new CodePointIterator(text.codePoints()), null);
    }

    public static @NotNull @Unmodifiable Stream<Object> stream(
            final @NotNull Reader reader,
            final @Nullable EdnaOptions options) {
        return stream(new CodePointIterator(reader), options);
    }

    public static @NotNull @Unmodifiable Stream<Object> stream(
            final @NotNull Reader reader) {
        return stream(reader, null);
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
