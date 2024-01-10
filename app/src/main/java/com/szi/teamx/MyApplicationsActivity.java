package com.szi.teamx;

import static com.szi.teamx.model.MyTeams.MY_TEAMS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.szi.teamx.model.OwnerApplication;
import com.szi.teamx.model.Team;
import com.szi.teamx.ui.ApplicationListAdapter;
import com.szi.teamx.ui.OwnerApplicationListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MyApplicationsActivity extends BaseActivity {
    List<String> myApplications = new ArrayList<>();
    List<OwnerApplication> ownerApplications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_applications);

        String userId = getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("users").child(userId);

        ListView listMyApplication = findViewById(R.id.lApps);
        ApplicationListAdapter adapterMyApplications = new ApplicationListAdapter(this, R.layout.my_application_list_item, myApplications);
        listMyApplication.setAdapter(adapterMyApplications);

        databaseReference.child("applications").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String teamId = (String) snapshot.getKey();
                if (teamId != null) {
                    retrieveTeamName(teamId, adapterMyApplications);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String teamId = (String) snapshot.getKey();
                if (teamId != null) {
                    retrieveTeamNameAndDelete(teamId, adapterMyApplications);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ListView listOwnerApplication = findViewById(R.id.lTeams);
        OwnerApplicationListAdapter adapterOwnerApplications = new OwnerApplicationListAdapter(this, R.layout.owner_application_list_item, ownerApplications);
        listOwnerApplication.setAdapter(adapterOwnerApplications);

        databaseReference.child("applicationsOwner").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String teamId = (String) snapshot.getKey();
                if (teamId != null) {
                    Map<String, Boolean> values = (Map<String, Boolean>) snapshot.getValue();
                    if (values != null)
                        values.keySet().forEach(id -> retrieveUserName(id, teamId, adapterOwnerApplications));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String teamId = (String) snapshot.getKey();
                if (teamId != null) {
                    Map<String, Boolean> values = (Map<String, Boolean>) snapshot.getValue();
                    if (values != null)
                        Log.d("team", "removed owner app");
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void retrieveTeamNameAndDelete(String teamId, ApplicationListAdapter adapter) {
        DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference();
        teamsRef.child("teams").child(teamId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("team", "get success");
                        Team team = task.getResult().getValue(Team.class);
                        if (team != null) {
                            myApplications.remove(team.getName());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("team", "get error");
                    }
                });
    }

    private void retrieveTeamName(String teamId, ApplicationListAdapter adapter) {
        DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference();
        teamsRef.child("teams").child(teamId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("team", "get success");
                        Team team = task.getResult().getValue(Team.class);
                        if (team != null) {
                            myApplications.add(team.getName());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("team", "get error");
                    }
                });
    }

    private void retrieveUserName(String userId, String teamId, OwnerApplicationListAdapter adapter) {
        DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference();
        teamsRef.child("users").child(userId).child("name").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("team", "get success");
                        String name = task.getResult().getValue(String.class);
                        if (name != null) {
                            addTeamName(name, teamId, adapter);
                        }
                    } else {
                        Log.d("team", "get error");
                    }
                });
    }

    private void addTeamName(String name, String teamId, OwnerApplicationListAdapter adapter) {
        DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference();
        teamsRef.child("teams").child(teamId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("team", "get success");
                        Team team = task.getResult().getValue(Team.class);
                        if (team != null) {
                            OwnerApplication app = new OwnerApplication();
                            app.setTeamName(team.getName());
                            app.setUserName(name);
                            ownerApplications.add(app);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("team", "get error");
                    }
                });
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

    public void acceptApplication(View view) {

    }

    public void deleteApplication(View view) {

    }
}