package com.example.tripplanner;

public class SignupRequest {
    private String full_name;
    private String email;
    private String password;
    private String confirm_password;

    public SignupRequest(String full_name, String email, String password, String confirm_password) {
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.confirm_password = confirm_password;
    }
}
