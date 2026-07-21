package com.example.ashraftraders.data.model;

import com.google.gson.annotations.SerializedName;

public class SalesSummaryModel {

    @SerializedName("total_sale")
    private Double totalSale;

    @SerializedName("total_order")
    private Integer totalOrder;

    @SerializedName("total_due")
    private Double totalDue;

    @SerializedName("total_profit")
    private Double totalProfit;

    public Double getTotalSale() {
        return totalSale;
    }

    public Integer getTotalOrder() {
        return totalOrder;
    }

    public Double getTotalDue() {
        return totalDue;
    }

    public Double getTotalProfit() {
        return totalProfit;
    }
    @Override
    public String toString() {
        return "SalesSummaryModel{" +
                "totalSale=" + totalSale +
                ", totalOrder=" + totalOrder +
                ", totalDue=" + totalDue +
                ", totalProfit=" + totalProfit +
                '}';
    }
}