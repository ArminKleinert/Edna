package de.kleinert.edna.reader;

import org.jetbrains.annotations.Nullable;

public sealed class EdnReaderException extends Exception {
    private final int lineIndex;
    private final int textIndex;

    public EdnReaderException(final int lineIndex, final int textIndex, final @Nullable String text, final @Nullable Throwable cause) {
        super(text, cause);
        this.lineIndex = lineIndex;
        this.textIndex = textIndex;
    }

    public EdnReaderException(final int lineIndex, final int textIndex, String text) {
        this(lineIndex, textIndex, text, null);
    }

    public EdnReaderException(final int lineIndex, final int textIndex) {
        this(lineIndex, textIndex, null, null);
    }

    public final int getLineIndex() {
        return this.lineIndex;
    }

    public final int getTextIndex() {
        return this.textIndex;
    }

    public static final class EdnClassConversionError extends EdnReaderException {
        public EdnClassConversionError(final int lineIndex, final int textIndex, final @Nullable String text, final @Nullable Throwable cause) {
            super(lineIndex, textIndex, text, cause);
        }

        public EdnClassConversionError(final int lineIndex, final int textIndex, final @Nullable String text) {
            this(lineIndex, textIndex, text, null);
        }

        public EdnClassConversionError(final int lineIndex, final int textIndex) {
            this(lineIndex, textIndex, null, null);
        }
    }
}
