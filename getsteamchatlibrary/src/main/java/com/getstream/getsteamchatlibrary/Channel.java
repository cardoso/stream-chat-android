package com.getstream.getsteamchatlibrary;

import com.stfalcon.chatkit.commons.models.IDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Channel{

    String type , id,cid;
    String last_message_at;
    User create_by;
    boolean frozen;
    int member_count;
    String name,image;
    int session;

    Boolean isTyping, initialized;
    static public StreamChat client;
    ChannelState state;
    String lastTypingEvent;

    int unreadCount;

    Message lastMessage = new Message();

    Config config = new Config();



    public ArrayList<Message> messageLists = new ArrayList<Message>();
    public ArrayList<Member> members = new ArrayList<Member>();



    public Channel(){

        this.client = null;
        this.type = "";
        this.id = "";


        this.cid = type + ":" + id;
        // perhaps the state variable should be private
        this.state = new ChannelState(this);
        this.initialized = false;
        this.lastTypingEvent = "";
        this.isTyping = false;


        this.name = "";
        this.image = "";
        this.session = 0;
        create_by = new User();
        this.unreadCount = 0;
    }


    public Channel(StreamChat client, String type, String id, String name,String image,ArrayList<Member> members, int session) {

        this.client = client;
        this.type = type;
        this.id = id;


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
        create_by = new User();
        this.unreadCount = 0;
    }


    String _channelURL() {
        if(id == null){
            return "";
        }
        String channelURL = client.baseURL + "/channels/" + type + "/" + id;
        return channelURL;
    }


    public void sendMessage(String message){

        String jsonData="";
        try {

            jsonData = new JSONObject().put("message", new JSONObject()
                    .put("text", message))
                    .toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        ChannelListsActivity.client.post(_channelURL() + "/message" + "?api_key=" + client.key,jsonData);

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
                this.state.watchers.set(Integer.parseInt(watcher.id), watcher);
            }
        }

        if(state.members.size() > 0) {
            for(int i = 0; i < state.members.size(); i++) {
                User members = state.members.get(i);
                this.state.members.set(Integer.parseInt(members.id), members);
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
                .add("data", String.valueOf(""))
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


            for(int i=0;i<this.members.size();i++){
                memArray.put(members.get(i).user.id);
            }


            jsonData = new JSONObject().put("data", new JSONObject()
                    .put("name", this.name)
                    .put("image", this.image)
                    .put("members",memArray)
                    .put("session",this.session))
                    .put("state",true)
                    .toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonData);



        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
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

                if(response.code() == 200){
                    String jsonData = response.body().string();
                    try {
                        //JsonData
                        JSONObject jsonObj = new JSONObject(jsonData);


                        //Channel

                        JSONObject channelObject = jsonObj.getJSONObject("channel");

                        id = channelObject.getString("id");
                        type = channelObject.getString("type");
                        cid = channelObject.getString("cid");
                        last_message_at = channelObject.getString("last_message_at");

                        JSONObject createby = channelObject.getJSONObject("created_by");



                        create_by.id = createby.getString("id");
                        create_by.role = createby.getString("role");
                        create_by.created_at = createby.getString("created_at");
                        create_by.updated_at = createby.getString("updated_at");
                        create_by.last_active = createby.getString("last_active");
                        create_by.online = createby.getBoolean("online");
                        create_by.name = createby.getString("name");
                        create_by.image = createby.getString("image");

                        frozen = channelObject.getBoolean("frozen");
                        member_count = channelObject.getInt("member_count");


                        //Config

                        JSONObject configObject = channelObject.getJSONObject("config");
                        config.created_at = configObject.getString("created_at");
                        config.updated_at = configObject.getString("updated_at");
                        config.name = configObject.getString("name");
                        config.typing_events = configObject.getBoolean("typing_events");
                        config.read_events = configObject.getBoolean("read_events");
                        config.connect_events = configObject.getBoolean("connect_events");
                        config.search = configObject.getBoolean("search");
                        config.reactions = configObject.getBoolean("reactions");
                        config.replies = configObject.getBoolean("replies");
                        config.mutes = configObject.getBoolean("mutes");
                        config.message_retention = configObject.getString("message_retention");
                        config.max_message_length = configObject.getInt("max_message_length");
                        config.automod = configObject.getString("automod");

                        session = channelObject.getInt("session");
                        name = channelObject.getString("name");
                        image = channelObject.getString("image");




                        //Messages

                        JSONArray messages = jsonObj.getJSONArray("messages");
                        for (int i=0;i<messages.length();i++){
                            JSONObject message = messages.getJSONObject(i);

                            //Message
                            Message messageModel = new Message();

                            messageModel.id = message.getString("id");
                            messageModel.text = message.getString("text");

                            User user = new User();

                            JSONObject userObject = message.getJSONObject("user");

                            user.id = userObject.getString("id");
                            user.role = userObject.getString("role");
                            user.created_at = userObject.getString("created_at");
                            user.updated_at = userObject.getString("updated_at");
                            user.last_active = userObject.getString("last_active");
                            user.online = userObject.getBoolean("online");
                            user.name = userObject.getString("name");
                            user.image = userObject.getString("image");

                            messageModel.user = user;


//                            messageModel.reaction_counts = message.getInt("reaction_counts");
                            messageModel.reply_count = message.getInt("reply_count");

                            messageModel.create_at = message.getString("created_at");
                            messageModel.updated_at = message.getString("updated_at");

                            messageLists.add( messageModel);
                        }

                        //Members

                        members.clear();
                        JSONArray membersObject = jsonObj.getJSONArray("members");
                        for (int i=0;i<membersObject.length();i++) {

                            Member member = new Member();
                            JSONObject user = membersObject.getJSONObject(i).getJSONObject("user");
                            member.user.id = user.getString("id");
                            member.user.role = user.getString("role");
                            member.user.created_at = user.getString("created_at");
                            member.user.updated_at = user.getString("updated_at");
                            member.user.last_active = user.getString("last_active");
                            member.user.online = user.getBoolean("online");
                            member.user.image = user.getString("image");
                            member.user.name = user.getString("name");


                            members.add(member);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


             }

        });

    }

    public String getId() {
        return id;
    }

    public String getDialogPhoto() {
        return image;
    }

    public String getDialogName() {
        return name;
    }

    public ArrayList<User> getUsers() {

        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0; i < members.size(); i++){
            users.add(members.get(i).user);
        }

        return users;
    }

    public Message getLastMessage() {

        return lastMessage;
    }

    public void setLastMessage(Message message) {
        if(messageLists.size() > 0){
            lastMessage = messageLists.get(messageLists.size()-1);
        }
    }

    public int getUnreadCount() {
        return unreadCount;
    }
    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getUrl() {
        return image;
    }

    public int getMemberCount() {
        return members.size();
    }

    public List<Member> getMembers() {

        return members;
    }
}