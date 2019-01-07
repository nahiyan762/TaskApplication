package com.nahiyan.project.taskapp.views.activitys;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.adapter.MessageAdapter;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserMessage;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.presenters.MessagePresenter;
import com.nahiyan.project.taskapp.presenters.implementation.MessagePresenterImpl;
import com.nahiyan.project.taskapp.views.MessageView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.nahiyan.project.taskapp.adapter.MessageAdapter;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserMessage;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.presenters.MessagePresenter;
import com.nahiyan.project.taskapp.presenters.implementation.MessagePresenterImpl;
import com.nahiyan.project.taskapp.views.MessageView;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements View.OnClickListener, MessageView {

    UserTasks userTasks;
    FirebaseAuth mAuth;
    MessagePresenter messagePresenter;
    EditText et_message;
    User senderUser;
    User receiverUser;
    RecyclerView message_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.nahiyan.project.taskapp.R.layout.fragment_message, container, false);

        if(getArguments() != null){
            this.userTasks = (UserTasks) getArguments().getSerializable("Task");
        }

        Toolbar toolbar = view.findViewById(com.nahiyan.project.taskapp.R.id.toolbar);
        ((AppCompatActivity)Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("");
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getFragmentManager()).popBackStackImmediate();
            }
        });

        CircleImageView civ_image = view.findViewById(com.nahiyan.project.taskapp.R.id.civ_image);
        TextView tv_name = view.findViewById(com.nahiyan.project.taskapp.R.id.tv_name);
        this.message_view = view.findViewById(com.nahiyan.project.taskapp.R.id.message_view);
        et_message = view.findViewById(com.nahiyan.project.taskapp.R.id.et_message);
        ImageButton ib_sendMessage = view.findViewById(com.nahiyan.project.taskapp.R.id.ib_sendMessage);
        ib_sendMessage.setOnClickListener(this);
        this.mAuth = FirebaseAuth.getInstance();

        this.messagePresenter = new MessagePresenterImpl(mAuth,MessageFragment.this);

        if(userTasks.getTaskOwner().equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())){
            this.senderUser = userTasks.getTaskUserLists().getListOwner();
            this.receiverUser = userTasks.getTaskAssign();
            if(userTasks.getTaskUserLists().getListOwner().getImageUrl().equals("default")){
                civ_image.setBackgroundResource(com.nahiyan.project.taskapp.R.drawable.user);
            } else{
                Glide.with(AppMain.instance).load(userTasks.getTaskUserLists().getListOwner().getImageUrl()).into(civ_image);
            }
            tv_name.setText(userTasks.getTaskUserLists().getListOwner().getUsername());
        } else{
            this.senderUser = userTasks.getTaskAssign();
            this.receiverUser = userTasks.getTaskUserLists().getListOwner();
            if(userTasks.getTaskAssign().getImageUrl().equals(mAuth.getCurrentUser().getUid())){
                civ_image.setBackgroundResource(com.nahiyan.project.taskapp.R.drawable.user);
            } else{
                Glide.with(AppMain.instance).load(userTasks.getTaskAssign().getImageUrl()).into(civ_image);
            }
            tv_name.setText(userTasks.getTaskAssign().getUsername());
        }

        message_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        message_view.setLayoutManager(linearLayoutManager);
        this.messagePresenter.getMessage(senderUser,receiverUser,userTasks);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == com.nahiyan.project.taskapp.R.id.ib_sendMessage){
            String message = et_message.getText().toString();
            messagePresenter.sendMessage(message,senderUser,receiverUser,userTasks);
        }
    }

    @Override
    public void validationError() {
//        Toast.makeText(getContext(),"You can't send empty message",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void insertMessageSuccess() {
        et_message.setText("");
    }

    @Override
    public void setMessage(ArrayList<UserMessage> userMessages) {
        MessageAdapter messageAdapter = new MessageAdapter(getContext(),userMessages,mAuth);
        this.message_view.setAdapter(messageAdapter);
    }

    @Override
    public void sendPushMessageSuccess() {
        Toast.makeText(getContext(),"Notification Send Successfully",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendPushMessageFailed() {
        Toast.makeText(getContext(),"Notification Send Failed",Toast.LENGTH_SHORT).show();
    }
}
