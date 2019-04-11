package com.getstream.getsteamchatlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {



    public Channel parseChannelData(JSONObject jsonObj) {

        //Channel
        Channel channel = new Channel();
        try {
                JSONObject channelObject = jsonObj.getJSONObject("channel");
                channel.id = channelObject.getString("id");
                channel.type = channelObject.getString("type");
                channel.cid = channelObject.getString("cid");
                if (channelObject.has("last_message_at"))
                    channel.last_message_at = channelObject.getString("last_message_at");

                JSONObject createby = channelObject.getJSONObject("created_by");

                User create_by = new User();

                channel.create_by.id = createby.getString("id");
                channel.create_by.role = createby.getString("role");
                channel.create_by.created_at = createby.getString("created_at");
                channel.create_by.updated_at = createby.getString("updated_at");
                channel.create_by.last_active = createby.getString("last_active");
                channel.create_by.online = createby.getBoolean("online");
                channel.create_by.name = createby.getString("name");
                channel.create_by.image = createby.getString("image");

                channel.frozen = channelObject.getBoolean("frozen");
                channel.member_count = channelObject.getInt("member_count");


                //Config

                JSONObject configObject = channelObject.getJSONObject("config");
                channel.config.created_at = configObject.getString("created_at");
                channel.config.updated_at = configObject.getString("updated_at");
                channel.config.name = configObject.getString("name");
                channel.config.typing_events = configObject.getBoolean("typing_events");
                channel.config.read_events = configObject.getBoolean("read_events");
                channel.config.connect_events = configObject.getBoolean("connect_events");
                channel.config.search = configObject.getBoolean("search");
                channel.config.reactions = configObject.getBoolean("reactions");
                channel.config.replies = configObject.getBoolean("replies");
                channel.config.mutes = configObject.getBoolean("mutes");
                channel.config.message_retention = configObject.getString("message_retention");
                channel.config.max_message_length = configObject.getInt("max_message_length");
                channel.config.automod = configObject.getString("automod");

                channel.session = channelObject.getInt("session");
                channel.name = channelObject.getString("name");
                channel.image = channelObject.getString("image");

                //Messages

                JSONArray messages = jsonObj.getJSONArray("messages");
                for (int i = 0; i < messages.length(); i++) {
                    JSONObject message = messages.getJSONObject(i);

                    //Message

                    channel.messageLists.add(parseMessageData(message));

                }

                //Members

                channel.members.clear();
                JSONArray membersObject = jsonObj.getJSONArray("members");
                for (int i = 0; i < membersObject.length(); i++) {

                    Member member = new Member();
                    JSONObject user = membersObject.getJSONObject(i).getJSONObject("user");
                    member.user.id = user.getString("id");
                    member.user.role = user.getString("role");
                    member.user.created_at = user.getString("created_at");
                    member.user.updated_at = user.getString("updated_at");
                    member.user.last_active = user.getString("last_active");
                    member.user.online = user.getBoolean("online");
                    member.user.image = user.getString("image");
                    member.user.name = user.getString("name");


                    channel.members.add(member);
                }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return channel;
    }

    public MessageModel parseMessageData(JSONObject message) {

        //Message
        MessageModel messageModel = new MessageModel();

        try {
            messageModel.id = message.getString("id");
            messageModel.text = message.getString("text");

            messageModel.user = parseUserData(message);


            //messageModel.reaction_counts = message.getInt("reaction_counts");
            messageModel.reply_count = message.getInt("reply_count");

            messageModel.create_at = message.getString("created_at");
            messageModel.updated_at = message.getString("updated_at");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageModel;


    }

    public User parseUserData(JSONObject jsonObj) {


        User user = new User();

        JSONObject userObject = null;
        try {

            userObject = jsonObj.getJSONObject("user");

            user.id = userObject.getString("id");
            user.role = userObject.getString("role");
            user.created_at = userObject.getString("created_at");
            user.updated_at = userObject.getString("updated_at");
            user.last_active = userObject.getString("last_active");
            user.online = userObject.getBoolean("online");
            user.name = userObject.getString("name");
            user.image = userObject.getString("image");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;

    }
}
