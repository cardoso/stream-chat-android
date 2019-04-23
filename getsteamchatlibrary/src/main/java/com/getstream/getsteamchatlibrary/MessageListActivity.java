package com.getstream.getsteamchatlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;

public class MessageListActivity extends AppCompatActivity{

    MessagesList messagesList;


    private RelativeLayout mRootLayout;
    static RecyclerView mRecyclerView;
    public static MessageListAdapter messageListAdapter;
    private LinearLayoutManager mLayoutManager;
    static EditText mMessageEditText;
    static Button mMessageSendButton;
    static ImageButton mUploadFileButton;
    private View mCurrentEventLayout;
    private TextView mCurrentEventText;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_EDIT = 1;
    static int mCurrentState = STATE_NORMAL;

    static Message mEditingMessage = null;
    static InputMethodManager mIMM;

    static Channel mChannel;


    private static final int INTENT_REQUEST_CHOOSE_MEDIA = 301;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 13;

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
                            mChannel.updateMessage(userInput,mEditingMessage.id);
                        }
                    }
                    mUploadFileButton.setVisibility(View.VISIBLE);
                    mMessageSendButton.setText("SEND");
                    mMessageEditText.setText("");

                    mCurrentState = STATE_NORMAL;
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

        mUploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMedia();
            }
        });

    }

    private void editMessage(final Message message, String editedMessage) {
        if (mChannel == null) {
            return;
        }

    }
    private void requestMedia() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If storage permissions are not granted, request permissions at run-time,
            // as per < API 23 guidelines.
            requestStoragePermissions();
        } else {
            Intent intent = new Intent();

            // Pick images or videos
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setType("*/*");
                String[] mimeTypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            } else {
                intent.setType("image/* video/*");
            }

            intent.setAction(Intent.ACTION_GET_CONTENT);

            // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Media"), INTENT_REQUEST_CHOOSE_MEDIA);

            // Set this as false to maintain connection
            // even when an external Activity is started.

        }
    }

    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(mRootLayout, "Storage access permissions are required to upload/download files.",
                    Snackbar.LENGTH_LONG)
                    .setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {
            // Permission has not been granted yet. Request it directly.
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == INTENT_REQUEST_CHOOSE_MEDIA && resultCode == Activity.RESULT_OK) {
            // If user has successfully chosen the image, show a dialog to confirm upload.
            if (data == null) {
                return;
            }
            ArrayList<String> mArrayUri = new ArrayList<String>();
            if(data.getData()!=null) {

                Uri mImageUri = data.getData();
                mArrayUri.add(mImageUri.toString());
            }else{
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri.toString());
                    }
                }
            }

//            Toast.makeText(this, data.getType(), Toast.LENGTH_SHORT).show();


            mChannel.sendImageMessage("",mArrayUri);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mCurrentState == STATE_EDIT) {
            mCurrentState = STATE_NORMAL;
        }

        mIMM.hideSoftInputFromWindow(mMessageEditText.getWindowToken(), 0);
    }
}
