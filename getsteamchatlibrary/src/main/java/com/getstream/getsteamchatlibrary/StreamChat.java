package com.getstream.getsteamchatlibrary;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.getstream.getsteamchatlibrary.Signing.JWTUserToken;
import static com.getstream.getsteamchatlibrary.Signing.UserFromToken;

public class StreamChat {

    String key, secret, userToken;
    ClientState state;
    static String baseURL, wsBaseURL, wsURL;
    boolean anonymous, connecting;
    String UUID_,clientID,userID;
    int failures = 0;
    User user;
    ArrayList<Channel> activeChannels;


    public StreamChat(String key, String secretOrToptions, String options) {

        // Set the key
        this.key = key;
        this.secret = null;
        this.state = new ClientState();

        activeChannels = new ArrayList<Channel>();
        // set the secret

//        this.browser =
//                typeof options.browser !== 'undefined'
//                ? options.browser
//                : typeof window !== 'undefined';
//        this.node = !this.browser;
//
//		const defaultOptions = {
//                timeout: 3000,
//		};


        FormBody.Builder defaultOptions = new FormBody.Builder()
                .add("timeout", "3000");

//        RequestBody formBody = defaultOptions.build();


        this.setBsaeURL("https://chat-us-east-1.stream-io-api.com");

    }

    public Channel channel(String channelType, String channelID, String custom){

        Channel channel = null;
        if(channelID.length()>0){
            String cid = channelID + ":" + channelID;
            for(int i=0;i<this.activeChannels.size();i++){
                if(this.activeChannels.get(i).cid.equals(cid)){
                    channel = this.activeChannels.get(i);
//                    channel._data = custom;
//                    channel.data = custom;
                }
            }

            channel = new Channel(this,channelType,channelID,custom);
            this.activeChannels.add(channel);


        }else{

            channel = new Channel(this,channelType,"",custom);

        }
        return channel;

    }

    public void setUser(User user, String userToken){

        this.userToken = userToken;

        this.userID = user.userId;
        if(userToken == null && this.secret != null){
            this.userToken = this.createToken(user.userId);
        }

        if(this.userToken == null){
            return;
        }

        String tokenUserId = UserFromToken(this.userToken);

        this._setUser(user);
        this.anonymous = false;


        this._setupConnection();
    }

    void _setupConnection() {

        this.UUID_ = String.valueOf(UUID.randomUUID());
        this.clientID = this.user.userId + "--" + this.UUID_;
        this.connect();
    }

    void connect() {
        this.connecting = true;
        this.failures = 0;

//        if(client.userID == null){
//            return;
//        }



        Map<String,String> params =  new HashMap<String,String>();
//add items
        String client_id = "\"client_id\":\""+ this.clientID + "\"";
        String user_id = "\"user_id\":\""+ this.userID + "\"";
        String userString = "\"" +"id\":\"" + this.userID + "\"" + ",\"name\":" + "\"" + this.user.name + "\"" + ",\"image\":" + "\"" + this.user.imguser + "\"";
        String user_details = "\"user_details\":{" + userString + "}";
        String user_token = "\"user_token\":\"" + this.userToken + "\"";

//		const qs = encodeURIComponent(JSON.stringify(params));
		String qs = URLEncoder.encode("{" + client_id +"," + user_id +"," + user_details +"," + user_token +"}");
		if(qs.length() > 1900)
            return;

        String token = "";
        if(this.anonymous == false){
            token = this.userToken != null ? this.userToken : JWTUserToken("",user.userId,"","");
        }

        String authType = this.getAuthType();

        this.wsURL = this.wsBaseURL + "/connect?json=" + qs + "&api_keyf="
                + this.key + "&authorization=" + token + "&stream-auth-type=" + authType;

        StableWSConnection wsConnection = new StableWSConnection(wsURL, clientID, user.userId/*, this.recoverState, this.handleEvent, this.dispatchEvent*/);
//        wsConnection.connect();
        wsConnection.connect();

//        return this.wsPromise;

    }

    String getAuthType() {
        return this.anonymous ? "anonymous" : "jwt";
    }

    void _setUser(User user){
        this.user = user;
    }

    String createToken(String userID){

        return JWTUserToken("1",userID,"1","1");

    }

    void setBsaeURL(String baseURL) {
        this.baseURL = baseURL;
        this.wsBaseURL = this.baseURL.replace("http","ws");
    }

    public void createChannelType(String data){
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("name", this.UUID_);
        RequestBody formBody = formBuilder.build();

        APIManager.getInstance().post(baseURL + "/channeltypes", formBody, new APIManager.MyCallBackInterface() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailure(final String error, int nCode) {

            }
        });
    }

    void getChannelType(String channelType){

        APIManager.getInstance().get(this.baseURL+"/channeltypes/" + channelType, new APIManager.MyCallBackInterface() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailure(final String error, int nCode) {

            }
        });
    }

}
