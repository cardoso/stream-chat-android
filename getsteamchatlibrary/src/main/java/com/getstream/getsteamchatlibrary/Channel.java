package com.getstream.getsteamchatlibrary;

public class Channel {

    String client, type, id, data;
    Boolean isTyping, initialized;
    String _data;
    ChannelState


    public Channel(String client, String type, String id, String data) {

        String validTypeRe = "/^[\\w_-]+$/";
        String validIDRe = "/^[\\w_-]+$/";

        this.client = client;
        this.type = type;
        this.id = id;
        // used by the frontend, gets updated:
        this.data = data;
        // this._data is used for the requests...
        this._data = { ...data };

        this.cid = `${type}:${id}`;
        // perhaps the state variable should be private
        this.state = new ChannelState(this);
        this.initialized = false;
        this.lastTypingEvent = null;
        this.isTyping = false;

    }

}