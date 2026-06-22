package de.kleinert.edna;

import de.kleinert.edna.data.*;
import de.kleinert.edna.pprint.EdnaTagVal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * This class represents options for both reading and writing EDN files with Edna.
 *
 *
 *
 * @param allowSchemeUTF32Codes Allows chars of the form {@code \\xXXXXXXXX} where uppercase X represents any hex digit. The value is limited to {@code #\x0000FFFF} (@link Character#MAX_VALUE).
 * @param allowDispatchChars Allows characters to start with the dispatch sign {@code #}. These characters are then treated as instances of {@link Char32} and therefore limited to 0..{@link Char32#MAX_VALUE}. This also allows the use of Unicode literals with eight hexadecimal digits of the form {@code #\\uXXXXXXXX}. The option is compatible with {@link #allowSchemeUTF32Codes} and {@link #allowBase8Chars()}.
 * @param allowBase8Chars Allows the use of character literals of the form {@code \oXX} or {@code \oXXX} where X is any octal digit (0 to 7).
 * @param taggedElementDecoders A map of Strings to functions to decode EDN values into objects. E.g. {@code Map.of("my/int", (o) -> ((Number) o).intValue())} allows the EDN file {@code "#my/int 123"} to be read as the integer {@code 123} (instead of the default number type). If {@link #allowTaggedElementsWithoutNS()} is {@code false}, the "/" is mandatory to represent a symbol with a namespace. All tags must be valid symbols according to {@link Symbol#parse(String)}.
 * @param taggedElementEncoders A setting the pretty-printer only. It allows defining encoders for classes. The mapping must be sequential because the user could, for example, define an encoder for {@code ArrayList} and {@code List}. In such a case, the encoder for ArrayList must appear first.
 * @param moreNumberPrefixes Allows the use of prefixes for Long literals ({@code 0b} for binary, {@code 0b} for binary, {@code 0x} for hex) as well as arbitrary bases with the {@code XXr} prefix (where {@code X} is any decimal digit) like {@code 36rS0MENUMBER}. This works with the {@code N} postfix for {@link java.math.BigInteger} as well. Attention: If you use a base big enough to treat "N" as a digit, the "N" will be treated as marking a BigInteger (e.g. {@code 36r1TN} is 65 as a BigInteger).
 * @param allowTaggedElementsWithoutNS Related to {@link #taggedElementDecoders()}. If true, tags are valid without a namespace.
 * @param encodingSequenceSeparator
 * @param listToEdnListConverter
 * @param listToEdnVectorConverter
 * @param listToEdnSetConverter
 * @param listToEdnMapConverter
 * @param allowUTFSymbols
 * @param encoderSequenceElementLimit
 * @param encoderCollectionElementLimit
 * @param encoderMaxColumn
 * @param encoderLineIndent
 * @param encoderPrettyPrint
 * @param allowMetaData
 * @param allowZeroPrefix
 * @param allowSymbolicValues
 * @param symbolicValues
 */
public record EdnaOptions(boolean allowSchemeUTF32Codes,
                          boolean allowDispatchChars,
                          boolean allowBase8Chars,
                          @NotNull Map<@NotNull String, @NotNull Function<Object, Object>> taggedElementDecoders,
                          @NotNull SequencedMap<@NotNull Class<?>, @NotNull Function<@NotNull Object, @NotNull EdnaTagVal>> taggedElementEncoders,
                          boolean moreNumberPrefixes,
                          boolean allowTaggedElementsWithoutNS,
                          @NotNull String encodingSequenceSeparator,
                          @NotNull Function<@NotNull List<@Nullable Object>, List<@Nullable Object>> listToEdnListConverter,
                          @NotNull Function<@NotNull List<@Nullable Object>, List<@Nullable Object>> listToEdnVectorConverter,
                          @NotNull Function<@NotNull List<@Nullable Object>, Set<@Nullable Object>> listToEdnSetConverter,
                          @NotNull Function<@NotNull List<@Nullable Object>, Map<@Nullable Object, @Nullable Object>> listToEdnMapConverter,
                          boolean allowUTFSymbols,
                          int encoderSequenceElementLimit,
                          int encoderCollectionElementLimit,
                          int encoderMaxColumn,
                          @NotNull String encoderLineIndent,
                          boolean encoderPrettyPrint,
                          boolean allowMetaData,
                          boolean allowZeroPrefix,
                          boolean allowSymbolicValues,
                          @NotNull Map<@NotNull Symbol, Object> symbolicValues) {
    /**
     * A mapping of the following symbolic values:
     * <ul>
     *     <li>{@code ##NaN} to a {@code Double} representation of NaN.</li>
     *     <li>{@code ##Inf} to a {@code Double} representation of infinity.</li>
     *     <li>{@code ##-Inf} to a {@code Double} representation of negative infinity.</li>
     * </ul>
     * Note that if {@link #allowSymbolicValues()} is false on the options, the symbolic values are not considered at all.
     * @return A mapping of some standard symbolic values.
     */
    public static @Unmodifiable @NotNull Map<@NotNull Symbol, Object> defaultSymbolicValues() {
        return Map.of(
                Symbol.symbol("NaN"), Double.NaN,
                Symbol.symbol("Inf"), Double.POSITIVE_INFINITY,
                Symbol.symbol("-Inf"), Double.NEGATIVE_INFINITY);
    }

    /**
     * Default parsing options specified in the EDN file specification.
     *
     * @return Default options.
     * @see Edna#defaultOptions()
     */
    public static @NotNull EdnaOptions defaultOptions() {
        return new EdnaOptions(
                false,
                false,
                false,
                Map.of(),
                EdnaMap.of(),
                false,
                false,
                ", ",
                EdnaList::create,
                EdnaVector::create,
                EdnaSet::create,
                EdnaMap::create,
                false,
                1000,
                10000,
                80,
                "  ",
                true,
                false,
                false,
                false,
                defaultSymbolicValues());
    }

    /**
     * Uses {@link #defaultOptions()} with the following set to true:
     * {@link #allowSchemeUTF32Codes}, {@link #allowDispatchChars}, {@link #allowBase8Chars}, {@link #moreNumberPrefixes}, {@link #allowTaggedElementsWithoutNS}, {@link #allowUTFSymbols}, {@link #allowMetaData}, {@link #allowZeroPrefix}, {@link #allowSymbolicValues}
     *
     * @return Edna's extended EDN features.
     */
    public static @NotNull EdnaOptions extendedOptions() {
        return defaultOptions().copy(builder -> builder
                .allowSchemeUTF32Codes(true)
                .allowDispatchChars(true)
                .allowBase8Chars(true)
                .moreNumberPrefixes(true)
                .allowTaggedElementsWithoutNS(true)
                .allowUTFSymbols(true)
                .allowMetaData(true)
                .allowZeroPrefix(true)
                .allowSymbolicValues(true));
    }

    /**
     * Creates a new instance based on the callee by using a {@link Builder}.
     * <p>
     * The example below shows how to use a builder to enable zero-prefixed numbers and metadata.
     * <pre>
     * {@code
     *         var opts = Edna.defaultOptions().copy(builder -> builder
     *                      .allowZeroPrefix(true)
     *                      .allowMetaData(true)
     *         );
     * }
     * </pre>
     *
     * @param f The builder function.
     * @return A new instance based on a {@link Builder}.
     */
    public @NotNull EdnaOptions copy(final @NotNull UnaryOperator<Builder> f) {
        final @NotNull Builder b = new Builder(this);
        return f.apply(b).build();
    }

    /**
     * A builder class for {@link EdnaOptions}. This class should be exclusively used in {@link EdnaOptions#copy(UnaryOperator)} and not be directly created by the user.
     */
    public static final class Builder {
        private boolean allowSchemeUTF32Codes;
        private boolean allowDispatchChars;
        private boolean allowBase8Chars;
        private @NotNull Map<@NotNull String, @NotNull Function<Object, Object>> taggedElementDecoders;
        private @NotNull SequencedMap<@NotNull Class<?>, @NotNull Function<@NotNull Object, @NotNull EdnaTagVal>> taggedElementEncoders;
        private boolean moreNumberPrefixes;
        private boolean allowTaggedElementsWithoutNS;
        private @NotNull String encodingSequenceSeparator;
        private @NotNull Function<@NotNull List<@Nullable Object>, List<@Nullable Object>> listToEdnListConverter;
        private @NotNull Function<@NotNull List<@Nullable Object>, List<@Nullable Object>> listToEdnVectorConverter;
        private @NotNull Function<@NotNull List<@Nullable Object>, Set<@Nullable Object>> listToEdnSetConverter;
        private @NotNull Function<@NotNull List<@Nullable Object>, Map<@Nullable Object, @Nullable Object>> listToEdnMapConverter;
        private boolean allowUTFSymbols;
        private int encoderSequenceElementLimit;
        private int encoderCollectionElementLimit;
        private int encoderMaxColumn;
        private @NotNull String encoderLineIndent;
        private boolean encoderPrettyPrint;
        private boolean allowMetaData;
        private boolean allowZeroPrefix;
        private boolean allowSymbolicValues;
        private @NotNull Map<@NotNull Symbol, Object> symbolicValues;

        Builder(final @NotNull EdnaOptions o) {
            this.allowSchemeUTF32Codes = o.allowSchemeUTF32Codes();
            this.allowDispatchChars = o.allowDispatchChars();
            this.allowBase8Chars = o.allowBase8Chars;
            this.taggedElementDecoders = o.taggedElementDecoders();
            this.taggedElementEncoders = o.taggedElementEncoders();
            this.moreNumberPrefixes = o.moreNumberPrefixes();
            this.allowTaggedElementsWithoutNS = o.allowTaggedElementsWithoutNS();
            this.encodingSequenceSeparator = o.encodingSequenceSeparator();
            this.listToEdnListConverter = o.listToEdnListConverter();
            this.listToEdnVectorConverter = o.listToEdnVectorConverter();
            this.listToEdnSetConverter = o.listToEdnSetConverter();
            this.listToEdnMapConverter = o.listToEdnMapConverter();
            this.allowUTFSymbols = o.allowUTFSymbols();
            this.encoderSequenceElementLimit = o.encoderSequenceElementLimit();
            this.encoderCollectionElementLimit = o.encoderCollectionElementLimit();
            this.encoderMaxColumn = o.encoderMaxColumn();
            this.encoderLineIndent = o.encoderLineIndent();
            this.encoderPrettyPrint = o.encoderPrettyPrint();
            this.allowMetaData = o.allowMetaData();
            this.allowZeroPrefix = o.allowZeroPrefix();
            this.allowSymbolicValues = o.allowSymbolicValues();
            this.symbolicValues = o.symbolicValues();
        }

        /**
         * Sets the value of {@link #allowSchemeUTF32Codes()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder allowSchemeUTF32Codes(final boolean v) {
            this.allowSchemeUTF32Codes = v;
            return this;
        }

        /**
         * Sets the value of {@link #allowDispatchChars()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder allowDispatchChars(final boolean v) {
            this.allowDispatchChars = v;
            return this;
        }

        /**
         * Sets the value of {@link #allowBase8Chars()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder allowBase8Chars(final boolean v) {
            this.allowBase8Chars = v;
            return this;
        }

        /**
         * Sets the value of {@link #taggedElementDecoders()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public <A, B> Builder taggedElementDecoders(final Map<@NotNull String, @NotNull Function<A, B>> v) {
            //noinspection unchecked,rawtypes
            this.taggedElementDecoders = new HashMap<>((Map) v);
            return this;
        }

        /**
         * Sets the value of {@link #taggedElementEncoders()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        @SuppressWarnings("rawtypes")
        public Builder taggedElementEncoders(final @NotNull SequencedMap<@NotNull Class, @NotNull Function<Object, EdnaTagVal>> v) {
            //noinspection unchecked
            this.taggedElementEncoders = new LinkedHashMap<>(
                    (Map<Class<?>, Function<@NotNull Object, @NotNull EdnaTagVal>>) (Map<?, ?>) v
            );
            return this;
        }

        /**
         * Sets the value of {@link #moreNumberPrefixes()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder moreNumberPrefixes(final boolean v) {
            this.moreNumberPrefixes = v;
            return this;
        }

        /**
         * Sets the value of {@link #allowTaggedElementsWithoutNS()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder allowTaggedElementsWithoutNS(boolean v) {
            this.allowTaggedElementsWithoutNS = v;
            return this;
        }

        /**
         * Sets the value of {@link #encodingSequenceSeparator()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder encodingSequenceSeparator(final @NotNull String v) {
            this.encodingSequenceSeparator = v;
            return this;
        }

        /**
         * Sets the value of {@link #listToEdnListConverter()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public <A> Builder listToEdnListConverter(final @NotNull Function<@NotNull List<@Nullable Object>, List<A>> v) {
            // noinspection unchecked
            this.listToEdnListConverter = (@NotNull Function<@NotNull List<@Nullable Object>, List<@Nullable Object>>) ((Function<?, ?>) v);
            return this;
        }

        /**
         * Sets the value of {@link #listToEdnVectorConverter()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public <A> Builder listToEdnVectorConverter(final @NotNull Function<@NotNull List<@Nullable Object>, List<A>> v) {
            //noinspection unchecked
            this.listToEdnVectorConverter = (@NotNull Function<@NotNull List<@Nullable Object>, List<@Nullable Object>>) ((Function<?, ?>) v);
            return this;
        }

        /**
         * Sets the value of {@link #listToEdnSetConverter()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public <A> Builder listToEdnSetConverter(final @NotNull Function<@NotNull List<@Nullable Object>, Set<A>> v) {
            //noinspection unchecked
            this.listToEdnSetConverter = (Function<@NotNull List<@Nullable Object>, Set<@Nullable Object>>) ((Function<?, ?>) v);
            return this;
        }

        /**
         * Sets the value of {@link #listToEdnMapConverter()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public <A, B> Builder listToEdnMapConverter(final @NotNull Function<@NotNull List<@Nullable Object>, Map<A, B>> v) {
            // noinspection unchecked
            this.listToEdnMapConverter = (Function<@NotNull List<@Nullable Object>, Map<@Nullable Object, @Nullable Object>>) ((Function<?, ?>) v);
            return this;
        }

        /**
         * Sets the value of {@link #allowUTFSymbols()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder allowUTFSymbols(final boolean v) {
            this.allowUTFSymbols = v;
            return this;
        }

        /**
         * Sets the value of {@link #encoderSequenceElementLimit()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder encoderSequenceElementLimit(final int v) {
            this.encoderSequenceElementLimit = v;
            return this;
        }

        /**
         * Sets the value of {@link #encoderCollectionElementLimit()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder encoderCollectionElementLimit(final int v) {
            this.encoderCollectionElementLimit = v;
            return this;
        }

        /**
         * Sets the value of {@link #encoderMaxColumn()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder encoderMaxColumn(final int v) {
            this.encoderMaxColumn = v;
            return this;
        }

        /**
         * Sets the value of {@link #encoderLineIndent()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder encoderLineIndent(final @NotNull String v) {
            this.encoderLineIndent = v;
            return this;
        }

        /**
         * Sets the value of {@link #encoderPrettyPrint()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder encoderPrettyPrint(final boolean v) {
            this.encoderPrettyPrint = v;
            return this;
        }

        /**
         * Sets the value of {@link #allowMetaData()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder allowMetaData(final boolean v) {
            this.allowMetaData = v;
            return this;
        }

        /**
         * Sets the value of {@link #allowZeroPrefix()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder allowZeroPrefix(final boolean v) {
            this.allowZeroPrefix = v;
            return this;
        }

        /**
         * Sets the value of {@link #allowSymbolicValues()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder allowSymbolicValues(final boolean v) {
            this.allowSymbolicValues = v;
            return this;
        }

        /**
         * Sets the value of {@link #symbolicValues()}
         * and returns {@code this}.
         *
         * @param v The value.
         * @return The builder
         */
        public Builder symbolicValues(final Map<@NotNull Symbol, ?> v) {
            //noinspection unchecked
            this.symbolicValues = (Map<Symbol, Object>) v;
            return this;
        }

        @NotNull EdnaOptions build() {
            return new EdnaOptions(
                    allowSchemeUTF32Codes,
                    allowDispatchChars,
                    allowBase8Chars,
                    taggedElementDecoders,
                    taggedElementEncoders,
                    moreNumberPrefixes,
                    allowTaggedElementsWithoutNS,
                    encodingSequenceSeparator,
                    listToEdnListConverter,
                    listToEdnVectorConverter,
                    listToEdnSetConverter,
                    listToEdnMapConverter,
                    allowUTFSymbols,
                    encoderSequenceElementLimit,
                    encoderCollectionElementLimit,
                    encoderMaxColumn,
                    encoderLineIndent,
                    encoderPrettyPrint,
                    allowMetaData,
                    allowZeroPrefix,
                    allowSymbolicValues,
                    symbolicValues
            );
        }
    }
}
