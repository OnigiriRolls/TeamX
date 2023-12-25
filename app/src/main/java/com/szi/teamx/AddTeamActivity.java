package com.szi.teamx;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.szi.teamx.model.RequirementItem;
import com.szi.teamx.model.Team;
import com.szi.teamx.ui.AddTeamListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTeamActivity extends BaseActivity {
    private TextView teamName;
    private TextView description;
    private ListView lRequirements;
    private List<RequirementItem> requirements = new ArrayList<>();
    private AddTeamListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        teamName = findViewById(R.id.tName);
        description = findViewById(R.id.tDescription);
        lRequirements = findViewById(R.id.lRequirements);

        adapter = new AddTeamListAdapter(this, R.layout.requirement_input_list_item, requirements);
        lRequirements.setAdapter(adapter);

        requirements.add(new RequirementItem());
        adapter.notifyDataSetChanged();
    }

    public void onCreateTeam(View view) {
        if (teamName == null || description == null || requirements == null)
            return;
        if (teamName.getText().toString().isEmpty() || description.getText().toString().isEmpty()
                || requirements.size() == 0 || requirements.get(0).getUserInput().isEmpty())
            return;
        Team team = new Team();
        team.setName(teamName.getText().toString());
        team.setLowerCaseName(teamName.getText().toString().toLowerCase());
        team.setDescription(description.getText().toString());
        setRequirements(requirements, team);
        team.setOwner(getCurrentUser().getUid());
    }

    private void setRequirements(List<RequirementItem> requirements, Team team) {
        String r = "r_";
        int i = 1;
        for (RequirementItem requirement : requirements) {
            String key = r + i;
            Map<String, String> requirementsTeam = new HashMap<>();
            requirementsTeam.put(key, requirement.getUserInput());
            team.setRequirements(requirementsTeam);

            Map<String, String> requirementsLowerCaseTeam = new HashMap<>();
            requirementsLowerCaseTeam.put(key, requirement.getUserInput().toLowerCase());
            team.setRequirementsLower(requirementsLowerCaseTeam);

            i++;
        }
    }

    public void addInput(View view) {
        requirements.add(new RequirementItem());
        adapter.notifyDataSetChanged();
    }

    public void deleteInput(View view) {
        String positionString = view.getContentDescription().toString();
        int position = Integer.parseInt(positionString);
        if (position > 0 && position < requirements.size())
        {
            requirements.remove(position);
            adapter.notifyDataSetChanged();
        }
    }
}