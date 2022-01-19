package com.inland.pilot;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String KEY_CHANNEL_ID="notification_channel";
    private static final String KEY_CHANNEL_NAME="in.inlandworldlogisticsmanagement";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        Log.e("nottttt",remoteMessage.getNotification().getTitle());
        if(remoteMessage.getNotification() != null)
            generateNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    void generateNotification(String title, String message){

        Intent intent = new Intent(this, com.inland.pilot.NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),KEY_CHANNEL_ID)
                .setSmallIcon(R.drawable.app_logo)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000,1000,1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        builder = builder.setContent(getRemoteView(title,message));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(KEY_CHANNEL_ID, KEY_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0,builder.build());
    }

    private RemoteViews getRemoteView(String title, String message) {
        @SuppressLint("RemoteViewLayout") RemoteViews remoteViews= new RemoteViews("in.inlandworldlogisticsmanagement",R.layout.notification);

        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);

        return remoteViews;
    }
}
