package com.szi.teamx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.szi.teamx.model.Team;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    protected FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    protected FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    protected boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void startActivity(Class<? extends AppCompatActivity> cl) {
        Intent intent = new Intent(this, cl);
        startActivity(intent);
        finish();
    }

    protected void startActivityWithoutFinish(Class<? extends AppCompatActivity> cl) {
        Intent intent = new Intent(this, cl);
        startActivity(intent);
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void logout(View view) {
        logout();
    }

    public void handleAllTeams(View view) {
        Log.d("all", "in handle");
        startActivityWithoutFinish(AllTeamsActivity.class);
    }
}