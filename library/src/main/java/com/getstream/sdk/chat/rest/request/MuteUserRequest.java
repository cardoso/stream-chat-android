package com.getstream.sdk.chat.rest.request;

import com.getstream.sdk.chat.utils.Global;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MuteUserRequest {
    @SerializedName("target_id")
    @Expose
    String target_id;

    @SerializedName("user_id")
    @Expose
    String user_id;

    public MuteUserRequest(String target_id){
        this.target_id = target_id;
        this.user_id = Global.client.user.getId();
    }
}
