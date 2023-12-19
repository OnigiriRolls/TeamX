package com.szi.teamx.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.szi.teamx.R;
import com.szi.teamx.model.OwnerApplication;

import java.util.List;

public class OwnerApplicationListAdapter extends ArrayAdapter<OwnerApplication> {
    private Context context;
    private List<OwnerApplication> applications;
    private int layoutResID;

    public OwnerApplicationListAdapter(Context context, int layoutResID, List<OwnerApplication> applications) {
        super(context, layoutResID, applications);
        this.context = context;
        this.applications = applications;
        this.layoutResID = layoutResID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OwnerApplicationListAdapter.ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new OwnerApplicationListAdapter.ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.tTeam = (TextView) view.findViewById(R.id.tTeam);
            itemHolder.tUser = (TextView) view.findViewById(R.id.tUser);

            view.setTag(itemHolder);
        } else {
            itemHolder = (OwnerApplicationListAdapter.ItemHolder) view.getTag();
        }

        if (applications != null && applications.size() >= position) {
            final OwnerApplication tItem = applications.get(position);
            itemHolder.tTeam.setText(tItem.getTeamName());
            itemHolder.tUser.setText(tItem.getUserName());
        }

        return view;
    }

    private static class ItemHolder {
        TextView tTeam;
        TextView tUser;
    }
}
