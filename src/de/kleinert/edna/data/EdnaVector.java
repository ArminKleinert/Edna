package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

@Unmodifiable
public final class EdnaVector<T>
        extends AbstractList<T>
        implements SequencedCollection<T>, RandomAccess {
    private final @NotNull List<T> delegate;

    public EdnaVector(final @NotNull List<T> delegate) {
        this.delegate = Collections.unmodifiableList(delegate);
    }

    @SafeVarargs
    public static <T> @NotNull EdnaVector<T> of(T... xs) {
        return new EdnaVector<>(Arrays.asList(xs));
    }

    public static <T> @NotNull EdnaVector<T> create(
            final @NotNull Iterable<T> xs) {
        if (xs instanceof EdnaVector<?>)
            return (EdnaVector<T>) xs;
        final @NotNull var temp = new ArrayList<T>();
        for (T x : xs) {
            temp.add(x);
        }
        return new EdnaVector<>(temp);
    }

    @Override
    public T getFirst() {
        return get(0);
    }

    @Override
    public T get(int i) {
        Objects.checkIndex(i, size());
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
