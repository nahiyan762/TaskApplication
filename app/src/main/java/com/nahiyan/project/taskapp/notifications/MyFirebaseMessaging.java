package com.nahiyan.project.taskapp.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.views.activitys.HomeActivity;
import com.nahiyan.project.taskapp.views.activitys.TaskActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.views.activitys.TaskActivity;

import java.util.Calendar;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sentId = remoteMessage.getData().get("sentId");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null && sentId.equals(firebaseUser.getUid())){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                sendOreoNotification(remoteMessage);
            } else{
                sendNotification(remoteMessage);
            }
        }
    }

    private void sendOreoNotification(RemoteMessage remoteMessage) {
        String id = remoteMessage.getData().get("listId");
        String ListOrTask = remoteMessage.getData().get("ListOrTask");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        PendingIntent pendingIntent = null;

        Calendar c = Calendar.getInstance();
        int requestCode = c.get(Calendar.MILLISECOND);

        if(Integer.parseInt(ListOrTask) == 0){
            Intent intent = new Intent(this, TaskActivity.class);
            intent.putExtra("ListId",id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(AppMain.instance,requestCode,intent,PendingIntent.FLAG_ONE_SHOT);
        } else if(Integer.parseInt(ListOrTask) == 1){
            Intent intent = new Intent(AppMain.instance, TaskActivity.class);
            intent.putExtra("TaskId",id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this,requestCode,intent,PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title,body, pendingIntent, defaultSound, icon);
        oreoNotification.getManager().notify(requestCode,builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String id = remoteMessage.getData().get("listId");
        String ListOrTask = remoteMessage.getData().get("ListOrTask");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        PendingIntent pendingIntent = null;

//        RemoteMessage.Notification notification = remoteMessage.getNotification();

        Calendar c = Calendar.getInstance();
        int requestCode = c.get(Calendar.MILLISECOND);

        if(Integer.parseInt(ListOrTask) == 0){
            Intent intent = new Intent(AppMain.instance, TaskActivity.class);
            intent.putExtra("ListId",id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(AppMain.instance,requestCode,intent,PendingIntent.FLAG_ONE_SHOT);
        } else if(Integer.parseInt(ListOrTask) == 1){
            Intent intent = new Intent(AppMain.instance, TaskActivity.class);
            intent.putExtra("TaskId",id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(AppMain.instance,requestCode,intent,PendingIntent.FLAG_ONE_SHOT);
        }



        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppMain.instance)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        assert manager != null;
        manager.notify(requestCode,builder.build());

    }
}
