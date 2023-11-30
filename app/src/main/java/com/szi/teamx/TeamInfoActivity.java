package com.szi.teamx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        if (requirements != null) {
            ListView gridRequirementsLeft = findViewById(R.id.gRequirementsLeft);

            int splitIndex = requirements.size() / 2;
            List<String> sublist1 = requirements.subList(0, splitIndex);
            List<String> sublist2 = requirements.subList(splitIndex, requirements.size());


            if (requirements.size() / 2 - 1 != 0) {
                final RequirementGridAdapter adapterLeft = new RequirementGridAdapter(this, R.layout.requirement_grid_item, sublist1);
                gridRequirementsLeft.setAdapter(adapterLeft);
            } else {
                final RequirementGridAdapter adapterLeft = new RequirementGridAdapter(this, R.layout.requirement_grid_item, Collections.singletonList(requirements.get(0)));
                gridRequirementsLeft.setAdapter(adapterLeft);
            }


            ListView gridRequirementsRight = findViewById(R.id.gRequirementsRight);
            if (requirements.size() / 2 != requirements.size() - 1) {
                final RequirementGridAdapter adapterRight = new RequirementGridAdapter(this, R.layout.requirement_grid_item, sublist2);
                gridRequirementsRight.setAdapter(adapterRight);
            } else {
                final RequirementGridAdapter adapterRight = new RequirementGridAdapter(this, R.layout.requirement_grid_item, Collections.singletonList(requirements.get(requirements.size() / 2)));
                gridRequirementsRight.setAdapter(adapterRight);
            }
        }
    }

    public void onApply(View view) {
        Button applyButton = (Button) view;
        applyButton.setText(R.string.team_applied);
        applyButton.setEnabled(false);
    }

    public void onQRCode(View view) {
    }
}