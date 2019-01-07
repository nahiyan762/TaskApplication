package com.nahiyan.project.taskapp.views.activitys;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.adapter.MyTaskAdapter;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.presenters.TaskListPresenter;
import com.nahiyan.project.taskapp.presenters.implementation.TaskListPresenterImpl;
import com.nahiyan.project.taskapp.utils.Utils;
import com.nahiyan.project.taskapp.views.TaskListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nahiyan.project.taskapp.adapter.MyTaskAdapter;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.presenters.TaskListPresenter;
import com.nahiyan.project.taskapp.presenters.implementation.TaskListPresenterImpl;
import com.nahiyan.project.taskapp.views.TaskListView;

import java.util.ArrayList;
import java.util.Objects;

public class TaskListFragment extends Fragment implements TaskListView, View.OnClickListener {

    private UserLists userLists;
    private RecyclerView recyclerViewList;
    private FirebaseAuth auth;
    private TaskListPresenter taskPresenter;
    private ProgressDialog progressDialog;
    private AdView mAdView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.nahiyan.project.taskapp.R.layout.fragment_task_list, container, false);

        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setMessage("Loading");
        this.progressDialog.show();

        mAdView = view.findViewById(com.nahiyan.project.taskapp.R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.auth = FirebaseAuth.getInstance();
        if(getArguments() != null){
            userLists = (UserLists) getArguments().getSerializable("UserLists");
        }


        Toolbar toolbar = view.findViewById(com.nahiyan.project.taskapp.R.id.toolbar);
        ((AppCompatActivity)Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("");
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        this.taskPresenter = new TaskListPresenterImpl(auth, TaskListFragment.this);
        this.taskPresenter.getTaskData(userLists.getListId());

        ImageView iv_addTask = view.findViewById(com.nahiyan.project.taskapp.R.id.iv_addTask);
        TextView tv_listName = view.findViewById(com.nahiyan.project.taskapp.R.id.tv_listName);
        tv_listName.setText(this.userLists.getListName());
        iv_addTask.setOnClickListener(this);

        if(!userLists.getListOwner().getId().equals(auth.getCurrentUser().getUid())){
            iv_addTask.setVisibility(View.GONE);
        }

        recyclerViewList = view.findViewById(com.nahiyan.project.taskapp.R.id.recyclerViewList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL,false);
        recyclerViewList.setLayoutManager(layoutManager);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == com.nahiyan.project.taskapp.R.id.iv_addTask){
            Bundle bundle = new Bundle();
            bundle.putSerializable("UserLists", userLists);
            CreateTaskFragment createTaskFragment = new CreateTaskFragment();
            createTaskFragment.setArguments(bundle);

            FragmentTransaction transaction = ((FragmentActivity)Objects.requireNonNull(getContext())).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(com.nahiyan.project.taskapp.R.id.container,createTaskFragment, "CreateTaskFragment")
                    .addToBackStack("TaskListFragment");

            transaction.commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(com.nahiyan.project.taskapp.R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case com.nahiyan.project.taskapp.R.id.task_done:
                this.taskPresenter.getDoneTaskData(userLists.getListId());
                return false;

            case com.nahiyan.project.taskapp.R.id.task_pause:
                this.taskPresenter.getPauseTaskData(userLists.getListId());
                return false;

            case com.nahiyan.project.taskapp.R.id.task_progress:
                this.taskPresenter.getProgressTaskData(userLists.getListId());
                return false;

            case com.nahiyan.project.taskapp.R.id.task_all:
                this.taskPresenter.getTaskData(userLists.getListId());
                return false;

            default:
                break;
        }

        return false;
    }

    @Override
    public void setTask(ArrayList<UserTasks> userTasksArrayList) {
        this.progressDialog.dismiss();
        recyclerViewList.setAdapter(new MyTaskAdapter(getContext(),userTasksArrayList,this.auth,this.taskPresenter));
    }
}
