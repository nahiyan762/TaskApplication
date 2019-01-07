package com.nahiyan.project.taskapp.views.activitys;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.models.UserTasks;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDescriptionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.nahiyan.project.taskapp.R.layout.fragment_task_description, container, false);

        Toolbar toolbar = view.findViewById(com.nahiyan.project.taskapp.R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        UserTasks userTasks = (UserTasks) getArguments().getSerializable("Task");

        TextView tv_taskName = view.findViewById(com.nahiyan.project.taskapp.R.id.tv_taskName);
        TextView tv_taskNote = view.findViewById(com.nahiyan.project.taskapp.R.id.tv_taskNote);

        tv_taskName.setText(userTasks.getTaskName());
        tv_taskNote.setText(userTasks.getTaskNote());

        return view;
    }

}
