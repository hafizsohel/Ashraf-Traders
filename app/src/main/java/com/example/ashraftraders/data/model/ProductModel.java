package com.example.ashraftraders.data.model;

import com.google.gson.annotations.SerializedName;

public class ProductModel {

    // long এর বদলে অবজেক্ট টাইপ Long করা হলো যেন ডিফল্ট ০ না হয়ে null থাকে
    @SerializedName("id")
    private Long id;

    @SerializedName("product_code")
    private String product_code;

    @SerializedName("product_name")
    private String productName;

    // ডাটাবেজের int8 এর সাথে সামঞ্জস্য রেখে String থেকে Long করা হলো
    @SerializedName("brand_id")
    private Long brandId;

    @SerializedName("brand_name")
    private String brandName;

    // double এর বদলে Double করা হলো যেন ডিফল্ট 0.0 না হয়ে null থাকে
    @SerializedName("purchase_price")
    private Double purchasePrice;

    @SerializedName("sale_price")
    private Double salePrice;

    // int এর বদলে Integer করা হলো যেন ডিফল্ট 0 না হয়ে null থাকে
    @SerializedName("stock")
    private Integer stock;

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "id=" + id +
                ", product_code='" + product_code + '\'' +
                ", productName='" + productName + '\'' +
                ", brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", salePrice=" + salePrice +
                ", stock=" + stock +
                '}';
    }
}