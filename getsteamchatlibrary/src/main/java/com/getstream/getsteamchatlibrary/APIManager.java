package com.getstream.getsteamchatlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ruby on 6/17/2017.
 */

public class APIManager {

    interface MyCallBackInterface {

        void onSuccess(String result);

        void onFailure(String error, int nCode);
    }


    private static APIManager instance;
    OkHttpClient client;

    private APIManager() {
        client = new OkHttpClient();
    }

    public static APIManager getInstance() {
        if (instance == null) {
            instance = new APIManager();
        }
        return instance;
    }

    public void post(String strURL, RequestBody parameters, final MyCallBackInterface callback) {

        Request request = new Request.Builder()
                .url(strURL)
                .post(parameters)

                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();
                if(code == 200){
                    callback.onSuccess(jsonData);
                }else{
                    callback.onFailure(jsonData, code);
                }

            }
        });

    }

    public void get(final String URL,final MyCallBackInterface callback){

        String strURL = String.format("%s",URL);
        Request request = new Request.Builder()
                .url(strURL)
                .get()

                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed", 500);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int code = response.code();
                if(code == 200){
                    callback.onSuccess(jsonData);
                }else{
                    callback.onFailure(jsonData, code);
                }
            }
        });

    }
}