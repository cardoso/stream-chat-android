package com.getstream.sdk.chat.rest.interfaces;

import com.getstream.sdk.chat.rest.response.FlagUserResponse;

public interface FlagUserCallback {
    void onSuccess(FlagUserResponse response);

    void onError(String errMsg, int errCode);
}
