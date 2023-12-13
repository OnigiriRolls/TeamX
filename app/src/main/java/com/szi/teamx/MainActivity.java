package com.szi.teamx;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.szi.teamx.utils.AuthenticationValidator;

import java.util.Objects;

public class MainActivity extends BaseActivity {
    private EditText email;
    private EditText password;
    private TextView emailError, passwordError;
    private AuthenticationValidator inputsValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputsValidator = new AuthenticationValidator();

        email = findViewById(R.id.tEmail);
        password = findViewById(R.id.tPassword);
        emailError = findViewById(R.id.tEmailError);
        passwordError = findViewById(R.id.tPasswordError);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        FirebaseApp.initializeApp(this);

        if (isUserLoggedIn()) {
            Intent intent = new Intent(this, MyTeamsActivity.class);
            startActivity(intent);
            finish();
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void onLogin(View view) {
        if (inputsValidator.inputsAreValid(email.getText().toString(), password.getText().toString(), emailError, passwordError)) {
            login(email.getText().toString(), password.getText().toString());
        }
    }

    private void startMyTeamsActivity() {
        Intent intent = new Intent(this, MyTeamsActivity.class);
        startActivity(intent);
        finish();
    }

    private void login(String email, String password) {
        getFirebaseAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startMyTeamsActivity();
                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthWeakPasswordException e) {
                                passwordError.setText(R.string.password_6_characters);
                                passwordError.setVisibility(View.VISIBLE);
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                passwordError.setText(R.string.invalid_credentials);
                                passwordError.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                passwordError.setText(R.string.login_failed);
                                passwordError.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }


    public void onRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit the application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
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
}