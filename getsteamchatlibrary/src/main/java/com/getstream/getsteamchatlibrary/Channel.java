package com.getstream.getsteamchatlibrary;

import java.util.ArrayList;

public class Channel {

    String type, id, data;
    Boolean isTyping, initialized;
    String _data;
    StreamChat client;
    ChannelState state;
    String lastTypingEvent;


    public Channel(StreamChat client, String type, String id, String data) {

        String validTypeRe = "/^[\\w_-]+$/";
        String validIDRe = "/^[\\w_-]+$/";

        this.client = client;
        this.type = type;
        this.id = id;
        // used by the frontend, gets updated:
        this.data = data;
        // this._data is used for the requests...
//        this._data = { ...data };

//        this.cid = `${type}:${id}`;
        // perhaps the state variable should be private
        this.state = new ChannelState(this);
        this.initialized = false;
        this.lastTypingEvent = "";
        this.isTyping = false;


    }


    String _channelURL() {
        if(this.id == null){
            return "";
        }
        String channelURL = this.client.baseURL + "/channels/" + this.type + this.id;
        return channelURL;
    }


    void _initializeState(ChannelState state) {

        // immutable list of maps

        ArrayList<MessageModel> messages = new ArrayList<MessageModel>();
        messages = state.messages;

        this.state.addMessagesSorted(messages);
        this.state.online = state.online;

        // convert the arrays into objects for easier syncing...

        if(state.watchers.size() > 0) {
            for(int i = 0; i < state.watchers.size(); i++) {
                User watcher = state.watchers.get(i);
                this.state.watchers.set(watcher.UserId, watcher);
            }
        }

        if(state.members.size() > 0) {
            for(int i = 0; i < state.members.size(); i++) {
                User members = state.members.get(i);
                this.state.members.set(members.UserId, members);
            }
        }
    }

}