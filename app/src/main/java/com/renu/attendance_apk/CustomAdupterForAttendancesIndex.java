package com.renu.attendance_apk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomAdupterForAttendancesIndex extends BaseAdapter {
    String[] dt;
    String[] att;
    Context context;
    private LayoutInflater layoutInflater;

    public CustomAdupterForAttendancesIndex(Context context, String[] dt, String[] att) {
        this.dt = dt;
        this.att = att;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dt.length;
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
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.attendances_index_listview, parent, false);


        }
        TextView atTextview = convertView.findViewById(R.id.attTextViewId);
        TextView dtTextView = convertView.findViewById(R.id.dtTextViewId);
        atTextview.setText(att[position]);
        dtTextView.setText(dt[position]);


        return convertView;
    }
}
