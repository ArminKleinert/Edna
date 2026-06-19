package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

/**
 *
 * All collection types in Edna are unmodifiable, meaning that all mutating methods throw exceptions.
 *
 * @param <K>
 * @param <V>
 */
@Unmodifiable
public final class EdnaMap<K, V>
        extends AbstractMap<K, V>
        implements SequencedMap<K, V>, IObj {
    private static EdnaMap<?, ?> empty = null;

    private static <K, V> EdnaMap<K, V> empty() {
        if (empty == null) empty = new EdnaMap<>(List.of());
        //noinspection unchecked
        return (EdnaMap<K, V>) empty;
    }

    private final @NotNull SequencedMap<K, V> delegate;
    private final @NotNull Map<Object, Object> meta;

    private EdnaMap(final @NotNull List<Entry<K, V>> delegate) {
        this(null, sequencedMapFromEntryList(delegate));
    }

    private static <K, V> @NotNull SequencedMap<K, V> sequencedMapFromEntryList(
            final @NotNull List<Entry<K, V>> delegate) {
        final @NotNull var temp = new LinkedHashMap<K, V>();
        for (Entry<K, V> kvEntry : delegate) {
            temp.putIfAbsent(kvEntry.getKey(), kvEntry.getValue());
        }
        return temp;
    }

    private EdnaMap(final @Nullable Map<?, ?> meta, final @NotNull SequencedMap<K, V> delegate) {
        //noinspection unchecked
        this.meta = meta == null ? Map.of() : (Map<Object, Object>) meta;
        this.delegate = Collections.unmodifiableSequencedMap(delegate);
    }


    /**
     * Create a new instance with the provided keys and values.
     *
     * @param <K> Key type.
     * @param <V> Value type.
     * @return A new instance.
     * @throws IllegalArgumentException if there are duplicate keys or if there is a key without an associated value.
     * @see #create(List)
     */
    public static <K, V> @NotNull EdnaMap<K, V> of(K k, V v) {
        return create(List.of(k, v));
    }


    /**
     * Create a new instance with the provided keys and values.
     *
     * @param k1  Key 1.
     * @param v1  Value 1.
     * @param k2  Key 2.
     * @param v2  Value 2.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return A new instance.
     * @throws IllegalArgumentException if there are duplicate keys or if there is a key without an associated value.
     * @see #create(List)
     */
    public static <K, V> @NotNull EdnaMap<K, V> of(K k1, V v1, K k2, V v2) {
        return create(List.of(k1, v1, k2, v2));
    }


    /**
     * Create a new instance with the provided keys and values.
     *
     * @param k1  Key 1.
     * @param v1  Value 1.
     * @param k2  Key 2.
     * @param v2  Value 2.
     * @param k3  Key 3.
     * @param v3  Value 3.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return A new instance.
     * @throws IllegalArgumentException if there are duplicate keys or if there is a key without an associated value.
     * @see #create(List)
     */
    public static <K, V> @NotNull EdnaMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return create(List.of(k1, v1, k2, v2, k3, v3));
    }


    /**
     * Create a new instance with the provided keys and values.
     *
     * @param k1  Key 1.
     * @param v1  Value 1.
     * @param k2  Key 2.
     * @param v2  Value 2.
     * @param k3  Key 3.
     * @param v3  Value 3.
     * @param k4  Key 4.
     * @param v4  Value 4.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return A new instance.
     * @see #create(List)
     * @throws IllegalArgumentException if there are duplicate keys or if there is a key without an associated value.
     */
    public static <K, V> @NotNull EdnaMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return create(List.of(k1, v1, k2, v2, k3, v3, k4, v4));
    }


    /**
     * Create a new instance with the provided keys and values.
     *
     * @param k1  Key 1.
     * @param v1  Value 1.
     * @param k2  Key 2.
     * @param v2  Value 2.
     * @param k3  Key 3.
     * @param v3  Value 3.
     * @param k4  Key 4.
     * @param v4  Value 4.
     * @param k5  Key 5.
     * @param v5  Value 5.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return A new instance.
     * @see #create(List)
     * @throws IllegalArgumentException if there are duplicate keys or if there is a key without an associated value.
     */
    public static <K, V> @NotNull EdnaMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return create(List.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
    }

    /**
     * Create a new instance with the provided keys and values.
     *
     * @param k1  Key 1.
     * @param v1  Value 1.
     * @param k2  Key 2.
     * @param v2  Value 2.
     * @param k3  Key 3.
     * @param v3  Value 3.
     * @param k4  Key 4.
     * @param v4  Value 4.
     * @param k5  Key 5.
     * @param v5  Value 5.
     * @param k6  Key 6.
     * @param v6  Value 6.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return A new instance.
     * @see #create(List)
     * @throws IllegalArgumentException if there are duplicate keys or if there is a key without an associated value.
     */
    public static <K, V> @NotNull EdnaMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return create(List.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6));
    }


    /**
     * Create a new instance with the provided keys and values.
     *
     * @param kvs Keys and values as a variadic argument.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return A new instance.
     * @see #create(List)
     * @throws IllegalArgumentException if there are duplicate keys or if there is a key without an associated value.
     */
    public static <K, V> @NotNull EdnaMap<K, V> of(final Object... kvs) {
        return create(Arrays.stream(kvs).toList());
    }

    /**
     * Create a new instance with the provided keys and values.
     * <pre>
     *     {@code
     *     EdnaMap.create(List.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e", 6, "f", 7, "g"))
     *     // => {1=a, 2=b, 3=c, 4=d, 5=e, 6=f, 7=g} (but as Map<Object, Object>
     *
     *     EdnaMap.<Integer, String>create(List.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e", 6, "f", 7, "g"))
     *     // => {1=a, 2=b, 3=c, 4=d, 5=e, 6=f, 7=g}
     *     }
     * </pre>
     *
     * @param kvs Keys and values as a list.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return A new instance.
     * @throws IllegalArgumentException if there are duplicate keys or if there is a key without an associated value.
     */
    public static <K, V> @NotNull EdnaMap<K, V> create(
            final @NotNull List<Object> kvs) {
        if (kvs.isEmpty())
            return EdnaMap.empty();
        if (kvs.size() % 2 != 0) {
            throw new IllegalArgumentException("Key " + kvs.getLast() + " has no associated value.");
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
        return createFromEntries(kvList);
    }

    /**
     * Create a new instance with the provided entries.
     * <pre>
     *     {@code
     *     EdnaMap.create(List.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e", 6, "f", 7, "g"))
     *     // => {1=a, 2=b, 3=c, 4=d, 5=e, 6=f, 7=g} (but as Map<Object, Object>
     *
     *     EdnaMap.<Integer, String>create(List.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e", 6, "f", 7, "g"))
     *     // => {1=a, 2=b, 3=c, 4=d, 5=e, 6=f, 7=g}
     *     }
     * </pre>
     *
     * @param kvs Keys and values as a list.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return A new instance.
     * @throws IllegalArgumentException if there are duplicate keys or if there is a key without an associated value.
     */
    public static <K, V> @NotNull EdnaMap<K, V> createFromEntries(
            final @NotNull List<Entry<K, V>> kvs) {
        return new EdnaMap<>(kvs);
    }

    /**
     * Create a new instance with the provided Map. If the input is a {@link SequencedMap}, the implementation guarantees that the order of elements is preserved when copying.
     *
     * @param kvs The Map.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return A new instanced.
     */
    public static <K, V> @NotNull EdnaMap<K, V> create(
            final @NotNull Map<K, V> kvs) {
        if (kvs instanceof EdnaMap<K, V>) return (EdnaMap<K, V>) kvs;
        if (kvs.isEmpty()) return empty();

        if (kvs instanceof SequencedMap<K, V>)
            return createFromEntries(((SequencedMap<K, V>) kvs).sequencedEntrySet().stream().toList());

        return createFromEntries(kvs.entrySet().stream().toList());
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
        return new EdnaMap<>(meta, delegate.reversed());
    }

    @Override
    public @NotNull String toString() {
        return EdnaCollections.toStringHelper(this.sequencedEntrySet(), '{', '}', ',');
    }

    @Override
    public @Unmodifiable @NotNull Map<Object, Object> meta() {
        return meta;
    }

    @Override
    public @NotNull IObj withMeta(@NotNull Map<Object, Object> newMeta) {
        return new EdnaMap<>(newMeta, this.delegate);
    }
}
