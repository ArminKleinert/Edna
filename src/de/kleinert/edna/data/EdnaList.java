package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class EdnaList<T>
        extends AbstractList<T>
        implements SequencedCollection<T> {
    private final @NotNull List<T> delegate;

    public EdnaList(final @NotNull List<T> delegate) {
        this.delegate = Collections.unmodifiableList(delegate);
    }

    @SafeVarargs
    public static <T> @NotNull EdnaList<T> of(T... xs) {
        return new EdnaList<>(Arrays.asList(xs));
    }

    public static <T> @NotNull EdnaList<T> create(
            final @NotNull Iterable<T> xs) {
        if (xs instanceof EdnaList<?>)
            return (EdnaList<T>) xs;
        final @NotNull var temp = new ArrayList<T>();
        for (T x : xs) {
            temp.add(x);
        }
        return new EdnaList<>(temp);
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
    public int size() {
        return delegate.size();
    }

    @Override
    public @NotNull String toString() {
        return EdnaCollections.toStringHelper(this, '(', ')', ',');
    }
}
