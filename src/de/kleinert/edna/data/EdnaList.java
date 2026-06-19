package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.StreamSupport;

/**
 * A list type used by this library. Unlike {@link EdnaVector}, this type does not make guarantees about access complexity.
 * All collection types in Edna are unmodifiable, meaning that all mutating methods throw exceptions.
 *
 * @param <T> Generic type for entries.
 */
@Unmodifiable
public final class EdnaList<T>
        extends AbstractSequentialList<T>
        implements SequencedCollection<T>, IObj {
    private final @NotNull List<T> delegate;
    private final @NotNull Map<Object, Object> meta;

    private EdnaList(final @NotNull List<T> delegate) {
        this(null, delegate);
    }

    private EdnaList(final @Nullable Map<?, ?> meta, final @NotNull List<T> delegate) {
        //noinspection unchecked
        this.meta = meta == null ? Map.of() : (Map<Object, Object>) meta;
        this.delegate = Collections.unmodifiableList(delegate);
    }

    /**
     * Creates a new instance from variadic entries.
     *
     * @param xs  The entries.
     * @param <T> The type of entries.
     * @return A new instance.
     */
    @SafeVarargs
    public static <T> @NotNull EdnaList<T> of(T... xs) {
        return new EdnaList<>(Arrays.asList(xs));
    }

    /**
     * Copies the contents of the input into a new instance.
     *
     * @param xs  The entries.
     * @param <T> The type of entries.
     * @return A new instance.
     */
    public static <T> @NotNull EdnaList<T> create(
            final @NotNull Iterable<T> xs) {
        if (xs instanceof EdnaList<?>)
            return (EdnaList<T>) xs;
        var spliterator = Spliterators
                .spliteratorUnknownSize(xs.iterator(), Spliterator.ORDERED);
        return new EdnaList<>(
                StreamSupport
                        .stream(spliterator, false)
                        .toList());
    }

    @Override
    public T getFirst() {
        return get(0);
    }

    @Override
    public T get(int i) {
        Objects.checkIndex(i, this.size());
        return delegate.get(i);
    }

    @Override
    public @NotNull ListIterator<T> listIterator(int i) {
        return delegate.listIterator(i);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public @NotNull String toString() {
        return EdnaCollections.toStringHelper(this, '(', ')', ',');
    }

    @Override
    public @Unmodifiable @NotNull Map<Object, Object> meta() {
        return meta;
    }

    @Override
    public @NotNull IObj withMeta(@NotNull Map<Object, Object> newMeta) {
        return new EdnaList<>(newMeta, this.delegate);
    }
}
