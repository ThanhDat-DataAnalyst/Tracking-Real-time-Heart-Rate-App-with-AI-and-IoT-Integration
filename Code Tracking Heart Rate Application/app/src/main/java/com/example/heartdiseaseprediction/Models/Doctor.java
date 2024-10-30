package com.example.heartdiseaseprediction.Models;

public class Doctor {
    private String username;
    private String password;

    public Doctor(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters và setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
