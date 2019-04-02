package com.getstream.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.getstream.getsteamchatlibrary.Channel;
import com.getstream.getsteamchatlibrary.Signing;
import com.getstream.getsteamchatlibrary.StreamChat;
import com.getstream.getsteamchatlibrary.User;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StreamChat client = new StreamChat("qk4nn7rpcn75","","");

        User user = new User();
        user.userId = "jon-snow";
        user.name = "Jone Snow";
        user.imguser = "https://bit.ly/2F3KEoM";

        client.setUser(user, Signing.JWTUserToken("1",user.userId,"1","1"));

        Channel channel = new Channel(client,"type",user.userId,"");

        channel.sendMessage("asdf");


//        Toast.makeText(this, JWTUserToken("1",1,"1","1"), Toast.LENGTH_SHORT).show();
//
//        Channel.sendMessage("asdf");
//
////        Toast.makeText(this, Signing.decodeBase64("asdf"), Toast.LENGTH_SHORT).show();
//
//        Toast.makeText(this, Signing.UserFromToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoid2ludGVyLWdsaXR0ZXItMSJ9.RJUTybX8VeGiu9Jm8FCIrcqoJJV4lZffZzK9fYRBI6I"), Toast.LENGTH_SHORT).show();

    }
}
