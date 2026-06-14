package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public interface IObj {
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
