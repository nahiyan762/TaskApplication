package com.nahiyan.project.taskapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserListView;
import com.nahiyan.project.taskapp.presenters.ListPresenter;
import com.nahiyan.project.taskapp.presenters.implementation.ListPresenterImpl;
import com.nahiyan.project.taskapp.utils.Utils;
import com.nahiyan.project.taskapp.views.activitys.CreateListFragment;
import com.nahiyan.project.taskapp.views.activitys.ListFragment;
import com.nahiyan.project.taskapp.views.activitys.TaskActivity;
import com.nahiyan.project.taskapp.views.activitys.TaskListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.nahiyan.project.taskapp.models.UserListView;
import com.nahiyan.project.taskapp.views.activitys.CreateListFragment;
import com.nahiyan.project.taskapp.views.activitys.ListFragment;
import com.nahiyan.project.taskapp.views.activitys.TaskActivity;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyListHolder> implements Filterable {

    private Context context;
    private ArrayList<UserListView> userListViews;
    private ArrayList<UserListView> userListFull;
    private ListFragment listFragment;
    private FirebaseAuth mAuth;

    public MyListAdapter(Context context, ArrayList<UserListView> userListViews, ListFragment listFragment, FirebaseAuth mAuth) {
        this.context = context;
        this.userListViews = userListViews;
        this.userListFull = new ArrayList<>(userListViews);
        this.listFragment = listFragment;
        this.mAuth = mAuth;
    }

    @NonNull
    @Override
    public MyListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.individual_list_view,viewGroup,false);
        return new MyListHolder(view,listFragment,context,userListViews);
    }

    @Override
    public void onBindViewHolder(@NonNull MyListHolder myListHolder, final int position) {

        myListHolder.tv_listName.setText(userListViews.get(position).getUserLists().getListName());
        if(userListViews.get(position).getUserLists().getListOwner().getId().equals(mAuth.getCurrentUser().getUid())){
            myListHolder.tv_listOwner.setText("OWN");
        } else{
            myListHolder.tv_listOwner.setText(userListViews.get(position).getUserLists().getListOwner().getUsername());
        }
        myListHolder.viewForeground.setBackgroundColor(Color.WHITE);
        myListHolder.iv_listEdit.setTag(position);
        myListHolder.iv_listEdit.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return userListViews.size();
    }

    public static class MyListHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

        public RelativeLayout viewForeground;
        public TextView tv_listName,tv_listOwner;
        public ImageView iv_listEdit;
        ListFragment listFragment;
        Context context;
        ArrayList<UserListView> userListViews;

        public MyListHolder(@NonNull View itemView, ListFragment listFragment, Context context, ArrayList<UserListView> userListViews) {
            super(itemView);
            this.context = context;
            this.listFragment = listFragment;
            this.userListViews = userListViews;

            viewForeground = itemView.findViewById(R.id.view_foreground);
            viewForeground.setMotionEventSplittingEnabled(false);
            viewForeground.setOnLongClickListener(this);
            viewForeground.setOnClickListener(this);
            tv_listName = itemView.findViewById(R.id.tv_listName);
            tv_listOwner = itemView.findViewById(R.id.tv_listOwner);
            iv_listEdit = itemView.findViewById(R.id.iv_listEdit);
            iv_listEdit.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listFragment.onItemLongClick(view,this.getAdapterPosition(),iv_listEdit);
            return true;
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.view_foreground){
                if(listFragment.isInActionMood){
                    listFragment.onItemClick(view,getLayoutPosition(),iv_listEdit);
                } else {
                    Intent intent = new Intent(context, TaskActivity.class);
                    intent.putExtra("UserLists",userListViews.get(getAdapterPosition()).getUserLists());
                    context.startActivity(intent);
                }
            } else if(view.getId() == R.id.iv_listEdit){
                Bundle bundle = new Bundle();
                bundle.putSerializable("UserListsObject", userListViews.get(getAdapterPosition()).getUserLists());
                CreateListFragment createListFragment = new CreateListFragment();
                createListFragment.setArguments(bundle);

                FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.listContainer,createListFragment, "AddListFragment")
                    .addToBackStack("ListFragment");;

                transaction.commit();
            }
        }
    }

    public void removeItem(ArrayList<UserListView> lists) {
        for(UserListView userLists : lists){
            this.notifyItemRemoved(userListViews.indexOf(userLists));
            userListViews.remove(userLists);
            this.notifyDataSetChanged();
        }
    }

    public void refreshList(ArrayList<UserListView> selectedList){
        for(UserListView userLists : selectedList){
            int index = userListViews.indexOf(userLists);
            userListViews.get(index).setSelected(false);
            this.notifyItemChanged(index);
        }
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<UserListView> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(userListFull);
            } else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(UserListView userListView: userListFull){
                    if(userListView.getUserLists().getListName().toLowerCase().contains(filterPattern)){
                        filteredList.add(userListView);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            userListViews.clear();
            userListViews.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

}
