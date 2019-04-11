package com.getstream.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.getstream.getsteamchatlibrary.MessageModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        View vi = view;
        if(vi == null){
            vi = inflater.inflate(R.layout.item_message_sent,null);
        }

        TextView messageText, timeText;
        messageText = (TextView) vi.findViewById(R.id.text_message_body);
        timeText = (TextView) vi.findViewById(R.id.text_message_time);

        messageText.setText(mMessageList.get(i).text);



        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String oldstring = mMessageList.get(i).updated_at;

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
