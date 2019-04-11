package com.getstream.getsteamchatlibrary;

public class Config {

    String created_at,updated_at,name;
    boolean typing_events,read_events,connect_events,search,reactions,replies,mutes;
    String message_retention;
    int max_message_length;
    String automod;

    public Config() {


        created_at = "";
        updated_at = "";
        name = "";

        typing_events = false;
        read_events = false;
        connect_events = false;
        search = false;
        reactions = false;
        replies = false;
        mutes = false;
        message_retention = "";

        max_message_length = 100;
        automod = "";

    }

}
