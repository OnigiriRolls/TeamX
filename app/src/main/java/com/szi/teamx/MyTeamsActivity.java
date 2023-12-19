package com.szi.teamx;

import static com.szi.teamx.model.MyTeams.MY_TEAMS;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.szi.teamx.model.Team;
import com.szi.teamx.ui.TeamNameListAdapter;

import java.util.HashMap;
import java.util.Optional;

public class MyTeamsActivity extends BaseActivity {
    private DatabaseReference databaseReference;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams);

        TextView title = findViewById(R.id.tTitle);
        title.setText(R.string.my_teams);

        EditText searchText = findViewById(R.id.tSearchText);
        searchText.setVisibility(View.GONE);

        ListView listTeams = findViewById(R.id.lTeams);
        final TeamNameListAdapter adapter = new TeamNameListAdapter(this, R.layout.team_name_list_item, MY_TEAMS);
        listTeams.setAdapter(adapter);

        listTeams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Team selectedTeam = MY_TEAMS.get(position);
                openTeamInfoActivity(selectedTeam);
            }
        });

        String userId = getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("users").child(userId);

        databaseReference.child("teams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    String teamId = (String) snapshot.getValue();
                    String teamKey = (String) snapshot.getKey();
                    if (teamId != null)
                        retrieveTeamsDetails(teamKey, teamId, adapter);
                } catch (Exception e) {
                    System.out.println("Eroare la citire");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("team", "changed");
                String teamId = (String) snapshot.getValue();
                String teamKey = (String) snapshot.getKey();
                if (teamId != null) {
                    Optional<Team> removedTeam = MY_TEAMS.stream().filter(t -> t.getId().equals(teamId)).findFirst();
                    removedTeam.ifPresent(team -> MY_TEAMS.remove(team));

                    retrieveTeamsDetails(teamKey, teamId, adapter);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String teamId = (String) snapshot.getValue();
                if (teamId != null) {
                    Optional<Team> removedTeam = MY_TEAMS.stream().filter(t -> t.getId().equals(teamId)).findFirst();
                    if (removedTeam.isPresent()) {
                        MY_TEAMS.remove(removedTeam.get());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        databaseReference.child("teamsOwner").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    String teamId = (String) snapshot.getValue();
                    String teamKey = (String) snapshot.getKey();
                    if (teamId != null)
                        retrieveTeamsDetails(teamKey, teamId, adapter);
                } catch (Exception e) {
                    System.out.println("Eroare la citire");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("team", "changed");
                String teamId = (String) snapshot.getValue();
                String teamKey = (String) snapshot.getKey();
                if (teamId != null) {
                    Optional<Team> removedTeam = MY_TEAMS.stream().filter(t -> t.getId().equals(teamId)).findFirst();
                    removedTeam.ifPresent(team -> MY_TEAMS.remove(team));

                    retrieveTeamsDetails(teamKey, teamId, adapter);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String teamId = (String) snapshot.getValue();
                if (teamId != null) {
                    Optional<Team> removedTeam = MY_TEAMS.stream().filter(t -> t.getId().equals(teamId)).findFirst();
                    if (removedTeam.isPresent()) {
                        MY_TEAMS.remove(removedTeam.get());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void retrieveTeamsDetails(String teamKey, String teamId, TeamNameListAdapter adapter) {
        DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference().child("teams");
        teamsRef.child(teamId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Team team = snapshot.getValue(Team.class);
                    team.setKey(teamKey);
                    if (team != null) {
                        MY_TEAMS.add(team);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    System.out.println("Eroare la citire");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit the application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                String scannedId = result.getContents();
                Log.i("scan", scannedId);
                Optional<Team> team = MY_TEAMS.stream().filter(t -> t.getId().equals(scannedId)).findFirst();
                if (!team.isPresent()) {
                    getTeamFromAllTeams(scannedId);
                }
                team.ifPresent(this::openTeamInfoActivity);
            }
        }
    }

    private void getTeamFromAllTeams(String scannedId) {
        DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference();
        teamsRef.child("teams").child(scannedId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("team", "get success");
                        Team team = task.getResult().getValue(Team.class);
                        if (team != null)
                          openTeamInfoActivity(team);
                        else {
                            finish();
                        }
                    } else {
                        Log.d("team", "get error");
                    }
                });
    }
}