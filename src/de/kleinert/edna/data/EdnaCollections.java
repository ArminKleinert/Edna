package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public final class EdnaCollections {
    public interface IObj {
        @Unmodifiable
        @NotNull Map<Object, Object> meta();

        @NotNull IObj withMeta(@NotNull Map<Object, Object> newMeta);

        default <T> @NotNull IObj of(final @NotNull Map<Object, Object> meta,
                                     final T element) {
            if (element instanceof IObj)
                return ((IObj) element).withMeta(meta);
            return new Wrapper<>(meta, element);
        }

        default <T> @NotNull IObj of(final T element) {
            if (element instanceof IObj)
                return (IObj) element;
            return new Wrapper<>(Map.of(), element);
        }

        @Unmodifiable
        record Wrapper<T>(@NotNull Map<Object, Object> meta, T element)
                implements IObj {
            @Override
            public boolean equals(Object o) {
                if (!(o instanceof Wrapper<?>(
                        Map<Object, Object> meta1, Object element1)))
                    return false;
                return Objects.equals(element, element1)
                        && Objects.equals(meta, meta1);
            }

            @Override
            public int hashCode() {
                return Objects.hash(element);
            }

            @Override
            public @NotNull IObj withMeta(
                    final @NotNull Map<Object, Object> newMeta) {
                return new Wrapper<>(newMeta, element);
            }

            @Override
            public @NotNull String toString() {
                return element.toString();
            }
        }
    }

    private static <T> @NotNull String toStringHelper(
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

    @Unmodifiable
    public static final class EdnaList<T>
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
            return toStringHelper(this, '(', ')', ',');
        }
    }

    @Unmodifiable
    public static final class EdnaVector<T>
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
            return toStringHelper(this, '(', ')', ',');
        }
    }

    @Unmodifiable
    public static final class EdnaSet<T>
            extends AbstractSet<T>
            implements SequencedSet<T> {
        private final @NotNull SequencedSet<T> delegate;

        public EdnaSet(final @NotNull List<T> delegate) {
            this(new LinkedHashSet<>(delegate));
        }

        private EdnaSet(final @NotNull SequencedSet<T> delegate) {
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
            return new EdnaSet<>(delegate.reversed());
        }

        @Override
        public @NotNull String toString() {
            return "#" + toStringHelper(this, '{', '}', ',');
        }
    }

    @Unmodifiable
    public static final class EdnaMap<K, V>
            extends AbstractMap<K, V>
            implements SequencedMap<K, V> {
        private final @NotNull SequencedMap<K, V> delegate;

        public EdnaMap(final @NotNull List<Map.Entry<K, V>> delegate) {
            this(sequencedMapFromEntryList(delegate));
        }

        private static <K, V> @NotNull SequencedMap<K, V> sequencedMapFromEntryList(
                final @NotNull List<Map.Entry<K, V>> delegate) {
            final @NotNull var temp = new LinkedHashMap<K, V>();
            for (Entry<K, V> kvEntry : delegate) {
                temp.putIfAbsent(kvEntry.getKey(), kvEntry.getValue());
            }
            return temp;
        }

        public EdnaMap(final @NotNull SequencedMap<K, V> delegate) {
            this.delegate = delegate;
        }

        public static <K, V> @NotNull EdnaMap<K, V> create(
                final @NotNull List<Object> kvs) {
            if (kvs.size() % 2 != 0) {
                throw new IllegalArgumentException();
            }
            final @NotNull Set<K> gatheredKeys = new HashSet<>();
            final @NotNull List<Map.Entry<K, V>> kvList = new ArrayList<>();
            final @NotNull Iterator<Object> kvIter = kvs.iterator();
            while (kvIter.hasNext()) {
                final var k = (K) kvIter.next();
                if (gatheredKeys.contains(k))
                    throw new IllegalArgumentException("Duplicate key " + k);
                gatheredKeys.add(k);
                final var v = (V) kvIter.next();
                kvList.add(new AbstractMap.SimpleEntry<>(k, v));
            }
            return new EdnaMap<>(kvList);
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
        public @NotNull SequencedMap<K, V> reversed() {
            return new EdnaMap<>(delegate.reversed());
        }

        @Override
        public @NotNull String toString() {
            final @NotNull Iterator<Entry<K, V>> it =
                    delegate.entrySet().iterator();
            final @NotNull StringBuilder sb = new StringBuilder();
            sb.append('{');

            while (it.hasNext()) {
                final @NotNull Entry<K, V> e = it.next();
                sb.append(e.getKey()).append(' ').append(e.getValue());
                if (!it.hasNext()) break;
                sb.append(',').append(' ');
            }
            return sb.append('}').toString();
        }
    }
}
