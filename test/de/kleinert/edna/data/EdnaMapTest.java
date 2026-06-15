package de.kleinert.edna.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class EdnaMapTest {
    @Test
    void equalityToEmptyMap() {
        var ednaMap = EdnaMap.of();
        var normalMap = Map.of();
        Assertions.assertEquals(normalMap, ednaMap);
        Assertions.assertEquals(ednaMap, normalMap);
    }

    @Test
    void equalityToMap() {
        var ednaMap = EdnaMap.create(List.of(Map.entry(1, 2), Map.entry(3, 4)));
        var normalMap = Map.of(1, 2, 3, 4);
        Assertions.assertEquals(normalMap, ednaMap);
        Assertions.assertEquals(ednaMap, normalMap);
    }

    @Test
    void ofFixed() {
        var normalMap = new LinkedHashMap<>();
        Assertions.assertEquals(normalMap, EdnaMap.of());

        normalMap.put(1, 4);
        Assertions.assertEquals(normalMap, EdnaMap.of(1, 4));

        normalMap.put(2, 4);
        Assertions.assertEquals(normalMap, EdnaMap.of(1, 4, 2, 4));

        normalMap.put(3, 4);
        Assertions.assertEquals(normalMap, EdnaMap.of(1, 4, 2, 4, 3, 4));

        normalMap.put(4, 4);
        Assertions.assertEquals(normalMap, EdnaMap.of(1, 4, 2, 4, 3, 4, 4, 4));

        normalMap.put(5, 4);
        Assertions.assertEquals(normalMap, EdnaMap.of(1, 4, 2, 4, 3, 4, 4, 4, 5, 4));

        normalMap.put(6, 4);
        Assertions.assertEquals(normalMap, EdnaMap.of(1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4));

        normalMap.put(7, 4);
        Assertions.assertEquals(normalMap, EdnaMap.of(1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4, 7, 4));
    }

    @Test
    void ofVariadic() {
        Assertions.assertEquals(Map.of(), EdnaMap.of());

        var normalMap = new LinkedHashMap<>(Map.of(1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4));
        normalMap.put(7, 4);

        Assertions.assertEquals(normalMap, EdnaMap.of(1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4, 7, 4));
        Assertions.assertEquals(normalMap, EdnaMap.of(Arrays.asList(1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4, 7, 4).toArray()));
    }

    @Test
    void createErrorUneven() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> EdnaMap.create(List.of(1)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> EdnaMap.create(List.of(1, 2, 3, 4, 5, 6, 7)));
    }

    @Test
    void createErrorDuplicateKey() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> EdnaMap.create(List.of(1, 2, 1, 3)));
    }

    @Test
    void create() {
        Assertions.assertEquals(
                EdnaMap.of(),
                EdnaMap.create(List.of()));

        Assertions.assertEquals(
                EdnaMap.of(1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4, 7, 4),
                EdnaMap.create(List.of(1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4, 7, 4)));
    }

    @Test
    void createFromEntries() {
        Assertions.assertEquals(
                EdnaMap.of(),
                EdnaMap.createFromEntries(List.of()));

        Assertions.assertEquals(
                EdnaMap.of(1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4, 7, 4),
                EdnaMap.createFromEntries(List.of(
                        Map.entry(1, 4), Map.entry(2, 4), Map.entry(3, 4),
                        Map.entry(4, 4), Map.entry(5, 4), Map.entry(6, 4),
                        Map.entry(7, 4)))
        );
    }

    @Test
    void createFromSequencedMap() {
        Assertions.assertEquals(
                EdnaMap.of(),
                EdnaMap.createFromEntries(List.of()));

        Assertions.assertEquals(
                EdnaMap.of(1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4, 7, 4),
                EdnaMap.create(new LinkedHashMap<>(Map.of(
                        1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4, 7, 4)))
        );
    }

    @Test
    void createFromMap() {
        Assertions.assertEquals(
                EdnaMap.of(),
                EdnaMap.createFromEntries(List.of()));

        Assertions.assertEquals(
                EdnaMap.of(1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4, 7, 4),
                EdnaMap.create(new LinkedHashMap<>(Map.of(
                        1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 4, 7, 4)))
        );
    }

    @Test
    void entrySet() {
        Assertions.assertEquals(
                Map.of(1, 4, 2, 4, 3, 4, 4, 4).entrySet(),
                EdnaMap.of(1, 4, 2, 4, 3, 4, 4, 4).entrySet());
    }

    @Test
    void reversed() {
        Assertions.assertEquals(
                Map.entry(4, 4),
                EdnaMap.of(1, 4, 2, 4, 3, 4, 4, 4).reversed().firstEntry());
    }

    @Test
    void meta() {
        var o = EdnaMap.of(1, 2, 3, 4, 5, 6);
        Assertions.assertTrue(o.meta().isEmpty());

        Map<Object, Object> meta1 = Map.of(Keyword.keyword("hasMeta"), true);
        Assertions.assertEquals(meta1, o.withMeta(meta1).meta());

        Map<Object, Object> meta2 = Map.of(Keyword.keyword("hasMeta1"), true);
        Assertions.assertEquals(meta2, o.withMeta(meta2).meta());
    }

    @Test
    void withMeta() {
        var o = EdnaMap.of(1, 2, 3, 4, 5, 6);
        Assertions.assertTrue(o.meta().isEmpty());

        Map<Object, Object> meta1 = Map.of(Keyword.keyword("hasMeta"), true);
        Assertions.assertEquals(meta1, o.withMeta(meta1).meta());

        Map<Object, Object> meta2 = Map.of(Keyword.keyword("hasMeta1"), true);
        Assertions.assertEquals(meta2, o.withMeta(meta2).meta());
    }
}