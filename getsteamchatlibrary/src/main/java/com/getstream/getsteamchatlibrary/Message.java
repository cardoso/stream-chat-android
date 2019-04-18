package com.getstream.getsteamchatlibrary;

import android.support.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements IMessage, MessageContentType.Image, /*this is for default image messages implementation*/
        MessageContentType {

    /*...*/

    public String id;
    public String text;
    public String type;
    public User user;
    int reaction_counts,reply_count;
    public String create_at,updated_at;
    String status;
    private Image image;
    private Voice voice;

    int parent_id;
    boolean show_in_channel;

    String durations;

    public Message() {

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
    public String getStatus() {
        return "Sent";
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Date getCreatedAt() {

        Date date= null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX").parse(create_at);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Nullable
    @Override
    public String getImageUrl() {
        return image == null ? null : image.url;
    }
    public void setImage(Image image) {
        this.image = image;
    }

    public void setCreatedAt(Date create_at) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(create_at);

        this.create_at = strDate;
    }

    public static class Image {

        private String url;

        public Image(String url) {
            this.url = url;
        }
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public static class Voice {

        private String url;
        private int duration;

        public Voice(String url, int duration) {
            this.url = url;
            this.duration = duration;
        }

        public String getUrl() {
            return url;
        }

        public int getDuration() {
            return duration;
        }
    }
}
