package com.szi.teamx.ui;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.szi.teamx.R;
import com.szi.teamx.model.Team;

import java.util.List;

public class TeamNameListAdapter extends ArrayAdapter<Team> {
    private Context context;
    private List<Team> teams;
    private int layoutResID;

    public TeamNameListAdapter(Context context, int layoutResID, List<Team> teams) {
        super(context, layoutResID, teams);
        this.context = context;
        this.layoutResID = layoutResID;
        this.teams = teams;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.tName = (TextView) view.findViewById(R.id.tName);

            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) view.getTag();
        }
        Log.i("hello", "in adapter");
        if (teams != null && teams.size() >= position) {
            Log.i("hello", "in if");
            final Team tItem = teams.get(position);
            itemHolder.tName.setText(tItem.getName());
        }

        return view;
    }

    private static class ItemHolder {
        TextView tName;
    }
}
