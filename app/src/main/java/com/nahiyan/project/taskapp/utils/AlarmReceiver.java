package com.nahiyan.project.taskapp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.notifications.OreoNotification;
import com.nahiyan.project.taskapp.views.activitys.TaskActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String body = intent.getStringExtra("Body");
        String title = intent.getStringExtra("Title");
        String taskId = intent.getStringExtra("TaskId");

        Calendar c = Calendar.getInstance();
        int requestCode = c.get(Calendar.MILLISECOND);

        intent = new Intent(context, TaskActivity.class);
        intent.putExtra("AlarmTaskId",taskId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setTaskStatus(taskId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,requestCode,intent,PendingIntent.FLAG_ONE_SHOT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoNotification oreoNotification = new OreoNotification(AppMain.instance);
            Notification.Builder builder = oreoNotification.getOreoNotification(title,body, pendingIntent, defaultSound, String.valueOf(R.drawable.due));
            oreoNotification.getManager().notify(requestCode,builder.build());
        } else{
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.due)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            assert manager != null;
            manager.notify(requestCode,builder.build());
        }

    }

    private void setTaskStatus(String taskId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
        Query query = reference.orderByChild("taskId").equalTo(taskId);
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserTasks userTasks = snapshot.getValue(UserTasks.class);
                    assert userTasks != null;
                    if(userTasks.getTaskId().equals(taskId)){
                        if(userTasks.getTaskStatus().equals("Create")){
                            userTasks.setTaskStatus("Due");
                            reference.child(taskId).setValue(userTasks);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
