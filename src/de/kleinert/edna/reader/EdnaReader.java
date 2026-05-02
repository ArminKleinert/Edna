package de.kleinert.edna.reader;

import de.kleinert.edna.EdnaOptions;
import org.jetbrains.annotations.NotNull;

public class EdnaReader {
private final         @NotNull EdnaOptions options;
    private final    @NotNull EdnaParser parser;
         private final@NotNull CodePointIterator cpi;

    public EdnaReader(@NotNull EdnaOptions options, @NotNull EdnaParser parser, @NotNull CodePointIterator cpi) {
        this.options = options;
        this.parser = parser;
        this.cpi = cpi;
    }
}
