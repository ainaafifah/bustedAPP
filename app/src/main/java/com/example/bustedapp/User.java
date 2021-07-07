package com.example.bustedapp;

public class User {
    public String full_name, user_position, user_email, user_phone;

    public User(){

    }

    public User(String full_name, String user_position, String user_email, String user_phone) {
        this.full_name = full_name;
        this.user_position = user_position;
        this.user_email = user_email;
        this.user_phone = user_phone;
    }
}
