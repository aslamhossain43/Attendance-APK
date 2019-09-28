package com.renu.attendance_apk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomAdupterForManagingLocalAttIndex extends BaseAdapter {
    Context context;
    String[]sAtt;
    String[]sDateTime;

    public CustomAdupterForManagingLocalAttIndex(Context context, String[] sAtt, String[] sDateTime) {
        this.context = context;
        this.sAtt = sAtt;
        this.sDateTime = sDateTime;
    }

    @Override
    public int getCount() {
        return sAtt.length;
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
            convertView=layoutInflater.inflate(R.layout.manage_for_local_att_type_index_layout,parent,false);



        }
        TextView textView=convertView.findViewById(R.id.manageRollNameTextViewId);
        textView.setText(sAtt[position]);

        TextView textViewDate=convertView.findViewById(R.id.manageRollNameDateTextViewId);
        textViewDate.setText(sDateTime[position]);



        return convertView;
    }
}
