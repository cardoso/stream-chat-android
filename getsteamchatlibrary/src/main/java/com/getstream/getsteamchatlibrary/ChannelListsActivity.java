package com.getstream.getsteamchatlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ChannelListsActivity extends Activity{

    static StreamChat client = null;
    static  User me = new User();
    ChannelListAdapter channelListAdapter;

    static User you = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_lists);


        client = new StreamChat("xjtb8skbgnrr","","");


        me.id = "jon-snow";
        me.name = "Jon Snow";
        me.image = "https://bit.ly/2u9Vc0r";


//        me.id = "sergey-n";
//        me.name = "Sergey N";
//        me.image = "https://bit.ly/2Uumxti";

        client.setUser(me, Signing.JWTUserToken("v4dg6xc6kr6ygsvb2ej5j953ybjqddc9pjgvdqh6suag6hyhr2ezfctq6ez62qhq",me.id,"1","1"));

//        client.setUser(you, Signing.JWTUserToken("v4dg6xc6kr6ygsvb2ej5j953ybjqddc9pjgvdqh6suag6hyhr2ezfctq6ez62qhq",you.id,"1","1"));

        ListView list_channels = (ListView)findViewById(R.id.list_channels);
        channelListAdapter = new ChannelListAdapter(this,client.activeChannels);
        list_channels.setAdapter(channelListAdapter);
        client.queryChannels(new StreamChat.MyCallBackInterface() {
            @Override
            public void onSuccess(String result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        channelListAdapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onFailure(String error, int nCode) {

            }
        });

        list_channels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChannelListsActivity.this,MessageListActivity.class);
                String channelType = client.activeChannels.get(i).type;
                String channelid = client.activeChannels.get(i).id;
                intent.putExtra("channelType",channelType);
                intent.putExtra("channelId",channelid);
                startActivity(intent);
            }
        });

//        final Channel channel = client.channel("messaging","the-small-councli","Private Chat About the Kingdom","https://bit.ly/2F3KEoM", members,8);
//        channel.watch();

    }

    /*
    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            // into onPostExecute() but that is upto you
            channelListAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
    */
}
