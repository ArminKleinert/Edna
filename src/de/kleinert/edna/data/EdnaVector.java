package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

@Unmodifiable
public final class EdnaVector<T>
        extends AbstractList<T>
        implements SequencedCollection<T>, RandomAccess, IObj {
    private final @NotNull List<T> delegate;
    private final @NotNull Map<Object, Object> meta;

    public EdnaVector(final @NotNull List<T> delegate) {
        this(null, delegate);
    }

    public EdnaVector(final @Nullable Map<Object, Object> meta, final @NotNull List<T> delegate) {
        this.meta = meta == null ? Map.of() : meta;
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
        return EdnaCollections.toStringHelper(this, '[', ']', ',');
    }

    @Override
    public @Unmodifiable @NotNull Map<Object, Object> meta() {
        return meta;
    }

    @Override
    public @Unmodifiable Object obj() {
        return this;
    }

    @Override
    public @NotNull IObj withMeta(@NotNull Map<Object, Object> newMeta) {
        return new EdnaVector<>(meta, this.delegate);
    }
}
