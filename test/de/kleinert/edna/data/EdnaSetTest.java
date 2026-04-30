package de.kleinert.edna.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.SequencedSet;
import java.util.Set;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class EdnaSetTest {
    @SafeVarargs
    private <T> SequencedSet<T> sequencedSetOf(T... xs) {
        return new LinkedHashSet<>(Arrays.stream(xs).toList());
    }

    @Test
    void fromSequencedCollection() {
        Assertions.assertEquals(Set.of(1, 2, 3), EdnaSet.create(sequencedSetOf(1, 2, 3)));
        Assertions.assertEquals(Set.of(2, 3, 1), EdnaSet.create(sequencedSetOf(2, 3, 1)));

        Assertions.assertEquals(List.of(1, 2, 3), EdnaSet.create(sequencedSetOf(1, 2, 3)).stream().toList());
        Assertions.assertEquals(List.of(2, 3, 1), EdnaSet.create(sequencedSetOf(2, 3, 1)).stream().toList());

        final var lhs = new LinkedHashSet<Integer>(32);
        final var xs = new ArrayList<>(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1));
        Collections.shuffle(xs);
        lhs.addAll(xs);
        Assertions.assertEquals(new HashSet<>(lhs), EdnaSet.create(lhs));
        Assertions.assertEquals(new TreeSet<>(lhs), EdnaSet.create(lhs));
    }

    @Test
    void fromSelf() {
        final var l = EdnaSet.of(1, 2, 3, 4, 5);
        Assertions.assertSame(l, EdnaSet.create(l));
    }

    @Test
    void getSize() {
        Assertions.assertEquals(0, EdnaSet.<Integer>of().size());
        Assertions.assertEquals(5, EdnaSet.of(1, 2, 3, 4, 5).size());
        Assertions.assertEquals(1, EdnaSet.of(1, 1, 1, 1, 1).size());

        Assertions.assertEquals(0, EdnaSet.<Integer>create(sequencedSetOf()).size());
        Assertions.assertEquals(5, EdnaSet.create(sequencedSetOf(1, 2, 3, 4, 5)).size());
        Assertions.assertEquals(1, EdnaSet.create(sequencedSetOf(1, 1, 1, 1, 1)).size());
    }

    @Test
    void isEmpty() {
        Assertions.assertTrue(EdnaSet.<Integer>of().isEmpty());
        Assertions.assertFalse(EdnaSet.of(1, 2, 3, 4, 5).isEmpty());
    }

    @Test
    void containsAll() {
        Assertions.assertTrue(EdnaSet.of(1, 2, 3).containsAll(List.<Integer>of()));
        Assertions.assertTrue(EdnaSet.of(1, 2, 3).containsAll(List.of(1, 3, 2)));
        Assertions.assertFalse(EdnaSet.of(1, 2, 3).containsAll(List.of(1, 5)));
    }

    @Test
    void contains() {
        Assertions.assertTrue(EdnaSet.of(1, 2, 3).contains(1));
        Assertions.assertTrue(EdnaSet.of(1, 2, 3).contains(3));
        Assertions.assertFalse(EdnaSet.of(1, 2, 3).contains(4));
    }

    @Test
    void iterator() {
        Assertions.assertFalse(EdnaSet.<Integer>of().iterator().hasNext());
        Assertions.assertTrue(EdnaSet.of(1, 2).iterator().hasNext());
    }

    @Test
    void testEquals() {
        final var testSet = EdnaSet.of(1, 2, 3);
        Assertions.assertEquals(testSet, testSet);
        Assertions.assertEquals(testSet, EdnaSet.of(1, 2, 3));
        Assertions.assertEquals(testSet, Set.of(1, 2, 3));
        Assertions.assertEquals(testSet, EdnaSet.of(2, 1, 3));
        Assertions.assertEquals(testSet, Set.of(2, 1, 3));
        Assertions.assertNotEquals(testSet, List.<Integer>of());
        Assertions.assertNotEquals(testSet, Set.<Integer>of());
    }
}
