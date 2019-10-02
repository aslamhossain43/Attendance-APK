package com.renu.attendance_apk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AttBase.db";
    private static final String ATTENDANCES_TABLE = "attendance";
    private static final String ATTENDANCES_INDEX_TABLE = "attendanceindex";
    private static final String ROLL_NAME_TABLE = "rollname";
    private static final String ROLL_NAME_INDEX_TABLE = "rollnameindex";
    private static final String REGISTER_TABLE = "register";
    private static final String UUID_TABLE = "uuid";
    //private static final String PERCENTAGE_TABLE = "percent";
    private static final String TEST_TABLE = "test";
    //private static final String PERCENT_TABLE = "percentages";
    private static final int VERSION_NUMBER = 12;

    private static final String ROLL = "roll";
    private static final String NAME = "name";
    private static final String ATTENDANCES = "attendances";
    private static final String ATTENDANCES_TIMES = "datetime";

    private static final String ATTENDANCES_INDEX_TIME = "time";
    private static final String ATTENDANCES_FOR_INDEX = "attendancesfor";


    private static final String ROLL_FOR_ROLLNAME = "roll";
    private static final String NAME_FOR_ROLLNAME = "name";
    private static final String ATT_FOR_ROLLNAME = "attfor";
    private static final String DATETIME_FOR_ROLLNAME = "time";

    private static final String ATT_FOR_ROLLNAME_INDEX = "attfor";
    private static final String DATETIME_FOR_ROLLNAME_INDEX = "time";

    private static final String UUID_FOR_ATT = "uuidforatt";
    private static final String UUID_FOR_ATT_INDEX = "uuidforattindex";


    private static final String REGISTER_ID = "_id";
    private static final String REGISTER_USERNAME = "username";
    private static final String REGISTER_PASSWORD = "password";

    private static final String PERCENTAGE_ATT_FOR = "attfor";
    private static final String PERCENTAGE_ROLL = "roll";
    private static final String PERCENTAGE_DAY = "day";
    private static final String PERCENTAGE_P = "present";
    private static final String PERCENTAGE_A = "absent";
    private static final String PERCENTAGE_PERCENT = "percent";

    /*private static final String PERCENT_ATT_FOR = "attfor";
    private static final String PERCENT_ROLL = "roll";
    private static final String PERCENT_DAY = "day";
    private static final String PERCENT_P = "present";
    private static final String PERCENT_A = "absent";
    private static final String PERCENT_PERCENT = "percent";
*/

    private static final String CREATE_ATTENDANCES_TABLE = "CREATE TABLE " + ATTENDANCES_TABLE + "( " + ROLL + " VARCHAR(100)," + NAME + " VARCHAR(100)," + ATTENDANCES + " VARCHAR(10)," + ATTENDANCES_TIMES + " VARCHAR(100));";
    private static final String CREATE_ATTENDANCES_INDEX_TABLE = "CREATE TABLE " + ATTENDANCES_INDEX_TABLE + "( " + ATTENDANCES_INDEX_TIME + " VARCHAR(100)," + ATTENDANCES_FOR_INDEX + " VARCHAR(200));";
    private static final String CREATE_ROLLNAME_TABLE = "CREATE TABLE " + ROLL_NAME_TABLE + "( " + ROLL_FOR_ROLLNAME + " VARCHAR(100)," + NAME_FOR_ROLLNAME + " VARCHAR(100)," + ATT_FOR_ROLLNAME + " VARCHAR(100)," + DATETIME_FOR_ROLLNAME + " VARCHAR(100));";
    private static final String CREATE_ROLLNAME_INDEX_TABLE = "CREATE TABLE " + ROLL_NAME_INDEX_TABLE + "( " + ATT_FOR_ROLLNAME_INDEX + " VARCHAR(100)," + DATETIME_FOR_ROLLNAME_INDEX + " VARCHAR(100));";
    private static final String CREATE_REGISTER_TABLE = "CREATE TABLE " + REGISTER_TABLE + "( " + REGISTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + REGISTER_USERNAME + " TEXT," + REGISTER_PASSWORD + " TEXT);";
    private static final String CREATE_UUID_TABLE = "CREATE TABLE " + UUID_TABLE + "( " + UUID_FOR_ATT + " TEXT," + UUID_FOR_ATT_INDEX + " TEXT);";
    //private static final String CREATE_PERCENTAGE_TABLE = "CREATE TABLE " + PERCENTAGE_TABLE + "( " + PERCENTAGE_ATT_FOR + " TEXT," + PERCENTAGE_ROLL + " TEXT," + PERCENTAGE_DAY + " INT," + PERCENTAGE_P + " INT," + PERCENTAGE_A + " INT," + PERCENTAGE_PERCENT + " INT);";
    private static final String CREATE_TEST_TABLE = "CREATE TABLE " + TEST_TABLE + "( " + PERCENTAGE_ATT_FOR + " TEXT," + PERCENTAGE_ROLL + " TEXT," + PERCENTAGE_DAY +" INT," + PERCENTAGE_P + " INT," + PERCENTAGE_A + " INT," + PERCENTAGE_PERCENT +" INT);";
    //private static final String CREATE_PERCENT_TABLE = "CREATE TABLE " + PERCENT_TABLE + "( " + PERCENT_ATT_FOR + " TEXT," + PERCENT_ROLL + " TEXT," + PERCENT_DAY +" INT," + PERCENT_P + " INT," + PERCENT_A + " INT," + PERCENT_PERCENT +" INT);";


    private static final String DROP_ATTENDANCES_TABLE = "DROP TABLE IF EXISTS " + ATTENDANCES_TABLE;
    private static final String DROP_ATTENDANCES_INDEX_TABLE = "DROP TABLE IF EXISTS " + ATTENDANCES_INDEX_TABLE;
    private static final String DROP_ROLLNAME_TABLE = "DROP TABLE IF EXISTS " + ROLL_NAME_TABLE;
    private static final String DROP_ROLLNAME_INDEX_TABLE = "DROP TABLE IF EXISTS " + ROLL_NAME_INDEX_TABLE;
    private static final String DROP_REGISTER_TABLE = "DROP TABLE IF EXISTS " + REGISTER_TABLE;
    private static final String DROP_UUID_TABLE = "DROP TABLE IF EXISTS " + UUID_TABLE;
    //private static final String DROP_PERCENTAGE_TABLE = "DROP TABLE IF EXISTS " + PERCENTAGE_TABLE;
    private static final String DROP_TEST_TABLE = "DROP TABLE IF EXISTS " + TEST_TABLE;
    //private static final String DROP_PERCENT_TABLE = "DROP TABLE IF EXISTS " + PERCENT_TABLE;

    private static final String GET_ALL_ATTENDANCES_TABLE = "SELECT * FROM " + ATTENDANCES_TABLE;
    private static final String GET_ALL_ATTENDANCES_INDEX_TABLE = "SELECT * FROM " + ATTENDANCES_INDEX_TABLE;
    private static final String GET_ALL_ROLLNAME_TABLE = "SELECT * FROM " + ROLL_NAME_TABLE;
    private static final String GET_ALL_ROLLNAME_INDEX_TABLE = "SELECT * FROM " + ROLL_NAME_INDEX_TABLE;
    private static final String GET_ALL_UUID_FROM_TABLE = "SELECT * FROM " + UUID_TABLE;
    //private static final String GET_ALL_FROM_PERCENTAGE_TABLE = "SELECT * FROM " + PERCENTAGE_TABLE;

    private static final String DELETE_ALL_VALUES_FROM_ATTENDANCES_INDEX = "delete from " + ATTENDANCES_INDEX_TABLE;
    private static final String DELETE_ALL_VALUES_FROM_ATTENDANCE = "delete from " + ATTENDANCES_TABLE;
    private static final String DELETE_ALL_VALUES_FROM_ROLLNAME = "delete from " + ROLL_NAME_TABLE;
    private static final String DELETE_ALL_VALUES_FROM_ROLLNAME_INDEX = "delete from " + ROLL_NAME_INDEX_TABLE;
    private static final String DELETE_ALL_VALUES_FROM_UUID = "delete from " + UUID_TABLE;
    //private static final String DELETE_ALL_VALUES_FROM_PERCENTAGE = "delete from " + PERCENTAGE_TABLE;
    //private static final String DELETE_ALL_VALUES_FROM_PERCENT = "delete from " + PERCENT_TABLE;
    //private static final String DELETE_ALL_VALUES_FROM_TEST = "delete from " + TEST_TABLE;

    private Context context;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_ATTENDANCES_TABLE);
            sqLiteDatabase.execSQL(CREATE_ATTENDANCES_INDEX_TABLE);
            sqLiteDatabase.execSQL(CREATE_ROLLNAME_TABLE);
            sqLiteDatabase.execSQL(CREATE_ROLLNAME_INDEX_TABLE);
            sqLiteDatabase.execSQL(CREATE_REGISTER_TABLE);
            sqLiteDatabase.execSQL(CREATE_UUID_TABLE);
            //sqLiteDatabase.execSQL(CREATE_PERCENTAGE_TABLE);
            sqLiteDatabase.execSQL(CREATE_TEST_TABLE);
            //sqLiteDatabase.execSQL(CREATE_PERCENT_TABLE);

        } catch (Exception e) {

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try {
            sqLiteDatabase.execSQL(DROP_ATTENDANCES_TABLE);
            sqLiteDatabase.execSQL(DROP_ATTENDANCES_INDEX_TABLE);
            sqLiteDatabase.execSQL(DROP_ROLLNAME_TABLE);
            sqLiteDatabase.execSQL(DROP_ROLLNAME_INDEX_TABLE);
            sqLiteDatabase.execSQL(DROP_REGISTER_TABLE);
            sqLiteDatabase.execSQL(DROP_UUID_TABLE);
            //sqLiteDatabase.execSQL(DROP_PERCENTAGE_TABLE);
            sqLiteDatabase.execSQL(DROP_TEST_TABLE);
            //sqLiteDatabase.execSQL(DROP_PERCENT_TABLE);
            onCreate(sqLiteDatabase);
        } catch (Exception e) {
        }

    }


    public long addUser(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REGISTER_USERNAME, username);
        contentValues.put(REGISTER_PASSWORD, password);
        long res = sqLiteDatabase.insert(REGISTER_TABLE, null, contentValues);
        sqLiteDatabase.close();
        return res;
    }

    public boolean checkUser(String username, String password) {
        String[] columns = {REGISTER_ID};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String selections = REGISTER_USERNAME + "=?" + " and " + REGISTER_PASSWORD + "=?";
        String[] selectionArgs = {username, password};

        Cursor cursor = sqLiteDatabase.query(REGISTER_TABLE, columns, selections, selectionArgs, null, null, null);
        int c = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();
        if (c > 0) {
            return true;
        } else {
            return false;
        }

    }


    public void insertDataInToAttendancesTable(List<String> rollList, List<String> nameList, List<String> attendanceList, List<String> dateTimes) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        for (int i = 0; i < rollList.size(); i++) {


            sqLiteDatabase.execSQL("INSERT INTO " + ATTENDANCES_TABLE + " Values('" + rollList.get(i) + "','" + nameList.get(i) + "','" + attendanceList.get(i) + "','" + dateTimes.get(i) + "');");

        }
    }


    public void insertDataInToAttendancesIndexTable(String dateTime, String attendancesFor) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ATTENDANCES_INDEX_TIME, dateTime);
        contentValues.put(ATTENDANCES_FOR_INDEX, attendancesFor);
        sqLiteDatabase.insert(ATTENDANCES_INDEX_TABLE, null, contentValues);


    }

    public void insertDataInToRollNameIndexTable(String dateTime, String attendancesFor) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATETIME_FOR_ROLLNAME_INDEX, dateTime);
        contentValues.put(ATT_FOR_ROLLNAME_INDEX, attendancesFor);
        sqLiteDatabase.insert(ROLL_NAME_INDEX_TABLE, null, contentValues);


    }

    public void createUUID() {


        String uuidForAtt = UUID.randomUUID().toString();
        String uuidForAttIndex = UUID.randomUUID().toString();


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UUID_FOR_ATT, uuidForAtt);
        contentValues.put(UUID_FOR_ATT_INDEX, uuidForAttIndex);
        sqLiteDatabase.insert(UUID_TABLE, null, contentValues);


    }


    public Map<String, List<String>> getAllDataFromAttendancesTable() {
        List<String> rollList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<String> attendanceList = new ArrayList<>();
        List<String> dateTimeList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ALL_ATTENDANCES_TABLE, null);
        while (cursor.moveToNext()) {
            rollList.add(cursor.getString(0));
            nameList.add(cursor.getString(1));
            attendanceList.add(cursor.getString(2));
            dateTimeList.add(cursor.getString(3));

        }
        Map<String, List<String>> map = new HashMap<>();
        map.put("rollList", rollList);
        map.put("nameList", nameList);
        map.put("attendanceList", attendanceList);
        map.put("dateTimes", dateTimeList);
        return map;


    }


    public Map<String, List<String>> getAllDataFromRollNameTable() {
        List<String> rollList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ALL_ROLLNAME_TABLE, null);
        while (cursor.moveToNext()) {
            rollList.add(cursor.getString(0));
            nameList.add(cursor.getString(1));

        }
        Map<String, List<String>> map = new HashMap<>();
        map.put("rollList", rollList);
        map.put("nameList", nameList);
        return map;


    }


    public Cursor getAllDataFromAttendancesIndex() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ALL_ATTENDANCES_INDEX_TABLE, null);
        return cursor;


    }

    public Cursor getAllDataFromUUID() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ALL_UUID_FROM_TABLE, null);
        return cursor;


    }

    public Cursor getAllDataFromRollNameIndex() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ALL_ROLLNAME_INDEX_TABLE, null);
        return cursor;


    }


    public boolean updateForRollName(String oldRoll, String newRoll, String name, String attFor, String date) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROLL_FOR_ROLLNAME, newRoll);
        contentValues.put(NAME_FOR_ROLLNAME, name);
        contentValues.put(ATT_FOR_ROLLNAME, attFor);
        contentValues.put(DATETIME_FOR_ROLLNAME, date);
        sqLiteDatabase.update(ROLL_NAME_TABLE, contentValues, ROLL_FOR_ROLLNAME + "=" + oldRoll, null);

        return true;


    }


    public boolean updateForAttIndex(String date, String index) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATETIME_FOR_ROLLNAME_INDEX, date);
        contentValues.put(ATT_FOR_ROLLNAME_INDEX, index);
        sqLiteDatabase.update(ROLL_NAME_INDEX_TABLE, contentValues, DATETIME_FOR_ROLLNAME_INDEX + " =?", new String[]{date});

        return true;


    }

    public boolean deleteRollNameIndex(String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(ROLL_NAME_INDEX_TABLE, DATETIME_FOR_ROLLNAME_INDEX + "=?", new String[]{date}) > 0;


    }

    public boolean deleteRollNameByDate(String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(ROLL_NAME_TABLE, DATETIME_FOR_ROLLNAME + "=?", new String[]{date}) > 0;


    }


    public boolean deleteSpecificPersonByRoll(String roll) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(ROLL_NAME_TABLE, ROLL_FOR_ROLLNAME + "=?", new String[]{roll}) > 0;
    }


    public boolean deleteAnyRow(String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(ATTENDANCES_INDEX_TABLE, ATTENDANCES_FOR_INDEX + "=?", new String[]{name}) > 0;
    }

    public void insertRollNameIntoLocalStorage(List<String> rollList, List<String> nameList, List<String> attForList, List<String> dateTimeList) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        for (int i = 0; i < rollList.size(); i++) {


            sqLiteDatabase.execSQL("INSERT INTO " + ROLL_NAME_TABLE + " Values('" + rollList.get(i) + "','" + nameList.get(i) + "','" + attForList.get(i) + "','" + dateTimeList.get(i) + "');");

        }


    }


    public void insertIntoPercentage(List<String> attList, List<String> rollList, List<Integer> dayList, List<Integer> plist, List<Integer> aList, List<Integer> percentList) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


       for (int i = 0; i < attList.size(); i++) {
            Log.d("rr", "insertIntoPercentage: " + i);


            sqLiteDatabase.execSQL("INSERT INTO " + TEST_TABLE+" Values('" + attList.get(i) + "','" + rollList.get(i) + "','" + dayList.get(i) + "','" + plist.get(i) + "','" + aList.get(i) + "','" + percentList.get(i) + "');");
        }




    }


    public void deleteAllValuesFromAllTables() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        try {
            /*sqLiteDatabase.execSQL(DELETE_ALL_VALUES_FROM_ATTENDANCE);
            sqLiteDatabase.execSQL(DELETE_ALL_VALUES_FROM_ATTENDANCES_INDEX);
            sqLiteDatabase.execSQL(DELETE_ALL_VALUES_FROM_ROLLNAME);
            sqLiteDatabase.execSQL(DELETE_ALL_VALUES_FROM_ROLLNAME_INDEX);
            sqLiteDatabase.execSQL(DELETE_ALL_VALUES_FROM_TEST);
            onCreate(sqLiteDatabase);*/

            sqLiteDatabase.execSQL(DROP_ATTENDANCES_TABLE);
            sqLiteDatabase.execSQL(CREATE_ATTENDANCES_TABLE);

            sqLiteDatabase.execSQL(DROP_ATTENDANCES_INDEX_TABLE);
            sqLiteDatabase.execSQL(CREATE_ATTENDANCES_INDEX_TABLE);

            sqLiteDatabase.execSQL(DROP_ROLLNAME_TABLE);
            sqLiteDatabase.execSQL(CREATE_ROLLNAME_TABLE);

            sqLiteDatabase.execSQL(DROP_ROLLNAME_INDEX_TABLE);
            sqLiteDatabase.execSQL(CREATE_ROLLNAME_INDEX_TABLE);

            sqLiteDatabase.execSQL(DROP_TEST_TABLE);
            sqLiteDatabase.execSQL(CREATE_TEST_TABLE);



        } catch (Exception e) {
        }


    }

    public void delete_Att_And_Index() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        try {
            /*sqLiteDatabase.execSQL(DELETE_ALL_VALUES_FROM_ATTENDANCE);
            sqLiteDatabase.execSQL(DELETE_ALL_VALUES_FROM_ATTENDANCES_INDEX);
            sqLiteDatabase.execSQL(DELETE_ALL_VALUES_FROM_TEST);
            //onCreate(sqLiteDatabase);*/


            sqLiteDatabase.execSQL(DROP_ATTENDANCES_TABLE);
            sqLiteDatabase.execSQL(CREATE_ATTENDANCES_TABLE);

            sqLiteDatabase.execSQL(DROP_ATTENDANCES_INDEX_TABLE);
            sqLiteDatabase.execSQL(CREATE_ATTENDANCES_INDEX_TABLE);

            /*sqLiteDatabase.execSQL(DROP_TEST_TABLE);
            sqLiteDatabase.execSQL(CREATE_TEST_TABLE);*/




        } catch (Exception e) {
        }


    }


    public boolean deleteAllFromPercentage(String rollNameAttFor) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TEST_TABLE, PERCENTAGE_ATT_FOR + "=?", new String[]{rollNameAttFor}) > 0;


    }

    public boolean updatePercentage(String oldRoll, String attFor, String gettingRoll, int gettingDaySum, int gettingPSum, int gettingASum, int gettingPercentSum) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERCENTAGE_ROLL, gettingRoll);
        contentValues.put(PERCENTAGE_DAY, gettingDaySum);
        contentValues.put(PERCENTAGE_P, gettingPSum);
        contentValues.put(PERCENTAGE_A, gettingASum);
        contentValues.put(PERCENTAGE_PERCENT, gettingPercentSum);
        sqLiteDatabase.update(TEST_TABLE, contentValues, PERCENTAGE_ATT_FOR + "='" + attFor + "' AND " + PERCENTAGE_ROLL + "='" + oldRoll+"'", null);

        return true;


    }


    public boolean updatePercentageWhenDeleteOne(String oldRoll, String attFor, String paoff) {

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        String q = "SELECT * FROM " + TEST_TABLE + " WHERE " + PERCENTAGE_ATT_FOR + " = '" + attFor + "'"+" AND "+ PERCENTAGE_ROLL + " = '" + oldRoll + "'";

        Cursor cursor=sqLiteDatabase.rawQuery(q,null);
        String attFromTable=null,rollFromTable=null;
        int dayFromTable=0,pFromTable=0,aFromTable=0,percentFromTable=0;
        while (cursor.moveToNext()){
            attFromTable=cursor.getString(0);
            rollFromTable=cursor.getString(1);
            dayFromTable=cursor.getInt(2);
            pFromTable=cursor.getInt(3);
            aFromTable=cursor.getInt(4);
            percentFromTable=cursor.getInt(5);



        }
        int finalday=0,finalP=0,finalA=0,finalPercent=0;
        if (paoff.equals("P")){
            finalday=dayFromTable-1;
            finalP=pFromTable-1;
            finalA=aFromTable;
            finalPercent=finalP*100/finalday;


        }if (paoff.equals("A")){
            finalday=dayFromTable-1;
            finalP=pFromTable;
            finalA=aFromTable-1;
            finalPercent=finalP*100/finalday;


        }if (paoff.equals("Off")){
            finalday=dayFromTable-1;
            finalP=pFromTable;
            finalA=aFromTable;
            finalPercent=finalP*100/finalday;


        }




        ContentValues contentValues = new ContentValues();

        contentValues.put(PERCENTAGE_DAY, finalday);
        contentValues.put(PERCENTAGE_P, finalP);
        contentValues.put(PERCENTAGE_A, finalA);
        contentValues.put(PERCENTAGE_PERCENT, finalPercent);

        Log.d("rr", "================updatePercentageWhenDeleteOne: ================"+finalday+ " " +finalP+ "" +finalA+" "+finalPercent);
        sqLiteDatabase.update(TEST_TABLE, contentValues, PERCENTAGE_ATT_FOR + "='" + attFor + "' AND " + PERCENTAGE_ROLL + "='" + oldRoll+"'", null);

        return true;


    }


}
