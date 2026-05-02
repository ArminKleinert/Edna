package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

@Unmodifiable
public final class EdnaSet<T>
        extends AbstractSet<T>
        implements SequencedSet<T>, IObj {
    private final @NotNull SequencedSet<T> delegate;
    private final @NotNull Map<Object, Object> meta;

    public EdnaSet(final @NotNull List<T> delegate) {
        this(null, new LinkedHashSet<>(delegate));
    }

    private EdnaSet(final @Nullable Map<Object, Object> meta, final @NotNull SequencedSet<T> delegate) {
        this.meta = meta == null ? Map.of() : meta;
        this.delegate = delegate;
    }

    @SafeVarargs
    public static <T> @NotNull EdnaSet<T> of(T... xs) {
        return new EdnaSet<>(Arrays.asList(xs));
    }

    public static <T> @NotNull EdnaSet<T> create(
            final @NotNull Iterable<T> xs) {
        if (xs instanceof EdnaSet<?>)
            return (EdnaSet<T>) xs;
        final @NotNull var temp = new ArrayList<T>();
        for (final T x : xs) {
            temp.add(x);
        }
        return new EdnaSet<>(temp);
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
    public @Unmodifiable Object obj() {
        return this;
    }

    @Override
    public @NotNull IObj withMeta(@NotNull Map<Object, Object> newMeta) {
        return new EdnaSet<>(meta, this.delegate);
    }
}
