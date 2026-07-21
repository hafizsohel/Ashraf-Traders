package com.example.ashraftraders.data.model;

import com.google.gson.annotations.SerializedName;

public class InvoiceReturnModel {

    @SerializedName("id")
    private long id;

    @SerializedName("invoice_no")
    private String invoiceNo;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("invoice_date")
    private String invoiceDate;

    @SerializedName("grand_total")
    private double grandTotal;

    @SerializedName("paid_amount")
    private double paidAmount;

    @SerializedName("due_amount")
    private double dueAmount;

    public long getId() {
        return id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getDueAmount() {
        return dueAmount;
    }
}