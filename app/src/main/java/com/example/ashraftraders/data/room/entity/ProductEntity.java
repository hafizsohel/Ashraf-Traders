package com.example.ashraftraders.data.room.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class ProductEntity {

    @PrimaryKey
    private long id;
    private String productCode;
    private String productName;
    private String brandName;
    private double purchasePrice;
    private double salePrice;
    private int stock;

    @ColumnInfo(name = "created_at")
    private String createdDate;

    public ProductEntity(long id, String productCode, String productName, String brandName, double purchasePrice, double salePrice, int stock, String createdDate) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.brandName = brandName;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.stock = stock;
        this.createdDate = createdDate;
    }

    // Getters
    public long getId() { return id; }
    public String getProductCode() { return productCode; }
    public String getProductName() { return productName; }
    public String getBrandName() { return brandName; }
    public double getPurchasePrice() { return purchasePrice; }
    public int getStock() { return stock; }

    public double getSalePrice() {
        return salePrice;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}