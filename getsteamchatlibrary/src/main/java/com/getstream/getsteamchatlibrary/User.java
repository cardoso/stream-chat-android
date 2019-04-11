package com.getstream.getsteamchatlibrary;

public class User {

    public String id;
    String role;
    String created_at,updated_at,last_active;
    boolean online;
    public String name,image;

    public User() {

        id = "";
        role = "";
        created_at = "";
        updated_at = "";
        online = false;
        name = "";
        image = "";
    }

}
