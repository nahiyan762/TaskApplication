package com.nahiyan.project.taskapp.views.activitys;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.presenters.CreateTaskPresenter;
import com.nahiyan.project.taskapp.presenters.implementation.CreateTaskPresenterImpl;
import com.nahiyan.project.taskapp.views.CreateTaskView;
import com.github.aakira.compoundicontextview.CompoundIconTextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.nahiyan.project.taskapp.models.UserLists;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;
import java.util.Objects;

public class CreateTaskFragment extends Fragment implements CreateTaskView, View.OnClickListener {

    CompoundIconTextView tv_taskPriority,tv_taskNote,tv_taskDate,tv_taskTime,tv_taskAssign;
    EditText et_taskName;
    FirebaseAuth mAuth;
    private CreateTaskPresenter craterTaskPresenter;
    private UserTasks userTasks;
    private InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);

        mInterstitialAd = new InterstitialAd(AppMain.instance);
        mInterstitialAd.setAdUnitId("ca-app-pub-9786524414253219/4867015813");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("Create Task");
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getFragmentManager()).popBackStackImmediate();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        if(getArguments() != null){
            UserLists userLists = (UserLists) getArguments().getSerializable("UserLists");
            if(this.userTasks == null && userLists != null){
                this.userTasks = new UserTasks();
                this.userTasks.setTaskUserLists(userLists);
            } else{
                UserTasks userTasks = (UserTasks) getArguments().getSerializable("Tasks");
                if(userLists == null && userTasks != null){
                    this.userTasks = userTasks;
                }
            }
        }

        ImageView iv_done = view.findViewById(R.id.iv_done);
        iv_done.setOnClickListener(this);

        et_taskName = view.findViewById(R.id.et_taskName);
        et_taskName.setText(this.userTasks.getTaskName());

        tv_taskPriority = view.findViewById(R.id.tv_taskPriority);
        tv_taskPriority.setText(this.userTasks.getTaskPriority());
        tv_taskPriority.setOnClickListener(this);

        tv_taskNote = view.findViewById(R.id.tv_taskNote);
        tv_taskNote.setText(this.userTasks.getTaskNote());
        tv_taskNote.setOnClickListener(this);

        tv_taskDate = view.findViewById(R.id.tv_taskDate);
        tv_taskDate.setText(this.userTasks.getTaskDate());
        tv_taskDate.setOnClickListener(this);

        tv_taskTime = view.findViewById(R.id.tv_taskTime);
        tv_taskTime.setText(this.userTasks.getTaskTime());
        tv_taskTime.setOnClickListener(this);

        tv_taskAssign = view.findViewById(R.id.tv_taskAssign);
        if(this.userTasks.getTaskAssign() != null){
            tv_taskAssign.setText(this.userTasks.getTaskAssign().getEmail());
        }
        tv_taskAssign.setOnClickListener(this);

        craterTaskPresenter = new CreateTaskPresenterImpl(CreateTaskFragment.this, mAuth);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_taskPriority:
                setTaskPriority();
                break;
            case R.id.tv_taskNote:
                setTaskNote();
                break;
            case R.id.tv_taskDate:
                setTaskDate();
                break;
            case R.id.tv_taskTime:
                setTaskTime();
                break;
            case R.id.tv_taskAssign:
                setTaskAssign(userTasks);
                break;
            case R.id.iv_done:
                this.userTasks.setTaskName(et_taskName.getText().toString());
                craterTaskPresenter.setTask(userTasks);
                break;
        }
    }

    //show TaskAssign set Fragment and set assign in userTask object
        private void setTaskAssign(UserTasks userTasks) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("UsersTask", userTasks);
            AssignFragment assignFragment = new AssignFragment();
            assignFragment.setArguments(bundle);

            FragmentTransaction transaction = ((FragmentActivity)Objects.requireNonNull(getContext())).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,assignFragment, "AssignTaskFragment")
                    .addToBackStack("CreateTaskFragment");

            transaction.commit();
        }

    //show Time set Dialog and set time in userTask object
        private void setTaskTime() {
            Calendar calendar = Calendar.getInstance();
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                    String time = String.format("%02d:%02d", hourOfDay, minutes);
                    userTasks.setTaskTime(time);
                    tv_taskTime.setText(time);
                }
            }, currentHour, currentMinute, false);
            timePickerDialog.show();
        }

    //show Date set Dialog and set date in userTask object
        private void setTaskDate() {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
    
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month + 1;
                    String date = month + "/" + dayOfMonth + "/" + year;
                    userTasks.setTaskDate(date);
                    tv_taskDate.setText(date);
                }
            }, year,month,day);
            datePickerDialog.show();
        }

    //show Note set Dialog and set note in userTask object
        private void setTaskNote() {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View promptView = layoutInflater.inflate(R.layout.dailog_create_note, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setView(promptView);

            final MaterialEditText met_list_name = promptView.findViewById(R.id.met_list_name);
            met_list_name.setText(userTasks.getTaskNote());
            
            alertDialogBuilder.setTitle("Create Task Note").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).setPositiveButton("create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String taskNote = met_list_name.getText().toString();
                    userTasks.setTaskNote(taskNote);
                    tv_taskNote.setText(userTasks.getTaskNote());
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

    //show Priority set Dialog and set priority in userTask object
        private void setTaskPriority() {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
            builderSingle.setTitle("Select Task Priority");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);
            arrayAdapter.add("TOP");
            arrayAdapter.add("HIGH");
            arrayAdapter.add("MEDIUM");
            arrayAdapter.add("LOW");

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
                    userTasks.setTaskPriority(strName);
                    tv_taskPriority.setText(strName);
                    dialog.dismiss();
                }
            });
            builderSingle.show();
        }

    @Override
    public void showValidationError(boolean b) {
        if(!b){
            String errorString = "Task Name Required!";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorWhite));
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            this.et_taskName.setError(spannableStringBuilder);
        } else{
            goToTaskList();
        }
    }

    @Override
    public void dataInsertionSuccessfull() {
        goToTaskList();
    }

    @Override
    public void sendPushMessageFailed() {
//        Toast.makeText(getContext(),"Push Message Sending Failed!", Toast.LENGTH_SHORT).show();
    }

    public void goToTaskList() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("UserLists", userTasks.getTaskUserLists());
        TaskListFragment taskListFragment = new TaskListFragment();
        taskListFragment.setArguments(bundle);

        FragmentTransaction transaction = ((FragmentActivity)Objects.requireNonNull(getContext())).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,taskListFragment, "TaskListFragment")
                .disallowAddToBackStack();

        transaction.commit();
    }
}
