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


//    MessageModel(String text, int id, String type, int parent_id, boolean show_in_channel, String create_at, String updated_at) {
//
//        this.text = text;
//        this.id = id;
//        this.type = type;
//        this.parent_id = parent_id;
//        this.show_in_channel = show_in_channel;
//        this.create_at = create_at;
//        this.updated_at = updated_at;
//
//    }

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
