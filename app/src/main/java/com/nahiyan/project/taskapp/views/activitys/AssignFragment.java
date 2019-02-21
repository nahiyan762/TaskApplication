package com.nahiyan.project.taskapp.views.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.application.AppMain;

import java.util.ArrayList;
import java.util.Objects;

public class AssignFragment extends Fragment {

    private ListView lv_taskAssign;
    private User user = null ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assign, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getFragmentManager()).popBackStackImmediate();
            }
        });

        ImageView iv_done = view.findViewById(R.id.iv_done);
        lv_taskAssign = view.findViewById(R.id.lv_taskAssign);

        assert getArguments() != null;
        UserTasks userTasks = (UserTasks) getArguments().getSerializable("UsersTask");

        ArrayList<String> stringArrayList = new ArrayList<>();

        if(userTasks.getTaskUserLists().getListAssignUser() != null){
            for(User user:userTasks.getTaskUserLists().getListAssignUser()){
                stringArrayList.add(user.getEmail());
            }
        }

        ArrayAdapter<String>  adapter=new ArrayAdapter<>(AppMain.instance, R.layout.assign_listview,stringArrayList);
        lv_taskAssign.setAdapter(adapter);

        lv_taskAssign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                user = userTasks.getTaskUserLists().getListAssignUser().get(i);
            }
        });

        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user == null){
                    Objects.requireNonNull(getFragmentManager()).popBackStackImmediate();
                    userTasks.setTaskAssign(user);
                }else{
                    Objects.requireNonNull(userTasks).setTaskAssign(user);
                    Objects.requireNonNull(getFragmentManager()).popBackStackImmediate();
                }
            }
        });

        return view;
    }
}
