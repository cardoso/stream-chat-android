package com.getstream.getsteamchatlibrary;

import java.util.Date;

public class MessageModel {

    Date create_at;
    Date updated_at;
    String status;
    String msg;
    int parent_id;
    boolean show_in_channel;
    String type;
    int id;

    MessageModel(String msg, int id, String type, int parent_id, boolean show_in_channel, Date create_at, Date updated_at) {

        this.msg = msg;
        this.id = id;
        this.type = type;
        this.parent_id = parent_id;
        this.show_in_channel = show_in_channel;
        this.create_at = create_at;
        this.updated_at = updated_at;

    }

}
