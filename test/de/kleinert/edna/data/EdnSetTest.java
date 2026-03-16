package de.kleinert.edna.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import de.kleinert.edna.data.EdnCollections.EdnSet;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.SequencedSet;
import java.util.Set;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class EdnSetTest {
    @SafeVarargs
    private <T> SequencedSet<T> sequencedSetOf(T... xs) {
        return new LinkedHashSet<>(Arrays.stream(xs).toList());
    }

    @Test
    void fromSequencedCollection() {
        Assertions.assertEquals(Set.of(1, 2, 3), EdnSet.create(sequencedSetOf(1, 2, 3)));
        Assertions.assertEquals(Set.of(2, 3, 1), EdnSet.create(sequencedSetOf(2, 3, 1)));

        Assertions.assertEquals(List.of(1, 2, 3), EdnSet.create(sequencedSetOf(1, 2, 3)).stream().toList());
        Assertions.assertEquals(List.of(2, 3, 1), EdnSet.create(sequencedSetOf(2, 3, 1)).stream().toList());

        final var lhs = new LinkedHashSet<Integer>(32);
        final var xs = new ArrayList<>(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1));
        Collections.shuffle(xs);
        lhs.addAll(xs);
        Assertions.assertEquals(new HashSet<>(lhs), EdnSet.create(lhs));
        Assertions.assertEquals(new TreeSet<>(lhs), EdnSet.create(lhs));
    }

    @Test
    void fromSelf() {
        final var l = EdnSet.of(1, 2, 3, 4, 5);
        Assertions.assertSame(l, EdnSet.create(l));
    }

    @Test
    void getSize() {
        Assertions.assertEquals(0, EdnSet.<Integer>of().size());
        Assertions.assertEquals(5, EdnSet.of(1, 2, 3, 4, 5).size());
        Assertions.assertEquals(1, EdnSet.of(1, 1, 1, 1, 1).size());

        Assertions.assertEquals(0, EdnSet.<Integer>create(sequencedSetOf()).size());
        Assertions.assertEquals(5, EdnSet.create(sequencedSetOf(1, 2, 3, 4, 5)).size());
        Assertions.assertEquals(1, EdnSet.create(sequencedSetOf(1, 1, 1, 1, 1)).size());
    }

    @Test
    void isEmpty() {
        Assertions.assertTrue(EdnSet.<Integer>of().isEmpty());
        Assertions.assertFalse(EdnSet.of(1, 2, 3, 4, 5).isEmpty());
    }

    @Test
    void containsAll() {
        Assertions.assertTrue(EdnSet.of(1, 2, 3).containsAll(List.<Integer>of()));
        Assertions.assertTrue(EdnSet.of(1, 2, 3).containsAll(List.of(1, 3, 2)));
        Assertions.assertFalse(EdnSet.of(1, 2, 3).containsAll(List.of(1, 5)));
    }

    @Test
    void contains() {
        Assertions.assertTrue(EdnSet.of(1, 2, 3).contains(1));
        Assertions.assertTrue(EdnSet.of(1, 2, 3).contains(3));
        Assertions.assertFalse(EdnSet.of(1, 2, 3).contains(4));
    }

    @Test
    void iterator() {
        Assertions.assertFalse(EdnSet.<Integer>of().iterator().hasNext());
        Assertions.assertTrue(EdnSet.of(1, 2).iterator().hasNext());
    }

    @Test
    void testEquals() {
        final var testSet = EdnSet.of(1, 2, 3);
        Assertions.assertEquals(testSet, testSet);
        Assertions.assertEquals(testSet, EdnSet.of(1, 2, 3));
        Assertions.assertEquals(testSet, Set.of(1, 2, 3));
        Assertions.assertEquals(testSet, EdnSet.of(2, 1, 3));
        Assertions.assertEquals(testSet, Set.of(2, 1, 3));
        Assertions.assertNotEquals(testSet, List.<Integer>of());
        Assertions.assertNotEquals(testSet, Set.<Integer>of());
    }
}
