package com.example.learningapp;

public class User {
    public int id;
    public String username;
    public String email;
    public String password;
    public String phone;

    public User(int id, String username, String email, String password, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
