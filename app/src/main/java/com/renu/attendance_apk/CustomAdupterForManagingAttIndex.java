package com.renu.attendance_apk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdupterForManagingAttIndex extends BaseAdapter {
    Context context;
    String[]dt;
    String[]attFor;


    public CustomAdupterForManagingAttIndex(Context context, String[] dt, String[] attFor) {
        this.context = context;
        this.dt = dt;
        this.attFor = attFor;
    }

    @Override
    public int getCount() {
        return attFor.length;
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
            convertView = layoutInflater.inflate(R.layout.manage_for_att_index_layout, parent, false);


        }


        TextView manageAttTextView = convertView.findViewById(R.id.manageAttTextViewId);
        TextView manageDdtTextView = convertView.findViewById(R.id.manageDdtTextViewId);

        manageAttTextView.setText(attFor[position]);
        manageDdtTextView.setText(dt[position]);


        return convertView;
    }
}
