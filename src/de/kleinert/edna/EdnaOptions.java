package de.kleinert.edna;

import de.kleinert.edna.data.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public record EdnaOptions(boolean allowSchemeUTF32Codes,
                          boolean allowDispatchChars,
                          @NotNull Map<String, Function<Object, Object>> ednClassDecoders,
                          @NotNull SequencedMap<Class<?>, Function<Object, Map.Entry<String, ?>>> ednClassEncoders,
                          boolean moreNumberPrefixes,
                          boolean allowNumericSuffixes,
                          boolean allowMoreEncoderDecoderNames,
                          @NotNull String encodingSequenceSeparator,
                          @NotNull Function<List<?>, List<?>> listToEdnListConverter,
                          @NotNull Function<List<?>, List<?>> listToEdnVectorConverter,
                          @NotNull Function<List<?>, Set<?>> listToEdnSetConverter,
                          @NotNull Function<List<Map.Entry<Object, Object>>, Map<?, ?>> listToEdnMapConverter,
                          boolean allowUTFSymbols,
                          boolean allowReferences,
                          @NotNull Map<String, Function<?, ?>> dispatchMacros,
                          int encoderSequenceElementLimit,
                          int encoderCollectionElementLimit,
                          int encoderMaxColumn,
                          @NotNull String encoderLineIndent,
                          boolean encoderPrettyPrint,
                          @NotNull Map<Symbol, Object> referenceTable,
                          boolean allowMetaData,
                          boolean allowZeroPrefix) {
    private static EdnaOptions defaultOptions;

    public static @NotNull EdnaOptions defaultOptions() {
        if (defaultOptions == null) defaultOptions = new EdnaOptions(
                false,
                false,
                Map.of(),
                new EdnaMap<>(List.of()),
                false,
                false,
                false,
                ", ",
                EdnaList::new,
                EdnaVector::new,
                EdnaSet::new,
                EdnaMap::new,
                false,
                false,
                Map.of(),
                1000,
                10000,
                80,
                "  ",
                true,
                Map.of(),
                false,
                false);
        return defaultOptions;
    }

    private static EdnaOptions extendedOptions;

    public static @NotNull EdnaOptions extendedOptions() {
        if (extendedOptions == null) extendedOptions = new EdnaOptions(
                true,
                true,
                Map.of(),
                new EdnaMap<>(List.of()),
                true,
                true,
                true,
                ", ",
                EdnaList::new,
                EdnaVector::new,
                EdnaSet::new,
                EdnaMap::new,
                true,
                true,
                Map.of(),
                1000,
                10000,
                80,
                "  ",
                true,
                Map.of(),
                true,
                true);
        return extendedOptions;
    }


    public @NotNull EdnaOptions copy(final @NotNull UnaryOperator<Builder> f) {
        final @NotNull Builder b = new Builder(this);
        return f.apply(b).build();
    }

    public static final class Builder {
        private boolean allowSchemeUTF32Codes;
        private boolean allowDispatchChars;
        private @NotNull Map<String, Function<Object, Object>> ednClassDecoders;
        private @NotNull SequencedMap<Class<?>, Function<Object, Map.Entry<String, ?>>> ednClassEncoders;
        private boolean moreNumberPrefixes;
        private boolean allowNumericSuffixes;
        private boolean allowMoreEncoderDecoderNames;
        private @NotNull String encodingSequenceSeparator;
        private @NotNull Function<List<?>, List<?>> listToEdnListConverter;
        private @NotNull Function<List<?>, List<?>> listToEdnVectorConverter;
        private @NotNull Function<List<?>, Set<?>> listToEdnSetConverter;
        private @NotNull Function<List<Map.Entry<Object, Object>>, Map<?, ?>> listToEdnMapConverter;
        private boolean allowUTFSymbols;
        private boolean allowReferences;
        private @NotNull Map<String, Function<?, ?>> dispatchMacros;
        private int encoderSequenceElementLimit;
        private int encoderCollectionElementLimit;
        private int encoderMaxColumn;
        private @NotNull String encoderLineIndent;
        private boolean encoderPrettyPrint;
        private @NotNull Map<Symbol, Object> referenceTable;
        private boolean allowMetaData;
        private boolean allowZeroPrefix;

        public Builder(final @NotNull EdnaOptions o) {
            this.allowSchemeUTF32Codes = o.allowSchemeUTF32Codes();
            this.allowDispatchChars = o.allowDispatchChars();
            this.ednClassDecoders = o.ednClassDecoders();
            this.ednClassEncoders = o.ednClassEncoders();
            this.moreNumberPrefixes = o.moreNumberPrefixes();
            this.allowNumericSuffixes = o.allowNumericSuffixes();
            this.allowMoreEncoderDecoderNames = o.allowMoreEncoderDecoderNames();
            this.encodingSequenceSeparator = o.encodingSequenceSeparator();
            this.listToEdnListConverter = o.listToEdnListConverter();
            this.listToEdnVectorConverter = o.listToEdnVectorConverter();
            this.listToEdnSetConverter = o.listToEdnSetConverter();
            this.listToEdnMapConverter = o.listToEdnMapConverter();
            this.allowUTFSymbols = o.allowUTFSymbols();
            this.allowReferences = o.allowReferences();
            this.dispatchMacros = o.dispatchMacros();
            this.encoderSequenceElementLimit = o.encoderSequenceElementLimit();
            this.encoderCollectionElementLimit = o.encoderCollectionElementLimit();
            this.encoderMaxColumn = o.encoderMaxColumn();
            this.encoderLineIndent = o.encoderLineIndent();
            this.encoderPrettyPrint = o.encoderPrettyPrint();
            this.referenceTable = o.referenceTable();
            this.allowMetaData = o.allowMetaData();
            this.allowZeroPrefix = o.allowZeroPrefix();
        }

        public Builder allowSchemeUTF32Codes(final boolean v) {
            this.allowSchemeUTF32Codes = v;
            return this;
        }

        public Builder allowDispatchChars(final boolean v) {
            this.allowDispatchChars = v;
            return this;
        }

        public Builder ednClassDecoders(final @NotNull Map<String, Function<Object, Object>> v) {
            this.ednClassDecoders = v;
            return this;
        }

        public Builder ednClassEncoders(final @NotNull SequencedMap<Class<?>, Function<Object, Map.Entry<String, ?>>> v) {
            this.ednClassEncoders = v;
            return this;
        }

        public Builder moreNumberPrefixes(final boolean v) {
            this.moreNumberPrefixes = v;
            return this;
        }

        public Builder allowNumericSuffixes(final boolean v) {
            this.allowNumericSuffixes = v;
            return this;
        }

        public Builder allowMoreEncoderDecoderNames(boolean v) {
            this.allowMoreEncoderDecoderNames = v;
            return this;
        }

        public Builder encodingSequenceSeparator(final @NotNull String v) {
            this.encodingSequenceSeparator = v;
            return this;
        }

        public Builder listToEdnListConverter(final @NotNull Function<List<?>, List<?>> v) {
            this.listToEdnListConverter = v;
            return this;
        }

        public Builder listToEdnVectorConverter(final @NotNull Function<List<?>, List<?>> v) {
            this.listToEdnVectorConverter = v;
            return this;
        }

        public Builder listToEdnSetConverter(final @NotNull Function<List<?>, Set<?>> v) {
            this.listToEdnSetConverter = v;
            return this;
        }

        public Builder listToEdnMapConverter(final @NotNull Function<List<Map.Entry<Object, Object>>, Map<?, ?>> v) {
            this.listToEdnMapConverter = v;
            return this;
        }

        public Builder allowUTFSymbols(final boolean v) {
            this.allowUTFSymbols = v;
            return this;
        }

        public Builder allowReferences(final boolean v) {
            this.allowReferences = v;
            return this;
        }

        public Builder dispatchMacros(final @NotNull Map<String, Function<?, ?>> v) {
            this.dispatchMacros = v;
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

        public Builder referenceTable(final @NotNull Map<Symbol, Object> v) {
            this.referenceTable = v;
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

        public @NotNull EdnaOptions build() {
            return new EdnaOptions(
                    allowSchemeUTF32Codes,
                    allowDispatchChars,
                    ednClassDecoders,
                    ednClassEncoders,
                    moreNumberPrefixes,
                    allowNumericSuffixes,
                    allowMoreEncoderDecoderNames,
                    encodingSequenceSeparator,
                    listToEdnListConverter,
                    listToEdnVectorConverter,
                    listToEdnSetConverter,
                    listToEdnMapConverter,
                    allowUTFSymbols,
                    allowReferences,
                    dispatchMacros,
                    encoderSequenceElementLimit,
                    encoderCollectionElementLimit,
                    encoderMaxColumn,
                    encoderLineIndent,
                    encoderPrettyPrint,
                    referenceTable,
                    allowMetaData,
                    allowZeroPrefix
            );
        }
    }
}
