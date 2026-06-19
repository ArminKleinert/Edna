package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

/**
 *The Set type used by Edna. Elements are guaranteed to be ordered and the Set is unmodifiable, otherwise no guarantees are made.
 * @param <T> The generic type.
 */
@Unmodifiable
public final class EdnaSet<T>
        extends AbstractSet<T>
        implements SequencedSet<T>, IObj {
    private final @NotNull SequencedSet<T> delegate;
    private final @NotNull Map<Object, Object> meta;

    private EdnaSet(final @Nullable Map<Object, Object> meta, final @NotNull SequencedSet<T> delegate) {
        this.meta = meta == null ? Map.of() : EdnaMap.create(meta);
        this.delegate = delegate;
    }

    /**
     * Creates a new instance from variadic entries.
     *
     * @param xs  The entries.
     * @param <T> The type of entries.
     * @return A new instance.
     */
    @SafeVarargs
    public static <T> @NotNull EdnaSet<T> of(T... xs) {
        return new EdnaSet<>(null, new LinkedHashSet<>(Arrays.asList(xs)));
    }

    /**
     * Copies the contents of the input into a new instance.
     *
     * @param xs  The entries.
     * @param <T> The type of entries.
     * @return A new instance.
     */
    public static <T> @NotNull EdnaSet<T> create(
            final @NotNull Iterable<T> xs) {
        if (xs instanceof EdnaSet<?>)
            return (EdnaSet<T>) xs;
        final @NotNull var temp = new LinkedHashSet<T>();
        for (final T x : xs) {
            var alreadyPresent = temp.add(x);
            if (!alreadyPresent) {
                throw new IllegalArgumentException("Duplicate element " + x + " was already added.");
            }
        }
        return new EdnaSet<>(null, temp);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return delegate.iterator();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public @NotNull SequencedSet<T> reversed() {
        return new EdnaSet<>(meta, delegate.reversed());
    }

    @Override
    public @NotNull String toString() {
        return "#" + EdnaCollections.toStringHelper(this, '{', '}', ',');
    }

    @Override
    public @Unmodifiable @NotNull Map<Object, Object> meta() {
        return meta;
    }

    @Override
    public @NotNull IObj withMeta(@NotNull Map<Object, Object> newMeta) {
        return new EdnaSet<>(newMeta, this.delegate);
    }
}
