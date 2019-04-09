package com.getstream.getsteamchatlibrary;

import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Channel {

    static public String type , id;
    Boolean isTyping, initialized;
    static public String cid;
    static public StreamChat client;
    ChannelState state;
    String lastTypingEvent;
    String _data,data;


    public Channel(StreamChat client, String type, String id, String data) {

        String validTypeRe = "/^[\\w_-]+$/";
        String validIDRe = "/^[\\w_-]+$/";

        this.client = client;
        this.type = type;
        this.id = id;
        // used by the frontend, gets updated:
        this.data = data;
        // this._data is used for the requests...
//        this._data = { ...data };

        this.cid = type + ":" + id;
        // perhaps the state variable should be private
        this.state = new ChannelState(this);
        this.initialized = false;
        this.lastTypingEvent = "";
        this.isTyping = false;


        this.create();
    }


    static String _channelURL() {
        if(id == null){
            return "";
        }
        String channelURL = client.baseURL + "/channels/" + type + id;
        return channelURL;
    }


    static public void sendMessage(String message){
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("text", message);
        RequestBody formBody = formBuilder.build();

        APIManager.getInstance().post(_channelURL() + "/message", formBody, new APIManager.MyCallBackInterface() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailure(final String error, int nCode) {

            }
        });
    }

    void _initializeState(ChannelState state) {

        // immutable list of maps

        ArrayList<MessageModel> messages = new ArrayList<MessageModel>();
        messages = state.messages;

        this.state.addMessagesSorted(messages);
        this.state.online = state.online;

        // convert the arrays into objects for easier syncing...

        if(state.watchers.size() > 0) {
            for(int i = 0; i < state.watchers.size(); i++) {
                User watcher = state.watchers.get(i);
                this.state.watchers.set(Integer.parseInt(watcher.userId), watcher);
            }
        }

        if(state.members.size() > 0) {
            for(int i = 0; i < state.members.size(); i++) {
                User members = state.members.get(i);
                this.state.members.set(Integer.parseInt(members.userId), members);
            }
        }
    }

    void create() {
        this.query();
    }
    void query(){


        String queryURL = this.client.baseURL + "/channels/" + this.type;
        if(this.id.length()>0){
            queryURL += "/"+ this.id;
        }

        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("data", String.valueOf(this.data))
                .add("state", String.valueOf(true))
                .add("watch", String.valueOf(false))
                .add("state", String.valueOf(false))
                .add("presence", String.valueOf(false));
        RequestBody formBody = formBuilder.build();


        APIManager.getInstance().post(queryURL + "/query", formBody, new APIManager.MyCallBackInterface() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailure(final String error, int nCode) {

            }
        });
    }

}