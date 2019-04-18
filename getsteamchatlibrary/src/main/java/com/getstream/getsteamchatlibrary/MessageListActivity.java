package com.getstream.getsteamchatlibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.getstream.getsteamchatlibrary.messages.CustomIncomingImageMessageViewHolder;
import com.getstream.getsteamchatlibrary.messages.CustomIncomingTextMessageViewHolder;
import com.getstream.getsteamchatlibrary.messages.CustomOutcomingImageMessageViewHolder;
import com.getstream.getsteamchatlibrary.messages.CustomOutcomingTextMessageViewHolder;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;

public class MessageListActivity extends AppCompatActivity implements MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener, MessagesListAdapter.OnMessageLongClickListener<Message>{

    static MessagesListAdapter<Message> messagesAdapter;
    MessagesList messagesList;

    Channel channel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        String channelType = getIntent().getStringExtra("channelType");
        String channelId = getIntent().getStringExtra("channelId");

        Member member = new Member();
        member.user = ChannelListsActivity.me;

        ArrayList<Member> members = new ArrayList<Member>();
        members.add(member);


        channel = ChannelListsActivity.client.channel(channelType,channelId,"Private Chat About the Kingdom","https://bit.ly/2F3KEoM", members,8);

        // Message Input

        MessageInput messageInput = (MessageInput) findViewById(R.id.input);
        messagesList = (MessagesList)findViewById(R.id.messagesList);



        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message
                String message = input.toString();
                if (message.length() > 0){
                    channel.sendMessage(message);
                }
                return true;
            }
        });

        messageInput.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                //select attachments
            }
        });

        messageInput.setTypingListener(new MessageInput.TypingListener() {
            @Override
            public void onStartTyping() {

            }

            @Override
            public void onStopTyping() {

            }
        });


        initAdapter();

    }
    private void initAdapter() {

        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Picasso.with(MessageListActivity.this).load(url).into(imageView);
            }
        };

        //We can pass any data to ViewHolder with payload
        CustomIncomingTextMessageViewHolder.Payload payload = new CustomIncomingTextMessageViewHolder.Payload();
        //For example click listener
        payload.avatarClickListener = new CustomIncomingTextMessageViewHolder.OnAvatarClickListener() {
            @Override
            public void onAvatarClick() {
                Toast.makeText(MessageListActivity.this,
                        "Text message avatar clicked", Toast.LENGTH_SHORT).show();
            }
        };

        MessageHolders holdersConfig = new MessageHolders()
                .setIncomingTextConfig(
                        CustomIncomingTextMessageViewHolder.class,
                        R.layout.item_custom_incoming_text_message,
                        payload)
                .setOutcomingTextConfig(
                        CustomOutcomingTextMessageViewHolder.class,
                        R.layout.item_custom_outcoming_text_message)
                .setIncomingImageConfig(
                        CustomIncomingImageMessageViewHolder.class,
                        R.layout.item_custom_incoming_image_message)
                .setOutcomingImageConfig(
                        CustomOutcomingImageMessageViewHolder.class,
                        R.layout.item_custom_outcoming_image_message);

        messagesAdapter = new MessagesListAdapter<Message>(ChannelListsActivity.me.id, holdersConfig, imageLoader);
//        messagesAdapter.setOnMessageLongClickListener(this);
        messagesAdapter.setLoadMoreListener(this);
        messagesList.setAdapter(messagesAdapter);
        if(channel.messageLists.size() > 0)
            messagesAdapter.addToEnd(channel.messageLists,true);
    }


    @Override
    public void onLoadMore(int page, int totalItemsCount) {

    }

    @Override
    public void onSelectionChanged(int count) {

    }

    @Override
    public void onMessageLongClick(Message message) {

    }
}
