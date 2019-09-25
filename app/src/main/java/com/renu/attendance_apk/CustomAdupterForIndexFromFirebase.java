package com.renu.attendance_apk;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomAdupterForIndexFromFirebase extends BaseAdapter {
    String[] rollString;
    String[] nameString;
    String[] attString;
    Context context;

    public CustomAdupterForIndexFromFirebase(Context context, String[] rollString, String[] nameString, String[] attString) {
        this.rollString = rollString;
        this.nameString = nameString;
        this.attString = attString;
        this.context = context;
    }

    @Override
    public int getCount() {
        return rollString.length;
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
            convertView = layoutInflater.inflate(R.layout.specific_attendances_from_firebase_list_layout, parent, false);


        }


        TextView rollTextView = convertView.findViewById(R.id.rollTextViewId);
        TextView nameTextView = convertView.findViewById(R.id.nameTextViewId);
        TextView attTextView = convertView.findViewById(R.id.attTextViewId);
        if (!(rollString[position] == null && nameString[position] == null && attString[position] == null)) {


            rollTextView.setText(rollString[position]);
            nameTextView.setText(nameString[position]);
            attTextView.setText(attString[position]);
        }else {
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,position));
            convertView.setVisibility(View.GONE);
        }


        return convertView;
    }
}
