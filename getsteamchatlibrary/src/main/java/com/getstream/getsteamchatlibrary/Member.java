package com.getstream.getsteamchatlibrary;

public class Member {


    public User user;
    String role,created_at,updated_at;

    public Member() {

        user = new User();
        role = "";
        created_at = "";
        updated_at = "";
    }

    public String getProfileUrl() {
        return user.image;
    }

    public String getUserId() {
        return user.id;
    }

    public String getNickname() {
        return user.name;
    }
}
