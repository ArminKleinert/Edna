package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

/**
 * Objects which implement this interface can hold metadata. The metadata is represented as a {@link Map}. Few guarantees are made about the exact type. The metadata can be assumed to be immutable.
 * <p>
 * Example:
 * <pre>
 * {@code
 *      var o = EdnaList.create(List.of(1, 2, 3, 4, 5));
 *      Map<Object, Object> meta1 = Map.of(Keyword.keyword("hasMeta"), true);
 *      Assertions.assertEquals(meta1, o.withMeta(meta1).meta());
 * }
 * </pre>
 */
public interface IObj {
    /**
     * Returns the metadata of the object. The metadata is an immutable Map.
     * @return the metadata.
     */
    @Unmodifiable
    @NotNull Map<Object, Object> meta();

    @NotNull IObj withMeta(@NotNull Map<Object, Object> newMeta);

    static @NotNull Map<Object, Object> formatMeta(final @Nullable Object meta) {
        return switch (meta) {
            case null -> EdnaMap.create(List.of());
            case String v -> EdnaMap.create(List.of(Keyword.keyword("tag"), v));
            case Symbol v -> EdnaMap.create(List.of(Keyword.keyword("tag"), v));
            case Keyword v -> EdnaMap.create(List.of(v, true));
            case Map<?, ?> tempMap -> //noinspection unchecked
                    (Map<Object, Object>) tempMap;
            default -> throw new IllegalArgumentException();
        };
    }


    static IObj mergeMeta(Object unformattedMeta, @NotNull IObj obj) {
        if (unformattedMeta instanceof Map<?, ?> && ((Map<?, ?>) unformattedMeta).isEmpty())
            return obj;

        var meta1 = IObj.formatMeta(unformattedMeta);
        var objMeta = obj.meta();
        var merged = new LinkedHashMap<>(objMeta);
        merged.putAll(meta1);
        return obj.withMeta(merged);
    }
}
