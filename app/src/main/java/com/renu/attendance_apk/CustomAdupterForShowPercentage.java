package com.renu.attendance_apk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomAdupterForShowPercentage extends BaseAdapter {


    Context context;
    String[]sAtt;

    public CustomAdupterForShowPercentage(Context context, String[] sAtt) {
        this.context = context;
        this.sAtt = sAtt;

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
            convertView=layoutInflater.inflate(R.layout.percentage_field,parent,false);



        }
        TextView textView=convertView.findViewById(R.id.percenttageAttTextViewId);
        textView.setText(sAtt[position]);


        return convertView;
    }













}
