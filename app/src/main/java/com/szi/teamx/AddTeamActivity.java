package com.szi.teamx;

import static com.szi.teamx.utils.ProgressBar.hideLoadingDialog;
import static com.szi.teamx.utils.ProgressBar.showLoadingDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.szi.teamx.model.RequirementItem;
import com.szi.teamx.model.Team;
import com.szi.teamx.ui.AddTeamListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddTeamActivity extends BaseActivity {
    private TextView teamName;
    private TextView description;
    private ListView lRequirements;
    private List<RequirementItem> requirements = new ArrayList<>();
    private AddTeamListAdapter adapter;
    private Button addButton;
    private AlertDialog dialog;

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
        if (teamName == null || description == null || requirements == null) {
            showMessageDialog("Empty fields!");
            return;
        }

        if (teamName.getText().toString().isEmpty() || description.getText().toString().isEmpty()
                || requirements.size() == 0 || requirements.get(0).getUserInput().isEmpty()) {
            showMessageDialog("Empty fields!");
            return;
        }

        dialog = showLoadingDialog(this);
        Team team = new Team();
        team.setName(teamName.getText().toString());
        team.setLowerCaseName(teamName.getText().toString().toLowerCase());
        team.setDescription(description.getText().toString());
        setRequirements(requirements, team);
        team.setId("tmp");
        team.setOwner(getCurrentUser().getUid());
        saveTeam(team);
    }

    private void saveTeam(Team team) {
        DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference().child("teams");

        String teamId = teamsRef.push().getKey();

        teamsRef.child(teamId).setValue(team)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setId(teamId);
                    } else {
                        Log.e("Firebase", "Error saving team: " + task.getException().getMessage());
                        showMessageDialog("There was an error! Please try again later!");
                    }
                });
    }

    private void setId(String teamId) {
        DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference().child("teams");

        teamsRef.child(teamId).child("id").setValue(teamId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Firebase", "Team saved successfully");
                hideLoadingDialog(dialog);
                showMessageDialog("Team created successfully!");
            } else {
                Log.e("Firebase", "Error updating id: " + task.getException().getMessage());
                addButton.setEnabled(true);
                hideLoadingDialog(dialog);
                showMessageDialog("There was an error! Please try again later!");
            }
        });
    }

    private void setRequirements(List<RequirementItem> requirements, Team team) {
        String r = "r_";
        int i = 1;
        team.setRequirements(new HashMap<String, String>());
        team.setRequirementsLower(new HashMap<String, String>());
        for (RequirementItem requirement : requirements) {
            if (!requirement.getUserInput().isEmpty()) {
                String key = r + i;
                team.getRequirements().put(key, requirement.getUserInput());
                team.getRequirementsLower().put(key, requirement.getUserInput().toLowerCase());
                i++;
            }
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

    private void showMessageDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}