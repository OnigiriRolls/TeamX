package com.szi.teamx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.UiModeManager;
import android.content.Context;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SysMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSystemTheme();
    }

    public void onLogin(View view) {
        Intent intent = new Intent(this, MyTeamsActivity.class);
        startActivity(intent);
    }

    private void checkSystemTheme() {
        UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);

        if (uiModeManager != null) {
            int currentMode = uiModeManager.getNightMode();

            if (currentMode == UiModeManager.MODE_NIGHT_NO) {
                Log.i(MainActivity.TAG, "Light mode");
            } else if (currentMode == UiModeManager.MODE_NIGHT_YES) {
                Log.i(MainActivity.TAG, "Dark mode");
            } else {
                Log.i(MainActivity.TAG, "Unknown mode");
            }
        }
    }

}