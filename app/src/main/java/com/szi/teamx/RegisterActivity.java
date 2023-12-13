package com.szi.teamx;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.szi.teamx.utils.AuthenticationValidator;

import java.util.Objects;

public class RegisterActivity extends BaseActivity {
    private EditText email;
    private EditText password;
    private TextView emailError, passwordError;
    private AuthenticationValidator inputsValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
    }

    public void onRegister(View view) {
        if (inputsValidator.inputsAreValid(email.getText().toString(), password.getText().toString(), emailError, passwordError)) {
            register(email.getText().toString(), password.getText().toString());

            if (getCurrentUser() != null) {
                Intent intent = new Intent(this, MyTeamsActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void register(String email, String password) {

        getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthWeakPasswordException e) {
                                passwordError.setText(R.string.password_6_characters);
                                passwordError.setVisibility(View.VISIBLE);
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                passwordError.setText(R.string.invalid_email);
                                passwordError.setVisibility(View.VISIBLE);
                            } catch (FirebaseAuthUserCollisionException e) {
                                passwordError.setText(R.string.already_user_registered);
                                passwordError.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                passwordError.setText(R.string.register_fail_error);
                                passwordError.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }
}