package com.example.partiallypersistentlistrest;

import com.example.partiallypersistentlistrest.exceptions.ElementDoesNotExistException;
import com.example.partiallypersistentlistrest.exceptions.InvalidVersionException;
import com.example.partiallypersistentlistrest.request_data.AddElementRequestData;
import com.example.partiallypersistentlistrest.request_data.DeleteElementRequestData;
import com.example.partiallypersistentlistrest.request_data.UpdateElementRequestData;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Class is a wrapper around PartiallyPersistentList
 * Please see {@link com.example.partiallypersistentlistrest.PartiallyPersistentList} for true identity
 * */
@ApplicationScoped
public class PartiallyPersistentListCache {

    private PartiallyPersistentList list; // our main and only resource to cache

    @PostConstruct
    private void init() {
        list = new PartiallyPersistentList();
    }

    /**
     * Retrieves versions of this PartiallyPersistentList
     * @return list of versions of this list
     */
    public List<PartiallyPersistentList.Version> getVersions() {
        return list.getVersions();
    }

    /**
     * Get list of data of the given version
     * @param version the version of data list
     * @return data list of version
     * @throws InvalidVersionException if version id is invalid
     */
    public List<Integer> getData(int version) throws InvalidVersionException {
        return list.getData(version);
    }

    /**
     * Get data on the index in the according version
     * @param version the version of data list
     * @param index index of element in data list
     * @return element on the index
     * @throws InvalidVersionException if version id is invalid
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Integer getElementByIndex(int version, int index) throws InvalidVersionException, IndexOutOfBoundsException {
        return list.getElementByIndex(version, index);
    }

    /**
     * Add element to the end of the version data list
     * @param data data.version - version, data.newElement - element to be added
     * @return version id of the new version
     * @throws InvalidVersionException if version id is invalid
     */
    public Integer addElement(AddElementRequestData data) throws InvalidVersionException {
        return list.pushBack(data.getVersion(), data.getNewElement());
    }

    /**
     * Remove element by value from the version data list
     * @param data data.version - version, data.oldElement - element to be removed
     * @return version id of the new version
     * @throws InvalidVersionException if version id is invalid
     */
    public Integer deleteElementByValue(DeleteElementRequestData data) throws InvalidVersionException, ElementDoesNotExistException {
        return list.deleteByValue(data.getVersion(), data.getOldElement());
    }

    /**
     * Remove element by index from the version data list
     * @param version the version of data list
     * @param index index of element in data list to be deleted
     * @return version id of the new version
     * @throws InvalidVersionException if version id is invalid
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Integer deleteElementByIndex(int version, int index) throws InvalidVersionException, IndexOutOfBoundsException {
        return list.deleteByIndex(version, index);
    }

    /**
     * Update oldVal element by the newVal in the version data list
     * @param data data.version - version, data.oldElement, data.newElement - element to be updated from and to
     * @return version id of the new version
     * @throws InvalidVersionException if version id is invalid
     * @throws ElementDoesNotExistException if oldVal is not in the list
     */
    public Integer updateElement(UpdateElementRequestData data) throws InvalidVersionException, ElementDoesNotExistException {
        return list.update(data.getVersion(), data.getOldElement(), data.getNewElement());
    }

    /**
     * Returns true if this version of list contains the specified element
     * @param version version of the list to be examined
     * @param element element whose presence in this version of list is to be tested
     * @return true if this version of list contains the specified element
     * @throws InvalidVersionException if version id is invalid
     * */
    public boolean contains(int version, int element) throws InvalidVersionException {
        return list.contains(version, element);
    }
}
