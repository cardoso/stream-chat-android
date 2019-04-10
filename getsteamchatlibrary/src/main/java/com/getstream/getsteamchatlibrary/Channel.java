package com.getstream.getsteamchatlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Channel {

    static public String type , id;
    Boolean isTyping, initialized;
    static public String cid;
    static public StreamChat client;
    ChannelState state;
    String lastTypingEvent;
    String _data,data;

    String name,image;
    String[] members = {};
    int session;



    public Channel(StreamChat client, String type, String id, String name,String image,String[] members, int session) {

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


        this.name = name;
        this.image = image;
        this.members = members;
        this.session = session;

    }


    static String _channelURL() {
        if(id == null){
            return "";
        }
        String channelURL = client.baseURL + "/channels/" + type + "/" + id;
        return channelURL;
    }


    static public void sendMessage(String message){

        String jsonData="";
        try {

            jsonData = new JSONObject().put("message", new JSONObject()
                    .put("text", message))
                    .toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.post(_channelURL() + "/message" + "?api_key=" + client.key,jsonData);

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

    public void create() {
        this.query();
    }
    void query(){


        String queryURL = this.client.baseURL + "/channels/" + this.type;
        if(this.id.length()>0){
            queryURL += "/"+ this.id;
        }

        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("data", String.valueOf(this.data))
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

    public void watch(){

        String jsonData="";
        try {

            JSONArray memArray = new JSONArray();

            for(int i=0;i<this.members.length;i++){
                memArray.put(members[i]);
            }


            jsonData = new JSONObject().put("data", new JSONObject()
                    .put("name", this.name)
                    .put("image", this.image)
                    .put("members",memArray)
                    .put("session",this.session)
                    .put("state",true))
                    .toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonData);



        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(this.client.baseURL + "/channels/" + type + "/" + id + "/query?api_key=" + this.client.key)
                .post(body)
                .addHeader("Authorization", this.client.userToken)
                .addHeader("Content-Type","application/json")//Notice this request has header if you don't need to send a header just erase this part
                .addHeader("Stream-Auth-Type","jwt")
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }

        });

    }

}