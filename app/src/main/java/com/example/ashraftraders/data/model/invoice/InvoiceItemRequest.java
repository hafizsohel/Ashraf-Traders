package com.example.ashraftraders.data.model.invoice;


import com.google.gson.annotations.SerializedName;

public class InvoiceItemRequest {

    @SerializedName("product_id")
    private Long productId;

    @SerializedName("price")
    private Double price;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("discount")
    private Double discount;

    @SerializedName("subtotal")
    private Double subtotal;

    public InvoiceItemRequest() {
    }

    public InvoiceItemRequest(Long productId,
                              Double price,
                              Integer quantity,
                              Double discount,
                              Double subtotal) {

        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.subtotal = subtotal;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}