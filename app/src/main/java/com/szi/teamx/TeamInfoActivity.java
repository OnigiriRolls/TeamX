package com.szi.teamx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TeamInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        String teamName = getIntent().getStringExtra("teamName");
        String teamDescription = getIntent().getStringExtra("teamDescription");

        TextView textViewTeamName = findViewById(R.id.tName);
        TextView textViewTeamDescription = findViewById(R.id.tDescription);

        textViewTeamName.setText(teamName);
        textViewTeamDescription.setText(teamDescription);

        Button applyButton = findViewById(R.id.bApply);
        Log.i("register", applyButton.getBackground().getCurrent().toString());
        applyButton.setBackgroundResource(R.drawable.button_dark);
    }

    public void onApply(View view) {
        Button applyButton = (Button) view;
        applyButton.setText(R.string.team_applied);
        applyButton.setEnabled(false);
        Log.i("register", applyButton.getBackground().getCurrent().toString());
    }
}