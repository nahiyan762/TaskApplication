package com.nahiyan.project.taskapp.views.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.presenters.implementation.TaskPresenterImpl;
import com.nahiyan.project.taskapp.models.UserLists;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        String alarmTaskId= getIntent().getStringExtra("AlarmTaskId");
        String listId= getIntent().getStringExtra("ListId");
        String taskId= getIntent().getStringExtra("TaskId");
        TaskPresenterImpl taskPresenter = new TaskPresenterImpl(TaskActivity.this);

        if(listId != null){
            taskPresenter.getUserListByListId(listId);
        } else if(taskId != null){
            taskPresenter.getUserTaskByTaskId(taskId);
        } else if(alarmTaskId != null){
            taskPresenter.getUserTaskByAlarmTaskId(alarmTaskId);
        } else{
            UserLists userLists = (UserLists) getIntent().getSerializableExtra("UserLists");

            Bundle bundle = new Bundle();
            bundle.putSerializable("UserLists", userLists);
            TaskListFragment taskListFragment = new TaskListFragment();
            taskListFragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,taskListFragment, "TaskListFragment");

            transaction.commit();
        }
    }

    public void getList(UserLists userLists) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserLists", userLists);
        TaskListFragment taskListFragment = new TaskListFragment();
        taskListFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,taskListFragment, "TaskListFragment");

        transaction.commit();
    }

    public void getTask(UserTasks userTasks) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Task", userTasks);
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,messageFragment, "MessageFragment");


        transaction.commit();
    }
}
