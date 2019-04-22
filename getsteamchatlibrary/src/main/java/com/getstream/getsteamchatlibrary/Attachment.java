package com.getstream.getsteamchatlibrary;

public class Attachment {

    String type,thumb_url,asset_url;
    int myCustomField;

    public Attachment() {

        type = "";
        thumb_url = "";
        asset_url = "";
        myCustomField = 0;
    }

    public String getThumbnail() {
        return  thumb_url;
    }

    public String getType() {
        return type;
    }
}
