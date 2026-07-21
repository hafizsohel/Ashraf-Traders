package com.example.ashraftraders.data.model.customer;


import com.google.gson.annotations.SerializedName;

public class CustomerModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("father_name")
    private String fatherName;

    @SerializedName("guarantor_name")
    private String guarantorName;

    @SerializedName("guarantor_phone")
    private String guarantorPhone;

    @SerializedName("address")
    private String address;

    @SerializedName("note")
    private String note;

    public CustomerModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(String guarantorName) {
        this.guarantorName = guarantorName;
    }

    public String getGuarantorPhone() {
        return guarantorPhone;
    }

    public void setGuarantorPhone(String guarantorPhone) {
        this.guarantorPhone = guarantorPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}