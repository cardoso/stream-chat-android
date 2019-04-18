package com.getstream.getsteamchatlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChannelListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Channel> mChannelList;

    static LayoutInflater inflater = null;

    ChannelListAdapter(Context context, ArrayList<Channel> mChannelList){

        this.mChannelList = mChannelList;
        this.mContext = context;

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

        View vi = view;
        if(vi == null){
            vi = inflater.inflate(R.layout.item_channels,null);
        }

        TextView messageText, timeText,nameText;

        nameText = (TextView) vi.findViewById(R.id.tv_name);
        messageText = (TextView) vi.findViewById(R.id.tv_last_message);
        timeText = (TextView) vi.findViewById(R.id.tv_datetime);
        CircleImageView img_avtar = vi.findViewById(R.id.img_avatar);

        String channelName = "";
        ArrayList<Member> members = mChannelList.get(position).members;
        for (int i = 0; i < members.size(); i++){
            channelName += members.get(i).user.name;
            if (i < members.size()-1){
                channelName +=  ", ";
            }
        }

        nameText.setText(channelName);

        ArrayList<Message> messages = new ArrayList<Message>();
        messages = mChannelList.get(position).messageLists;



        if(messages.size() > 0){
            Message lastMsg = messages.get(messages.size()-1);
            messageText.setText(lastMsg.text);
            CircleImageView img_last_seen_avatar = vi.findViewById(R.id.img_last_seen_avatar);
            Picasso.with(mContext).load(lastMsg.user.image).into(img_last_seen_avatar);
        }else{
            messageText.setText("Start Conversation");
        }

        Picasso.with(mContext).load(mChannelList.get(position).members.get(0).user.image).into(img_avtar);


//        messageText.setText(mMessageList.get(i).text);



        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String oldstring = mChannelList.get(position).last_message_at;


        if(oldstring != null){
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX").parse(oldstring);
                String dateFormat = "mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                String tDate = sdf.format(date);
                timeText.setText(tDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            timeText.setText("");
        }






        return vi;
    }
}
