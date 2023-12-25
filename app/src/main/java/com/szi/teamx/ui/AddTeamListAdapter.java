package com.szi.teamx.ui;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.szi.teamx.R;
import com.szi.teamx.model.RequirementItem;

import java.util.List;

public class AddTeamListAdapter extends ArrayAdapter<RequirementItem> {
    private Context context;
    private List<RequirementItem> requirements;
    private int layoutResID;

    public AddTeamListAdapter(Context context, int layoutResID, List<RequirementItem> requirements) {
        super(context, layoutResID, requirements);
        this.context = context;
        this.requirements = requirements;
        this.layoutResID = layoutResID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddTeamListAdapter.ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new AddTeamListAdapter.ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.tRequirement = (EditText) view.findViewById(R.id.tRequirement);
            itemHolder.bDelete = (ImageButton) view.findViewById(R.id.bDelete);

            view.setTag(itemHolder);
        } else {
            itemHolder = (AddTeamListAdapter.ItemHolder) view.getTag();
        }

        if (requirements != null && requirements.size() >= position) {
            final RequirementItem tItem = requirements.get(position);
            itemHolder.tRequirement.setText(tItem.getUserInput());
            itemHolder.bDelete.setContentDescription(String.valueOf(position));

            itemHolder.tRequirement.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                   tItem.setUserInput(editable.toString());
                }
            });
        }

        return view;
    }

    private static class ItemHolder {
        EditText tRequirement;
        ImageButton bDelete;
    }
}
