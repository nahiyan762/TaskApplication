package com.nahiyan.project.taskapp.presenters.implementation;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.notifications.Client;
import com.nahiyan.project.taskapp.notifications.Data;
import com.nahiyan.project.taskapp.notifications.MyResponse;
import com.nahiyan.project.taskapp.notifications.Sender;
import com.nahiyan.project.taskapp.notifications.Token;
import com.nahiyan.project.taskapp.presenters.CreateTaskPresenter;
import com.nahiyan.project.taskapp.views.CreateTaskView;
import com.nahiyan.project.taskapp.views.activitys.CreateTaskFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.notifications.Data;
import com.nahiyan.project.taskapp.notifications.MyResponse;
import com.nahiyan.project.taskapp.notifications.Token;
import com.nahiyan.project.taskapp.presenters.CreateTaskPresenter;
import com.nahiyan.project.taskapp.views.CreateTaskView;
import com.nahiyan.project.taskapp.views.activitys.CreateTaskFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTaskPresenterImpl implements CreateTaskPresenter {

    private CreateTaskView createTaskView;
    private FirebaseAuth auth;
    private Client client;
    private DatabaseReference reference;

    public CreateTaskPresenterImpl(CreateTaskFragment createTaskView, FirebaseAuth mAuth) {
        this.createTaskView = createTaskView;
        this.auth = mAuth;
        if (this.client == null) {
            this.client = new Client();
        }
    }


    @Override
    public void setTask(UserTasks userTasks) {
        if(!TextUtils.isEmpty(userTasks.getTaskName())){
            if(userTasks.getTaskAssign() != null){
                sendMessage(userTasks);
                setDataInFirebase(userTasks);
            }
            else{
                setDataInFirebase(userTasks);
            }
        } else{
            createTaskView.showValidationError(false);
        }
    }

    private void setDataInFirebase(UserTasks userTasks) {
        reference = FirebaseDatabase.getInstance().getReference("Tasks");
        reference.keepSynced(true);
            if(userTasks.getTaskId()!= null){
                String taskId = userTasks.getTaskId();
                reference.child(taskId).setValue(userTasks);
                createTaskView.dataInsertionSuccessfull();
            } else{
                String userId = auth.getCurrentUser().getUid();
                String taskId = reference.push().getKey();
                userTasks.setTaskOwner(userId);
                userTasks.setTaskStatus("Create");
                userTasks.setTaskId(taskId);
                reference.child(taskId).setValue(userTasks);
                createTaskView.dataInsertionSuccessfull();
            }
    }

    private void sendMessage(final UserTasks userTasks) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
//        reference.keepSynced(true);
        Query query = tokens.orderByKey().equalTo(userTasks.getTaskAssign().getId());
//        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Token token =  snapshot.getValue(Token.class);
                    Data data = new Data(userTasks.getTaskUserLists().getListId(),0,R.drawable.project,
                            userTasks.getTaskName(),userTasks.getTaskUserLists().getListName(),userTasks.getTaskAssign().getId());
                    Sender sender = new Sender(data,token.getToken());

                        Client.getClient()
                            .sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        assert response.body() != null;
                                        if(response.body().success != 1){
                                            createTaskView.sendPushMessageFailed();
                                        } else{
                                            setDataInFirebase(userTasks);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
