package com.example.ashraftraders.data.model;

public class User {

    private long id;
    private String username;
    private String password;
    private String full_name;
    private String role;
    private boolean status;

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
        return full_name;
    }

    public String getRole() {
        return role;
    }

    public boolean isStatus() {
        return status;
    }
}