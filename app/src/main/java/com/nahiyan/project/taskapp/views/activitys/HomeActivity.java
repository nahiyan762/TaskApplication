package com.nahiyan.project.taskapp.views.activitys;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.utils.AlarmService;
import com.crashlytics.android.Crashlytics;
import com.nahiyan.project.taskapp.application.AppMain;

import io.fabric.sdk.android.Fabric;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Fabric fabric = new Fabric.Builder(AppMain.instance)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

        Intent intent = new Intent(this,AlarmService.class);
        AlarmService.enqueueWork(HomeActivity.this,intent);

        ListFragment listFragment = new ListFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.listContainer,listFragment, "ListFragment");

        transaction.commit();
    }
}
