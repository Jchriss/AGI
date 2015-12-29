package com.example.jbtang.agi.dao.users;

/**
 * user info class
 * Created by jbtang on 10/2/2015.
 */
public class User {
    public final String name;
    public final String password;

    private User() {
        this.name = "";
        this.password = "";
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
