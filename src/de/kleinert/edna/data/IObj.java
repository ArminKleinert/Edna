package de.kleinert.edna.data;

import de.kleinert.edna.Edna;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public interface IObj {
    @Unmodifiable
    @NotNull Map<Object, Object> meta();

    @Unmodifiable
    Object obj();

    @NotNull IObj withMeta(@NotNull Map<Object, Object> newMeta);

    static <T> @NotNull IObj of(final @NotNull Object meta,
                                final T element) {
        var newMeta = formatMeta(meta);
        return new Wrapper<>(Collections.unmodifiableMap(newMeta), element);
    }

     static @NotNull Map<Object, Object> formatMeta(final @Nullable Object meta) {
        return switch (meta) {
            case null -> EdnaMap.create(List.of());
            case String v -> EdnaMap.create(List.of(Keyword.keyword("tag"), v));
            case Symbol v -> EdnaMap.create(List.of(Keyword.keyword("tag"), v));
            case Keyword v -> EdnaMap.create(List.of(v, true));
            case Map<?, ?> tempMap -> (Map<Object, Object>) tempMap;
            default -> throw new IllegalArgumentException();
        };}

    default <T> @NotNull IObj of(final T element) {
        if (element instanceof IObj)
            return (IObj) element;
        return new Wrapper<>(Collections.unmodifiableMap(Map.of()), element);
    }

    public static IObj mergeMeta(Object unformattedMeta, Object obj) {
        var meta1 = IObj.formatMeta(unformattedMeta);
        if (obj instanceof IObj) {
            var objMeta = ((IObj) obj).meta();
            var merged = new HashMap<>(objMeta);
            merged.putAll(meta1);
            return ((IObj) obj).withMeta(merged);
        }
        return IObj.of(meta1, obj);
    }

    @Unmodifiable
    record Wrapper<T>(@NotNull Map<Object, Object> meta, T obj)
            implements IObj {
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Wrapper<?>(
                    Map<Object, Object> meta1, Object element1
            )))
                return false;
            return Objects.equals(obj, element1)
                    && Objects.equals(meta, meta1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(obj);
        }

        @Override
        public @NotNull IObj withMeta(
                final @NotNull Map<Object, Object> newMeta) {
            return new Wrapper<>(Collections.unmodifiableMap(newMeta), obj);
        }

        @Override
        public @NotNull String toString() {
            return obj.toString();
        }
    }
}
