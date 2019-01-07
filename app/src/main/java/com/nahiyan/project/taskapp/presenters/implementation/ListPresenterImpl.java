package com.nahiyan.project.taskapp.presenters.implementation;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nahiyan.project.taskapp.models.TaskList;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserListView;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserMessage;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.notifications.Token;
import com.nahiyan.project.taskapp.presenters.ListPresenter;
import com.nahiyan.project.taskapp.views.ListFragmentView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nahiyan.project.taskapp.models.UserListView;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.notifications.Token;

import java.util.ArrayList;
import java.util.Objects;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListPresenterImpl implements ListPresenter {

    private static String TAG = "MainActivity";
    private ListFragmentView listFragmentView;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private Disposable disposable;

    public ListPresenterImpl(ListFragmentView listFragmentView, FirebaseAuth auth) {
        this.listFragmentView = listFragmentView;
        this.mAuth = auth;
    }

    @Override
    public void getTaskLists(){
        final String userId = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Lists");
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<UserLists> userListsArrayList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userListsArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserLists userLists = snapshot.getValue(UserLists.class);
                    if(userLists.getListOwner().getId().equals(userId)){
                        if(userLists.getListAssignUser() == null){
                            userLists.setListAssignUser(new ArrayList<>());
                            userListsArrayList.add(userLists);
                        } else{
                            userListsArrayList.add(userLists);
                        }
                    } else {
                        if(userLists.getListAssignUser() != null){
                            for(User user: userLists.getListAssignUser()){
                                if(user.getId().equals(userId)){
                                    userListsArrayList.add(userLists);
                                }
                            }
                        }
                    }
                }

                listFragmentView.getList(userListsArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        disposable = RxFirebaseDatabase.observeSingleValueEvent(reference,DataSnapshotMapper.listOf(UserLists.class))
//                .toObservable()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(userLists -> {
//                            for(UserLists userLists1: userLists){
//                                if(userLists1.getListOwner().getId().equals(userId)){
//                                    if(userLists1.getListAssignUser() == null){
//                                        userLists1.setListAssignUser(new ArrayList<>());
//                                        userListsArrayList.add(userLists1);
//                                    } else{
//                                        userListsArrayList.add(userLists1);
//                                    }
//                                } else {
//                                    if(userLists1.getListAssignUser() != null){
//                                        for(User user: userLists1.getListAssignUser()){
//                                            if(user.getId().equals(userId)){
//                                                userListsArrayList.add(userLists1);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        },
//                        throwable -> {
//                            throw new Exception("UserLists");},
//                        () -> {
//                            dataRetrivingComplete();
//                            listFragmentView.getList(userListsArrayList);});
    }

    //update token for cloud message
    @Override
    public void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
//        reference.keepSynced(true);
        Token token1 = new Token(token);
        reference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(token1);
    }

    @Override
    public ListFragmentView getListFragmentView(){
        return listFragmentView;
    }

    @Override
    public void deleteUserLists(ArrayList<UserListView> selectedList) {
        ArrayList<UserLists> userLists = new ArrayList<>();
        for(UserListView userListView:selectedList){
            userLists.add(userListView.getUserLists());
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lists");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //Load all List in userLists1 from DB
                    UserLists userLists1 = snapshot.getValue(UserLists.class);
                    //Load selected List in userLists2 for delete
                    for(UserLists userLists2: userLists){
                        //check userLists1 & userLists2 are same or not
                        if(userLists1.getListId().equals(userLists2.getListId())){
                            //check list owner i am or not
                            if(userLists1.getListOwner().getId().equals(mAuth.getCurrentUser().getUid())){
                                snapshot.getRef().removeValue();
                                deleteTaskUser(userLists1);
                            } else{
                                //Load all the user from userLists1 ListAssignUser
                                if(userLists1.getListAssignUser() != null){
                                    for(User user : userLists1.getListAssignUser()){
                                        //check ListAssignUser have me or not
                                        if(user.getId().equals(mAuth.getCurrentUser().getUid())){
                                            userLists1.getListAssignUser().remove(user);
                                            break;
                                        }
                                    }
                                    reference.child(Objects.requireNonNull(snapshot.getKey())).child("listAssignUser").setValue(userLists1.getListAssignUser());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteTaskUser(UserLists userLists) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
        Query query = reference.orderByChild("taskOwner").equalTo(mAuth.getCurrentUser().getUid());
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserTasks userTasks = snapshot.getValue(UserTasks.class);
                    if(userTasks.getTaskUserLists().getListId().equals(userLists.getListId())){
                        snapshot.getRef().removeValue();
                        deleteMessageUser(userTasks);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteMessageUser(UserTasks userTasks) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserMessage userMessage = snapshot.getValue(UserMessage.class);
                    if(userMessage.getUserTasks().getTaskId().equals(userTasks.getTaskId())){
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getUser(String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    listFragmentView.setUser(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void searchList(String s) {
        final String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Lists");
        reference.keepSynced(true);
        ArrayList<UserLists> userListsArrayList = new ArrayList<>();

        disposable = RxFirebaseDatabase.observeSingleValueEvent(reference,DataSnapshotMapper.listOf(UserLists.class))
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userLists -> {
                        for(UserLists userLists1: userLists){
                            if(userLists1.getListOwner().getId().equals(userId)){
                                if(userLists1.getListAssignUser() == null){
                                    userLists1.setListAssignUser(new ArrayList<>());
                                    if(userLists1.getListName().equals(s)){
                                        userListsArrayList.add(userLists1);
                                    }
                                } else{
                                    if(userLists1.getListName().equals(s)){
                                        userListsArrayList.add(userLists1);
                                    }
                                }
                            } else {
                                if(userLists1.getListAssignUser() != null){
                                    for(User user: userLists1.getListAssignUser()){
                                        if(user.getId().equals(userId)){
                                            if(userLists1.getListName().equals(s)){
                                                userListsArrayList.add(userLists1);
                                            }
                                        }
                                    }
                                }
                            }

                        }
                        listFragmentView.getList(userListsArrayList);
                    },
                    throwable -> {
                        throw new Exception("UserLists");},
                    () -> {
                        dataRetrivingComplete();
                    });
    }

    private void dataRetrivingComplete() {
        if(disposable.isDisposed()){
            disposable.dispose();
        } else{
            Log.d(TAG, "dataRetrivingComplete: Dispose");
        }
    }

}
