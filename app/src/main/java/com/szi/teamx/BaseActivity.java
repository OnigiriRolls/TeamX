package com.szi.teamx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    protected void logout() {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        logout();
    }

    public void startActivity(Class<? extends AppCompatActivity> cl) {
        Intent intent = new Intent(this, cl);
        startActivity(intent);
        finish();
    }

    public void startActivityWithoutFinish(Class<? extends AppCompatActivity> cl) {
        Intent intent = new Intent(this, cl);
        startActivity(intent);
    }
}