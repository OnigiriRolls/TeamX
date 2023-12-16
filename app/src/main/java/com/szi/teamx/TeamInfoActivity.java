package com.szi.teamx;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.szi.teamx.model.User;
import com.szi.teamx.ui.RequirementGridAdapter;
import com.szi.teamx.utils.QRCodeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeamInfoActivity extends BaseActivity {
    private String id;
    private String ownerId;
    private String userId;
    private QRCodeGenerator generator;
    private Button applyButton;
    private Button deleteButton;
    private Button leaveButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        generator = new QRCodeGenerator();

        applyButton = findViewById(R.id.bApply);
        deleteButton = findViewById(R.id.bDelete);
        leaveButton = findViewById(R.id.bLeave);

        String teamName = getIntent().getStringExtra("teamName");
        String teamDescription = getIntent().getStringExtra("teamDescription");

        id = getIntent().getStringExtra("teamId");
        ownerId = getIntent().getStringExtra("teamOwner");
        userId = getCurrentUser().getUid();

        if (Objects.equals(ownerId, userId)) {
            applyButton.setVisibility(View.GONE);
            leaveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference().child("users").child(userId);

            databaseReference.child("teams").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        List<String> teamIds = new ArrayList<>();
                        for (DataSnapshot teamSnapshot : snapshot.getChildren()) {
                            String teamId = (String) teamSnapshot.getValue();
                            teamIds.add(teamId);
                        }

                        checkTeamIdsAndDisplayButton(teamIds, id);
                    } catch (Exception e) {
                        Log.i("hello", "eroare la citire");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        List<String> requirements = getIntent().getStringArrayListExtra("teamRequirements");

        TextView textViewTeamName = findViewById(R.id.tName);
        TextView textViewTeamDescription = findViewById(R.id.tDescription);

        textViewTeamName.setText(teamName);
        textViewTeamDescription.setText(teamDescription);

        GridView gridRequirementsLeft = findViewById(R.id.gRequirements);
        final RequirementGridAdapter adapterLeft = new RequirementGridAdapter(this, R.layout.requirement_grid_item, requirements);
        gridRequirementsLeft.setAdapter(adapterLeft);
    }

    private void checkTeamIdsAndDisplayButton(List<String> teamIds, String id) {
        if (teamIds.contains(id)) {
            applyButton.setVisibility(View.GONE);
            leaveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
        } else {
            applyButton.setVisibility(View.VISIBLE);
            leaveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }
    }

    public void onApply(View view) {
        applyButton.setText(R.string.team_applied);
        applyButton.setEnabled(false);
    }

    public void onDelete(View view) {
        deleteButton.setEnabled(false);
        deleteTeamFromUser();
    }

    private void deleteTeamFromUser() {
        DatabaseReference userTeamsOwnerRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("teamsOwner");

        userTeamsOwnerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> teamsOwnerList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String teamId = snapshot.getValue(String.class);

                        if (!teamId.equals(id)) {
                            teamsOwnerList.add(teamId);
                        }
                    }

                    userTeamsOwnerRef.setValue(teamsOwnerList)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    deleteTeamFromTeams();
                                    Log.d("team", "Team deleted successfully");
                                } else {

                                    Log.d("team", "Error deleting team");
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("DeleteTeam", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void deleteTeamFromTeams() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("teams").child(id).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("team", "delete success");
                        finish();
                    } else {
                        Log.d("team", "delete error");
                    }
                });
    }

    public void onLeave(View view) {
        leaveButton.setEnabled(false);
    }

    public void onQRCode(View view) {
        Dialog qrCodeDialog = new Dialog(this);
        qrCodeDialog.setContentView(R.layout.popup_qr_code);

        ImageView imageViewQrCode = qrCodeDialog.findViewById(R.id.imageViewQrCode);
        Bitmap qrCodeBitmap = generator.generateQrCode(id);
        imageViewQrCode.setImageBitmap(qrCodeBitmap);

        qrCodeDialog.show();
    }
}