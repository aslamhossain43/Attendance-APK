package com.renu.attendance_apk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyBroadcastReceiver extends BroadcastReceiver {
    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex,
            databaseReferenceForRollName, databaseReferenceForRollNameIndex, databaseReferenceForPercentage;
    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;
    String uuidForAtt, uuidForAttIndex;
    List<String> rollList, nameList, attendancesList, dateTimeList;
    List<String> rollListForRoll, nameListForRoll, attendancesListForRoll, dateTimeListForRoll;


    List<String> attListForTest, rollListForTest;
    List<Integer> dayList, pList, aList, percentList;

    final String ROLL_NAME_TABLE = "rollname";
    final String ROLL_NAME_INDEX_TABLE = "rollnameindex";

    final String ROLL_NAME_TABLE_FIREBASE = "rollnamefirebase";
    final String ROLL_NAME_INDEX_TABLE_FIREBASE = "rollnameindexfirebase";


    final String TEST_TABLE = "test";
    final String PERCENTAGE_ATT_FOR = "attfor";


    final String ATT_FOR_ROLLNAME = "attfor";
    final String DATETIME_FOR_ROLLNAME = "time";

    final String ATTENDANCES_TABLE = "attendance";
    final String ATTENDANCES_TIMES = "datetime";


    private static final String WHOLE_INFORMATION_TABLE = "wholeinformations";
    private String emailMobilePassRollnameIndex = null;
    private String emailMobilePassRollname = null;
    private String emailMobilePassAttIndex = null;
    private String emailMobilePassAtt = null;
    private String emailMobilePassTest = null;
    private String emailMobilePass = null;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {


            if (Network.isNetworkAvailable(context)) {


                getWholeInformation(context);
                initAll(context);
                handleAttAndAttIndex(context);
                handleRollnameAndRollnameIndex(context);


            }


        }
    }

    private void handleRollnameAndRollnameIndex(Context context) {


        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        Cursor cursor = dataBaseHelper.getAllDataFromRollNameIndexFirebase();
        try {


            while (cursor.moveToNext()) {
                String attendanceFor = cursor.getString(0);
                String dateTime = cursor.getString(1);


                RollNameIndexModel rollNameIndexModel = new RollNameIndexModel(dateTime, attendanceFor);

                databaseReferenceForRollNameIndex.child(dateTime).getRef().setValue(rollNameIndexModel);
                //--------------------------
                String q = "SELECT * FROM " + ROLL_NAME_TABLE_FIREBASE + " WHERE " + DATETIME_FOR_ROLLNAME + " = '" + dateTime + "'";

                Cursor cursor1 = sqLiteDatabase.rawQuery(q, null);
                while (cursor1.moveToNext()) {
                    rollListForRoll.add(cursor1.getString(0));
                    nameListForRoll.add(cursor1.getString(1));
                    attendancesListForRoll.add(cursor1.getString(2));
                    dateTimeListForRoll.add(cursor1.getString(3));

                }
                RollNameModel rollNameModel = new RollNameModel(rollListForRoll, nameListForRoll, attendancesListForRoll, dateTimeListForRoll);
                databaseReferenceForRollName.child(dateTime).getRef().setValue(rollNameModel);
                rollListForRoll.clear();
                nameListForRoll.clear();
                attendancesListForRoll.clear();
                dateTimeListForRoll.clear();


            }

            dataBaseHelper.delete_rollname_And_IndexFirebase();
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            sqLiteDatabase.close();
        }


    }

    private void handleAttAndAttIndex(Context context) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        Cursor cursor = dataBaseHelper.getAllDataFromAttendancesIndex();
        try {


            while (cursor.moveToNext()) {
                String dateTime = cursor.getString(0);
                String attendanceFor = cursor.getString(1);

                AttendanceIndexModel attendanceIndexModel = new AttendanceIndexModel(dateTime, attendanceFor);
                databaseReferenceForattendancesIndex.child(dateTime).setValue(attendanceIndexModel);
                //--------------------------
                String q = "SELECT * FROM " + ATTENDANCES_TABLE + " WHERE " + ATTENDANCES_TIMES + " = '" + dateTime + "'";

                Cursor cursor1 = sqLiteDatabase.rawQuery(q, null);
                while (cursor1.moveToNext()) {
                    rollList.add(cursor1.getString(0));
                    nameList.add(cursor1.getString(1));
                    attendancesList.add(cursor1.getString(2));
                    dateTimeList.add(cursor1.getString(3));

                }
                AttendanceModel attendanceModel = new AttendanceModel(rollListForRoll, nameListForRoll, attendancesListForRoll, dateTimeListForRoll);
                databaseReferenceForattendances.child(dateTime).setValue(attendanceModel);
                rollList.clear();
                nameList.clear();
                attendancesList.clear();
                dateTimeList.clear();


            }

            dataBaseHelper.delete_Att_And_Index();
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            sqLiteDatabase.close();
        }

    }

    private void initAll(Context context) {


        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAttIndex);
        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAtt);

        databaseReferenceForRollNameIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollnameIndex);
        databaseReferenceForRollName = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollname);


        rollList = new ArrayList<>();
        nameList = new ArrayList<>();
        attendancesList = new ArrayList<>();
        dateTimeList = new ArrayList<>();

        rollListForRoll = new ArrayList<>();
        nameListForRoll = new ArrayList<>();
        attendancesListForRoll = new ArrayList<>();
        dateTimeListForRoll = new ArrayList<>();


        attListForTest = new ArrayList<>();
        rollListForTest = new ArrayList<>();
        dayList = new ArrayList<>();
        pList = new ArrayList<>();
        aList = new ArrayList<>();
        percentList = new ArrayList<>();


    }

    private void getWholeInformation(Context context) {


        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + WHOLE_INFORMATION_TABLE, null);

        try {


            while (cursor.moveToNext()) {

                emailMobilePassRollnameIndex = cursor.getString(0);
                emailMobilePassRollname = cursor.getString(1);
                emailMobilePassAttIndex = cursor.getString(2);
                emailMobilePassAtt = cursor.getString(3);
                emailMobilePassTest = cursor.getString(4);
                emailMobilePass = cursor.getString(5);

            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            sqLiteDatabase.close();

        }


    }


}
