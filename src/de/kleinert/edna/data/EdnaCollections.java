package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;

import java.util.*;

final class EdnaCollections {
    static <T> @NotNull String toStringHelper(
            final @NotNull Iterable<T> iterable,
            final char startDelimiter,
            final char endDelimiter,
            final char separator) {
        final @NotNull Iterator<T> it = iterable.iterator();
        final @NotNull StringBuilder sb = new StringBuilder();
        sb.append(startDelimiter);

        while (it.hasNext()) {
            final T e = it.next();
            sb.append(e == iterable ? "(this Collection)" : e);
            if (!it.hasNext()) break;
            sb.append(separator).append(' ');
        }
        return sb.append(endDelimiter).toString();
    }
}
