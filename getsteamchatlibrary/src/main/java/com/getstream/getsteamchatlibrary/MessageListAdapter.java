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

public class MessageListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<MessageModel> mMessageList;

    static LayoutInflater inflater = null;

    MessageListAdapter(Context context, ArrayList<MessageModel> mMessageList){

        this.mMessageList = mMessageList;
        this.mContext = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mMessageList.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        boolean issent = true;
        View vi = view;
        MessageModel mMessage = mMessageList.get(i);
//        if(vi == null){
            if(mMessage.user.id.equals(ChannelListsActivity.me.id)){
                vi = inflater.inflate(R.layout.item_message_sent,null);
                issent = true;
            }else{
                vi = inflater.inflate(R.layout.item_message_received,null);
                issent = false;
            }

//        }

        if(!issent){
            TextView message_name = (TextView)vi.findViewById(R.id.text_message_name);
            message_name.setText(mMessage.user.name);
            CircleImageView img_profile = vi.findViewById(R.id.image_message_profile);
            Picasso.with(mContext).load(mMessage.user.image).into(img_profile);
//            img_profile.setImageURI(Uri.parse(mMessage.user.image));
        }

        TextView messageText, timeText;
        messageText = (TextView) vi.findViewById(R.id.text_message_body);
        timeText = (TextView) vi.findViewById(R.id.text_message_time);

        messageText.setText(mMessage.text);

        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String oldstring = mMessage.updated_at;

        Date date= null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX").parse(oldstring);
            String dateFormat = "mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            String tDate = sdf.format(date);
            timeText.setText(tDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }





        return vi;
    }
}
