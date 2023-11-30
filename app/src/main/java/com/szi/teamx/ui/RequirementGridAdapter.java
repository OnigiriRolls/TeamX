package com.szi.teamx.ui;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.szi.teamx.R;

import java.util.List;

public class RequirementGridAdapter extends ArrayAdapter<String>  {
    private Context context;
    private List<String> requirements;
    private int layoutResID;

    public RequirementGridAdapter(Context context, int layoutResID, List<String> requirements) {
        super(context, layoutResID, requirements);
        this.context = context;
        this.layoutResID = layoutResID;
        this.requirements = requirements;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequirementGridAdapter.ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new RequirementGridAdapter.ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.tRequirement = (TextView) view.findViewById(R.id.tRequirement);

            view.setTag(itemHolder);
        } else {
            itemHolder = (RequirementGridAdapter.ItemHolder) view.getTag();
        }

        if (requirements != null && requirements.size() >= position) {
            final String tItem = requirements.get(position);
            itemHolder.tRequirement.setText(tItem);
            Log.i("hello", tItem);
        }

        return view;
    }

    private static class ItemHolder {
        TextView tRequirement;
    }
}
