package com.example.ashraftraders.data.model;



import com.google.gson.annotations.SerializedName;

public class SaleReturnItemModel {
    @SerializedName("invoice_item_id")
    private long invoiceItemId;
    @SerializedName("product_id")
    private long productId;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("sale_qty")
    private double saleQty;

    @SerializedName("returned_qty")
    private double returnedQty;

    @SerializedName("unit_price")
    private double unitPrice;

    // UI Only
    private double returnQty = 0;
    private double returnPrice = 0;


    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getSaleQty() {
        return saleQty;
    }

    public double getReturnedQty() {
        return returnedQty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(double returnQty) {
        this.returnQty = returnQty;
    }

    public double getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(double returnPrice) {
        this.returnPrice = returnPrice;
    }

    public double getAvailableQty() {
        return saleQty - returnedQty;
    }
    public long getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(long invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }
}