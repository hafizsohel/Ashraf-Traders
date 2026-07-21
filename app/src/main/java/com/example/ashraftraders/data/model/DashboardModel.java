package com.example.ashraftraders.data.model;

import com.google.gson.annotations.SerializedName;

public class DashboardModel {

    @SerializedName("total_products")
    private int totalProducts;

    @SerializedName("total_brands")
    private int totalBrands;

    @SerializedName("total_purchase")
    private double totalPurchase;

    @SerializedName("low_stock")
    private int lowStock;

    public int getTotalProducts() {
        return totalProducts;
    }

    public int getTotalBrands() {
        return totalBrands;
    }

    public double getTotalPurchase() {
        return totalPurchase;
    }

    public int getLowStock() {
        return lowStock;
    }

    @Override
    public String toString() {
        return "DashboardModel{" +
                "totalProducts=" + totalProducts +
                ", totalBrands=" + totalBrands +
                ", totalPurchase=" + totalPurchase +
                ", lowStock=" + lowStock +
                '}';
    }
}