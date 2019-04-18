package com.getstream.getsteamchatlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.getstream.getsteamchatlibrary.channels.CustomDialogViewHolder;
import com.getstream.getsteamchatlibrary.fixtures.DialogsFixtures;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

public class ChannelListsActivity extends Activity implements DialogsListAdapter.OnDialogClickListener<Channel>,
        DialogsListAdapter.OnDialogLongClickListener<Channel> {


    ImageLoader imageLoader;
    DialogsListAdapter<Channel> dialogsAdapter;

    static StreamChat client = null;
    static  User me = new User();
//    ChannelListAdapter channelListAdapter;

    DialogsList dialogsList;

    static User you = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_lists);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Picasso.with(ChannelListsActivity.this).load(url).into(imageView);
            }
        };


        client = new StreamChat("xjtb8skbgnrr","","");


        me.id = "jon-snow";
        me.name = "Jon Snow";
        me.image = "https://bit.ly/2u9Vc0r";


//        me.id = "sergey-n";
//        me.name = "Sergey N";
//        me.image = "https://bit.ly/2Uumxti";

        client.setUser(me, Signing.JWTUserToken("v4dg6xc6kr6ygsvb2ej5j953ybjqddc9pjgvdqh6suag6hyhr2ezfctq6ez62qhq",me.id,"1","1"));

//        client.setUser(you, Signing.JWTUserToken("v4dg6xc6kr6ygsvb2ej5j953ybjqddc9pjgvdqh6suag6hyhr2ezfctq6ez62qhq",you.id,"1","1"));


        dialogsList = (DialogsList) findViewById(R.id.channelsList);


        initAdapter();

        client.queryChannels(new StreamChat.MyCallBackInterface() {
            @Override
            public void onSuccess(String result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }

            @Override
            public void onFailure(String error, int nCode) {

            }
        });

//        final Channel channel = client.channel("messaging","the-small-councli","Private Chat About the Kingdom","https://bit.ly/2F3KEoM", members,8);
//        channel.watch();

    }
    private void initAdapter() {
        dialogsAdapter = new DialogsListAdapter<>(
                R.layout.item_custom_dialog_view_holder,
                CustomDialogViewHolder.class,
                imageLoader);

        dialogsAdapter.setItems(DialogsFixtures.getDialogs());

        dialogsAdapter.setOnDialogClickListener(this);
        dialogsAdapter.setOnDialogLongClickListener(this);

        dialogsList.setAdapter(dialogsAdapter);
    }

    @Override
    public void onDialogClick(Channel channel) {
        Toast.makeText(this, channel.getDialogName() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogLongClick(Channel dialog) {

    }
}
