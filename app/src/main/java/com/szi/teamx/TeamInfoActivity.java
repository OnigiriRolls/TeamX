package com.szi.teamx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.szi.teamx.ui.RequirementGridAdapter;

import java.util.Collections;
import java.util.List;

public class TeamInfoActivity extends AppCompatActivity {

    private String id;
    private String ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        String teamName = getIntent().getStringExtra("teamName");
        String teamDescription = getIntent().getStringExtra("teamDescription");
        id = getIntent().getStringExtra("teamId");
        ownerId = getIntent().getStringExtra("teamOwner");
        List<String> requirements = getIntent().getStringArrayListExtra("teamRequirements");

        TextView textViewTeamName = findViewById(R.id.tName);
        TextView textViewTeamDescription = findViewById(R.id.tDescription);

        textViewTeamName.setText(teamName);
        textViewTeamDescription.setText(teamDescription);
        
        GridView gridRequirementsLeft = findViewById(R.id.gRequirements);
        final RequirementGridAdapter adapterLeft = new RequirementGridAdapter(this, R.layout.requirement_grid_item, requirements);
        gridRequirementsLeft.setAdapter(adapterLeft);
    }

    public void onApply(View view) {
        Button applyButton = (Button) view;
        applyButton.setText(R.string.team_applied);
        applyButton.setEnabled(false);
    }

    public void onQRCode(View view) {
    }
}