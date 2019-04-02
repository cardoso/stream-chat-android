package com.getstream.getsteamchatlibrary;


import java.util.ArrayList;

public class ClientState {

    ArrayList<User> users;

    ClientState(){

        this.users = new ArrayList<User>();
    }

    public void updateUser(User user) {
        if(user != null){
            this.users.set(Integer.valueOf(user.userId),user);
        }
    }
}
