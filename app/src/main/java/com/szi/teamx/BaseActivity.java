package com.szi.teamx;

import static com.szi.teamx.model.MyTeams.MY_TEAMS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.szi.teamx.model.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        if(!this.getClass().equals(AllTeamsActivity.class))
            startActivityWithoutFinish(AllTeamsActivity.class);
    }

    public void onScan(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setPrompt("Scan Team QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
        intentIntegrator.initiateScan();
    }

    protected void openTeamInfoActivity(Team team) {
        Intent intent = new Intent(this, TeamInfoActivity.class);
        intent.putExtra("teamName", team.getName());
        intent.putExtra("teamDescription", team.getDescription());
        intent.putExtra("teamId", team.getId());
        intent.putExtra("teamOwner", team.getOwner());
        intent.putExtra("teamKey", team.getKey());

        Collection<String> values = team.getRequirements().values();
        ArrayList<String> valuesList = new ArrayList<>(values);

        intent.putStringArrayListExtra("teamRequirements", valuesList);
        startActivity(intent);
    }
}