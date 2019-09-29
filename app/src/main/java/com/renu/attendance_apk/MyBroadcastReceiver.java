package com.renu.attendance_apk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Network.isNetworkAvailable(context)) {


            final String ATTENDANCES_TABLE = "attendance";
            final String ATTENDANCES_TIMES = "datetime";


            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
            DatabaseReference databaseReferenceForAttendances = FirebaseDatabase.getInstance().getReference("attendance");
            DatabaseReference databaseReferenceForAttendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex");


            List<String> rollList = new ArrayList<>();
            List<String> nameList = new ArrayList<>();
            List<String> attendancesList = new ArrayList<>();
            List<String> dateTimeList = new ArrayList<>();
            Cursor cursor = dataBaseHelper.getAllDataFromAttendancesIndex();

            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
            while (cursor.moveToNext()) {
                String dateTime = cursor.getString(0);
                String attendanceFor = cursor.getString(1);

                AttendanceIndexModel attendanceIndexModel = new AttendanceIndexModel(dateTime, attendanceFor);
                databaseReferenceForAttendancesIndex.child(dateTime).setValue(attendanceIndexModel);
                //--------------------------
                String q = "SELECT * FROM " + ATTENDANCES_TABLE + " WHERE " + ATTENDANCES_TIMES + " = '" + dateTime + "'";

                Cursor cursor1 = sqLiteDatabase.rawQuery(q, null);
                while (cursor1.moveToNext()) {
                    rollList.add(cursor1.getString(0));
                    nameList.add(cursor1.getString(1));
                    attendancesList.add(cursor1.getString(2));
                    dateTimeList.add(cursor1.getString(3));

                }
                AttendanceModel attendanceModel = new AttendanceModel(rollList, nameList, attendancesList, dateTimeList);
                databaseReferenceForAttendances.child(dateTime).setValue(attendanceModel);
                rollList.clear();
                nameList.clear();
                attendancesList.clear();
                dateTimeList.clear();


            }
            Intent i = new Intent(context, AttendancesIndex.class);
            context.startActivity(i);
            Toast.makeText(context, "Your Counted Attendances Saved Successfully !", Toast.LENGTH_SHORT).show();


        } else {
            return;
        }
    }


}
