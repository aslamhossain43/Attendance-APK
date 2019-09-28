package com.renu.attendance_apk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomAdaptureForManagingAttPerson extends BaseAdapter {
    Context context;
    String[] roll;
    String[] name;


    public CustomAdaptureForManagingAttPerson(Context context, String[] roll, String[] name) {
        this.context = context;
        this.roll = roll;
        this.name = name;
    }

    @Override
    public int getCount() {
        return roll.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.manage_for_att_person_layout, parent, false);


        }
        TextView textViewRoll = convertView.findViewById(R.id.managePersonRollTextViewId);
        TextView textViewName = convertView.findViewById(R.id.managePersonNameTextViewId);
        textViewRoll.setText(roll[position]);
        textViewName.setText(name[position]);


        return convertView;
    }
}
