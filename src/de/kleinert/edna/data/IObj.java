package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface IObj {
    @Unmodifiable
    @NotNull Map<Object, Object> meta();

    @Unmodifiable
    Object obj();

    @NotNull IObj withMeta(@NotNull Map<Object, Object> newMeta);

    static <T> @NotNull IObj of(final @NotNull Object meta,
                                final T element) {
        var newMeta = switch (meta) {
            case String v -> EdnaMap.create(List.of(Keyword.keyword("tag"), v));
            case Symbol v -> EdnaMap.create(List.of(Keyword.keyword("tag"), v));
            case Keyword v -> EdnaMap.create(List.of(v, true));
            case Map<?, ?> tempMap -> (Map<Object, Object>) tempMap;
            default -> throw new IllegalArgumentException();
        };
        return new Wrapper<>(newMeta, element);
    }

    default <T> @NotNull IObj of(final T element) {
        if (element instanceof IObj)
            return (IObj) element;
        return new Wrapper<>(Map.of(), element);
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
            return new Wrapper<>(newMeta, obj);
        }

        @Override
        public @NotNull String toString() {
            return obj.toString();
        }
    }
}
