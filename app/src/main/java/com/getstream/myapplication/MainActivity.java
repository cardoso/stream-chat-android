package com.getstream.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.getstream.getsteamchatlibrary.ChannelListsActivity;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startActivity(new Intent(MainActivity.this,ChannelListsActivity.class));

//        final StreamChat client = new StreamChat("xjtb8skbgnrr","","");
//
//        User me = new User();
//        me.id = "jon-snow";
//        me.name = "Jon Snow";
//        me.image = "https://bit.ly/2u9Vc0r";
//
//        Member member = new Member();
//        member.user = me;
//
//        ArrayList<Member> members = new ArrayList<Member>();
//        members.add(member);
//
//        client.setUser(me, Signing.JWTUserToken("v4dg6xc6kr6ygsvb2ej5j953ybjqddc9pjgvdqh6suag6hyhr2ezfctq6ez62qhq",me.id,"1","1"));
//
////        client.queryChannels();
//
//        final Channel channel = client.channel("messaging","the-small-councli","Private Chat About the Kingdom","https://bit.ly/2F3KEoM", members,8);
//        channel.watch();


    }
}
