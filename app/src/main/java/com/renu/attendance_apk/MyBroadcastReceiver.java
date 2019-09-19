package com.renu.attendance_apk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Network.isNetworkAvailable(context)) {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("attendance");
            List<String> rollList = new ArrayList<>();
            List<String> attendancesList = new ArrayList<>();
            List<String> dateTimeList = new ArrayList<>();
            Cursor cursor = dataBaseHelper.getAllDataFromAttendancesIndex();

            while (cursor.moveToNext()) {
                String dateTime = cursor.getString(0);
                String attendanceFor = cursor.getString(1);

                String pushKeyForAttendancesIndex = databaseReference.push().getKey();
                AttendanceIndexModel attendanceIndexModel = new AttendanceIndexModel(dateTime, attendanceFor);
                databaseReference.child(pushKeyForAttendancesIndex).setValue(attendanceIndexModel);


            }


            Map<String, List<String>> map = dataBaseHelper.getAllDataFromAttendancesTable();
            rollList = map.get("rollList");
            attendancesList = map.get("attendanceList");
            dateTimeList = map.get("dateTimeList");

            String pushKey = databaseReference.push().getKey();
            AttendanceModel attendanceModel = new AttendanceModel(rollList, attendancesList, dateTimeList);
            databaseReference.child(pushKey).setValue(attendanceModel);


        }
    }


}
