package com.getstream.getsteamchatlibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MessageListActivity extends AppCompatActivity{

    static ListView mMessageListView;
    static MessageListAdapter mMessageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        String channelType = getIntent().getStringExtra("channelType");
        String channelId = getIntent().getStringExtra("channelId");

        final EditText edittext_chatbox = (EditText)findViewById(R.id.edittext_chatbox);

        Member member = new Member();
        member.user = ChannelListsActivity.me;

        ArrayList<Member> members = new ArrayList<Member>();
        members.add(member);


        final Channel channel = ChannelListsActivity.client.channel(channelType,channelId,"Private Chat About the Kingdom","https://bit.ly/2F3KEoM", members,8);

        Button btn_send = (Button)findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edittext_chatbox.getText().toString();
                edittext_chatbox.setText("");
                if(message.length() > 0)
                    channel.sendMessage(message);
            }
        });

//        channel.watch();

        mMessageListView = (ListView) findViewById(R.id.reyclerview_message_list);

        mMessageAdapter = new MessageListAdapter(getApplicationContext(), channel.messageLists);
        mMessageListView.setAdapter(mMessageAdapter);

    }

}
