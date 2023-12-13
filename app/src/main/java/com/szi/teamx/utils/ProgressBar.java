package com.szi.teamx.utils;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.szi.teamx.BaseActivity;
import com.szi.teamx.R;

public class ProgressBar {
    public static AlertDialog showLoadingDialog(BaseActivity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_bar, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        dialog.show();
        return dialog;
    }

    public static void hideLoadingDialog(AlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
