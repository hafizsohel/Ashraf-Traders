package com.example.ashraftraders.data.model.customer;


import com.google.gson.annotations.SerializedName;

public class CustomerRequest {

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

    @SerializedName("email")
    private String email;
    @SerializedName("customer_type")
    private String customerType;
    @SerializedName("credit_limit")
    private Double creditLimit;
    @SerializedName("current_due")
    private Double currentDue;

    @SerializedName("previous_due")
    private Double previousDue;

    public CustomerRequest() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customer_type) {
        this.customerType = customer_type;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double credit_limit) {
        this.creditLimit = credit_limit;
    }

    public Double getCurrentDue() {
        return currentDue;
    }

    public void setCurrentDue(Double currentDue) {
        this.currentDue = currentDue;
    }

    public Double getPreviousDue() {
        return previousDue;
    }

    public void setPreviousDue(Double previousDue) {
        this.previousDue = previousDue;
    }
}