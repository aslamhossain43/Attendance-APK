package com.renu.attendance_apk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BackgroundUpdate {

    static Context context;


    DatabaseReference databaseReferenceForattendances;
    DatabaseReference databaseReferenceForattendancesIndex;
    DatabaseReference databaseReferenceForRollName;
    DatabaseReference databaseReferenceForRollNameIndex;
    static DatabaseReference databaseReferenceForPercentage;
    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    static DataBaseHelper dataBaseHelper;
    static SQLiteDatabase sqLiteDatabase;
    String uuidForAtt, uuidForAttIndex;
    static List<String> rollList;
    static List<String> nameList;
    static List<String> attendancesList;
    static List<String> dateTimeList;
    static List<String> rollListForRoll;
    static List<String> nameListForRoll;
    static List<String> attendancesListForRoll;
    static List<String> dateTimeListForRoll;


    static List<String> attListForTest;
    static List<String> rollListForTest;
    static List<Integer> dayList;
    static List<Integer> pList;
    static List<Integer> aList;
    static List<Integer> percentList;

    final String ROLL_NAME_TABLE = "rollname";
    final String ROLL_NAME_INDEX_TABLE = "rollnameindex";

    final String ROLL_NAME_TABLE_FIREBASE = "rollnamefirebase";
    final String ROLL_NAME_INDEX_TABLE_FIREBASE = "rollnameindexfirebase";


    static final String TEST_TABLE = "test";
    static final String PERCENTAGE_ATT_FOR = "attfor";


    final String ATT_FOR_ROLLNAME = "attfor";
    final String DATETIME_FOR_ROLLNAME = "time";

    final String ATTENDANCES_TABLE = "attendance";
    final String ATTENDANCES_TIMES = "datetime";


    private static final String WHOLE_INFORMATION_TABLE = "wholeinformations";
    private static String emailMobilePassRollnameIndex = null;
    private static String emailMobilePassRollname = null;
    private static String emailMobilePassAttIndex = null;
    private static String emailMobilePassAtt = null;
    private static String emailMobilePassTest = null;
    private static String emailMobilePass = null;


    public BackgroundUpdate() {

        //getWholeInformation();
        //initAll(context);


       // handlePercentage(context);


    }

    public static void getWholeInformation(Context context) {

        dataBaseHelper = new DataBaseHelper(context);

        sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + WHOLE_INFORMATION_TABLE, null);

        //if (cursor.getCount() != 0) {
try {

    while (cursor.moveToNext()) {

        emailMobilePassRollnameIndex = cursor.getString(0);
        emailMobilePassRollname = cursor.getString(1);
        emailMobilePassAttIndex = cursor.getString(2);
        emailMobilePassAtt = cursor.getString(3);
        emailMobilePassTest = cursor.getString(4);
        emailMobilePass = cursor.getString(5);

    }
}
finally {
    if (cursor != null && !cursor.isClosed())
        cursor.close();
    sqLiteDatabase.close();
}
        //}
        sqLiteDatabase.close();
    }

    public static void initAll(Context context) {
        databaseReferenceForPercentage = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassTest);


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


    public static void handlePercentage(Context context) {
        getWholeInformation(context);
        initAll(context);


        dataBaseHelper = new DataBaseHelper(context);

        sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        databaseReferenceForPercentage.getRef().removeValue();


        Cursor cursor = dataBaseHelper.getAllDataFromRollNameIndex();
        Log.d("rr", "handlePercentage: =============percentage handle");
try {

    while (cursor.moveToNext()) {
        String attendanceFor = cursor.getString(0);
        String dateTime = cursor.getString(1);

        String q = "SELECT * FROM " + TEST_TABLE + " WHERE " + PERCENTAGE_ATT_FOR + " = '" + attendanceFor + "'";

        Cursor cursor1 = sqLiteDatabase.rawQuery(q, null);


        while (cursor1.moveToNext()) {
            attListForTest.add(cursor1.getString(0));
            rollListForTest.add(cursor1.getString(1));
            dayList.add(cursor1.getInt(2));
            pList.add(cursor1.getInt(3));
            aList.add(cursor1.getInt(4));
            percentList.add(cursor1.getInt(5));




        }
        PercentageModel percentageModel = new PercentageModel(attListForTest, rollListForTest, dayList, pList, aList, percentList);
        databaseReferenceForPercentage.child(attendanceFor).getRef().setValue(percentageModel);

        attListForTest.clear();
        rollListForTest.clear();
        dayList.clear();
        pList.clear();
        percentList.clear();


    }
}finally {

    if (cursor != null && !cursor.isClosed())
        cursor.close();
    sqLiteDatabase.close();

}
    }

}
