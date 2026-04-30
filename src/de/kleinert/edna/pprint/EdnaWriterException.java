package de.kleinert.edna.pprint;

import org.jetbrains.annotations.Nullable;

public  class EdnaWriterException extends RuntimeException {
    public EdnaWriterException(final @Nullable Throwable cause) {
        super(cause);
    }
}
