package com.getstream.getsteamchatlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.getstream.getsteamchatlibrary.utils.DateUtils;
import com.squareup.picasso.Picasso;
import com.stfalcon.multiimageview.MultiImageView;
import com.teleclinic.bulent.smartimageview.SmartImageViewLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChannelListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Channel> mChannelList;

    static LayoutInflater inflater = null;



    ConcurrentHashMap<SimpleTarget<Bitmap>, Integer> mSimpleTargetIndexMap;
    ConcurrentHashMap<SimpleTarget<Bitmap>, Channel> mSimpleTargetGroupChannelMap;
    ConcurrentHashMap<String, Integer> mChannelImageNumMap;
    ConcurrentHashMap<String, ImageView> mChannelImageViewMap;
    ConcurrentHashMap<String, SparseArray<Bitmap>> mChannelBitmapMap;


    ChannelListAdapter(Context context, ArrayList<Channel> mChannelList){

        this.mChannelList = mChannelList;
        this.mContext = context;

        mSimpleTargetIndexMap = new ConcurrentHashMap<>();
        mSimpleTargetGroupChannelMap = new ConcurrentHashMap<>();
        mChannelImageNumMap = new ConcurrentHashMap<>();
        mChannelImageViewMap = new ConcurrentHashMap<>();
        mChannelBitmapMap = new ConcurrentHashMap<>();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mChannelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View itemView = view;
        if(itemView == null){
            itemView = inflater.inflate(R.layout.item_channels,null);
        }

        TextView topicText, lastMessageText, unreadCountText, dateText, memberCountText;
        SmartImageViewLayout coverImage;
        LinearLayout typingIndicatorContainer;

        topicText = (TextView) itemView.findViewById(R.id.text_group_channel_list_topic);
        lastMessageText = (TextView) itemView.findViewById(R.id.text_group_channel_list_message);
        unreadCountText = (TextView) itemView.findViewById(R.id.text_group_channel_list_unread_count);
        dateText = (TextView) itemView.findViewById(R.id.text_group_channel_list_date);
        memberCountText = (TextView) itemView.findViewById(R.id.text_group_channel_list_member_count);
        coverImage = (SmartImageViewLayout) itemView.findViewById(R.id.image_group_channel_list_cover);

        typingIndicatorContainer = (LinearLayout) itemView.findViewById(R.id.container_group_channel_list_typing_indicator);





        Channel channel = mChannelList.get(position);

        String[] urls = new String[channel.members.size()];
        for(int i = 0; i < channel.members.size();i ++){

            urls[i] = channel.members.get(i).user.image;

        }
        coverImage.putImages(urls);
        memberCountText.setText(String.valueOf(channel.getMemberCount()));

//        setChannelImage(mContext, position, channel, coverImage);



        String channelName = "";
        ArrayList<Member> members = channel.members;
        for (int i = 0; i < members.size(); i++){
            channelName += members.get(i).user.name;
            if (i < members.size()-1){
                channelName +=  ", ";
            }
        }

        topicText.setText(channelName);

        ArrayList<Message> messages = new ArrayList<Message>();
        messages = channel.messageLists;



        if(messages.size() > 0){
            Message lastMsg = messages.get(messages.size()-1);
            lastMessageText.setText(lastMsg.text);
        }else{
            lastMessageText.setText("Start Conversation");
        }

        String last_message_at = mChannelList.get(position).last_message_at;

        Date date = null;
        if(last_message_at != null){
            try {
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX").parse(last_message_at);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(date == null){
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSX").parse(last_message_at);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String dateFormat = "mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            String tDate = sdf.format(date);
            dateText.setText(tDate);
        }else{
            dateText.setVisibility(View.INVISIBLE);
            lastMessageText.setVisibility(View.INVISIBLE);
        }



        unreadCountText.setText(String.valueOf(channel.unreadCount));

        // If someone in the channel is typing, display the typing indicator.
        if (channel.isTyping) {
            typingIndicatorContainer.setVisibility(View.VISIBLE);
            lastMessageText.setText(("Someone is typing"));
        } else {
            // Display typing indicator only when someone is typing
            typingIndicatorContainer.setVisibility(View.GONE);
        }


        return itemView;
    }
}
