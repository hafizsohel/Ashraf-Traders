package com.example.ashraftraders.data.model;

import com.google.gson.annotations.SerializedName;

public class ProductModel {

    @SerializedName("id")
    private long id;

    @SerializedName("product_code")
    private String product_code;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("brand_name")
    private String brandName;

    @SerializedName("purchase_price")
    private double purchasePrice;

    @SerializedName("stock")
    private int stock;

    public long getId() {
        return id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public String getProductName() {
        return productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public int getStock() {
        return stock;
    }
}