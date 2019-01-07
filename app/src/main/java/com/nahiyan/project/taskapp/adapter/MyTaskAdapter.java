package com.nahiyan.project.taskapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.presenters.TaskListPresenter;
import com.nahiyan.project.taskapp.utils.Utils;
import com.nahiyan.project.taskapp.views.activitys.CreateTaskFragment;
import com.nahiyan.project.taskapp.views.activitys.MessageFragment;
import com.nahiyan.project.taskapp.views.activitys.TaskDescriptionFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.presenters.TaskListPresenter;
import com.nahiyan.project.taskapp.utils.Utils;
import com.nahiyan.project.taskapp.views.activitys.CreateTaskFragment;

import java.util.ArrayList;

public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.MyTaskHolder> {

    private Context context;
    private ArrayList<UserTasks> userTasksArrayList;
    private FirebaseAuth auth;
    private TaskListPresenter taskPresenter;

    public MyTaskAdapter(Context context, ArrayList<UserTasks> userTasksArrayList, FirebaseAuth auth, TaskListPresenter taskPresenter) {
        this.context = context;
        this.userTasksArrayList = userTasksArrayList;
        this.auth = auth;
        this.taskPresenter = taskPresenter;
    }

    @NonNull
    @Override
    public MyTaskHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.individual_task_view,viewGroup,false);
        return new MyTaskHolder(view,context,userTasksArrayList,taskPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyTaskHolder myTaskHolder, int i) {
        UserTasks userTasks = userTasksArrayList.get(i);

        if(userTasks.getTaskAssign() == null){
            myTaskHolder.iv_comment.setVisibility(View.GONE);
        } else{
            myTaskHolder.iv_comment.setVisibility(View.VISIBLE);
        }

        myTaskHolder.tv_priority.setText(userTasks.getTaskPriority());
        myTaskHolder.tv_date.setText(userTasks.getTaskDate());
        myTaskHolder.tv_time.setText(userTasks.getTaskTime());
        myTaskHolder.tv_task.setText(userTasks.getTaskName());
        if(userTasks.getTaskAssign() != null){
            myTaskHolder.tv_assign.setText(userTasks.getTaskAssign().getUsername());
        }
        myTaskHolder.tv_note.setText(userTasks.getTaskNote());

        if(userTasks.getTaskOwner().equals(userTasks.getTaskUserLists().getListOwner().getId())){
            if(userTasks.getTaskAssign() != null){
                myTaskHolder.iv_done.setVisibility(View.GONE);
                if(userTasks.getTaskAssign().getId().equals(auth.getCurrentUser().getUid())){
                    myTaskHolder.iv_done.setVisibility(View.VISIBLE);
                }
            } else{
                myTaskHolder.iv_done.setVisibility(View.VISIBLE);
            }
        }

        if(userTasks.getTaskOwner().equals(auth.getCurrentUser().getUid())){
            myTaskHolder.iv_edit.setVisibility(View.VISIBLE);
        }

        if(userTasks.getTaskStatus().equals("Done")){
            myTaskHolder.iv_status.setImageResource(R.drawable.done);
        } else if(userTasks.getTaskStatus().equals("Due")){
            myTaskHolder.iv_status.setImageResource(R.drawable.due);
        } else if(userTasks.getTaskStatus().equals("Pause")){
            myTaskHolder.iv_status.setImageResource(R.drawable.pause);
        } else if(userTasks.getTaskStatus().equals("Progress")){
            myTaskHolder.iv_status.setImageResource(R.drawable.progress);
        } else{
            myTaskHolder.iv_status.setImageResource(R.drawable.project);
        }
    }

    @Override
    public int getItemCount() {
        return userTasksArrayList.size();
    }

    public static class MyTaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tv_task,tv_priority,tv_date,tv_assign,tv_note,tv_time;
        public ImageView iv_status, iv_delete,iv_done,iv_comment,iv_edit,iv_description;
        public RelativeLayout rl_details,rl_listview;
        private Context context;
        ArrayList<UserTasks> userTasksArrayList;
        TaskListPresenter taskPresenter;

        MyTaskHolder(@NonNull View view, Context context, ArrayList<UserTasks> userTasksArrayList, TaskListPresenter taskPresenter) {
            super(view);
            this.taskPresenter = taskPresenter;
            this.userTasksArrayList = userTasksArrayList;
            this.context = context;
            rl_details = view.findViewById(R.id.rl_details);
            tv_task = view.findViewById(R.id.tv_task);
            tv_priority = view.findViewById(R.id.tv_taskPriority);
            tv_date = view.findViewById(R.id.tv_taskCalender);
            tv_time = view.findViewById(R.id.tv_taskTime);
            tv_assign = view.findViewById(R.id.tv_taskAssign);
            tv_note = view.findViewById(R.id.tv_taskNote);

            rl_listview = view.findViewById(R.id.rl_listview);
            rl_listview.setOnClickListener(this);

            iv_status = view.findViewById(R.id.iv_status);
            iv_status.setOnClickListener(this);

            iv_delete = view.findViewById(R.id.iv_delete);
            iv_delete.setOnClickListener(this);

            iv_done = view.findViewById(R.id.iv_done);
            iv_done.setOnClickListener(this);
            iv_done.setVisibility(View.GONE);

            iv_comment = view.findViewById(R.id.iv_comment);
            iv_comment.setOnClickListener(this);

            iv_edit = view.findViewById(R.id.iv_edit);
            iv_edit.setOnClickListener(this);
            iv_edit.setVisibility(View.GONE);

            iv_description = view.findViewById(R.id.iv_description);
            iv_description.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            UserTasks userTasks = userTasksArrayList.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            FragmentTransaction transaction;
            switch(view.getId()){

                case R.id.rl_listview:
                    if(rl_details.getVisibility() == View.GONE){
                        Utils.expand(rl_details);
                    } else if(rl_details.getVisibility() == View.VISIBLE){
                        Utils.collapse(rl_details);
                    }
                    break;

                case R.id.iv_delete:
                    this.taskPresenter.deleteTask(userTasks);
                    break;

                case R.id.iv_edit:
                    bundle.putSerializable("Tasks", userTasksArrayList.get(getAdapterPosition()));
                    CreateTaskFragment createTaskFragment = new CreateTaskFragment();

                    createTaskFragment.setArguments(bundle);

                    transaction = ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,createTaskFragment, "CreateTaskFragment")
                        .addToBackStack("TaskListFragment");

                    transaction.commit();
                    break;

                case R.id.iv_description:

                    bundle.putSerializable("Task", userTasks);
                    TaskDescriptionFragment taskDescriptionFragment = new TaskDescriptionFragment();
                    taskDescriptionFragment.setArguments(bundle);

                    transaction = ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,taskDescriptionFragment, "TaskDescriptionFragment")
                        .addToBackStack("TaskListFragment");

                    transaction.commit();
                    break;

                case R.id.iv_comment:
                    bundle.putSerializable("Task", userTasks);
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setArguments(bundle);

                    transaction = ((FragmentActivity)context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container,messageFragment, "MessageFragment")
                            .addToBackStack("TaskListFragment");

                    transaction.commit();
                    break;

                case R.id.iv_done:
                    setTaskStatus();
                    break;
            }
        }

        private void setTaskStatus() {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setTitle("Select Task Status");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice);
            arrayAdapter.add("Done");
            arrayAdapter.add("Pause");
            arrayAdapter.add("Progress");

            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    UserTasks userTasks = userTasksArrayList.get(getAdapterPosition());
                    userTasks.setTaskStatus(strName);
                    taskPresenter.setStatus(userTasks);
                    dialog.dismiss();
                }
            });
            builderSingle.show();
        }
    }
}
