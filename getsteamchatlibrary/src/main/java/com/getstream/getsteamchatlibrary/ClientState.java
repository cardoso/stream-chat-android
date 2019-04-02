package com.getstream.getsteamchatlibrary;


import java.util.ArrayList;

public class ClientState {

    ArrayList<User> users;

    public ClientState(){

        this.users = new ArrayList<User>();
    }

    public void updateUser(User user) {
        if(user != null){
            this.users.set(Integer.valueOf(user.UserId),user);
        }
    }
}
