package com.getstream.sdk.chat.model;

import com.getstream.sdk.chat.rest.User;
import com.google.gson.annotations.SerializedName;

public class Device {
    @SerializedName("id")
    String id;

    @SerializedName("push_provider")
    String push_provider;

    @SerializedName("user_id")
    String user_id;

    @SerializedName("user")
    User user;

    @SerializedName("created_at")
    String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPush_provider() {
        return push_provider;
    }

    public void setPush_provider(String push_provider) {
        this.push_provider = push_provider;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
