package de.kleinert.edna;

import de.kleinert.edna.data.EdnCollections;
import de.kleinert.edna.data.Symbol;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;
import java.util.function.Function;

public class EdnOptions {
    private boolean allowSchemeUTF32Codes = false;
    private boolean allowDispatchChars = false;
    private @NotNull Map<String, Function<Object, Object>> ednClassDecoders = Map.of();
    private @NotNull SequencedMap<Class<?>, Function<Object, Map.Entry<String, ?>>> ednClassEncoders =
            new EdnCollections.EdnMap<>(List.of());
    private boolean moreNumberPrefixes;
    private boolean allowNumericSuffixes = false;
    private boolean allowMoreEncoderDecoderNames = false;
    private @NotNull String encodingSequenceSeparator = ", ";
    private @NotNull Function<List<?>, List<?>> listToEdnListConverter = EdnCollections.EdnList::new;
    private @NotNull Function<List<?>, List<?>> listToEdnVectorConverter = EdnCollections.EdnVector::new;
    private @NotNull Function<List<?>, Set<?>> listToEdnSetConverter = EdnCollections.EdnSet::new;
    private @NotNull Function<List<Map.Entry<Object, Object>>, Map<?, ?>> listToEdnMapConverter =
            EdnCollections.EdnMap::new;
    private boolean allowUTFSymbols = false;
    private boolean allowDefinitionsAndReferences = false;
    private @NotNull Map<String, Function<?, ?>> dispatchMacros = Map.of();
    private int encoderSequenceElementLimit = 1000;
    private int encoderCollectionElementLimit = 10000;
    private int encoderMaxColumn = 80;
    private @NotNull String encoderLineIndent = "  ";
    private boolean encoderPrettyPrint = true;
    private @NotNull Map<Symbol, Object> referenceTable = Map.of();
    private boolean allowMetaData = false;
    private boolean allowZeroPrefix = false;
    private boolean allowRatios = false;

    private static EdnOptions defaultOptions;

    public static EdnOptions defaultOptions() {
        if (defaultOptions == null) defaultOptions = new EdnOptions();
        return defaultOptions;
    }

    private static EdnOptions extendedOptions;

    public static EdnOptions extendedOptions() {
        if (extendedOptions == null)
            extendedOptions = new EdnOptions(
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true);
        return extendedOptions;
    }

    private EdnOptions(
            final boolean allowSchemeUTF32Codes,
            final boolean allowDispatchChars,
            final boolean moreNumberPrefixes,
            final boolean allowNumericSuffixes,
            final boolean allowMoreEncoderDecoderNames,
            final boolean allowUTFSymbols,
            final boolean allowRatios,
            final boolean allowMetaData
    ) {
        this.allowSchemeUTF32Codes = allowSchemeUTF32Codes;
        this.allowDispatchChars = allowDispatchChars;
        this.moreNumberPrefixes = moreNumberPrefixes;
        this.allowNumericSuffixes = allowNumericSuffixes;
        this.allowMoreEncoderDecoderNames = allowMoreEncoderDecoderNames;
        this.allowUTFSymbols = allowUTFSymbols;
        this.allowMetaData = allowMetaData;
        this.allowRatios = allowRatios;
    }

    public EdnOptions() {
    }

    public boolean allowSchemeUTF32Codes() {
        return allowSchemeUTF32Codes;
    }

    public @NotNull EdnOptions allowSchemeUTF32Codes(final boolean allowSchemeUTF32Codes) {
        this.allowSchemeUTF32Codes = allowSchemeUTF32Codes;
        return this;
    }

    public boolean allowDispatchChars() {
        return allowDispatchChars;
    }

    public @NotNull EdnOptions allowDispatchChars(final boolean allowDispatchChars) {
        this.allowDispatchChars = allowDispatchChars;
        return this;
    }

    public @NotNull Map<String, Function<Object, Object>> ednClassDecoders() {
        return ednClassDecoders;
    }

    public @NotNull EdnOptions ednClassDecoders(final @NotNull Map<String, Function<Object, Object>> ednClassDecoders) {
        this.ednClassDecoders = ednClassDecoders;
        return this;
    }

    public @NotNull SequencedMap<@NotNull Class<?>, @NotNull Function<Object, Map.Entry<String, ?>>> ednClassEncoders() {
        return ednClassEncoders;
    }

    public @NotNull EdnOptions ednClassEncoders(
            final @NotNull SequencedMap<@NotNull Class<?>, @NotNull Function<Object, Map.Entry<String, ?>>> ednClassEncoders) {
        this.ednClassEncoders = ednClassEncoders;
        return this;
    }

    public boolean moreNumberPrefixes() {
        return moreNumberPrefixes;
    }

    public @NotNull EdnOptions moreNumberPrefixes(final boolean moreNumberPrefixes) {
        this.moreNumberPrefixes = moreNumberPrefixes;
        return this;
    }

    public boolean allowNumericSuffixes() {
        return allowNumericSuffixes;
    }

    public @NotNull EdnOptions allowNumericSuffixes(final boolean allowNumericSuffixes) {
        this.allowNumericSuffixes = allowNumericSuffixes;
        return this;
    }

    public boolean allowMoreEncoderDecoderNames() {
        return allowMoreEncoderDecoderNames;
    }

