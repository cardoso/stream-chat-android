package com.getstream.getsteamchatlibrary;

import com.stfalcon.chatkit.commons.models.IUser;

public class User implements IUser {

    public String id;
    String role;
    String created_at,updated_at,last_active;
    boolean online;
    public String name,image;

    public User(String id, String name, String image, boolean online) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.online = online;
    }

    public User() {

        id = "";
        role = "";
        created_at = "";
        updated_at = "";
        online = false;
        name = "";
        image = "";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return image;
    }

    public boolean isOnline() {
        return online;
    }

}
