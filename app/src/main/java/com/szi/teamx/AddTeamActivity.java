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

        RequirementItem item = new RequirementItem();
        item.setUserInput("");
        requirements.add(item);
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
        team.setRequirements(new HashMap<String, String>());
        team.setRequirementsLower(new HashMap<String, String>());
        for (RequirementItem requirement : requirements) {
            String key = r + i;
            team.getRequirements().put(key, requirement.getUserInput());
            team.getRequirementsLower().put(key, requirement.getUserInput().toLowerCase());
            i++;
        }
    }

    public void addInput(View view) {
        RequirementItem item = new RequirementItem();
        item.setUserInput("");
        List<RequirementItem> tmp = new ArrayList<>(requirements);
        tmp.add(item);
        requirements.clear();
        requirements.addAll(tmp);
        adapter.notifyDataSetChanged();
    }

    public void deleteInput(View view) {
        String positionString = view.getContentDescription().toString();
        int position = Integer.parseInt(positionString);
        if (position > 0 && position < requirements.size()) {
            RequirementItem item = requirements.get(position);
            List<RequirementItem> tmp = new ArrayList<>(requirements);
            tmp.remove(item);
            requirements.clear();
            requirements.addAll(tmp);
            adapter.notifyDataSetChanged();
        }
    }
}