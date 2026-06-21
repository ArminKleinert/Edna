package de.kleinert.edna;

import de.kleinert.edna.data.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

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
    public static @NotNull Map<@NotNull Symbol, Object> defaultSymbolicValues() {
        return new HashMap<>(Map.of(
                Symbol.symbol("NaN"), Double.NaN,
                Symbol.symbol("Inf"), Double.POSITIVE_INFINITY,
                Symbol.symbol("-Inf"), Double.NEGATIVE_INFINITY));
    }

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
                Map.of());
    }

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

    public @NotNull EdnaOptions copy(final @NotNull UnaryOperator<Builder> f) {
        final @NotNull Builder b = new Builder(this);
        return f.apply(b).build();
    }

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

        public Builder(final @NotNull EdnaOptions o) {
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

        public Builder allowSchemeUTF32Codes(final boolean v) {
            this.allowSchemeUTF32Codes = v;
            return this;
        }

        public Builder allowDispatchChars(final boolean v) {
            this.allowDispatchChars = v;
            return this;
        }

        public Builder allowBase8Chars(final boolean v) {
            this.allowBase8Chars = v;
            return this;
        }

        public <A, B> Builder taggedElementDecoders(final Map<@NotNull String, @NotNull Function<A, B>> v) {
            //noinspection unchecked,rawtypes
            this.taggedElementDecoders = new HashMap<>((Map) v);
            return this;
        }

        public <A> Builder taggedElementEncoders(final @NotNull SequencedMap<@NotNull Class, @NotNull Function<A, EdnaTagVal>> v) {
            //noinspection unchecked
            this.taggedElementEncoders = new LinkedHashMap<>(
                    (Map<Class<?>, Function<@NotNull Object, @NotNull EdnaTagVal>>) (Map<?, ?>) v
            );
            return this;
        }

        public Builder moreNumberPrefixes(final boolean v) {
            this.moreNumberPrefixes = v;
            return this;
        }

        public Builder allowTaggedElementsWithoutNS(boolean v) {
            this.allowTaggedElementsWithoutNS = v;
            return this;
        }

        public Builder encodingSequenceSeparator(final @NotNull String v) {
            this.encodingSequenceSeparator = v;
            return this;
        }

        public <A> Builder listToEdnListConverter(final @NotNull Function<@NotNull List<@Nullable Object>, List<A>> v) {
            // noinspection unchecked
            this.listToEdnListConverter = (@NotNull Function<@NotNull List<@Nullable Object>, List<@Nullable Object>>) ((Function<?, ?>) v);
            return this;
        }

        public <A> Builder listToEdnVectorConverter(final @NotNull Function<@NotNull List<@Nullable Object>, List<A>> v) {
            //noinspection unchecked
            this.listToEdnVectorConverter = (@NotNull Function<@NotNull List<@Nullable Object>, List<@Nullable Object>>) ((Function<?, ?>) v);
            return this;
        }

        public <A> Builder listToEdnSetConverter(final @NotNull Function<@NotNull List<@Nullable Object>, Set<A>> v) {
            //noinspection unchecked
            this.listToEdnSetConverter = (Function<@NotNull List<@Nullable Object>, Set<@Nullable Object>>) ((Function<?, ?>) v);
            return this;
        }

        public <A, B> Builder listToEdnMapConverter(final @NotNull Function<@NotNull List<@Nullable Object>, Map<A, B>> v) {
            // noinspection unchecked
            this.listToEdnMapConverter = (Function<@NotNull List<@Nullable Object>, Map<@Nullable Object, @Nullable Object>>) ((Function<?, ?>) v);
            return this;
        }

        public Builder allowUTFSymbols(final boolean v) {
            this.allowUTFSymbols = v;
            return this;
        }

        public Builder encoderSequenceElementLimit(final int v) {
            this.encoderSequenceElementLimit = v;
            return this;
        }

        public Builder encoderCollectionElementLimit(final int v) {
            this.encoderCollectionElementLimit = v;
            return this;
        }

        public Builder encoderMaxColumn(final int v) {
            this.encoderMaxColumn = v;
            return this;
        }

        public Builder encoderLineIndent(final @NotNull String v) {
            this.encoderLineIndent = v;
            return this;
        }

        public Builder encoderPrettyPrint(final boolean v) {
            this.encoderPrettyPrint = v;
            return this;
        }

        public Builder allowMetaData(final boolean v) {
            this.allowMetaData = v;
            return this;
        }

        public Builder allowZeroPrefix(final boolean v) {
            this.allowZeroPrefix = v;
            return this;
        }

        public Builder allowSymbolicValues(final boolean v) {
            this.allowSymbolicValues = v;
            return this;
        }

        public Builder symbolicValues(final Map<@NotNull Symbol, ?> v) {
            this.symbolicValues = new HashMap<>(v);
            return this;
        }

        public @NotNull EdnaOptions build() {
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
