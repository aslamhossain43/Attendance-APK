package com.renu.attendance_apk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Attendance.db";
    private static final String ATTENDANCES_TABLE = "attendance";
    private static final String ATTENDANCES_INDEX_TABLE = "attendanceindex";
    private static final int VERSION_NUMBER = 1;

    private static final String ROLL = "_roll";
    private static final String ATTENDANCES = "attendances";


    private static final String ATTENDANCES_TIME = "_time";
    private static final String ATTENDANCES_FOR = "attendancesfor";


    private static final String CREATE_ATTENDANCES_TABLE = "CREATE TABLE " + ATTENDANCES_TABLE + "( " + ROLL + " INTEGER PRIMARY KEY AUTOINCREMENT," + ATTENDANCES + " VARCHAR(25),"+ ATTENDANCES_TIME + " VARCHAR(100));";
    private static final String CREATE_ATTENDANCES_INDEX_TABLE = "CREATE TABLE " + ATTENDANCES_INDEX_TABLE + "( " + ATTENDANCES_TIME + " VARCHAR(25)," + ATTENDANCES_FOR + " VARCHAR(25));";
    private static final String DROP_ATTENDANCES_TABLE = "DROP TABLE IF EXISTS " + ATTENDANCES_TABLE;
    private static final String DROP_ATTENDANCES_INDEX_TABLE = "DROP TABLE IF EXISTS " + ATTENDANCES_INDEX_TABLE;
    private static final String GET_ALL_ATTENDANCES_TABLE = "SELECT * FROM " + ATTENDANCES_TABLE;
    private static final String GET_ALL_ATTENDANCES_INDEX_TABLE = "SELECT * FROM " + ATTENDANCES_INDEX_TABLE;


    private Context context;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Toast.makeText(context, "onCreate is called", Toast.LENGTH_SHORT).show();
            sqLiteDatabase.execSQL(CREATE_ATTENDANCES_TABLE);
            sqLiteDatabase.execSQL(CREATE_ATTENDANCES_INDEX_TABLE);

        } catch (Exception e) {
            Toast.makeText(context, "Exception : " + e, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try {
            Toast.makeText(context, "onUpgrade is called", Toast.LENGTH_SHORT).show();

            sqLiteDatabase.execSQL(DROP_ATTENDANCES_TABLE);
            sqLiteDatabase.execSQL(DROP_ATTENDANCES_INDEX_TABLE);
            onCreate(sqLiteDatabase);
        } catch (Exception e) {
            Toast.makeText(context, "Exception : " + e, Toast.LENGTH_LONG).show();

        }

    }

    public void insertDataInToAttendancesTable(List<String>rollList, List<String>attendanceList,List<String>dateTimeList) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
for (int i=0;i<rollList.size();i++) {


    sqLiteDatabase.execSQL("INSERT INTO "+ATTENDANCES_TABLE+" Values('" + rollList.get(i) + "','" + attendanceList.get(i) + "','" + dateTimeList.get(i) + "');");

}
    }


    public void insertDataInToAttendancesIndexTable(String dateTime,String attendancesFor) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ATTENDANCES_TIME, dateTime);
        contentValues.put(ATTENDANCES_FOR, attendancesFor);
        sqLiteDatabase.insert(ATTENDANCES_INDEX_TABLE, null, contentValues);


    }



    public Map<String,List<String>> getAllDataFromAttendancesTable() {
        List<String>rollList=new ArrayList<>();
        List<String>attendanceList=new ArrayList<>();
        List<String>dateTimeList=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ALL_ATTENDANCES_TABLE, null);
        while (cursor.moveToNext()){
            rollList.add(cursor.getString(0));
            attendanceList.add(cursor.getString(1));
            dateTimeList.add(cursor.getString(2));

        }
        Map<String,List<String>>map=new HashMap<>();
        map.put("rollList",rollList);
        map.put("attendanceList",attendanceList);
        map.put("dateTimeList",dateTimeList);
        return map;


    }

    public Cursor getAllDataFromAttendancesIndex() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ALL_ATTENDANCES_INDEX_TABLE, null);
        return cursor;


    }


    public boolean updateData(String id, String name, String age, String gender) {
        /*SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(NAME, name);
        contentValues.put(AGE, age);
        contentValues.put(GENDER, gender);
        sqLiteDatabase.update(TABLE_NAME, contentValues, ID + " =?", new String[]{id});
       */ return true;


    }

    public void delete(String id){
        /*SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME,ID+" =?",new String[]{id});*/
    }


}
