package io.getstream.chat.sdk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.getstream.sdk.chat.model.User;
import com.getstream.sdk.chat.rest.core.StreamChat;
import com.getstream.sdk.chat.view.fragment.ChannelListFragment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        setStream(R.id.title_fragment);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener((@NonNull Task<InstanceIdResult> task) -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "getInstanceId failed:" + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
//                    sendMessge(token);
                    // Log and toast
                    String msg = "device Token: " + token;
                    Log.d(TAG, msg);
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                });


    }

    void setStream(@NonNull int containerResId) {
        StreamChat streamChat = new StreamChat(AppSecretKey.API_KEY);
        User user = new User(AppSecretKey.USER_ID, AppSecretKey.USER_NAME, AppSecretKey.USER_IMAGE);
        streamChat.setUser(user, AppSecretKey.USER_TOKEN);
        navigationChatListFragment(containerResId, streamChat);
    }
    private void sendMessge(String tokenId){
        String token1 = "fiBrgKPMVA4:APA91bFMgb1atoIea8w1BeY8gHdSDy2ve5XfZNI9BN_PZMirOZ8NT1MsYtMzmGuuMfBnLjlipV6YOW-EUvxJoEhAflRv0C9iNaQUvcuTYngHyTWiIQthidNSOdqFVKS6aoj-hZhioOOQ";
        String tokenEmulator = "c4wpRpwrbcI:APA91bFkVpgJPPZwLj_31IvoFV7GuTpkByybvI7QxWVelVpUhIF4fScRCUcVH0N1b_MqMt6TK5l7wViBhhcK1lthQabX8Mi1IhrS2YVDY-rj36Ea4a-sk5gbgjnrMHUMhb5dzq5aK8J_";
        RemoteMessage message = new RemoteMessage.Builder(token1)
                .setMessageId("Test")
                .addData("message", "Hello")
                .build();

        FirebaseMessaging.getInstance().send(message);
    }
    private void navigationChatListFragment(@NonNull int containerResId, @NonNull StreamChat streamChat) {
        ChannelListFragment fragment = new ChannelListFragment();
        fragment.containerResId = containerResId;
        fragment.streamChat = streamChat;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerResId, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
