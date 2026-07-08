package com.example.ashraftraders.data.model;

import com.google.gson.annotations.SerializedName;

public class BrandModel {

    @SerializedName("id")
    private long id;

    @SerializedName("brand_name")
    private String brandName;

    public long getId() {
        return id;
    }

    public String getBrandName() {
        return brandName;
    }
}