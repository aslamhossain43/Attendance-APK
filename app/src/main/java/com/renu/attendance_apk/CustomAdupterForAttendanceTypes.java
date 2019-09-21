package com.renu.attendance_apk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomAdupterForAttendanceTypes extends BaseAdapter {
    Context context;
    String[]keys;

    public CustomAdupterForAttendanceTypes(Context context, String[] keys) {
        this.context = context;
        this.keys = keys;
    }

    @Override
    public int getCount() {
        return keys.length;
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
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.exist_rollname_layout,parent,false);



        }
        TextView textView=convertView.findViewById(R.id.rollNameTextViewId);
        textView.setText(keys[position]);




        return convertView;
    }
}
