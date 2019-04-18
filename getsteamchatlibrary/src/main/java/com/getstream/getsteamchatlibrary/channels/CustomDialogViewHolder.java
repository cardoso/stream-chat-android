package com.getstream.getsteamchatlibrary.channels;

import android.view.View;

import com.getstream.getsteamchatlibrary.Channel;
import com.getstream.getsteamchatlibrary.R;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

/*
 * Created by Anton Bevza on 1/18/17.
 */
public class CustomDialogViewHolder
        extends DialogsListAdapter.DialogViewHolder<Channel> {

    private View onlineIndicator;

    public CustomDialogViewHolder(View itemView) {
        super(itemView);
        onlineIndicator = itemView.findViewById(R.id.onlineIndicator);
    }

    @Override
    public void onBind(Channel channel) {
        super.onBind(channel);

        if (channel.getUsers().size() > 1) {
            onlineIndicator.setVisibility(View.GONE);
        } else {
            boolean isOnline = channel.getUsers().get(0).isOnline();
            onlineIndicator.setVisibility(View.VISIBLE);
            if (isOnline) {
                onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_online);
            } else {
                onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_offline);
            }
        }
    }
}
