package com.example.ashraftraders.data.model;

import com.google.gson.annotations.SerializedName;

public class SaleModel {

    @SerializedName("invoice_id")
    private long invoiceId;

    @SerializedName("invoice_no")
    private String invoiceNo;

    @SerializedName("product_summary")
    private String productSummary;

    @SerializedName("qty")
    private double qty;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("total")
    private double total;

    @SerializedName("paid")
    private double paid;

    @SerializedName("due")
    private double due;

    @SerializedName("status")
    private String status;

    @SerializedName("invoice_date")
    private String invoiceDate;

    @SerializedName("phone")
    private String phone;

    public long getInvoiceId() {
        return invoiceId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public String getProductSummary() {
        return productSummary;
    }

    public double getQty() {
        return qty;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getTotal() {
        return total;
    }

    public double getPaid() {
        return paid;
    }

    public double getDue() {
        return due;
    }

    public String getStatus() {
        return status;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "SaleModel{" +
                "invoiceId=" + invoiceId +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", productSummary='" + productSummary + '\'' +
                ", qty=" + qty +
                ", customerName='" + customerName + '\'' +
                ", phone='" + phone + '\'' +
                ", total=" + total +
                ", paid=" + paid +
                ", due=" + due +
                ", status='" + status + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                '}';
    }
}