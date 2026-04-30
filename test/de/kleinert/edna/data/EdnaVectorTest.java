package de.kleinert.edna.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

public class EdnaVectorTest {
    @Test
    void fromSequencedCollection() {
        Assertions.assertEquals(List.of(1, 2, 3), EdnaVector.create(EdnaSet.of(1, 2, 3)));
        Assertions.assertEquals(List.of(2, 3, 1), EdnaVector.create(EdnaSet.of(2, 3, 1)));

        final var lhs = new LinkedHashSet<Integer>(32);
        lhs.addAll(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1));
        Assertions.assertEquals(lhs.stream().toList(), EdnaVector.create(lhs));
    }

    @Test
    void fromSelf() {
        final var l = EdnaVector.of(1, 2, 3, 4, 5);
        Assertions.assertSame(l, EdnaVector.create(l));
    }

    @Test
    void getSize() {
        Assertions.assertEquals(0, EdnaVector.<Integer>of().size());
        Assertions.assertEquals(5, EdnaVector.of(1, 2, 3, 4, 5).size());
        Assertions.assertEquals(0, EdnaVector.<Integer>create(List.of()).size());
        Assertions.assertEquals(5, EdnaVector.create(List.of(1, 2, 3, 4, 5)).size());
    }

    @Test
    void get() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnaVector.<Integer>of().getFirst());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnaVector.of(1, 2, 3).get(3));
        Assertions.assertEquals(5, EdnaVector.of(1, 2, 3, 4, 5).get(4));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnaVector.<Integer>create(List.of()).getFirst());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnaVector.create(List.of(1, 2, 3)).get(3));
        Assertions.assertEquals(5, EdnaVector.create(List.of(1, 2, 3, 4, 5)).get(4));
    }

    @Test
    void isEmpty() {
        Assertions.assertTrue(EdnaVector.<Integer>of().isEmpty());
        Assertions.assertFalse(EdnaVector.of(1, 2, 3, 4, 5).isEmpty());

        Assertions.assertTrue(EdnaVector.<Integer>create(List.of()).isEmpty());
        Assertions.assertFalse(EdnaVector.create(List.of(1, 2, 3, 4, 5)).isEmpty());
    }

    @Test
    void iterator() {
        Assertions.assertEquals(List.of("1", "2"), EdnaVector.of(1, 2).stream().map(Object::toString).toList());
        Assertions.assertFalse(EdnaVector.<Integer>of().iterator().hasNext());
        Assertions.assertTrue(EdnaVector.of(1, 2).iterator().hasNext());

        Assertions.assertEquals(List.of("1", "2"), EdnaVector.of(1, 2).stream().map(Object::toString).toList());
        Assertions.assertFalse(EdnaVector.<Integer>create(List.of()).iterator().hasNext());
        Assertions.assertTrue(EdnaVector.create(List.of(1, 2)).iterator().hasNext());
    }

    @Test
    void listIterator() {
        Assertions.assertTrue(EdnaVector.of(1, 2).listIterator().hasNext());
        Assertions.assertFalse(EdnaVector.of(1, 2).listIterator(2).hasNext());

        Assertions.assertTrue(EdnaVector.create(List.of(1, 2)).listIterator().hasNext());
        Assertions.assertFalse(EdnaVector.create(List.of(1, 2)).listIterator(2).hasNext());
    }

    @Test
    void subList() {
        Assertions.assertEquals(List.of(2, 3), EdnaVector.of(1, 2, 3).subList(1, 3));
        Assertions.assertEquals(List.of(2, 3), EdnaVector.create(List.of(1, 2, 3)).subList(1, 3));
    }

    @Test
    void lastIndexOf() {
        Assertions.assertEquals(-1, EdnaVector.of(1, 2, 3, 2, 1).lastIndexOf(4));
        Assertions.assertEquals(4, EdnaVector.of(1, 2, 3, 2, 1).lastIndexOf(1));

        Assertions.assertEquals(-1, EdnaVector.create(List.of(1, 2, 3, 2, 1)).lastIndexOf(4));
        Assertions.assertEquals(4, EdnaVector.create(List.of(1, 2, 3, 2, 1)).lastIndexOf(1));
    }

    @Test
    void indexOf() {
        Assertions.assertEquals(-1, EdnaVector.of(1, 2, 3, 2, 1).indexOf(4));
        Assertions.assertEquals(0, EdnaVector.of(1, 2, 3, 2, 1).indexOf(1));

        Assertions.assertEquals(-1, EdnaVector.create(List.of(1, 2, 3, 2, 1)).indexOf(4));
        Assertions.assertEquals(0, EdnaVector.create(List.of(1, 2, 3, 2, 1)).indexOf(1));
    }

    @Test
    void containsAll() {
        Assertions.assertTrue(EdnaVector.of(1, 2, 3, 2, 1).containsAll(List.<Integer>of()));
        Assertions.assertTrue(EdnaVector.of(1, 2, 3, 2, 1).containsAll(List.of(1, 3, 2)));
        Assertions.assertFalse(EdnaVector.of(1, 2, 3, 2, 1).containsAll(List.of(1, 5)));

        Assertions.assertTrue(EdnaVector.create(List.of(1, 2, 3, 2, 1)).containsAll(List.<Integer>of()));
        Assertions.assertTrue(EdnaVector.create(List.of(1, 2, 3, 2, 1)).containsAll(List.of(1, 3, 2)));
        Assertions.assertFalse(EdnaVector.create(List.of(1, 2, 3, 2, 1)).containsAll(List.of(1, 5)));
    }

    @Test
    void contains() {
        Assertions.assertTrue(EdnaVector.of(1, 2, 3, 2, 1).contains(1));
        Assertions.assertTrue(EdnaVector.of(1, 2, 3, 2, 1).contains(3));
        Assertions.assertFalse(EdnaVector.of(1, 2, 3, 2, 1).contains(4));

        Assertions.assertTrue(EdnaVector.create(List.of(1, 2, 3, 2, 1)).contains(1));
        Assertions.assertTrue(EdnaVector.create(List.of(1, 2, 3, 2, 1)).contains(3));
        Assertions.assertFalse(EdnaVector.create(List.of(1, 2, 3, 2, 1)).contains(4));
    }

    @Test
    void testEquals() {
        {
            var list = EdnaVector.of(1, 2, 3, 2, 1);
            Assertions.assertEquals(list, list);
            Assertions.assertEquals(list, EdnaVector.of(1, 2, 3, 2, 1));
            Assertions.assertEquals(list, EdnaVector.create(List.of(1, 2, 3, 2, 1)));
            Assertions.assertEquals(list, List.of(1, 2, 3, 2, 1));
            Assertions.assertNotEquals(list, List.<Integer>of());
        }
        {
            var list = EdnaVector.create(List.of(1, 2, 3, 2, 1));
            Assertions.assertEquals(list, list);
            Assertions.assertEquals(list, EdnaVector.of(1, 2, 3, 2, 1));
            Assertions.assertEquals(list, EdnaVector.create(List.of(1, 2, 3, 2, 1)));
            Assertions.assertEquals(list, List.of(1, 2, 3, 2, 1));
            Assertions.assertNotEquals(list, List.<Integer>of());
        }
    }
}
