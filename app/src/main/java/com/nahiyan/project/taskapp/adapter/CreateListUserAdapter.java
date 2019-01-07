package com.nahiyan.project.taskapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.views.activitys.CreateListFragment;
import com.bumptech.glide.Glide;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.views.activitys.CreateListFragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateListUserAdapter extends RecyclerView.Adapter<CreateListUserAdapter.CreateListUserHolder>  {

    Context context;
    ArrayList<User> userArrayList;
    CreateListFragment createListFragment;

    public CreateListUserAdapter(Context context, ArrayList<User> userArrayList, CreateListFragment createListFragment) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.createListFragment = createListFragment;
    }

    @NonNull
    @Override
    public CreateListUserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.individual_assign_list_view,viewGroup,false);
        return new CreateListUserAdapter.CreateListUserHolder(view,createListFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateListUserHolder createListUserHolder, int i) {
        User user = userArrayList.get(i);

        if(user.getImageUrl().equals("default")){
            createListUserHolder.userImage.setBackgroundResource(R.drawable.user);
        } else{
            Glide.with(AppMain.instance).load(user.getImageUrl()).into(createListUserHolder.userImage);
        }

        createListUserHolder.userName.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class CreateListUserHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener {

        public RelativeLayout rl_view;
        public CircleImageView userImage;
        public TextView userName;
        public ImageView userDelete;
        CreateListFragment createListFragment;

        public CreateListUserHolder(@NonNull View itemView, CreateListFragment createListFragment) {
            super(itemView);
            userDelete = itemView.findViewById(R.id.userDelete);
            userDelete.setOnClickListener(this);
            rl_view = itemView.findViewById(R.id.rl_view);
            rl_view.setOnLongClickListener(this);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            this.createListFragment = createListFragment;
        }

        @Override
        public void onClick(View view) {
            createListFragment.deleteUserFromList(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if(userDelete.getVisibility() == View.GONE){
                userDelete.setVisibility(View.VISIBLE);
            } else{
                userDelete.setVisibility(View.GONE);
            }

            return true;
        }
    }
}
