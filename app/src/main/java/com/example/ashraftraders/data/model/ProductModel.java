package com.example.ashraftraders.data.model;

import com.google.gson.annotations.SerializedName;

public class ProductModel {

    @SerializedName("id")
    private long id;

    @SerializedName("product_code")
    private String product_code;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("brand_id")
    private String brandId;
    @SerializedName("brand_name")
    private String brandName;

    @SerializedName("purchase_price")
    private double purchasePrice;

    @SerializedName("stock")
    private int stock;

    public long getId() {
        return id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "id=" + id +
                ", product_code='" + product_code + '\'' +
                ", productName='" + productName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", stock=" + stock +
                '}';
    }
}