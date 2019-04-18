package com.getstream.getsteamchatlibrary.messages;

import android.graphics.Color;
import android.view.View;

import com.getstream.getsteamchatlibrary.Message;
import com.stfalcon.chatkit.messages.MessageHolders;

public class CustomOutcomingTextMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> {

    public CustomOutcomingTextMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

//        time.setText(message.getStatus() + " " + time.getText());
        time.setText(time.getText());
        time.setTextColor(Color.GRAY);
    }
}
