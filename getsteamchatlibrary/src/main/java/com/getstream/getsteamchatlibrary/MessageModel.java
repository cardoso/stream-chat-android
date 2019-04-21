package com.getstream.getsteamchatlibrary;

public class MessageModel {

    public String id;
    public String text;
    public String type;
    public User user;
    int reaction_counts,reply_count;
    public String create_at,updated_at;
    String status;


    int parent_id;
    boolean show_in_channel;

    String durations;


    public MessageModel() {

        id = "";
        text = "";
        user = new User();

        reaction_counts = 0;
        reply_count = 0;

        create_at = "";
        updated_at = "";
        status = "";
        durations = "";
    }

}
