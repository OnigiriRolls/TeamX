package com.szi.teamx.ui;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        AddTeamListAdapter.ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new AddTeamListAdapter.ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.tRequirement = view.findViewById(R.id.tRequirement);
            itemHolder.bDelete = view.findViewById(R.id.bDelete);
            itemHolder.bAdd = view.findViewById(R.id.bAdd);

            itemHolder.tRequirement.setTag(position);
            itemHolder.bDelete.setTag(position);

            view.setTag(itemHolder);
        } else {
            itemHolder = (AddTeamListAdapter.ItemHolder) view.getTag();
        }


        itemHolder.bDelete.setContentDescription(String.valueOf(position));

        itemHolder.tRequirement.removeTextChangedListener(itemHolder.textWatcher);
        itemHolder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final EditText caption = itemHolder.tRequirement;
                if (caption.getText().toString().length() > 0) {
                    RequirementItem item = requirements.get(position);
                    item.setUserInput(caption.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        itemHolder.tRequirement.addTextChangedListener(itemHolder.textWatcher);


        return view;
    }

    private static class ItemHolder {
        EditText tRequirement;
        ImageButton bDelete;
        ImageButton bAdd;
        TextWatcher textWatcher;
    }
}
