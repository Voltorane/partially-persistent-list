package com.example.partiallypersistentlistrest;

import com.example.partiallypersistentlistrest.exceptions.ElementDoesNotExistException;
import com.example.partiallypersistentlistrest.exceptions.InvalidVersionException;

import java.util.ArrayList;
import java.util.List;

public class PartiallyPersistentList {
    public static class Version {
        private final int id;
        private final List<Integer> data;

        public Version(List<Integer> dataList, int versionId) {
            data = new ArrayList<>(dataList);
            id = versionId;
        }

        public int getId() {
            return id;
        }

        public List<Integer> getData() {
            return data;
        }

        @Override
        public String toString() {
            return "" + id;
        }
    }

    public int versionId = 0;

    private final List<Version> versions;
    private List<Integer> currentData;

    public PartiallyPersistentList() {
        versions = new ArrayList<>();
        currentData = new ArrayList<>();
        versions.add(new Version(currentData, ++versionId));
    }

    /**
     * Get list of data of the given version
     * @param version the version of data list
     * @return data list of version
     * @throws InvalidVersionException if version id is invalid
     */
    public List<Integer> getData(int version) throws InvalidVersionException {
        if (version < 1 || version > versionId || version > versions.size()) {
            throw new InvalidVersionException("Invalid version " + version + "! The latest one is " + versionId);
        }
        return versions.get(version - 1).data;
    }

    /**
     * Get data on the index in the according version
     * @param version the version of data list
     * @param index index of element in data list
     * @return element on the index
     * @throws InvalidVersionException if version id is invalid
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Integer getElementByIndex(int version, int index) throws IndexOutOfBoundsException, InvalidVersionException {
        List<Integer> l = getData(version);
        if (index >= l.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for version " + version + "" +
                    " with size " + l.size() + "!");
        }
        return l.get(index);
    }

    /**
     * Saves new version with the currentData list.
     * Adds new version to the version list.
     * @return version id of created version
     */
    private int saveVersion() {
        Version v = new Version(currentData, ++versionId);
        versions.add(v);
        return v.id;
    }

    /**
     * Retrieves versions of this PartiallyPersistentList
     * @return list of versions of this list
     */
    public List<Version> getVersions() {
        return versions;
    }

    /**
     * Add element to the end of the version data list
     * @param version the version of data list
     * @param element element to be added to the data list
     * @return version id of the new version
     * @throws InvalidVersionException if version id is invalid
     */
    public Integer pushBack(int version, int element) throws InvalidVersionException {
        List<Integer> l = getData(version);
        currentData = new ArrayList<>(l);
        currentData.add(element);
        return saveVersion();
    }

    /**
     * Remove element by value from the version data list
     * @param version the version of data list
     * @param element value of element in data list to be deleted
     * @return version id of the new version
     * @throws InvalidVersionException if version id is invalid
     */
    public Integer deleteByValue(int version, int element) throws InvalidVersionException, ElementDoesNotExistException {
        List<Integer> l = getData(version);
        if (!l.contains(element)) {
            throw new ElementDoesNotExistException("Element with value: " + element
                    + " is not present in version " + version);
        }
        currentData = new ArrayList<>(l);
        currentData.remove((Integer) element);
        return saveVersion();
    }

    /**
     * Remove element by index from the version data list
     * @param version the version of data list
     * @param index index of element in data list to be deleted
     * @return version id of the new version
     * @throws InvalidVersionException if version id is invalid
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Integer deleteByIndex(int version, int index) throws IndexOutOfBoundsException, InvalidVersionException {
        List<Integer> l = getData(version);
        currentData = new ArrayList<>(l);
        currentData.remove(index);
        return saveVersion();
    }

    /**
     * Update oldVal element by the newVal in the version data list
     * @param version the version of data list
     * @param oldVal old value of element in data list to be updated
     * @param newVal new value of element in data list
     * @return version id of the new version
     * @throws InvalidVersionException if version id is invalid
     * @throws ElementDoesNotExistException if oldVal is not in the list
     */
    public Integer update(int version, int oldVal, int newVal) throws InvalidVersionException, ElementDoesNotExistException {
        List<Integer> l = getData(version);
        currentData = new ArrayList<>(l);
        int index = currentData.indexOf(oldVal);
        try {
            currentData.set(index, newVal);
        } catch (IndexOutOfBoundsException e) {
            throw new ElementDoesNotExistException("Element with value: " + oldVal
                    + " is not present in version " + version);
        }
        return saveVersion();
    }

    /**
     * Returns true if this version of list contains the specified element
     * @param version version of the list to be examined
     * @param element element whose presence in this version of list is to be tested
     * @return true if this version of list contains the specified element
     * @throws InvalidVersionException if version id is invalid
     * */
    public boolean contains(int version, int element) throws InvalidVersionException {
        return getData(version).contains(element);
    }
}
