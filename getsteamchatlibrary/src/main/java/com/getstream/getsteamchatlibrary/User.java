package com.getstream.getsteamchatlibrary;

import com.stfalcon.chatkit.commons.models.IUser;

public class User{

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

    public String getUserId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return image;
    }

    public boolean isOnline() {
        return online;
    }

    public String getProfileUrl() {
        return image;
    }
}
