package de.kleinert.edna.reader;

import de.kleinert.edna.EdnOptions;
import de.kleinert.edna.data.Symbol;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EdnaReader {
    private final @NotNull EdnOptions options;
    private final @NotNull CodePointIterator cpi;
    private final @NotNull Map<Symbol, Object> references;

    private EdnaReader(@NotNull EdnOptions options, @NotNull CodePointIterator cpi, @NotNull Map<Symbol, Object> references) {
        this.options = options;
        this.cpi = cpi;
        this.references = references;
    }
}
