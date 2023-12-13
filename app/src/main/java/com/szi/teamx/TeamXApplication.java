package com.szi.teamx;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

public class TeamXApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("start", "in app");
        FirebaseApp.initializeApp(this);
    }
}
