package com.nahiyan.project.taskapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.UserMessage;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.nahiyan.project.taskapp.application.AppMain;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Context context;
    ArrayList<UserMessage> messageArrayList;
    FirebaseAuth auth;

    public MessageAdapter(Context context, ArrayList<UserMessage> messageArrayList, FirebaseAuth auth) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        this.auth = auth;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,viewGroup,false);
            return new MessageAdapter.MessageHolder(view);
        } else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageAdapter.MessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder messageHolder, int i) {
        UserMessage userMessage = messageArrayList.get(i);
        messageHolder.tv_message.setText(userMessage.getMessage());
        if(userMessage.getSender().getImageUrl().equals("default")){
            messageHolder.civ_image.setBackgroundResource(R.drawable.user);
        } else{
            Glide.with(AppMain.instance).load(userMessage.getSender().getImageUrl()).into(messageHolder.civ_image);
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messageArrayList.get(position).getSender().getId().equals(auth.getCurrentUser().getUid())){
            return MSG_TYPE_RIGHT;
        } else{
            return MSG_TYPE_LEFT;
        }
    }

    public static class MessageHolder extends RecyclerView.ViewHolder{

        CircleImageView civ_image;
        TextView tv_message;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            civ_image = itemView.findViewById(R.id.civ_image);
            tv_message = itemView.findViewById(R.id.tv_message);
        }
    }
}
