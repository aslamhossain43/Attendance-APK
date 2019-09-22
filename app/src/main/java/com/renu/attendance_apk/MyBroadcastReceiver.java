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
            final String NAME = "name";
            final String ATTENDANCES = "attendances";
            final String ATTENDANCES_TIMES = "datetime";

            final String ROLL_NAME_TABLE = "rollname";
            final String ROLL_FOR_ROLLNAME = "roll";
            final String NAME_FOR_ROLLNAME = "name";
            final String ATT_FOR_ROLLNAME = "attfor";
            final String DATETIME_FOR_ROLLNAME = "time";


            final String ROLL_NAME_INDEX_TABLE = "rollnameindex";


            final String ATT_FOR_ROLLNAME_INDEX = "attfor";
            final String DATETIME_FOR_ROLLNAME_INDEX = "time";

            final String DELETE_ALL_VALUES_FROM_ATTENDANCES_INDEX = "delete from " + ATTENDANCES_INDEX_TABLE;
            final String DELETE_ALL_VALUES_FROM_ATTENDANCE = "delete from " + ATTENDANCES_TABLE;


            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
            DatabaseReference databaseReferenceForAttendances = FirebaseDatabase.getInstance().getReference("attendance");
            DatabaseReference databaseReferenceForRollName = FirebaseDatabase.getInstance().getReference("rollnames");
            DatabaseReference databaseReferenceForRollNameIndex = FirebaseDatabase.getInstance().getReference("rollnamesindex");
            DatabaseReference databaseReferenceForAttendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex");

            List<String> roll = new ArrayList<>();
            List<String> name = new ArrayList<>();
            List<String> attFor = new ArrayList<>();
            List<String> dtRollNameList = new ArrayList<>();

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

//---------------------------------------------------------------------------------
/*

            Cursor cur = dataBaseHelper.getAllDataFromRollNameIndex();


            while (cur.moveToNext()) {
                String attendanceFor = cur.getString(0);
                String dateTime = cur.getString(1);

                RollNameIndexModel rollNameIndexModel = new RollNameIndexModel(dateTime, attendanceFor);
                databaseReferenceForRollNameIndex.child(dateTime).setValue(rollNameIndexModel);

                //------------------------------------------------------------------------------------
                String q = "SELECT * FROM " + ROLL_NAME_TABLE + " WHERE " + DATETIME_FOR_ROLLNAME + " = '" + dateTime + "'";

                Cursor cursor1 = sqLiteDatabase.rawQuery(q, null);
                while (cursor1.moveToNext()) {
                    rollList.add(cursor1.getString(0));
                    nameList.add(cursor1.getString(1));
                    attendancesList.add(cursor1.getString(2));
                    dateTimeList.add(cursor1.getString(3));

                }


                RollNameModel rollNameModel = new RollNameModel(rollList, nameList, attendancesList, dateTimeList);

                databaseReferenceForRollName.child(dateTime).setValue(rollNameModel);
                rollList.clear();
                nameList.clear();
                attendancesList.clear();
                dateTimeList.clear();


            }

*/

        }else {
            return;
        }
    }


}
