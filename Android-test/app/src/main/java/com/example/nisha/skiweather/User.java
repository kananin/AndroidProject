package com.example.nisha.skiweather;

/**
 * Created by Nisha on 11/20/2016.
 */
public class User {
    String username, password, email;

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;

    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.email="";

    }
}
