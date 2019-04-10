package com.getstream.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.getstream.getsteamchatlibrary.Channel;
import com.getstream.getsteamchatlibrary.Signing;
import com.getstream.getsteamchatlibrary.StreamChat;
import com.getstream.getsteamchatlibrary.User;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StreamChat client = new StreamChat("xjtb8skbgnrr","","");

        User user = new User();
        user.userId = "jon-snow";
        user.name = "Jon Snow";
        user.imguser = "https://bit.ly/2u9Vc0r";


        client.setUser(user, Signing.JWTUserToken("v4dg6xc6kr6ygsvb2ej5j953ybjqddc9pjgvdqh6suag6hyhr2ezfctq6ez62qhq",user.userId,"1","1"));

        final Channel channel = client.channel("messaging","the-small-councli","Private Chat About the Kingdom","https://bit.ly/2F3KEoM", new String[]{"jon-snow"},8);
        channel.watch();


        Button btn_send = (Button)findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                channel.sendMessage("test");
            }
        });




//        Channel channel = new Channel(client,"private",user.userId,"");

//        channel.sendMessage("asdf");


//        Toast.makeText(this, JWTUserToken("1",1,"1","1"), Toast.LENGTH_SHORT).show();
//
//        Channel.sendMessage("asdf");
//
////        Toast.makeText(this, Signing.decodeBase64("asdf"), Toast.LENGTH_SHORT).show();
//
//        Toast.makeText(this, Signing.UserFromToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoid2ludGVyLWdsaXR0ZXItMSJ9.RJUTybX8VeGiu9Jm8FCIrcqoJJV4lZffZzK9fYRBI6I"), Toast.LENGTH_SHORT).show();

    }
}
