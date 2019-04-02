package com.getstream.getsteamchatlibrary;

import java.lang.reflect.Array;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.getstream.getsteamchatlibrary.Signing.JWTUserToken;
import static com.getstream.getsteamchatlibrary.Signing.UserFromToken;

public class SteamChat {

    String key, secret, broswer, node, userToken;
    ClientState state;
    String baseURL, wsBaseURL;
    User user;

    public SteamChat(String key, String secretOrToptions, String options) {

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

    void setUser(User user, String userToken){

        this.userToken = userToken;

        if(userToken == null && this.secret != null){
            this.userToken = this.createToken(user.UserId);
        }

        if(this.userToken == null){
            return;
        }

        String tokenUserId = UserFromToken(this.userToken);

        this._setUser(user);


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
