package com.kangarooinsurance.kiapp.model;

import com.google.gson.JsonObject;

public class DefaultResponse {

    protected final String ID = "id";
    protected final String VALUE = "value";

    private String id;

    private String value;

    public DefaultResponse(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public DefaultResponse(JsonObject jsonObject) {
        this.id = jsonObject.get(ID).getAsString();
        this.value = jsonObject.get(VALUE).getAsString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
