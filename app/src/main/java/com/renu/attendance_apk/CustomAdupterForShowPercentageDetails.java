package com.renu.attendance_apk;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomAdupterForShowPercentageDetails extends BaseAdapter {
    Context context;
    String[]percentRoll;
    int[]percentDay,percentP,percentA;
    int[]percentPercent;

    public CustomAdupterForShowPercentageDetails(Context context, String[] percentRoll, int[] percentDay, int[] percentP, int[] percentA, int[] percentPercent) {
        this.context = context;
        this.percentRoll = percentRoll;
        this.percentDay = percentDay;
        this.percentP = percentP;
        this.percentA = percentA;
        this.percentPercent = percentPercent;
    }

    @Override
    public int getCount() {
        return percentRoll.length;
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
            convertView=layoutInflater.inflate(R.layout.percentage_show_field,parent,false);
        }

        TextView rollTextView=convertView.findViewById(R.id.percentageRollTextViewId);
        TextView dayTextView=convertView.findViewById(R.id.percentageDayTextViewId);
        TextView pTextView=convertView.findViewById(R.id.pTextViewId);
        TextView aTextView=convertView.findViewById(R.id.aTextViewId);
        TextView percentTextView=convertView.findViewById(R.id.percentageTextViewId);
        rollTextView.setText(percentRoll[position]);
        dayTextView.setText(""+percentDay[position]);
        pTextView.setText(""+percentP[position]);
        aTextView.setText(""+percentA[position]);
        percentTextView.setText(""+percentPercent[position]+" %");


        return convertView;
    }
}
