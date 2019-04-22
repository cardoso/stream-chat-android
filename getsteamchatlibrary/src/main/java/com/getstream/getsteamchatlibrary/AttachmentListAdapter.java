package com.getstream.getsteamchatlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.getstream.getsteamchatlibrary.utils.ImageUtils;
import com.teleclinic.bulent.smartimageview.SmartImageViewLayout;

import java.util.ArrayList;

public class AttachmentListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Attachment> mAttachments;
    static LayoutInflater inflater = null;
    boolean isSent;

    AttachmentListAdapter(Context context, ArrayList<Attachment> mAttachments, boolean isSent){
        this.mAttachments = mAttachments;
        this.context = context;
        this.isSent = isSent;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return this.mAttachments.size();
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

        Attachment attachment = this.mAttachments.get(i);
        View itemView = view;

            if(attachment.type.equals("image")){
                if(isSent)
                    itemView = inflater.inflate(R.layout.list_item_group_chat_file_image_me,null);
                else
                    itemView = inflater.inflate(R.layout.list_item_group_chat_file_image_other,null);
            }else{

            }


        ImageView fileThumbnailImage;
        CircleProgressBar circleProgressBar;

        fileThumbnailImage = (ImageView) itemView.findViewById(R.id.image_group_chat_file_thumbnail);
        circleProgressBar = (CircleProgressBar) itemView.findViewById(R.id.circle_progress);

        // If thumbnails exist, get smallest (first) thumbnail and display it in the message


        if (attachment.getType().toLowerCase().contains("gif")) {
            ImageUtils.displayGifImageFromUrl(context, attachment.getThumbnail(), fileThumbnailImage, (String) null, fileThumbnailImage.getDrawable());
        } else {
            ImageUtils.displayImageFromUrl(context, attachment.getThumbnail(), fileThumbnailImage, fileThumbnailImage.getDrawable());
        }

        return itemView;
    }
}
