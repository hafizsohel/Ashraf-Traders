package com.example.ashraftraders.data.model.invoice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InvoiceRequest {

    @SerializedName("customer_id")
    private Long customerId;

    @SerializedName("payment_type")
    private String paymentType;

    @SerializedName("subtotal")
    private Double subtotal;

    @SerializedName("discount")
    private Double discount;

    @SerializedName("paid_amount")
    private Double paidAmount;

    @SerializedName("due_amount")
    private Double dueAmount;

    @SerializedName("note")
    private String note;

    @SerializedName("items")
    private List<InvoiceItemRequest> items;

    public InvoiceRequest() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<InvoiceItemRequest> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemRequest> items) {
        this.items = items;
    }
}
