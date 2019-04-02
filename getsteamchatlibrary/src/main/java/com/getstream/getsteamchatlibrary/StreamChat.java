package com.getstream.getsteamchatlibrary;

import java.lang.reflect.Array;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.getstream.getsteamchatlibrary.Signing.JWTUserToken;
import static com.getstream.getsteamchatlibrary.Signing.UserFromToken;

public class StreamChat {

    String key, secret, broswer, node, userToken;
    ClientState state;
    String baseURL, wsBaseURL;
    User user;
    boolean anonymous;
    public StreamChat(String key, String secretOrToptions, String options) {

        // Set the key
        this.key = key;
        this.secret = null;
        this.state = new ClientState();

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

    public void setUser(User user, String userToken){

        this.userToken = userToken;

        if(userToken == null && this.secret != null){
            this.userToken = this.createToken(user.userId);
        }

        if(this.userToken == null){
            return;
        }

        String tokenUserId = UserFromToken(this.userToken);

        this._setUser(user);


        return this._setupConnection();
    }

    void _setupConnection() {
        this.UUID = uuidv4();
        this.clientID = `${this.userID}--${this.UUID}`;
        this.connect();
        return this.wsPromise;
    }

    void connect() {
        this.connecting = true;
        StreamChat client = this;
        this.failures = 0;

        if(client.user.userId == null){
            return;
        }

        const params = {
                client_id: client.clientID,
                user_id: client.userID,
                user_details: client._user,
                user_token: client.userToken,
		};
		const qs = encodeURIComponent(JSON.stringify(params));

        String token = "";
        if(this.anonymous == false){
            token = this.userToken != null ? this.userToken : JWTUserToken("",user.userId,"","");
        }

        String authType = this.getAuthType();

        client.wsURL = client.wsBaseURL + "/connect?json=" + qs + "&api_key="
                + this.key + "&authorization=" + token + "&stream-auth-type=" + authType;

        this.wsConnection = new StableWSConnection()

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

}
