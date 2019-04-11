package com.getstream.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.getstream.getsteamchatlibrary.Channel;
import com.getstream.getsteamchatlibrary.Member;
import com.getstream.getsteamchatlibrary.MessageModel;
import com.getstream.getsteamchatlibrary.Signing;
import com.getstream.getsteamchatlibrary.StreamChat;
import com.getstream.getsteamchatlibrary.User;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private ListView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    ArrayList<MessageModel> mMessageList = new ArrayList<MessageModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final StreamChat client = new StreamChat("xjtb8skbgnrr","","");

        User me = new User();
        me.id = "jon-snow";
        me.name = "Jon Snow";
        me.image = "https://bit.ly/2u9Vc0r";

        Member member = new Member();
        member.user = me;

        ArrayList<Member> members = new ArrayList<Member>();
        members.add(member);

        client.setUser(me, Signing.JWTUserToken("v4dg6xc6kr6ygsvb2ej5j953ybjqddc9pjgvdqh6suag6hyhr2ezfctq6ez62qhq",me.id,"1","1"));

//        client.queryChannels();

        final Channel channel = client.channel("messaging","the-small-councli","Private Chat About the Kingdom","https://bit.ly/2F3KEoM", members,8);
        channel.watch();

        final EditText edittext_chatbox = (EditText)findViewById(R.id.edittext_chatbox);

        Button btn_send = (Button)findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                channel.sendMessage(edittext_chatbox.getText().toString());
            }
        });




        mMessageRecycler = (ListView) findViewById(R.id.reyclerview_message_list);
        Button reload = (Button)findViewById(R.id.reload);

        mMessageList = channel.messageLists;
        mMessageAdapter = new MessageListAdapter(getApplicationContext(), mMessageList);
        mMessageRecycler.setAdapter(mMessageAdapter);


        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMessageList.add(client.wsConnection.mMessage);
                mMessageAdapter.notifyDataSetChanged();
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
