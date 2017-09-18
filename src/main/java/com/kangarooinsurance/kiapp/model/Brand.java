package com.kangarooinsurance.kiapp.model;

import com.google.gson.JsonObject;

public class Brand extends DefaultResponse{


    public Brand(String id, String value) {
        super(id, value);
    }

    public Brand(JsonObject jsonObject) {
        super(jsonObject);
    }
}
