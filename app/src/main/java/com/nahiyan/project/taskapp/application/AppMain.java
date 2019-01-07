package com.nahiyan.project.taskapp.application;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.FirebaseDatabase;

public class AppMain extends Application {

    public static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        this.instance = this;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        MobileAds.initialize(this, "ca-app-pub-9786524414253219~3129454397");
    }
}
