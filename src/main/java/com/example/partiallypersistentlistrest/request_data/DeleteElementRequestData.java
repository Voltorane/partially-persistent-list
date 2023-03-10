package com.example.partiallypersistentlistrest.request_data;

public class DeleteElementRequestData extends RequestData{
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

    public Integer getOldElement() {
        return oldElement;
    }

    @Override
    public boolean isValid() {
        return oldElement != null && version != null;
    }
}
