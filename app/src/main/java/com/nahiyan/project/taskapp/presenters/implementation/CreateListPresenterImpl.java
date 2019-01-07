package com.nahiyan.project.taskapp.presenters.implementation;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.nahiyan.project.taskapp.models.TaskList;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.presenters.CreateListPresenter;
import com.nahiyan.project.taskapp.views.CreateListView;
import com.nahiyan.project.taskapp.views.activitys.CreateListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.presenters.CreateListPresenter;
import com.nahiyan.project.taskapp.views.CreateListView;
import com.nahiyan.project.taskapp.views.activitys.CreateListFragment;

import java.util.ArrayList;
import java.util.List;

public class CreateListPresenterImpl implements CreateListPresenter {

    private FirebaseAuth mAuth;
    private CreateListView createListView;

    public CreateListPresenterImpl(CreateListFragment createListFragment, FirebaseAuth auth) {
        this.createListView = createListFragment;
        this.mAuth = auth;
    }

    //Check List Name empty and
    @Override
    public void addListName(String listName, List<User> selectedUsers) {
        if(TextUtils.isEmpty(listName)){
            createListView.showValidationError("List Name Required!");
        } else{
            getUser(listName,selectedUsers);
        }

    }

    private void getUser(String listName, List<User> selectedUsers) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                addListInFirebase(user,listName,selectedUsers);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void addListInFirebase(User user, String listName, List<User> selectedUsers){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lists");
        reference.keepSynced(true);
        String listId = reference.push().getKey();
        User listOwner = user;
        UserLists userLists = new UserLists(listId,listName,listOwner,selectedUsers);
        reference.child(listId).setValue(userLists);
        createListView.insertListSuccess();
    }


    @Override
    public void editListName(String listName, List<User> selectedUsers, UserLists userLists) {
        if(TextUtils.isEmpty(listName)){
            createListView.showValidationError("List Name Required!");
        } else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lists");
            reference.keepSynced(true);
            userLists.setListName(listName);
            userLists.setListAssignUser(selectedUsers);
            reference.child(userLists.getListId()).setValue(userLists);
            editTask(userLists);
        }
    }

    private void editTask(UserLists userLists) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserTasks userTasks = snapshot.getValue(UserTasks.class);
                    if(userTasks.getTaskUserLists().getListId().equals(userLists.getListId())){
                        userTasks.getTaskUserLists().setListAssignUser(userLists.getListAssignUser());
                        userTasks.getTaskUserLists().setListName(userLists.getListName());
                        reference.child(userTasks.getTaskId()).setValue(userTasks);
                        createListView.insertListSuccess();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void searchUser(String newText) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.keepSynced(true);
        final String userId = mAuth.getCurrentUser().getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(user.getEmail().equals(newText)){
                        if(!user.getId().equals(userId)){
                            createListView.setUsers(user);
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
