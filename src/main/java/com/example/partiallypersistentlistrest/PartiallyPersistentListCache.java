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

    public List<PartiallyPersistentList.Version> getVersions() {
        return list.getVersions();
    }

    public List<Integer> getData(int version) throws InvalidVersionException {
        return list.getData(version);
    }
    
    public Integer getElementByIndex(int version, int index) throws InvalidVersionException, IndexOutOfBoundsException {
        return list.getElementByIndex(version, index);
    }

    public Integer addElement(AddElementRequestData data) throws InvalidVersionException {
        return list.pushBack(data.getVersion(), data.getNewElement());
    }

    public Integer deleteElementByValue(DeleteElementRequestData data) throws InvalidVersionException, ElementDoesNotExistException {
        return list.deleteByValue(data.getVersion(), data.getOldElement());
    }

    public Integer deleteElementByIndex(int version, int index) throws InvalidVersionException, IndexOutOfBoundsException {
        return list.deleteByIndex(version, index);
    }

    public Integer updateElement(UpdateElementRequestData data) throws InvalidVersionException, ElementDoesNotExistException {
        return list.update(data.getVersion(), data.getOldElement(), data.getNewElement());
    }

    public boolean contains(int version, int element) throws InvalidVersionException {
        return list.contains(version, element);
    }
}
