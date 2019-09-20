package com.renu.attendance_apk;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomAdupterForIndexFromFirebase extends BaseAdapter {
    String[]rollString;
    String[]attString;
    Context context;

    public CustomAdupterForIndexFromFirebase(Context context,String[] rollString, String[] attString) {
        this.rollString = rollString;
        this.attString = attString;
        this.context=context;
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
        if (convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.specific_attendances_from_firebase_list_layout,parent,false);


        }


        TextView rollTextView=convertView.findViewById(R.id.rollTextViewId);
        TextView attTextView=convertView.findViewById(R.id.attTextViewId);
        rollTextView.setText(rollString[position]);
        attTextView.setText(attString[position]);


        return convertView;
    }
}
