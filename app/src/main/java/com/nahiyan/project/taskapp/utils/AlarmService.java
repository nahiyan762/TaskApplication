package com.nahiyan.project.taskapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.Toast;

import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.views.activitys.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.views.activitys.HomeActivity;

import java.util.ArrayList;
import java.util.Calendar;


public class AlarmService extends JobIntentService {

    final String TAG = "AlarmService";

    public static void enqueueWork(HomeActivity homeActivity, Intent intent) {
        enqueueWork(homeActivity, AlarmService.class, 123, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        getAllTask();
    }

    private void setAlarm(ArrayList<UserTasks> userTasksArrayList) {
        for(UserTasks userTasks: userTasksArrayList){
            String[] date = splitDate(userTasks.getTaskDate());
            String[] time = splitTime(userTasks.getTaskTime());
            setCalenderForAlarm(date,time,userTasks);
        }
    }

    private void setCalenderForAlarm(String[] date, String[] time, UserTasks userTasks) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, Integer.parseInt(date[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[0])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[1]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        calendar.set(Calendar.SECOND, 0);

        String requestCode = date[1]+date[0]+time[1]+time[0];
        int rqstCode = Integer.parseInt(requestCode);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("Body",userTasks.getTaskName());
        intent.putExtra("Title",userTasks.getTaskUserLists().getListName());
        intent.putExtra("TaskId",userTasks.getTaskId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), rqstCode, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private String[] splitTime(String time) {
        String[] separated = time.split(":");
        return separated;
    }

    private String[] splitDate(String date){
        String[] separated = date.split("/");
        return separated;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void getAllTask() {
        ArrayList<UserTasks> userTasksArrayList = new ArrayList<>();
        String auth = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userTasksArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserTasks userTasks = snapshot.getValue(UserTasks.class);
                    assert userTasks != null;
                    if(userTasks.getTaskOwner().equals(auth)){
                        if(userTasks.getTaskStatus().equals("Create") && userTasks.getTaskDate() != null && userTasks.getTaskTime() != null){
                            if(getAlarmTime(userTasks.getTaskDate(), userTasks.getTaskTime())){
                                userTasksArrayList.add(userTasks);
                            }
                        }
                    } else{
                        if(userTasks.getTaskAssign() != null){
                            if(userTasks.getTaskAssign().getId().equals(auth)){
                                if(userTasks.getTaskStatus().equals("Due") && userTasks.getTaskDate() != null && userTasks.getTaskTime() != null){
                                    if(getAlarmTime(userTasks.getTaskDate(), userTasks.getTaskTime())){
                                        userTasksArrayList.add(userTasks);
                                    }
                                }
                            }
                        }
                    }
                }
                setAlarm(userTasksArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean getAlarmTime(String taskDate, String taskTime) {
        String[] date = taskDate.split("/");
        String[] time = taskTime.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, Integer.parseInt(date[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[0])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[1]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        calendar.set(Calendar.SECOND, 0);

        if(calendar.getTimeInMillis() > System.currentTimeMillis()){
            return true;
        } else{
            return false;
        }


    }
}
