package com.getstream.getsteamchatlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.getstream.getsteamchatlibrary.utils.DateUtils;
import com.getstream.getsteamchatlibrary.utils.ImageUtils;
import com.getstream.getsteamchatlibrary.utils.UrlPreviewInfo;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_USER_MESSAGE_ME = 10;
    private static final int VIEW_TYPE_USER_MESSAGE_OTHER = 11;

    private ArrayList<Message> mMessageList = new ArrayList<Message>();
    private Context mContext;
    private HashMap<Message, CircleProgressBar> mFileMessageMap;
    private Channel mChannel;


    MessageListAdapter(Context context, ArrayList<Message> messageList) {
        mContext = context;
        mFileMessageMap = new HashMap<>();
        mMessageList = messageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_USER_MESSAGE_ME:
                View myUserMsgView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_group_chat_user_me, parent, false);
                return new MyUserMessageHolder(myUserMsgView);
            case VIEW_TYPE_USER_MESSAGE_OTHER:
                View otherUserMsgView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_group_chat_user_other, parent, false);
                return new OtherUserMessageHolder(otherUserMsgView);
                default:
                    return null;
        }

    }
    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);

        String currnetUserId = ChannelListsActivity.me.getUserId();

        if (currnetUserId.equals(message.user.id)) {

            return VIEW_TYPE_USER_MESSAGE_ME;

        } else {
            return VIEW_TYPE_USER_MESSAGE_OTHER;
        }

    }
    private boolean isContinuous(Message currentMsg, Message precedingMsg) {
        // null check
        if (currentMsg == null || precedingMsg == null) {
            return false;
        }

        User currentUser = currentMsg.getUser(), precedingUser = precedingMsg.getUser();


        // If admin message or
        return !(currentUser == null || precedingUser == null)
                && currentUser.getUserId().equals(precedingUser.getUserId());


    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = mMessageList.get(position);
        boolean isNewDay = false;
        boolean isContinuous = false;


        if (position < mMessageList.size() - 1) {
            Message prevMessage = mMessageList.get(position + 1);

            // If the date of the previous message is different, display the date before the message,
            // and also set isContinuous to false to show information such as the sender's nickname
            // and profile image.

            if (!DateUtils.hasSameDate(message.getCreatedAt(), prevMessage.getCreatedAt())) {
                isNewDay = true;
                isContinuous = false;
            } else {
                isContinuous = isContinuous(message, prevMessage);
            }
        } else if (position == mMessageList.size() - 1) {
            isNewDay = true;
        }

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_USER_MESSAGE_ME:
                ((MyUserMessageHolder) holder).bind(mContext, (Message) message, isContinuous, isNewDay, position);
                break;
            case VIEW_TYPE_USER_MESSAGE_OTHER:
                ((OtherUserMessageHolder) holder).bind(mContext, (Message) message, isNewDay, isContinuous, position);
//                ((MyUserMessageHolder) holder).bind(mContext, (Message) message, isContinuous, isNewDay, position);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    private class MyUserMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, editedText, timeText, readReceiptText, dateText;
        ViewGroup urlPreviewContainer;
        TextView urlPreviewSiteNameText, urlPreviewTitleText, urlPreviewDescriptionText;
        ImageView urlPreviewMainImageView;
        View padding;
        NonScrollListView list_attachment;
        AttachmentListAdapter attachmentListAdapter;


        MyUserMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_group_chat_message);
            editedText = (TextView) itemView.findViewById(R.id.text_group_chat_edited);
            timeText = (TextView) itemView.findViewById(R.id.text_group_chat_time);
            readReceiptText = (TextView) itemView.findViewById(R.id.text_group_chat_read_receipt);
            dateText = (TextView) itemView.findViewById(R.id.text_group_chat_date);

            urlPreviewContainer = (ViewGroup) itemView.findViewById(R.id.url_preview_container);
            urlPreviewSiteNameText = (TextView) itemView.findViewById(R.id.text_url_preview_site_name);
            urlPreviewTitleText = (TextView) itemView.findViewById(R.id.text_url_preview_title);
            urlPreviewDescriptionText = (TextView) itemView.findViewById(R.id.text_url_preview_description);
            urlPreviewMainImageView = (ImageView) itemView.findViewById(R.id.image_url_preview_main);

            // Dynamic padding that can be hidden or shown based on whether the message is continuous.
            padding = itemView.findViewById(R.id.view_group_chat_padding);

            list_attachment = (NonScrollListView) itemView.findViewById(R.id.list_attachment);

        }

        void bind(Context context, final Message message, boolean isContinuous, boolean isNewDay, final int position) {
            messageText.setText(message.getMessage());
            timeText.setText(DateUtils.formatTime(message.getCreatedAt()));

            attachmentListAdapter = new AttachmentListAdapter(mContext,message.attachments,true);
            list_attachment.setAdapter(attachmentListAdapter);

            if (message.getUpdatedAt().compareTo(message.getCreatedAt()) > 0) {
                editedText.setVisibility(View.VISIBLE);
            } else {
                editedText.setVisibility(View.GONE);
            }

            if(message.getMessage().equals("")){
                messageText.setVisibility(View.GONE);
            }else{
                messageText.setText(message.getMessage());
            }

//            if (isFailedMessage) {
//                readReceiptText.setText(R.string.message_failed);
//                readReceiptText.setVisibility(View.VISIBLE);
//            } else if (isTempMessage) {
//                readReceiptText.setText(R.string.message_sending);
//                readReceiptText.setVisibility(View.VISIBLE);
//            } else {
//
//                // Since setChannel is set slightly after adapter is created
//                if (channel != null) {
//                    int readReceipt = channel.getReadReceipt(message);
//                    if (readReceipt > 0) {
//                        readReceiptText.setVisibility(View.VISIBLE);
//                        readReceiptText.setText(String.valueOf(readReceipt));
//                    } else {
//                        readReceiptText.setVisibility(View.INVISIBLE);
//                    }
//                }
//            }

            // If continuous from previous message, remove extra padding.
            if (isContinuous) {
                padding.setVisibility(View.GONE);
            } else {
                padding.setVisibility(View.VISIBLE);
            }

            // If the message is sent on a different date than the previous one, display the date.
            if (isNewDay) {
                dateText.setVisibility(View.VISIBLE);
                dateText.setText(DateUtils.formatDate(message.getCreatedAt()));
            } else {
                dateText.setVisibility(View.GONE);
            }

            urlPreviewContainer.setVisibility(View.GONE);
//            if (message.getCustomType().equals(URL_PREVIEW_CUSTOM_TYPE)) {
//                try {
//                    urlPreviewContainer.setVisibility(View.VISIBLE);
//                    final UrlPreviewInfo previewInfo = new UrlPreviewInfo(message.create_at);
//                    urlPreviewSiteNameText.setText("@" + previewInfo.getSiteName());
//                    urlPreviewTitleText.setText(previewInfo.getTitle());
//                    urlPreviewDescriptionText.setText(previewInfo.getDescription());
//                    ImageUtils.displayImageFromUrl(context, previewInfo.getImageUrl(), urlPreviewMainImageView, null);
//                } catch (JSONException e) {
//                    urlPreviewContainer.setVisibility(View.GONE);
//                    e.printStackTrace();
//                }
//            }

//            if (clickListener != null) {
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        clickListener.onUserMessageItemClick(message);
//                    }
//                });
//            }
//
//            if (longClickListener != null) {
//                itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        longClickListener.onUserMessageItemLongClick(message, position);
//                        return true;
//                    }
//                });
//            }
        }

    }

    private class OtherUserMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, editedText, nicknameText, timeText, readReceiptText, dateText;
        ImageView profileImage;

        ViewGroup urlPreviewContainer;
        TextView urlPreviewSiteNameText, urlPreviewTitleText, urlPreviewDescriptionText;
        ImageView urlPreviewMainImageView;

        public OtherUserMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_group_chat_message);
            editedText = (TextView) itemView.findViewById(R.id.text_group_chat_edited);
            timeText = (TextView) itemView.findViewById(R.id.text_group_chat_time);
            nicknameText = (TextView) itemView.findViewById(R.id.text_group_chat_nickname);
            profileImage = (ImageView) itemView.findViewById(R.id.image_group_chat_profile);
            readReceiptText = (TextView) itemView.findViewById(R.id.text_group_chat_read_receipt);
            dateText = (TextView) itemView.findViewById(R.id.text_group_chat_date);

            urlPreviewContainer = (ViewGroup) itemView.findViewById(R.id.url_preview_container);
            urlPreviewSiteNameText = (TextView) itemView.findViewById(R.id.text_url_preview_site_name);
            urlPreviewTitleText = (TextView) itemView.findViewById(R.id.text_url_preview_title);
            urlPreviewDescriptionText = (TextView) itemView.findViewById(R.id.text_url_preview_description);
            urlPreviewMainImageView = (ImageView) itemView.findViewById(R.id.image_url_preview_main);
        }


        void bind(Context context, final Message message,boolean isNewDay, boolean isContinuous, final int position) {



            // Show the date if the message was sent on a different date than the previous message.
            if (isNewDay) {
                dateText.setVisibility(View.VISIBLE);
                dateText.setText(DateUtils.formatDate(message.getCreatedAt()));
            } else {
                dateText.setVisibility(View.GONE);
            }

            // Hide profile image and nickname if the previous message was also sent by current sender.
            if (isContinuous) {
                profileImage.setVisibility(View.INVISIBLE);
                nicknameText.setVisibility(View.GONE);
            } else {
                profileImage.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImageFromUrl(context, message.getUser().getProfileUrl(), profileImage);

                nicknameText.setVisibility(View.VISIBLE);
                nicknameText.setText(message.getUser().getName());
            }

            if(message.getMessage().equals("")){
                messageText.setVisibility(View.GONE);
            }else{
                messageText.setText(message.getMessage());
            }

            timeText.setText(DateUtils.formatTime(message.getCreatedAt()));

            if (message.getUpdatedAt().compareTo(message.getCreatedAt()) > 0) {
                editedText.setVisibility(View.VISIBLE);
            } else {
                editedText.setVisibility(View.GONE);
            }

            urlPreviewContainer.setVisibility(View.GONE);
//            if (message.getCustomType().equals(URL_PREVIEW_CUSTOM_TYPE)) {
//                try {
//                    urlPreviewContainer.setVisibility(View.VISIBLE);
//                    UrlPreviewInfo previewInfo = new UrlPreviewInfo(message.getData());
//                    urlPreviewSiteNameText.setText("@" + previewInfo.getSiteName());
//                    urlPreviewTitleText.setText(previewInfo.getTitle());
//                    urlPreviewDescriptionText.setText(previewInfo.getDescription());
//                    ImageUtils.displayImageFromUrl(context, previewInfo.getImageUrl(), urlPreviewMainImageView, null);
//                } catch (JSONException e) {
//                    urlPreviewContainer.setVisibility(View.GONE);
//                    e.printStackTrace();
//                }
//            }


//            if (clickListener != null) {
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        clickListener.onUserMessageItemClick(message);
//                    }
//                });
//            }
//            if (longClickListener != null) {
//                itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        longClickListener.onUserMessageItemLongClick(message, position);
//                        return true;
//                    }
//                });
//            }
        }
    }

}
