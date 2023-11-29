package com.szi.teamx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.szi.teamx.model.Team;
import com.szi.teamx.ui.TeamNameListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyTeamsActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<Team> teams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams);

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
                    Log.i("hello", "in on add");
                    Team team = snapshot.getValue(Team.class);

                    if (team != null) {
                        teams.add(team);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    System.out.println("Eroare la citire");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Team team = snapshot.getValue(Team.class);
                    Log.i("hello", "in on change");
                    if (team != null) {
                        if (teams.contains(team))
                            teams.set(teams.indexOf(team), team);
                        else teams.add(team);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    System.out.println("Eroare la citire");
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void openTeamInfoActivity(Team team) {
        Intent intent = new Intent(this, TeamInfoActivity.class);
        intent.putExtra("teamName", team.getName());
        intent.putExtra("teamDescription", team.getDescription());
        startActivity(intent);
    }

}