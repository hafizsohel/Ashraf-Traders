package com.example.ashraftraders.data.room;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
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
    private int stock;

    public ProductEntity(long id, String productCode, String productName, String brandName, double purchasePrice, int stock) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.brandName = brandName;
        this.purchasePrice = purchasePrice;
        this.stock = stock;
    }

    // Getters
    public long getId() { return id; }
    public String getProductCode() { return productCode; }
    public String getProductName() { return productName; }
    public String getBrandName() { return brandName; }
    public double getPurchasePrice() { return purchasePrice; }
    public int getStock() { return stock; }
}