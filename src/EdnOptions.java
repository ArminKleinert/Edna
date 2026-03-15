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
    private @NotNull SequencedMap<Class<?>, Function<Object, Map.Entry<String, ?>>> ednClassEncoders = new Edn.EdnMap<>(List.of());
    private boolean moreNumberPrefixes;
    private boolean allowNumericSuffixes = false;
    private boolean allowMoreEncoderDecoderNames = false;
    private @NotNull String encodingSequenceSeparator = ", ";
    private @NotNull Function<List<?>, List<?>> listToEdnListConverter = Edn.EdnList::new;
    private @NotNull Function<List<?>, List<?>> listToEdnVectorConverter = Edn.EdnVector::new;
    private @NotNull Function<List<?>, Set<?>> setToEdnSetConverter = Edn.EdnSet::new;
    private @NotNull Function<List<Map.Entry<Object,Object>>, Map<?,?>> mapToEdnMapConverter = Edn.EdnMap::new;
    private boolean allowUTFSymbols = false;
    private boolean allowDefinitionsAndReferences = false;
    private @NotNull Map<String, Function<?, ?>> dispatchMacros=Map.of();
    private int encoderSequenceElementLimit = 1000;
    private int encoderCollectionElementLimit = 10000;
    private int encoderMaxColumn = 80;
    private @NotNull String encoderLineIndent = "  ";
    private boolean encoderPrettyPrint = true;

    public EdnOptions() {
    }

    public boolean isAllowSchemeUTF32Codes() {
        return allowSchemeUTF32Codes;
    }

    public @NotNull EdnOptions allowSchemeUTF32Codes(final boolean allowSchemeUTF32Codes) {
        this.allowSchemeUTF32Codes = allowSchemeUTF32Codes;
        return this;
    }

    public boolean isAllowDispatchChars() {
        return allowDispatchChars;
    }

    public @NotNull EdnOptions allowDispatchChars(final boolean allowDispatchChars) {
        this.allowDispatchChars = allowDispatchChars;
        return this;
    }

    public @NotNull Map<String, Function<Object, Object>> getEdnClassDecoders() {
        return ednClassDecoders;
    }

    public @NotNull EdnOptions ednClassDecoders(final @NotNull Map<String, Function<Object, Object>> ednClassDecoders) {
        this.ednClassDecoders = ednClassDecoders;
        return this;
    }

    public @NotNull SequencedMap<Class<?>, Function<Object, Map.Entry<String, ?>>> getEdnClassEncoders() {
        return ednClassEncoders;
    }

    public @NotNull EdnOptions ednClassEncoders(final @NotNull SequencedMap<Class<?>, Function<Object, Map.Entry<String, ?>>> ednClassEncoders) {
        this.ednClassEncoders = ednClassEncoders;
        return this;
    }

    public boolean isMoreNumberPrefixes() {
        return moreNumberPrefixes;
    }

    public @NotNull EdnOptions moreNumberPrefixes(final boolean moreNumberPrefixes) {
        this.moreNumberPrefixes = moreNumberPrefixes;
        return this;
    }

    public boolean isAllowNumericSuffixes() {
        return allowNumericSuffixes;
    }

    public @NotNull EdnOptions allowNumericSuffixes(final boolean allowNumericSuffixes) {
        this.allowNumericSuffixes = allowNumericSuffixes;
        return this;
    }

    public boolean isAllowMoreEncoderDecoderNames() {
        return allowMoreEncoderDecoderNames;
    }

    public @NotNull EdnOptions allowMoreEncoderDecoderNames(final boolean allowMoreEncoderDecoderNames) {
        this.allowMoreEncoderDecoderNames = allowMoreEncoderDecoderNames;
        return this;
    }

    public @NotNull String getEncodingSequenceSeparator() {
        return encodingSequenceSeparator;
    }

    public @NotNull EdnOptions encodingSequenceSeparator(final @NotNull String encodingSequenceSeparator) {
        this.encodingSequenceSeparator = encodingSequenceSeparator;
        return this;
    }

    public @NotNull Function<List<?>, List<?>> getListToEdnListConverter() {
        return listToEdnListConverter;
    }

    public @NotNull EdnOptions listToEdnListConverter(final @NotNull Function<List<?>, List<?>> listToEdnListConverter) {
        this.listToEdnListConverter = listToEdnListConverter;
        return this;
    }

    public @NotNull Function<List<?>, List<?>> getListToEdnVectorConverter() {
        return listToEdnVectorConverter;
    }

    public @NotNull EdnOptions listToEdnVectorConverter(final @NotNull Function<List<?>, List<?>> listToEdnVectorConverter) {
        this.listToEdnVectorConverter = listToEdnVectorConverter;
        return this;
    }

    public @NotNull Function<List<?>, Set<?>> getSetToEdnSetConverter() {
        return setToEdnSetConverter;
    }

    public @NotNull EdnOptions setToEdnSetConverter(final @NotNull Function<List<?>, Set<?>> setToEdnSetConverter) {
        this.setToEdnSetConverter = setToEdnSetConverter;
        return this;
    }

    public @NotNull Function<List<Map.Entry<Object, Object>>, Map<?, ?>> getMapToEdnMapConverter() {
        return mapToEdnMapConverter;
    }

    public @NotNull EdnOptions mapToEdnMapConverter(final @NotNull Function<List<Map.Entry<Object, Object>>, Map<?, ?>> mapToEdnMapConverter) {
        this.mapToEdnMapConverter = mapToEdnMapConverter;
        return this;
    }

    public boolean isAllowUTFSymbols() {
        return allowUTFSymbols;
    }

    public @NotNull EdnOptions allowUTFSymbols(final boolean allowUTFSymbols) {
        this.allowUTFSymbols = allowUTFSymbols;
        return this;
    }

    public boolean isAllowDefinitionsAndReferences() {
        return allowDefinitionsAndReferences;
    }

    public @NotNull EdnOptions allowDefinitionsAndReferences(boolean allowDefinitionsAndReferences) {
        this.allowDefinitionsAndReferences = allowDefinitionsAndReferences;
        return this;
    }

    public @NotNull Map<String, Function<?, ?>> getDispatchMacros() {
        return dispatchMacros;
    }

    public @NotNull EdnOptions dispatchMacros(final @NotNull Map<String, Function<?, ?>> dispatchMacros) {
        this.dispatchMacros = dispatchMacros;
        return this;
    }

    public int getEncoderSequenceElementLimit() {
        return encoderSequenceElementLimit;
    }

    public @NotNull EdnOptions encoderSequenceElementLimit(final int encoderSequenceElementLimit) {
        this.encoderSequenceElementLimit = encoderSequenceElementLimit;
        return this;
    }

    public int getEncoderCollectionElementLimit() {
        return encoderCollectionElementLimit;
    }

    public @NotNull EdnOptions encoderCollectionElementLimit(final int encoderCollectionElementLimit) {
        this.encoderCollectionElementLimit = encoderCollectionElementLimit;
        return this;
    }

    public int getEncoderMaxColumn() {
        return encoderMaxColumn;
    }

    public @NotNull EdnOptions encoderMaxColumn(final int encoderMaxColumn) {
        this.encoderMaxColumn = encoderMaxColumn;
        return this;
    }

    public @NotNull String getEncoderLineIndent() {
        return encoderLineIndent;
    }

    public @NotNull EdnOptions encoderLineIndent(final @NotNull String encoderLineIndent) {
        this.encoderLineIndent = encoderLineIndent;
        return this;
    }

    public boolean isEncoderPrettyPrint() {
        return encoderPrettyPrint;
    }

    public @NotNull EdnOptions encoderPrettyPrint(final boolean encoderPrettyPrint) {
        this.encoderPrettyPrint = encoderPrettyPrint;
        return this;
    }
}
