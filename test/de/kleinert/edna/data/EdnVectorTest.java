package de.kleinert.edna.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import de.kleinert.edna.data.EdnCollections.EdnVector;

public class EdnVectorTest {
    @Test
    void fromSequencedCollection() {
        Assertions.assertEquals(List.of(1, 2, 3), EdnVector.create(EdnCollections.EdnSet.of(1, 2, 3)));
        Assertions.assertEquals(List.of(2, 3, 1), EdnVector.create(EdnCollections.EdnSet.of(2, 3, 1)));

        final var lhs = new LinkedHashSet<Integer>(32);
        lhs.addAll(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1));
        Assertions.assertEquals(lhs.stream().toList(), EdnVector.create(lhs));
    }

    @Test
    void fromSelf() {
        final var l = EdnVector.of(1, 2, 3, 4, 5);
        Assertions.assertSame(l, EdnVector.create(l));
    }

    @Test
    void getSize() {
        Assertions.assertEquals(0, EdnVector.<Integer>of().size());
        Assertions.assertEquals(5, EdnVector.of(1, 2, 3, 4, 5).size());
        Assertions.assertEquals(0, EdnVector.<Integer>create(List.of()).size());
        Assertions.assertEquals(5, EdnVector.create(List.of(1, 2, 3, 4, 5)).size());
    }

    @Test
    void get() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnVector.<Integer>of().getFirst());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnVector.of(1, 2, 3).get(3));
        Assertions.assertEquals(5, EdnVector.of(1, 2, 3, 4, 5).get(4));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnVector.<Integer>create(List.of()).getFirst());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> EdnVector.create(List.of(1, 2, 3)).get(3));
        Assertions.assertEquals(5, EdnVector.create(List.of(1, 2, 3, 4, 5)).get(4));
    }

    @Test
    void isEmpty() {
        Assertions.assertTrue(EdnVector.<Integer>of().isEmpty());
        Assertions.assertFalse(EdnVector.of(1, 2, 3, 4, 5).isEmpty());

        Assertions.assertTrue(EdnVector.<Integer>create(List.of()).isEmpty());
        Assertions.assertFalse(EdnVector.create(List.of(1, 2, 3, 4, 5)).isEmpty());
    }

    @Test
    void iterator() {
        Assertions.assertEquals(List.of("1", "2"), EdnVector.of(1, 2).stream().map(Object::toString).toList());
        Assertions.assertFalse(EdnVector.<Integer>of().iterator().hasNext());
        Assertions.assertTrue(EdnVector.of(1, 2).iterator().hasNext());

        Assertions.assertEquals(List.of("1", "2"), EdnVector.of(1, 2).stream().map(Object::toString).toList());
        Assertions.assertFalse(EdnVector.<Integer>create(List.of()).iterator().hasNext());
        Assertions.assertTrue(EdnVector.create(List.of(1, 2)).iterator().hasNext());
    }

    @Test
    void listIterator() {
        Assertions.assertTrue(EdnVector.of(1, 2).listIterator().hasNext());
        Assertions.assertFalse(EdnVector.of(1, 2).listIterator(2).hasNext());

        Assertions.assertTrue(EdnVector.create(List.of(1, 2)).listIterator().hasNext());
        Assertions.assertFalse(EdnVector.create(List.of(1, 2)).listIterator(2).hasNext());
    }

    @Test
    void subList() {
        Assertions.assertEquals(List.of(2, 3), EdnVector.of(1, 2, 3).subList(1, 3));
        Assertions.assertEquals(List.of(2, 3), EdnVector.create(List.of(1, 2, 3)).subList(1, 3));
    }

    @Test
    void lastIndexOf() {
        Assertions.assertEquals(-1, EdnVector.of(1, 2, 3, 2, 1).lastIndexOf(4));
        Assertions.assertEquals(4, EdnVector.of(1, 2, 3, 2, 1).lastIndexOf(1));

        Assertions.assertEquals(-1, EdnVector.create(List.of(1, 2, 3, 2, 1)).lastIndexOf(4));
        Assertions.assertEquals(4, EdnVector.create(List.of(1, 2, 3, 2, 1)).lastIndexOf(1));
    }

    @Test
    void indexOf() {
        Assertions.assertEquals(-1, EdnVector.of(1, 2, 3, 2, 1).indexOf(4));
        Assertions.assertEquals(0, EdnVector.of(1, 2, 3, 2, 1).indexOf(1));

        Assertions.assertEquals(-1, EdnVector.create(List.of(1, 2, 3, 2, 1)).indexOf(4));
        Assertions.assertEquals(0, EdnVector.create(List.of(1, 2, 3, 2, 1)).indexOf(1));
    }

    @Test
    void containsAll() {
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).containsAll(List.<Integer>of()));
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).containsAll(List.of(1, 3, 2)));
        Assertions.assertFalse(EdnVector.of(1, 2, 3, 2, 1).containsAll(List.of(1, 5)));

        Assertions.assertTrue(EdnVector.create(List.of(1, 2, 3, 2, 1)).containsAll(List.<Integer>of()));
        Assertions.assertTrue(EdnVector.create(List.of(1, 2, 3, 2, 1)).containsAll(List.of(1, 3, 2)));
        Assertions.assertFalse(EdnVector.create(List.of(1, 2, 3, 2, 1)).containsAll(List.of(1, 5)));
    }

    @Test
    void contains() {
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).contains(1));
        Assertions.assertTrue(EdnVector.of(1, 2, 3, 2, 1).contains(3));
        Assertions.assertFalse(EdnVector.of(1, 2, 3, 2, 1).contains(4));

        Assertions.assertTrue(EdnVector.create(List.of(1, 2, 3, 2, 1)).contains(1));
        Assertions.assertTrue(EdnVector.create(List.of(1, 2, 3, 2, 1)).contains(3));
        Assertions.assertFalse(EdnVector.create(List.of(1, 2, 3, 2, 1)).contains(4));
    }

    @Test
    void testEquals() {
        {
            var list = EdnVector.of(1, 2, 3, 2, 1);
            Assertions.assertEquals(list, list);
            Assertions.assertEquals(list, EdnVector.of(1, 2, 3, 2, 1));
            Assertions.assertEquals(list, EdnVector.create(List.of(1, 2, 3, 2, 1)));
            Assertions.assertEquals(list, List.of(1, 2, 3, 2, 1));
            Assertions.assertNotEquals(list, List.<Integer>of());
        }
        {
            var list = EdnVector.create(List.of(1, 2, 3, 2, 1));
            Assertions.assertEquals(list, list);
            Assertions.assertEquals(list, EdnVector.of(1, 2, 3, 2, 1));
            Assertions.assertEquals(list, EdnVector.create(List.of(1, 2, 3, 2, 1)));
            Assertions.assertEquals(list, List.of(1, 2, 3, 2, 1));
            Assertions.assertNotEquals(list, List.<Integer>of());
        }
    }
}
