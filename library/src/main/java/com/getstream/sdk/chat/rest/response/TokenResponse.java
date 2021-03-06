package com.getstream.sdk.chat.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.getstream.chat.android.client.models.User;

public class TokenResponse {
    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("access_token")
    @Expose
    private String token;

    public User getUser() {
        return user;
    }

    public String getToken() { return token; }
}
