package de.kleinert.edna.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import de.kleinert.edna.data.EdnaCollections.EdnaList;

class EdnaListTest {
    @Test
    void fromSequencedCollection() {
        Assertions.assertEquals(List.of(1, 2, 3), EdnaList.create(EdnaCollections.EdnaSet.of(1, 2, 3)));
        Assertions.assertEquals(List.of(2, 3, 1), EdnaList.create(EdnaCollections.EdnaSet.of(2, 3, 1)));

        final var lhs = new LinkedHashSet<Integer>(32);
        lhs.addAll(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1));
        Assertions.assertEquals(lhs.stream().toList(), EdnaList.create(lhs));
    }

    @Test
    void fromSelf() {
        final var l = EdnaList.of(1, 2, 3, 4, 5);
        Assertions.assertSame(l, EdnaList.create(l));
    }

    @Test
    void getSize() {
        Assertions.assertEquals(0, EdnaList.<Integer>of().size());
        Assertions.assertEquals(5, EdnaList.of(1, 2, 3, 4, 5).size());
        Assertions.assertEquals(0, EdnaList.<Integer>create(List.of()).size());
        Assertions.assertEquals(5, EdnaList.create(List.of(1, 2, 3, 4, 5)).size());
    }

    @Test
    void get() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnaList.<Integer>of().getFirst());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnaList.of(1, 2, 3).get(3));
        Assertions.assertEquals(5, EdnaList.of(1, 2, 3, 4, 5).get(4));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnaList.<Integer>create(List.of()).getFirst());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnaList.create(List.of(1, 2, 3)).get(3));
        Assertions.assertEquals(5, EdnaList.create(List.of(1, 2, 3, 4, 5)).get(4));
    }

    @Test
    void isEmpty() {
        Assertions.assertTrue(EdnaList.<Integer>of().isEmpty());
        Assertions.assertFalse(EdnaList.of(1, 2, 3, 4, 5).isEmpty());

        Assertions.assertTrue(EdnaList.<Integer>create(List.of()).isEmpty());
        Assertions.assertFalse(EdnaList.create(List.of(1, 2, 3, 4, 5)).isEmpty());
    }

    @Test
    void iterator() {
        Assertions.assertEquals(List.of("1", "2"), EdnaList.of(1, 2).stream().map(Object::toString).toList());
        Assertions.assertFalse(EdnaList.<Integer>of().iterator().hasNext());
        Assertions.assertTrue(EdnaList.of(1, 2).iterator().hasNext());

        Assertions.assertEquals(List.of("1", "2"), EdnaList.of(1, 2).stream().map(Object::toString).toList());
        Assertions.assertFalse(EdnaList.<Integer>create(List.of()).iterator().hasNext());
        Assertions.assertTrue(EdnaList.create(List.of(1, 2)).iterator().hasNext());
    }

    @Test
    void listIterator() {
        Assertions.assertTrue(EdnaList.of(1, 2).listIterator().hasNext());
        Assertions.assertFalse(EdnaList.of(1, 2).listIterator(2).hasNext());

        Assertions.assertTrue(EdnaList.create(List.of(1, 2)).listIterator().hasNext());
        Assertions.assertFalse(EdnaList.create(List.of(1, 2)).listIterator(2).hasNext());
    }

    @Test
    void subList() {
        Assertions.assertEquals(List.of(2, 3), EdnaList.of(1, 2, 3).subList(1, 3));
        Assertions.assertEquals(List.of(2, 3), EdnaList.create(List.of(1, 2, 3)).subList(1, 3));
    }

    @Test
    void lastIndexOf() {
        Assertions.assertEquals(-1, EdnaList.of(1, 2, 3, 2, 1).lastIndexOf(4));
        Assertions.assertEquals(4, EdnaList.of(1, 2, 3, 2, 1).lastIndexOf(1));

        Assertions.assertEquals(-1, EdnaList.create(List.of(1, 2, 3, 2, 1)).lastIndexOf(4));
        Assertions.assertEquals(4, EdnaList.create(List.of(1, 2, 3, 2, 1)).lastIndexOf(1));
    }

    @Test
    void indexOf() {
        Assertions.assertEquals(-1, EdnaList.of(1, 2, 3, 2, 1).indexOf(4));
        Assertions.assertEquals(0, EdnaList.of(1, 2, 3, 2, 1).indexOf(1));

        Assertions.assertEquals(-1, EdnaList.create(List.of(1, 2, 3, 2, 1)).indexOf(4));
        Assertions.assertEquals(0, EdnaList.create(List.of(1, 2, 3, 2, 1)).indexOf(1));
    }

    @Test
    void containsAll() {
        Assertions.assertTrue(EdnaList.of(1, 2, 3, 2, 1).containsAll(List.<Integer>of()));
        Assertions.assertTrue(EdnaList.of(1, 2, 3, 2, 1).containsAll(List.of(1, 3, 2)));
        Assertions.assertFalse(EdnaList.of(1, 2, 3, 2, 1).containsAll(List.of(1, 5)));

        Assertions.assertTrue(EdnaList.create(List.of(1, 2, 3, 2, 1)).containsAll(List.<Integer>of()));
        Assertions.assertTrue(EdnaList.create(List.of(1, 2, 3, 2, 1)).containsAll(List.of(1, 3, 2)));
        Assertions.assertFalse(EdnaList.create(List.of(1, 2, 3, 2, 1)).containsAll(List.of(1, 5)));
    }

    @Test
    void contains() {
        Assertions.assertTrue(EdnaList.of(1, 2, 3, 2, 1).contains(1));
        Assertions.assertTrue(EdnaList.of(1, 2, 3, 2, 1).contains(3));
        Assertions.assertFalse(EdnaList.of(1, 2, 3, 2, 1).contains(4));

        Assertions.assertTrue(EdnaList.create(List.of(1, 2, 3, 2, 1)).contains(1));
        Assertions.assertTrue(EdnaList.create(List.of(1, 2, 3, 2, 1)).contains(3));
        Assertions.assertFalse(EdnaList.create(List.of(1, 2, 3, 2, 1)).contains(4));
    }

    @Test
    void testEquals() {
        {
            var list = EdnaList.of(1, 2, 3, 2, 1);
            Assertions.assertEquals(list, list);
            Assertions.assertEquals(list, EdnaList.of(1, 2, 3, 2, 1));
            Assertions.assertEquals(list, EdnaList.create(List.of(1, 2, 3, 2, 1)));
            Assertions.assertEquals(list, List.of(1, 2, 3, 2, 1));
            Assertions.assertNotEquals(list, List.<Integer>of());
        }
        {
            var list = EdnaList.create(List.of(1, 2, 3, 2, 1));
            Assertions.assertEquals(list, list);
            Assertions.assertEquals(list, EdnaList.of(1, 2, 3, 2, 1));
            Assertions.assertEquals(list, EdnaList.create(List.of(1, 2, 3, 2, 1)));
            Assertions.assertEquals(list, List.of(1, 2, 3, 2, 1));
            Assertions.assertNotEquals(list, List.<Integer>of());
        }
    }
}