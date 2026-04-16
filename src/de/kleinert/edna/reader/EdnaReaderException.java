package de.kleinert.edna.reader;

import org.jetbrains.annotations.Nullable;

public sealed class EdnaReaderException extends RuntimeException {
    private final int lineIndex;
    private final int textIndex;

    public EdnaReaderException(final int lineIndex,
                               final int textIndex,
                               final @Nullable String text,
                               final @Nullable Throwable cause) {
        super(text, cause);
        this.lineIndex = lineIndex;
        this.textIndex = textIndex;
    }

    public EdnaReaderException(final int lineIndex,
                               final int textIndex,
                               final @Nullable String text) {
        this(lineIndex, textIndex, text, null);
    }

    public final int getLineIndex() {
        return this.lineIndex;
    }

    public final int getTextIndex() {
        return this.textIndex;
    }

    public static final class EdnClassConversionError
            extends EdnaReaderException {
        public EdnClassConversionError(final int lineIndex,
                                       final int textIndex,
                                       final @Nullable String text,
                                       final @Nullable Throwable cause) {
            super(lineIndex, textIndex, text, cause);
        }

        public EdnClassConversionError(final int lineIndex,
                                       final int textIndex,
                                       final @Nullable String text) {
            this(lineIndex, textIndex, text, null);
        }

        public EdnClassConversionError(final int lineIndex,
                                       final int textIndex) {
            this(lineIndex, textIndex, null, null);
        }
    }
}
