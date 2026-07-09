package com.example.ashraftraders.data.model;

import com.google.gson.annotations.SerializedName;

public class UserModel {

    private long id;
    private String username;
    private String password;

    @SerializedName("full_name")
    private String fullName;
    private String role;
    private boolean status;
    @SerializedName("profile_image")
    private String profileImage;

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFull_name() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public boolean isStatus() {
        return status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}