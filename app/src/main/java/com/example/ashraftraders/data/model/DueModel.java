package com.example.ashraftraders.data.model;

import com.google.gson.annotations.SerializedName;

public class DueModel {

    @SerializedName("invoice_id")
    private long invoiceId;

    @SerializedName("invoice_no")
    private String invoiceNo;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("due_amount")
    private double dueAmount;

    @SerializedName("invoice_date")
    private String invoiceDate;

    // Getter & Setter

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

}