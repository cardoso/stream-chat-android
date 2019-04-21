package com.getstream.getsteamchatlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;

public class MessageListActivity extends AppCompatActivity{

    MessagesList messagesList;


    private RelativeLayout mRootLayout;
    static RecyclerView mRecyclerView;
    public static MessageListAdapter messageListAdapter;
    private LinearLayoutManager mLayoutManager;
    private EditText mMessageEditText;
    private Button mMessageSendButton;
    private ImageButton mUploadFileButton;
    private View mCurrentEventLayout;
    private TextView mCurrentEventText;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_EDIT = 1;
    private int mCurrentState = STATE_NORMAL;

    private Message mEditingMessage = null;
    private InputMethodManager mIMM;

    static Channel mChannel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mIMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        String channelType = getIntent().getStringExtra("channelType");
        String channelId = getIntent().getStringExtra("channelId");

        Member member = new Member();
        member.user = ChannelListsActivity.me;

        ArrayList<Member> members = new ArrayList<Member>();
        members.add(member);


        mChannel = ChannelListsActivity.client.channel(channelType, channelId, "Private Chat About the Kingdom", "https://bit.ly/2F3KEoM", members, 8);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_group_chat);

        mCurrentEventLayout = findViewById(R.id.layout_group_chat_current_event);
        mCurrentEventText = (TextView) findViewById(R.id.text_group_chat_current_event);

        mMessageEditText = (EditText) findViewById(R.id.edittext_group_chat_message);
        mMessageSendButton = (Button) findViewById(R.id.button_group_chat_send);
        mUploadFileButton = (ImageButton) findViewById(R.id.button_group_chat_upload);



        messageListAdapter = new MessageListAdapter(this, mChannel.messageLists);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(messageListAdapter);
        mRecyclerView.smoothScrollToPosition(mChannel.messageLists.size()-1);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mMessageSendButton.setEnabled(true);
                } else {
                    mMessageSendButton.setEnabled(false);
                }
            }
        });
        mMessageSendButton.setEnabled(false);

        mMessageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState == STATE_EDIT) {
                    String userInput = mMessageEditText.getText().toString();
                    if (userInput.length() > 0) {
                        if (mEditingMessage != null) {
                            editMessage(mEditingMessage, userInput);
                        }
                    }
                    setState(STATE_NORMAL, null, -1);
                } else {
                    String newMessage = mMessageEditText.getText().toString();
                    if (newMessage.length() > 0) {
                        if(mChannel == null){
                            return;
                        }
                        mChannel.sendMessage(newMessage);
                        mMessageEditText.setText("");
                    }
                }
            }
        });

    }

    private void editMessage(final Message message, String editedMessage) {
        if (mChannel == null) {
            return;
        }

    }
    private void setState(int state, Message editingMessage, final int position) {
        switch (state) {
            case STATE_NORMAL:
                mCurrentState = STATE_NORMAL;
                mEditingMessage = null;

                mUploadFileButton.setVisibility(View.VISIBLE);
                mMessageSendButton.setText("SEND");
                mMessageEditText.setText("");
                break;

            case STATE_EDIT:
                mCurrentState = STATE_EDIT;
                mEditingMessage = editingMessage;

                mUploadFileButton.setVisibility(View.GONE);
                mMessageSendButton.setText("SAVE");
                String messageString = ((Message)editingMessage).getMessage();
                if (messageString == null) {
                    messageString = "";
                }
                mMessageEditText.setText(messageString);
                if (messageString.length() > 0) {
                    mMessageEditText.setSelection(0, messageString.length());
                }

                mMessageEditText.requestFocus();
                mMessageEditText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIMM.showSoftInput(mMessageEditText, 0);

                        mRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.scrollToPosition(position);
                            }
                        }, 500);
                    }
                }, 100);
                break;
        }
    }

        /*
        // Message Input


        MessageInput messageInput = (MessageInput) findViewById(R.id.input);
        messagesList = (MessagesList)findViewById(R.id.messagesList);



        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message
                String message = input.toString();
                if (message.length() > 0){
                    channel.sendMessage(message);
                }
                return true;
            }
        });

        messageInput.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                //select attachments
            }
        });

        messageInput.setTypingListener(new MessageInput.TypingListener() {
            @Override
            public void onStartTyping() {

            }

            @Override
            public void onStopTyping() {

            }
        });


        initAdapter();

    }
    private void initAdapter() {

        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Picasso.with(MessageListActivity.this).load(url).into(imageView);
            }
        };

        //We can pass any data to ViewHolder with payload
        CustomIncomingTextMessageViewHolder.Payload payload = new CustomIncomingTextMessageViewHolder.Payload();
        //For example click listener
        payload.avatarClickListener = new CustomIncomingTextMessageViewHolder.OnAvatarClickListener() {
            @Override
            public void onAvatarClick() {
                Toast.makeText(MessageListActivity.this,
                        "Text message avatar clicked", Toast.LENGTH_SHORT).show();
            }
        };

        MessageHolders holdersConfig = new MessageHolders()
                .setIncomingTextConfig(
                        CustomIncomingTextMessageViewHolder.class,
                        R.layout.item_custom_incoming_text_message,
                        payload)
                .setOutcomingTextConfig(
                        CustomOutcomingTextMessageViewHolder.class,
                        R.layout.item_custom_outcoming_text_message)
                .setIncomingImageConfig(
                        CustomIncomingImageMessageViewHolder.class,
                        R.layout.item_custom_incoming_image_message)
                .setOutcomingImageConfig(
                        CustomOutcomingImageMessageViewHolder.class,
                        R.layout.item_custom_outcoming_image_message);

        messagesAdapter = new MessagesListAdapter<Message>(ChannelListsActivity.me.id, holdersConfig, imageLoader);
//        messagesAdapter.setOnMessageLongClickListener(this);
        messagesAdapter.setLoadMoreListener(this);
        messagesList.setAdapter(messagesAdapter);
        if(channel.messageLists.size() > 0)
            messagesAdapter.addToEnd(channel.messageLists,true);
    }


    @Override
    public void onLoadMore(int page, int totalItemsCount) {

    }

    @Override
    public void onSelectionChanged(int count) {

    }

    @Override
    public void onMessageLongClick(Message message) {

    }

    */
}
