package com.example.partiallypersistentlistrest;

import com.example.partiallypersistentlistrest.exceptions.ElementDoesNotExistException;
import com.example.partiallypersistentlistrest.exceptions.InvalidVersionException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the most useful methods in {@link com.example.partiallypersistentlistrest.PartiallyPersistentList}
 * */
class PartiallyPersistentListTest {

    @Test
    void getDataTest() throws InvalidVersionException {
        List<Integer> intList = new ArrayList<>();
        PartiallyPersistentList list = new PartiallyPersistentList();
        assertThrows(InvalidVersionException.class, () -> list.getData(10));
        assertDoesNotThrow(() -> list.getData(1));

        for (int i = 0; i < 100; i++) {
            int num = new Random().nextInt();
            intList.add(num);
            list.pushBack(i+1, num);
            assertArrayEquals(intList.toArray(), list.getData(i+2).toArray());
        }
    }

    private Integer[] versionsToIDs(List<PartiallyPersistentList.Version> versionList) {
        return versionList.stream().map(PartiallyPersistentList.Version::getId).toArray(Integer[]::new);
    }

    @Test
    void getVersionsTest() throws InvalidVersionException {
        PartiallyPersistentList list = new PartiallyPersistentList();
        assertArrayEquals(new Integer[] {1}, versionsToIDs(list.getVersions()));
        list.pushBack(1, 1);
        assertArrayEquals(new Integer[] {1, 2}, versionsToIDs(list.getVersions()));
        list.pushBack(1, 1);
        assertArrayEquals(new Integer[] {1, 2, 3}, versionsToIDs(list.getVersions()));
        list.pushBack(1, 1);
        list = new PartiallyPersistentList();
        for (int i = 0; i < 100; i++) {
            int num = new Random().nextInt();
            list.pushBack(i+1, num);
            assertEquals(i+2, list.getVersions().size());
        }
    }

    @Test
    void getElementByIndex() throws InvalidVersionException {
        PartiallyPersistentList list = new PartiallyPersistentList();
        list.pushBack(1, 1);
        list.pushBack(2, 2);
        list.pushBack(3, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> list.getElementByIndex(1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.getElementByIndex(4, 3));
        assertThrows(InvalidVersionException.class, () -> list.getElementByIndex(0, 3));
        assertThrows(InvalidVersionException.class, () -> list.getElementByIndex(5, 3));
        assertEquals(1, list.getElementByIndex(2, 0));
        assertEquals(1, list.getElementByIndex(4, 0));
        assertEquals(3, list.getElementByIndex(4, 2));
    }

    @Test
    void deleteByIndexTest() throws InvalidVersionException {
        PartiallyPersistentList list = new PartiallyPersistentList();
        list.pushBack(1, 1);
        list.pushBack(2, 1);
        list.pushBack(3, 2);
        assertThrows(InvalidVersionException.class, () -> list.deleteByIndex(5, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.deleteByIndex(4, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> list.deleteByIndex(1, 0));
        assertEquals(5, list.deleteByIndex(4, 2));
        assertArrayEquals(new Integer[] {1, 1}, list.getData(5).toArray());
    }

    @Test
    void deleteByValueTest() throws InvalidVersionException, ElementDoesNotExistException {
        PartiallyPersistentList list = new PartiallyPersistentList();
        list.pushBack(1, 1);
        list.pushBack(2, 1);
        list.pushBack(3, 2);
        assertThrows(InvalidVersionException.class, () -> list.deleteByValue(5, 1));
        assertThrows(ElementDoesNotExistException.class, () -> list.deleteByValue(4, 5));
        assertThrows(ElementDoesNotExistException.class, () -> list.deleteByValue(1, 1));
        assertEquals(5, list.deleteByValue(4, 2));
        assertArrayEquals(new Integer[] {1, 1}, list.getData(5).toArray());
        assertEquals(6, list.deleteByValue(5, 1));
        assertArrayEquals(new Integer[] {1}, list.getData(6).toArray());
    }

    @Test
    void updateTest() throws InvalidVersionException, ElementDoesNotExistException {
        PartiallyPersistentList list = new PartiallyPersistentList();
        list.pushBack(1, 1);
        list.pushBack(2, 1);
        list.pushBack(3, 2);
        assertThrows(InvalidVersionException.class, () -> list.update(5, 1, 2));
        assertThrows(ElementDoesNotExistException.class, () -> list.update(1, 1, 2));
        assertEquals(5, list.update(4, 1, 3));
        assertArrayEquals(new Integer[] {3, 1, 2}, list.getData(5).toArray());
    }
}