package com.example.rzhdbrigada;

import java.io.Serializable;

public class UserItem implements Serializable {
    public String name;
    public String role;
    public String login;

    public UserItem(String name, String role, String login) {
        this.name = name;
        this.role = role;
        this.login = login;
    }
}