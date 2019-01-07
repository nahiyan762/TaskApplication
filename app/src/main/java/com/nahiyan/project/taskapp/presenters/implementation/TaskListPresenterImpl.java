package com.nahiyan.project.taskapp.presenters.implementation;

import android.support.annotation.NonNull;

import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserMessage;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.presenters.TaskListPresenter;
import com.nahiyan.project.taskapp.views.TaskListView;
import com.nahiyan.project.taskapp.views.activitys.TaskListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.views.TaskListView;

import java.util.ArrayList;
import java.util.Objects;

public class TaskListPresenterImpl implements TaskListPresenter {

    private TaskListView taskView;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    public TaskListPresenterImpl(FirebaseAuth auth, TaskListFragment taskListFragment) {
        this.auth = auth;
        this.taskView = taskListFragment;
    }

    @Override
    public void getTaskData(String listId) {
        reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            ArrayList<UserTasks> userTasksArrayList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userTasksArrayList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserTasks userTasks = snapshot.getValue(UserTasks.class);
                    if(Objects.requireNonNull(userTasks).getTaskUserLists().getListId().equals(listId)){
                        if(userTasks.getTaskOwner().equals(auth.getCurrentUser().getUid())){
                            userTasksArrayList.add(userTasks);
                        } else{
                            if(userTasks.getTaskAssign() != null){
                                if(userTasks.getTaskAssign().getId().equals(auth.getCurrentUser().getUid())){
                                    userTasksArrayList.add(userTasks);
                                }
                            }
                        }
                    }
                }
                taskView.setTask(userTasksArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setStatus(UserTasks userTasks) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.keepSynced(true);
        reference.child(userTasks.getTaskId()).setValue(userTasks);
    }

    @Override
    public void getDoneTaskData(String listId) {
        reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            ArrayList<UserTasks> userTasksArrayList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!userTasksArrayList.isEmpty()){
                    userTasksArrayList.clear();
                }
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserTasks userTasks = snapshot.getValue(UserTasks.class);
                    if(userTasks.getTaskUserLists().getListId().equals(listId)){
                        if(userTasks.getTaskOwner().equals(auth.getCurrentUser().getUid())){
                            if(userTasks.getTaskStatus().equals("Done")){
                                userTasksArrayList.add(userTasks);
                            }
                        }else{
                            if(userTasks.getTaskAssign() != null){
                                if(userTasks.getTaskAssign().getId().equals(auth.getCurrentUser().getUid())){
                                    if(userTasks.getTaskStatus().equals("Done")){
                                        userTasksArrayList.add(userTasks);
                                    }
                                }
                            }
                        }
                    }
                }
                taskView.setTask(userTasksArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getPauseTaskData(String listId) {
        reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            ArrayList<UserTasks> userTasksArrayList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!userTasksArrayList.isEmpty()){
                    userTasksArrayList.clear();
                }
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserTasks userTasks = snapshot.getValue(UserTasks.class);
                    if(userTasks.getTaskUserLists().getListId().equals(listId)){
                        if(userTasks.getTaskOwner().equals(auth.getCurrentUser().getUid())){
                            if(userTasks.getTaskStatus().equals("Pause")){
                                userTasksArrayList.add(userTasks);
                            }
                        } else{
                            if(userTasks.getTaskAssign() != null){
                                if(userTasks.getTaskAssign().getId().equals(auth.getCurrentUser().getUid())){
                                    if(userTasks.getTaskStatus().equals("Pause")){
                                        userTasksArrayList.add(userTasks);
                                    }
                                }
                            }
                        }
                    }
                }
                taskView.setTask(userTasksArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getProgressTaskData(String listId) {
        reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            ArrayList<UserTasks> userTasksArrayList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!userTasksArrayList.isEmpty()){
                    userTasksArrayList.clear();
                }
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserTasks userTasks = snapshot.getValue(UserTasks.class);
                    if(userTasks.getTaskUserLists().getListId().equals(listId)){
                        if(userTasks.getTaskOwner().equals(auth.getCurrentUser().getUid())){
                            if(userTasks.getTaskStatus().equals("Progress")){
                                userTasksArrayList.add(userTasks);
                            }
                        } else{
                            if(userTasks.getTaskAssign() != null){
                                if(userTasks.getTaskAssign().getId().equals(auth.getCurrentUser().getUid())){
                                    if(userTasks.getTaskStatus().equals("Progress")){
                                        userTasksArrayList.add(userTasks);
                                    }
                                }
                            }
                        }
                    }
                }
                taskView.setTask(userTasksArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void deleteTask(UserTasks userTasks) {
        reference = FirebaseDatabase.getInstance().getReference("Tasks").child(userTasks.getTaskId());
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserTasks userTasks1 = dataSnapshot.getValue(UserTasks.class);
                    if(userTasks1.getTaskOwner().equals(auth.getCurrentUser().getUid())){
                        dataSnapshot.getRef().removeValue();
                    } else{
                        userTasks1.setTaskAssign(null);
                        userTasks1.setTaskStatus("Create");
                        reference.setValue(userTasks1);
                    }
                deleteMessage(userTasks1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteMessage(UserTasks userTasks) {
        reference = FirebaseDatabase.getInstance().getReference("Messages");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserMessage userMessage = snapshot.getValue(UserMessage.class);
                    if(userMessage.getUserTasks().getTaskId().equals(userTasks.getTaskId()) &&
                            userMessage.getUserTasks().getTaskOwner().equals(auth.getCurrentUser().getUid())){
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
