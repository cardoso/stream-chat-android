package io.getstream.chat.sdk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.List;

public class CCFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = CCFirebaseMessagingService.class.getSimpleName();

    public interface ResultBookingNotification {
        void handleResult(String message, String title);
    }

    public static ResultBookingNotification mResultBookingNotification;

    public void setResultBookingNotification(ResultBookingNotification resultBookingNotification) {
        if (mResultBookingNotification == null)
            mResultBookingNotification = resultBookingNotification;
    }

    public CCFirebaseMessagingService() {
        Log.d(TAG, "create");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: " + remoteMessage.toString());
        String Message = "";
        String title = "";
        try {

            Message = remoteMessage.getData().get("body");
            title = remoteMessage.getData().get("title");
            Log.d(TAG, "Received Message: " + Message);
            Log.d(TAG, "Received title: " + title);
            int badge = Integer.parseInt(remoteMessage.getData().get("badge"));
            Log.d("notificationNUmber",":"+badge);
            setBadge(getApplicationContext(), badge);

            if (mResultBookingNotification != null)
                mResultBookingNotification.handleResult(Message, title);
            sendNotification(Message, title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            Log.e("classname","null");
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }
    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Device Token: " + token);
    }

    private void sendNotification(String message, String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("notification", true);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        Notification notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(getBitmapIcon())
                .setTicker(getResources().getString(R.string.app_name))
                .setWhen(0)
                .setAutoCancel(true).setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setSound(defaultSoundUri)
                .build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = (int) Calendar.getInstance().getTimeInMillis();
        notificationManager.notify(notificationId, notification);
    }

    private Bitmap getBitmapIcon() {
        return BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
    }
}
