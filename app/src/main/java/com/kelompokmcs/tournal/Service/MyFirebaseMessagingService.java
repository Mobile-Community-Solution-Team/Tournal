package com.kelompokmcs.tournal.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kelompokmcs.tournal.Activity.GroupActivity;
import com.kelompokmcs.tournal.Model.Group;
import com.kelompokmcs.tournal.R;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Notifikasi","From : "+ remoteMessage.getFrom());
        Log.d("Notifikasi","Notification Body : "+remoteMessage.getNotification().getBody());
        int groupId = Integer.parseInt(remoteMessage.getData().get("group_id"));
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),groupId);
    }

    private void showNotification(String title, String body,int groupId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String NOTIFICATION_ID = "SERVICEMSG";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID,"Notification",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Descriptions");
            notificationManager.createNotificationChannel(channel);
        }

        Intent resultIntent = new Intent(this, GroupActivity.class);
        resultIntent.putExtra("group_id",groupId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_ID);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notification_tournal)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(resultPendingIntent)
                .setContentInfo("info");

        notificationManager.notify(new Random().nextInt(),builder.build());
    }
}
