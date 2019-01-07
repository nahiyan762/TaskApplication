package com.nahiyan.project.taskapp.presenters.implementation;

import android.support.annotation.NonNull;

import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.views.activitys.TaskActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.views.activitys.TaskActivity;

import java.util.ArrayList;
import java.util.Objects;

public class TaskPresenterImpl {

    TaskActivity taskActivity;

    public TaskPresenterImpl(TaskActivity taskActivity) {
        this.taskActivity = taskActivity;
    }

    public void getUserListByListId(String listId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lists");
        reference.keepSynced(true);
        Query query = reference.orderByKey().equalTo(listId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserLists userLists = snapshot.getValue(UserLists.class);
                    taskActivity.getList(userLists);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUserTaskByTaskId(String taskId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.keepSynced(true);
        Query query = reference.orderByKey().equalTo(taskId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserTasks userTasks = snapshot.getValue(UserTasks.class);
                    taskActivity.getTask(userTasks);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUserTaskByAlarmTaskId(String alarmTaskId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.keepSynced(true);
        Query query = reference.orderByKey().equalTo(alarmTaskId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserTasks userTasks = snapshot.getValue(UserTasks.class);
                    taskActivity.getList(userTasks.getTaskUserLists());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
