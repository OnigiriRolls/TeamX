package com.szi.teamx.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.szi.teamx.R;

import java.util.List;

public class ApplicationListAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> applications;
    private int layoutResID;

    public ApplicationListAdapter(Context context, int layoutResID, List<String> applications) {
        super(context, layoutResID, applications);
        this.context = context;
        this.applications = applications;
        this.layoutResID = layoutResID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ApplicationListAdapter.ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ApplicationListAdapter.ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.tTeam = (TextView) view.findViewById(R.id.tTeam);

            view.setTag(itemHolder);
        } else {
            itemHolder = (ApplicationListAdapter.ItemHolder) view.getTag();
        }

        if (applications != null && applications.size() >= position) {
            final String tItem = applications.get(position);
            itemHolder.tTeam.setText(tItem);
        }

        return view;
    }

    private static class ItemHolder {
        TextView tTeam;
    }
}
