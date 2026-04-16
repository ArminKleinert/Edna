package de.kleinert.edna;

import de.kleinert.edna.data.EdnCollections;
import de.kleinert.edna.data.Symbol;
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
                          boolean allowZeroPrefix,
                          boolean allowRatios) {
    private static EdnaOptions defaultOptions;

    public static @NotNull EdnaOptions defaultOptions() {
        if (defaultOptions == null) defaultOptions = new EdnaOptions(
                false,
                false,
                Map.of(),
                new EdnCollections.EdnMap<>(List.of()),
                false,
                false,
                false,
                " ",
                EdnCollections.EdnList::new,
                EdnCollections.EdnVector::new,
                EdnCollections.EdnSet::new,
                EdnCollections.EdnMap::new,
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
                new EdnCollections.EdnMap<>(List.of()),
                true,
                true,
                true,
                " ",
                EdnCollections.EdnList::new,
                EdnCollections.EdnVector::new,
                EdnCollections.EdnSet::new,
                EdnCollections.EdnMap::new,
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
                true,
                true);
        return extendedOptions;
    }


    public @NotNull EdnaOptions copy(@NotNull UnaryOperator<Builder> f) {
        Builder b = new Builder(this);
        return f.apply(b).build();
    }

    public static final class Builder {
        private boolean allowSchemeUTF32Codes;
        private boolean allowDispatchChars;
        private Map<String, Function<Object, Object>> ednClassDecoders;
        private SequencedMap<Class<?>, Function<Object, Map.Entry<String, ?>>> ednClassEncoders;
        private boolean moreNumberPrefixes;
        private boolean allowNumericSuffixes;
        private boolean allowMoreEncoderDecoderNames;
        private String encodingSequenceSeparator;
        private Function<List<?>, List<?>> listToEdnListConverter;
        private Function<List<?>, List<?>> listToEdnVectorConverter;
        private Function<List<?>, Set<?>> listToEdnSetConverter;
        private Function<List<Map.Entry<Object, Object>>, Map<?, ?>> listToEdnMapConverter;
        private boolean allowUTFSymbols;
        private boolean allowReferences;
        private Map<String, Function<?, ?>> dispatchMacros;
        private int encoderSequenceElementLimit;
        private int encoderCollectionElementLimit;
        private int encoderMaxColumn;
        private String encoderLineIndent;
        private boolean encoderPrettyPrint;
        private Map<Symbol, Object> referenceTable;
        private boolean allowMetaData;
        private boolean allowZeroPrefix;
        private boolean allowRatios;

        public Builder(EdnaOptions o) {
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
            this.allowRatios = o.allowRatios();
        }

        public Builder allowSchemeUTF32Codes(boolean v) { this.allowSchemeUTF32Codes = v; return this; }
        public Builder allowDispatchChars(boolean v) { this.allowDispatchChars = v; return this; }
        public Builder ednClassDecoders(Map<String, Function<Object, Object>> v) { this.ednClassDecoders = v; return this; }
        public Builder ednClassEncoders(SequencedMap<Class<?>, Function<Object, Map.Entry<String, ?>>> v) { this.ednClassEncoders = v; return this; }
        public Builder moreNumberPrefixes(boolean v) { this.moreNumberPrefixes = v; return this; }
        public Builder allowNumericSuffixes(boolean v) { this.allowNumericSuffixes = v; return this; }
        public Builder allowMoreEncoderDecoderNames(boolean v) { this.allowMoreEncoderDecoderNames = v; return this; }
        public Builder encodingSequenceSeparator(String v) { this.encodingSequenceSeparator = v; return this; }
        public Builder listToEdnListConverter(Function<List<?>, List<?>> v) { this.listToEdnListConverter = v; return this; }
        public Builder listToEdnVectorConverter(Function<List<?>, List<?>> v) { this.listToEdnVectorConverter = v; return this; }
        public Builder listToEdnSetConverter(Function<List<?>, Set<?>> v) { this.listToEdnSetConverter = v; return this; }
        public Builder listToEdnMapConverter(Function<List<Map.Entry<Object, Object>>, Map<?, ?>> v) { this.listToEdnMapConverter = v; return this; }
        public Builder allowUTFSymbols(boolean v) { this.allowUTFSymbols = v; return this; }
        public Builder allowReferences(boolean v) { this.allowReferences = v; return this; }
        public Builder dispatchMacros(Map<String, Function<?, ?>> v) { this.dispatchMacros = v; return this; }
        public Builder encoderSequenceElementLimit(int v) { this.encoderSequenceElementLimit = v; return this; }
        public Builder encoderCollectionElementLimit(int v) { this.encoderCollectionElementLimit = v; return this; }
        public Builder encoderMaxColumn(int v) { this.encoderMaxColumn = v; return this; }
        public Builder encoderLineIndent(String v) { this.encoderLineIndent = v; return this; }
        public Builder encoderPrettyPrint(boolean v) { this.encoderPrettyPrint = v; return this; }
        public Builder referenceTable(Map<Symbol, Object> v) { this.referenceTable = v; return this; }
        public Builder allowMetaData(boolean v) { this.allowMetaData = v; return this; }
        public Builder allowZeroPrefix(boolean v) { this.allowZeroPrefix = v; return this; }
        public Builder allowRatios(boolean v) { this.allowRatios = v; return this; }

        public EdnaOptions build() {
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
                    allowZeroPrefix,
                    allowRatios
            );
        }
    }
}
