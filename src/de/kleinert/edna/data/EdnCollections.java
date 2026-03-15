package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public final class EdnCollections {
    public interface IObj {
        @NotNull @Unmodifiable Map<Object, Object> meta();

        @NotNull IObj withMeta(@NotNull Map<Object, Object> newMeta);

        @NotNull
        default <T> IObj of(final @NotNull Map<Object, Object> meta, final T element) {
            if (element instanceof IObj) return ((IObj) element).withMeta(meta);
            return new Wrapper<>(meta, element);
        }

        @NotNull
        default <T> IObj of(final T element) {
            if (element instanceof IObj) return (IObj) element;
            return new Wrapper<>(Map.of(), element);
        }

        @Unmodifiable
        record Wrapper<T>(@NotNull Map<Object, Object> meta, T element) implements IObj {
            @Override
            public boolean equals(Object o) {
                if (element == null) return o == null;
                return element.equals(o);
            }

            @Override
            public int hashCode() {
                return Objects.hash(element);
            }

            @Override
            public @NotNull IObj withMeta(@NotNull Map<Object, Object> newMeta) {
                return new Wrapper<>(newMeta, element);
            }

            @Override
            public String toString() {
                return element.toString();
            }
        }
    }

    @Unmodifiable
    public static final class EdnList<T> extends AbstractList<T> implements SequencedCollection<T> {
        private final @NotNull List<T> delegate;

        public EdnList(@NotNull List<T> delegate) {
            this.delegate = Collections.unmodifiableList(delegate);
        }

        @Override
        public T get(int i) {
            return delegate.get(i);
        }

        @Override
        public int size() {
            return delegate.size();
        }
    }

    @Unmodifiable
    public static final class EdnVector<T> extends AbstractList<T> implements SequencedCollection<T>, RandomAccess {
        private final @NotNull List<T> delegate;

        public EdnVector(@NotNull List<T> delegate) {
            this.delegate = Collections.unmodifiableList(delegate);
        }

        @Override
        public T get(int i) {
            return delegate.get(i);
        }

        @Override
        public int size() {
            return delegate.size();
        }
    }

    @Unmodifiable
    public static final class EdnSet<T> extends AbstractSet<T> implements SequencedSet<T> {
        private final @NotNull List<T> elements;
        private final @NotNull Set<T> delegate;

        public EdnSet(@NotNull List<T> delegate) {
            this.delegate = Set.copyOf(delegate);
            this.elements = delegate;
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
            return new EdnSet<>(elements.reversed());
        }
    }

    @Unmodifiable
    public static final class EdnMap<K, V> extends AbstractMap<K, V> implements SequencedMap<K, V> {
        private final @NotNull List<Map.Entry<K, V>> elements;
        private final @NotNull Map<K, V> delegate;

        public EdnMap(@NotNull List<Map.Entry<K, V>> delegate) {
            var temp = new HashMap<K, V>();
            for (Entry<K, V> kvEntry : delegate) {
                temp.putIfAbsent(kvEntry.getKey(), kvEntry.getValue());
            }
            this.delegate = Collections.unmodifiableMap(temp);
            this.elements = delegate;
        }

        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public @NotNull Set<Entry<K, V>> entrySet() {
            return delegate.entrySet();
        }

        @Override
        public SequencedMap<K, V> reversed() {
            return new EdnMap<>(elements.reversed());
        }
    }
}
