package com.getstream.getsteamchatlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.getstream.getsteamchatlibrary.utils.DateUtils;
import com.getstream.getsteamchatlibrary.utils.FileUtils;
import com.getstream.getsteamchatlibrary.utils.ImageUtils;
import com.getstream.getsteamchatlibrary.utils.MediaPlayerActivity;
import com.getstream.getsteamchatlibrary.utils.PhotoViewerActivity;
import com.getstream.getsteamchatlibrary.utils.UrlPreviewInfo;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_USER_MESSAGE_ME = 10;
    private static final int VIEW_TYPE_USER_MESSAGE_OTHER = 11;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_EDIT = 1;

    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 13;

    private ArrayList<Message> mMessageList = new ArrayList<Message>();
    private Context mContext;
    private HashMap<Message, CircleProgressBar> mFileMessageMap;


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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        Message message = mMessageList.get(position);
        boolean isNewDay = false;
        boolean isContinuous = false;



        if (position == 0) {
            isNewDay = true;
        } else if (position < mMessageList.size() - 1) {
            Message prevMessage = mMessageList.get(position - 1);

            // If the date of the previous message is different, display the date before the message,
            // and also set isContinuous to false to show information such as the sender's nickname
            // and profile image.

            if (!DateUtils.hasSameDate(message.getCreatedAt(), prevMessage.getCreatedAt())) {
                isNewDay = true;
                isContinuous = false;
            } else {
                isContinuous = isContinuous(message, prevMessage);
            }
        }

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_USER_MESSAGE_ME:
                ((MyUserMessageHolder) holder).bind(mContext, (Message) message, isContinuous, isNewDay, position);
                break;
            case VIEW_TYPE_USER_MESSAGE_OTHER:
                ((OtherUserMessageHolder) holder).bind(mContext, (Message) message, isNewDay, isContinuous, position);
                break;

        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showMessageOptionsDialog(mMessageList.get(position),position);
                return false;
            }
        });
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
        CardView card_group_chat_message;

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

            card_group_chat_message = (CardView) itemView.findViewById(R.id.card_group_chat_message);
            list_attachment = (NonScrollListView) itemView.findViewById(R.id.list_attachment);



        }

        void bind(Context context, final Message message, boolean isContinuous, boolean isNewDay, final int position) {
            messageText.setText(message.getMessage());
            timeText.setText(DateUtils.formatTime(message.getCreatedAt()));

            attachmentListAdapter = new AttachmentListAdapter(mContext,message,message.attachments,true);
            list_attachment.setAdapter(attachmentListAdapter);

            list_attachment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    onFileMessageClicked(message.attachments.get(i));

                }
            });


            if (message.getUpdatedAt().compareTo(message.getCreatedAt()) > 0) {
                editedText.setVisibility(View.VISIBLE);
            } else {
                editedText.setVisibility(View.GONE);
            }

            if(message.getMessage().equals("")){
                card_group_chat_message.setVisibility(View.GONE);
                timeText.setVisibility(View.GONE);
                readReceiptText.setVisibility(View.GONE);
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

        NonScrollListView list_attachment;
        AttachmentListAdapter attachmentListAdapter;
        CardView card_group_chat_message;

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

            card_group_chat_message = (CardView) itemView.findViewById(R.id.card_group_chat_message);
            list_attachment = (NonScrollListView) itemView.findViewById(R.id.list_attachment);


        }


        void bind(Context context, final Message message,boolean isNewDay, boolean isContinuous, final int position) {


            messageText.setText(message.getMessage());
            timeText.setText(DateUtils.formatTime(message.getCreatedAt()));

            attachmentListAdapter = new AttachmentListAdapter(mContext,message,message.attachments,false);
            list_attachment.setAdapter(attachmentListAdapter);

            list_attachment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    onFileMessageClicked(message.attachments.get(i));

                }
            });

            if (message.getUpdatedAt().compareTo(message.getCreatedAt()) > 0) {
                editedText.setVisibility(View.VISIBLE);
            } else {
                editedText.setVisibility(View.GONE);
            }

            if(message.getMessage().equals("")){
                card_group_chat_message.setVisibility(View.GONE);
                timeText.setVisibility(View.GONE);
                readReceiptText.setVisibility(View.GONE);
            }else{
                messageText.setText(message.getMessage());
            }


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
//                messageText.setVisibility(View.GONE);
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


    private void onFileMessageClicked(Attachment attachment) {
        String type = attachment.getType().toLowerCase();
        if (type.startsWith("image")) {
            Intent i = new Intent(mContext, PhotoViewerActivity.class);
            i.putExtra("url", attachment.getThumbnail());
            i.putExtra("type", type);
            mContext.startActivity(i);
        } else if (type.startsWith("video")) {
            Intent intent = new Intent(mContext, MediaPlayerActivity.class);
            intent.putExtra("url", type);
            mContext.startActivity(intent);
        } else {
        }
    }
    private void showMessageOptionsDialog(final Message message, final int position) {
        String[] options = new String[] { "Edit message", "Delete message" };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {

                    MessageListActivity.mCurrentState = STATE_EDIT;
                    MessageListActivity.mMessageEditText.setText(message.text);
                    MessageListActivity.mUploadFileButton.setVisibility(View.GONE);
                    MessageListActivity.mMessageSendButton.setText("SAVE");
                    MessageListActivity.mEditingMessage = message;

                    String messageString = message.getMessage();
                    if (messageString == null) {
                        messageString = "";
                    }
                    MessageListActivity.mMessageEditText.setText(messageString);
                    if (messageString.length() > 0) {
                        MessageListActivity.mMessageEditText.setSelection(0, messageString.length());
                    }

                    MessageListActivity.mMessageEditText.requestFocus();
                    MessageListActivity.mMessageEditText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MessageListActivity.mIMM.showSoftInput(MessageListActivity.mMessageEditText, 0);

                            MessageListActivity.mRecyclerView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    MessageListActivity.mRecyclerView.scrollToPosition(position);
                                }
                            }, 500);
                        }
                    }, 100);

                } else if (which == 1) {

                }
            }
        });
        builder.create().show();
    }

}
