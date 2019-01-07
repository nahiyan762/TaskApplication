package com.nahiyan.project.taskapp.presenters.implementation;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserMessage;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.notifications.Client;
import com.nahiyan.project.taskapp.notifications.Data;
import com.nahiyan.project.taskapp.notifications.MyResponse;
import com.nahiyan.project.taskapp.notifications.Sender;
import com.nahiyan.project.taskapp.notifications.Token;
import com.nahiyan.project.taskapp.presenters.MessagePresenter;
import com.nahiyan.project.taskapp.views.MessageView;
import com.nahiyan.project.taskapp.views.activitys.MessageFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.notifications.Data;
import com.nahiyan.project.taskapp.notifications.MyResponse;
import com.nahiyan.project.taskapp.notifications.Token;
import com.nahiyan.project.taskapp.presenters.MessagePresenter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagePresenterImpl implements MessagePresenter {

    private FirebaseAuth mAuth;
    private MessageView messageView;
    private DatabaseReference reference;

    public MessagePresenterImpl(FirebaseAuth mAuth, MessageFragment messageFragment) {
        this.mAuth = mAuth;
        this.messageView = messageFragment;
    }

    @Override
    public void sendMessage(String message, User senderUser, User receiverUser, UserTasks userTasks) {
        if(TextUtils.isEmpty(message)){
            messageView.validationError();
        } else{
            UserMessage userMessage = new UserMessage(message,senderUser,receiverUser,userTasks);
            reference = FirebaseDatabase.getInstance().getReference("Messages");
            reference.keepSynced(true);
            String id = reference.push().getKey();
            reference.child(id).setValue(userMessage);
            sendNotification(userMessage);
            messageView.insertMessageSuccess();
        }
    }

    private void sendNotification(UserMessage userMessage) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(userMessage.getReceiver().getId());
//        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Token token =  snapshot.getValue(Token.class);
                    Data data = new Data(userMessage.getUserTasks().getTaskId(),1,R.drawable.message,
                            userMessage.getMessage(),userMessage.getUserTasks().getTaskName(),userMessage.getReceiver().getId());
                    Sender sender = new Sender(data,token.getToken());

                    Client.getClient()
                            .sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        assert response.body() != null;
                                        if(response.body().success != 1){
//                                            messageView.sendPushMessageFailed();
                                        } else{
//                                            messageView.sendPushMessageSuccess();
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

    @Override
    public void getMessage(User senderUser, User receiverUser, UserTasks userTasks) {
        ArrayList<UserMessage> userMessages = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Messages");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userMessages.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserMessage userMessage = snapshot.getValue(UserMessage.class);
                    if(userMessage.getReceiver().getId().equals(senderUser.getId()) &&
                        userMessage.getSender().getId().equals(receiverUser.getId()) &&
                        userMessage.getUserTasks().getTaskId().equals(userTasks.getTaskId()) ||
                        userMessage.getReceiver().getId().equals(receiverUser.getId()) &&
                        userMessage.getSender().getId().equals(senderUser.getId()) &&
                        userMessage.getUserTasks().getTaskId().equals(userTasks.getTaskId())){

                        userMessages.add(userMessage);
                    }
                }
                messageView.setMessage(userMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
