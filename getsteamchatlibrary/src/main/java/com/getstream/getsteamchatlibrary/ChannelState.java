package com.getstream.getsteamchatlibrary;

import java.util.ArrayList;
import java.util.Date;

public class ChannelState {

    Channel _channel;

    int online;
    Boolean typing, read;
    ArrayList<MessageModel> messages = new ArrayList<MessageModel>();
    ArrayList<MessageModel> threads = new ArrayList<MessageModel>();

    ArrayList<User> mutedUsers, watchers, members;

    ChannelState(Channel channel) {

        this._channel = channel;
        this.online = 0;
        this.typing = false;
        this.read = false;
        this.messages = new ArrayList<MessageModel>();
        this.threads = new ArrayList<MessageModel>();

        // a list of users to hide messages from
        this.mutedUsers = new ArrayList<User>();
        this.watchers = new ArrayList<User>();
        this.members = new ArrayList<User>();

    }

    void addMessagesSorted(ArrayList<MessageModel> newMessages){

//        ArrayList<MessageModel> parsedMessages = new ArrayList<MessageModel>();
//
//        for(int i = 0; i < newMessages.size(); i++){
//            MessageModel message = newMessages.get(i);
//            parsedMessages.add(this.messageToImmutable(message));
//        }
//
//        // update or append the messages...
//
//        ArrayList<MessageModel> updatedThreads = new ArrayList<MessageModel>();
//
//        for(int i = 0; i < newMessages.size(); i++){
//            MessageModel message = newMessages.get(i);
//
//            boolean isThreadReply = (message.parent_id > 0) && !message.show_in_channel;
//            if(!isThreadReply){
//                this.messages = this._addToMessageList(messages,message);
//            }
//
//            // add to the thread if applicable..
//
//            int parentID = message.parent_id;
//            if(parentID > 0){
//                ///??
//                MessageModel thread = this.threads.get(parentID);
//                ArrayList<MessageModel> threadMessages = this._addToMessageList(thread, message);
//                this.threads.set(parentID, threadMessages);
//
//                updatedThreads.add(parentID);
//            }
//        }

    }

    MessageModel messageToImmutable(MessageModel message){

        message.create_at = new Date().toString();
        message.updated_at = new Date().toString();
        if(message.status == null){
            message.status = "received";
        }

        return  message;
    }

    ArrayList<MessageModel> _addToMessageList(ArrayList<MessageModel> messages, MessageModel newMessage){

        for (int i = 0; i < messages.size(); i++) {
            MessageModel msg = messages.get(i);
            if(msg.id == newMessage.id){
                messages.set(i, newMessage);

                return messages;
            }
        }

        // !Update
        messages.add(newMessage);

        return messages;
    }

    ArrayList<MessageModel> removeMessage(MessageModel newMessage){

        for (int i = 0; i < this.messages.size(); i++) {
            MessageModel msg = this.messages.get(i);
            if(msg.id == newMessage.id){

                this.messages.remove(i);

                return this.messages;
            }
        }

        return this.messages;
    }

    ArrayList<MessageModel> filterErrorMessages(){

        ArrayList<MessageModel> errorMessages = new ArrayList<MessageModel>();

        for (int i = 0; i < this.messages.size(); i++) {
            MessageModel msg = this.messages.get(i);
            if(msg.type == "error"){

                errorMessages.add(msg);
            }
        }

        return errorMessages;
    }


}
