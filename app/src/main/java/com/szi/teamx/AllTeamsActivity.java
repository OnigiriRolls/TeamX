package com.szi.teamx;

import static com.szi.teamx.model.MyTeams.MY_TEAMS;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.szi.teamx.model.Team;
import com.szi.teamx.ui.TeamNameListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllTeamsActivity extends BaseActivity {
    private DatabaseReference databaseReference;
    private List<Team> teams = new ArrayList<>();
    private List<Team> initialTeams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams);

        TextView title = findViewById(R.id.tTitle);
        title.setText(R.string.all_teams);

        EditText searchText = findViewById(R.id.tSearchText);
        searchText.setVisibility(View.VISIBLE);

        ListView listTeams = findViewById(R.id.lTeams);
        final TeamNameListAdapter adapter = new TeamNameListAdapter(this, R.layout.team_name_list_item, teams);
        listTeams.setAdapter(adapter);

        listTeams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Team selectedTeam = teams.get(position);
                openTeamInfoActivity(selectedTeam);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        databaseReference.child("teams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Team team = snapshot.getValue(Team.class);
                    if (team != null && !MY_TEAMS.contains(team)) {
                        teams.add(team);
                        initialTeams.add(team);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    System.out.println("Eroare la citire");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Team team = snapshot.getValue(Team.class);
                    Log.i("hello", "in on change");
                    if (team != null && !MY_TEAMS.contains(team)) {
                        if (teams.contains(team))
                            teams.set(teams.indexOf(team), team);
                        else teams.add(team);

                        if (initialTeams.contains(team))
                            initialTeams.set(initialTeams.indexOf(team), team);
                        else initialTeams.add(team);

                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    System.out.println("Eroare la citire");
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                try {
                    Team team = snapshot.getValue(Team.class);
                    if (team != null && !MY_TEAMS.contains(team)) {
                        teams.remove(team);
                        initialTeams.remove(team);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    System.out.println("Eroare la citire");
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().trim();
                if (query.isEmpty()) {
                    teams.clear();
                    teams.addAll(initialTeams);
                    adapter.notifyDataSetChanged();
                } else
                    updateListView(query, adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void updateListView(String query, TeamNameListAdapter adapter) {
        query = query.toLowerCase().trim();

        teams.clear();
        String finalQuery = query;
        List<Team> searchedTeams = initialTeams.stream()
                .filter(v -> v.getLowerCaseName().contains(finalQuery) || checkRequirements(v.getRequirementsLower(), finalQuery))
                .collect(Collectors.toList());

        if (!searchedTeams.isEmpty())
            teams.addAll(searchedTeams);

        adapter.notifyDataSetChanged();
    }

    private boolean checkRequirements(Map<String, String> requirements, String query) {
        return requirements.values().stream().anyMatch(r -> r.contains(query));
    }

    private void openTeamInfoActivity(Team team) {
        Intent intent = new Intent(this, TeamInfoActivity.class);
        intent.putExtra("teamName", team.getName());
        intent.putExtra("teamDescription", team.getDescription());
        intent.putExtra("teamId", team.getId());
        intent.putExtra("teamOwner", team.getOwner());

        Collection<String> values = team.getRequirements().values();
        ArrayList<String> valuesList = new ArrayList<>(values);

        intent.putStringArrayListExtra("teamRequirements", valuesList);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                String scannedId = result.getContents();
                Log.i("scan", scannedId);
                Optional<Team> team = teams.stream().filter(t -> t.getId().equals(scannedId)).findFirst();
                team.ifPresent(this::openTeamInfoActivity);
            }
        }
    }

    public void onScan(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setPrompt("Scan Team QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
        intentIntegrator.initiateScan();
    }
}