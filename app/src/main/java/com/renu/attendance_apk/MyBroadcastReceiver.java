package com.renu.attendance_apk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Network.isNetworkAvailable(context)) {

            final String ATTENDANCES_INDEX_TABLE = "attendanceindex";

            final String ATTENDANCES_TIME = "time";
            final String ATTENDANCES_FOR = "attendancesfor";

            final String ATTENDANCES_TABLE = "attendance";
            final String ROLL = "roll";
            final String ATTENDANCES = "attendances";
            final String ATTENDANCES_TIMES = "datetime";

            final String DELETE_ALL_VALUES_FROM_ATTENDANCES_INDEX = "delete from " + ATTENDANCES_INDEX_TABLE;
            final String DELETE_ALL_VALUES_FROM_ATTENDANCE = "delete from " + ATTENDANCES_TABLE;


            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
            DatabaseReference databaseReferenceForAttendances = FirebaseDatabase.getInstance().getReference("attendance");
            DatabaseReference databaseReferenceForAttendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex");
            List<String> rollList = new ArrayList<>();
            List<String> attendancesList = new ArrayList<>();
            List<String> dateTimeList = new ArrayList<>();
            Cursor cursor = dataBaseHelper.getAllDataFromAttendancesIndex();

            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
            while (cursor.moveToNext()) {
                String dateTime = cursor.getString(0);
                String attendanceFor = cursor.getString(1);

                AttendanceIndexModel attendanceIndexModel = new AttendanceIndexModel(dateTime, attendanceFor);
                databaseReferenceForAttendancesIndex.child(dateTime).setValue(attendanceIndexModel);
                String query = "SELECT * FROM " + ATTENDANCES_TABLE + " WHERE " + ATTENDANCES_TIMES + " = '" + dateTime + "'";

                Cursor c = sqLiteDatabase.rawQuery(query, null);
                while (c.moveToNext()) {
                    rollList.add(c.getString(0));
                    attendancesList.add(c.getString(1));
                    dateTimeList.add(c.getString(2));

                }
                AttendanceModel attendanceModel = new AttendanceModel(rollList, attendancesList, dateTimeList);
                databaseReferenceForAttendances.child(dateTime).setValue(attendanceModel);
                rollList.clear();
                attendancesList.clear();
                dateTimeList.clear();


            }
            sqLiteDatabase.execSQL(DELETE_ALL_VALUES_FROM_ATTENDANCES_INDEX);
            sqLiteDatabase.execSQL(DELETE_ALL_VALUES_FROM_ATTENDANCE);


        }
    }


}
