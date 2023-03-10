package com.example.partiallypersistentlistrest.request_data;

public class DeleteElementRequestData {
    private Integer oldElement;
    private Integer version;

    public DeleteElementRequestData() {
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setOldElement(Integer oldElement) {
        this.oldElement = oldElement;
    }

    public Integer getVersion() {
        return version;
    }

    public int getOldElement() {
        return oldElement;
    }
}
