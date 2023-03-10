package com.example.partiallypersistentlistrest.request_data;

public class AddElementRequestData {
    private Integer newElement;
    private Integer version;

    public AddElementRequestData() {
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setNewElement(Integer newElement) {
        this.newElement = newElement;
    }

    public Integer getVersion() {
        return version;
    }

    public Integer getNewElement() {
        return newElement;
    }
}
