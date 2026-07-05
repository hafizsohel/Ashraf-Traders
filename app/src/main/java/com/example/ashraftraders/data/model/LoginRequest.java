package com.example.ashraftraders.data.model;

public class LoginRequest {

    private final String p_username;
    private final String p_password;

    public LoginRequest(String username, String password) {
        this.p_username = username;
        this.p_password = password;
    }

    public String getP_username() {
        return p_username;
    }

    public String getP_password() {
        return p_password;
    }
}