    public @NotNull EdnOptions allowMoreEncoderDecoderNames(final boolean allowMoreEncoderDecoderNames) {
        this.allowMoreEncoderDecoderNames = allowMoreEncoderDecoderNames;
        return this;
    }

    public @NotNull String encodingSequenceSeparator() {
        return encodingSequenceSeparator;
    }

    public @NotNull EdnOptions encodingSequenceSeparator(final @NotNull String encodingSequenceSeparator) {
        this.encodingSequenceSeparator = encodingSequenceSeparator;
        return this;
    }

    public @NotNull Function<List<?>, List<?>> listToEdnListConverter() {
        return listToEdnListConverter;
    }

    public @NotNull EdnOptions listToEdnListConverter(final @NotNull Function<List<?>, List<?>> listToEdnListConverter) {
        this.listToEdnListConverter = listToEdnListConverter;
        return this;
    }

    public @NotNull Function<List<?>, List<?>> listToEdnVectorConverter() {
        return listToEdnVectorConverter;
    }

    public @NotNull EdnOptions listToEdnVectorConverter(final @NotNull Function<List<?>, List<?>> listToEdnVectorConverter) {
        this.listToEdnVectorConverter = listToEdnVectorConverter;
        return this;
    }

    public @NotNull Function<List<?>, Set<?>> listToEdnSetConverter() {
        return listToEdnSetConverter;
    }

    public @NotNull EdnOptions listToEdnSetConverter(final @NotNull Function<List<?>, Set<?>> setToEdnSetConverter) {
        this.listToEdnSetConverter = setToEdnSetConverter;
        return this;
    }

    public @NotNull Function<List<Map.Entry<Object, Object>>, Map<?, ?>> listToEdnMapConverter() {
        return listToEdnMapConverter;
    }

    public @NotNull EdnOptions listToEdnMapConverter(final @NotNull Function<List<Map.Entry<Object, Object>>, Map<?, ?>> mapToEdnMapConverter) {
        this.listToEdnMapConverter = mapToEdnMapConverter;
        return this;
    }

    public boolean allowUTFSymbols() {
        return allowUTFSymbols;
    }

    public @NotNull EdnOptions allowUTFSymbols(final boolean allowUTFSymbols) {
        this.allowUTFSymbols = allowUTFSymbols;
        return this;
    }

    public boolean allowDefinitionsAndReferences() {
        return allowDefinitionsAndReferences;
    }

    public @NotNull EdnOptions allowDefinitionsAndReferences(boolean allowDefinitionsAndReferences) {
        this.allowDefinitionsAndReferences = allowDefinitionsAndReferences;
        return this;
    }

    public @NotNull Map<String, Function<?, ?>> dispatchMacros() {
        return dispatchMacros;
    }

    public @NotNull EdnOptions dispatchMacros(final @NotNull Map<String, Function<?, ?>> dispatchMacros) {
        this.dispatchMacros = dispatchMacros;
        return this;
    }

    public int encoderSequenceElementLimit() {
        return encoderSequenceElementLimit;
    }

    public @NotNull EdnOptions encoderSequenceElementLimit(final int encoderSequenceElementLimit) {
        this.encoderSequenceElementLimit = encoderSequenceElementLimit;
        return this;
    }

    public int encoderCollectionElementLimit() {
        return encoderCollectionElementLimit;
    }

    public @NotNull EdnOptions encoderCollectionElementLimit(final int encoderCollectionElementLimit) {
        this.encoderCollectionElementLimit = encoderCollectionElementLimit;
        return this;
    }

    public int encoderMaxColumn() {
        return encoderMaxColumn;
    }

    public @NotNull EdnOptions encoderMaxColumn(final int encoderMaxColumn) {
        this.encoderMaxColumn = encoderMaxColumn;
        return this;
    }

    public @NotNull String encoderLineIndent() {
        return encoderLineIndent;
    }

    public @NotNull EdnOptions encoderLineIndent(final @NotNull String encoderLineIndent) {
        this.encoderLineIndent = encoderLineIndent;
        return this;
    }

    public boolean encoderPrettyPrint() {
        return encoderPrettyPrint;
    }

    public @NotNull EdnOptions encoderPrettyPrint(final boolean encoderPrettyPrint) {
        this.encoderPrettyPrint = encoderPrettyPrint;
        return this;
    }

    public @NotNull Map<Symbol, Object> referenceTable() {
        return referenceTable;
    }

    public @NotNull EdnOptions referenceTable(final @NotNull Map<Symbol, Object> references) {
        this.referenceTable = references;
        return this;
    }

    public boolean allowMetaData() {
        return allowMetaData;
    }

    public EdnOptions allowMetaData(final boolean allowMetaData) {
        this.allowMetaData = allowMetaData;
        return this;
    }

    public boolean allowZeroPrefix() {
        return allowZeroPrefix;
    }

    public EdnOptions allowZeroPrefix(final boolean allowZeroPrefix) {
        this.allowZeroPrefix = allowZeroPrefix;
        return this;
    }

    public boolean allowRatios() {
        return allowRatios;
    }

    public EdnOptions allowRatios(final boolean allowRatios) {
        this.allowRatios = allowRatios;
        return this;
    }
}
