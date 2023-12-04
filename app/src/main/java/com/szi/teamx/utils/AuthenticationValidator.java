package com.szi.teamx.utils;

import android.view.View;
import android.widget.TextView;

import com.szi.teamx.R;

public class AuthenticationValidator {

    public boolean inputsAreValid(String email, String password, TextView emailError, TextView passwordError) {
        boolean errors = false;

        if (email == null || email.isEmpty()) {
            emailError.setText(R.string.email_empty_error);
            emailError.setVisibility(View.VISIBLE);
            errors = true;
        }

        if (password == null || password.isEmpty()) {
            passwordError.setText(R.string.password_empty_error);
            passwordError.setVisibility(View.VISIBLE);
            errors = true;
        }
        return errors ? false : true;
    }
}
