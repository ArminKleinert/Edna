package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

@Unmodifiable
public final class EdnaMap<K, V>
        extends AbstractMap<K, V>
        implements SequencedMap<K, V> {
    private static EdnaMap empty = null;

    private static <K, V> EdnaMap<K, V> empty() {
        if (empty == null) empty = new EdnaMap<>(List.of());
        return (EdnaMap<K, V>) empty;
    }

    private final @NotNull SequencedMap<K, V> delegate;

    public EdnaMap(final @NotNull List<Entry<K, V>> delegate) {
        this(sequencedMapFromEntryList(delegate));
    }

    private static <K, V> @NotNull SequencedMap<K, V> sequencedMapFromEntryList(
            final @NotNull List<Entry<K, V>> delegate) {
        final @NotNull var temp = new LinkedHashMap<K, V>();
        for (Entry<K, V> kvEntry : delegate) {
            temp.putIfAbsent(kvEntry.getKey(), kvEntry.getValue());
        }
        return temp;
    }

    public EdnaMap(final @NotNull SequencedMap<K, V> delegate) {
        this.delegate = delegate;
    }

    public static <K, V> @NotNull EdnaMap<K, V> of(K k, V v) {
        return create(List.of(k, v));
    }

    public static <K, V> @NotNull EdnaMap<K, V> of(K k1, V v1, K k2, V v2) {
        return create(List.of(k1, v1, k2, v2));
    }

    public static <K, V> @NotNull EdnaMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return create(List.of(k1, v1, k2, v2, k3, v3));
    }

    public static <K, V> @NotNull EdnaMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return create(List.of(k1, v1, k2, v2, k3, v3, k4, v4));
    }

    public static <K, V> @NotNull EdnaMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return create(List.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
    }

    public static <K, V> @NotNull EdnaMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return create(List.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6));
    }

    public static <K, V> @NotNull EdnaMap<K, V> of(final Object... kvs) {
        return create(Arrays.stream(kvs).toList());
    }

    public static <K, V> @NotNull EdnaMap<K, V> create(
            final @NotNull List<Object> kvs) {
        if (kvs.isEmpty())
            return EdnaMap.<K, V>empty();
        if (kvs.size() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        final @NotNull Set<K> gatheredKeys = new HashSet<>();
        final @NotNull List<Entry<K, V>> kvList = new ArrayList<>();
        final @NotNull Iterator<Object> kvIter = kvs.iterator();
        while (kvIter.hasNext()) {
            final var k = (K) kvIter.next();
            if (gatheredKeys.contains(k))
                throw new IllegalArgumentException("Duplicate key " + k);
            gatheredKeys.add(k);
            final var v = (V) kvIter.next();
            kvList.add(new SimpleEntry<>(k, v));
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
