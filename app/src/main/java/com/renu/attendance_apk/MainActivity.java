package com.renu.attendance_apk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private LinearLayout parentLinearLayout;
    private EditText editTextForField, editTextNameForField;
    private Button add_To_Firebase;
    private Spinner spinnerForField;


    private static final String ROLL_NAME_TABLE = "rollname";
    private static final String DATETIME_FOR_ROLLNAME = "time";

    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex,
            databaseReferenceForRollName, databaseReferenceForRollNameIndex;
    List<String> roolList, nameList, attendanceList, dateTimeList, attListForPercentage;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;
    String rollNameAttFor, rollNameDateTime;
    String uuidForAtt, uuidForAttIndex;
    private MyBroadcastReceiver myBroadcastReceiver;
    //private static final String PERCENTAGE_TABLE = "percentage";
    private static final String PERCENTAGE_ATT_FOR = "attfor";

    private static final String TEST_TABLE = "test";
    private static final String PERCENT_TABLE = "percentages";

    private static final String WHOLE_INFORMATION_TABLE = "wholeinformations";
    private String emailMobilePassRollnameIndex = null;
    private String emailMobilePassRollname = null;
    private String emailMobilePassAttIndex = null;
    private String emailMobilePassAtt = null;
    private String emailMobilePassTest = null;
    private String emailMobilePass = null;

    List<String> roll, name, attFor, date;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getINtentValues();


        initView();
        setVariousValues();
        getWholeInformation();
        initOthers();


        handleRollNames();

//--------------------------


        add_To_Firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roolList.clear();
                nameList.clear();
                attendanceList.clear();
                dateTimeList.clear();
                //--------------------------------
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd ',' hh:mm:ss a");
                String dateTime = sim.format(new Date());
                sendToAttendanceIndex(rollNameAttFor, dateTime);
                //------------------------------------
                for (int i = 1; i < parentLinearLayout.getChildCount() - 1; i++) {
                    View ltV = parentLinearLayout.getChildAt(i);


                    editTextForField = ltV.findViewById(R.id.rollForFieldId);
                    editTextNameForField = ltV.findViewById(R.id.nameForFieldId);
                    spinnerForField = ltV.findViewById(R.id.spinnerForFieldId);
                    roolList.add(editTextForField.getText().toString().trim());
                    nameList.add(editTextNameForField.getText().toString().trim());
                    attendanceList.add(spinnerForField.getSelectedItem().toString().trim());


                }
                addValuesToList(rollNameAttFor, dateTime);

            }
        });


    }

    private void handleRollNames() {


        if (Network.isNetworkAvailable(this)) {

            databaseReferenceForRollName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    RollNameModel rollNameModel = dataSnapshot.getValue(RollNameModel.class);
                    roll.addAll(rollNameModel.getRollNo());
                    name.addAll(rollNameModel.getName());
                    attFor.addAll(rollNameModel.getAttFor());
                    date.addAll(rollNameModel.getDateTime());


                    String[] stringsForRoll = roll.toArray(new String[roll.size()]);
                    String[] stringsForNames = name.toArray(new String[name.size()]);
                    for (int i = 0; i < roll.size(); i++) {


                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View rowView = inflater.inflate(R.layout.field, null);
                        // Add the new row before the add field button.

                        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                        editTextForField = rowView.findViewById(R.id.rollForFieldId);
                        editTextNameForField = rowView.findViewById(R.id.nameForFieldId);
                        spinnerForField = rowView.findViewById(R.id.spinnerForFieldId);
                        editTextForField.setText(stringsForRoll[i]);
                        editTextNameForField.setText(stringsForNames[i]);

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {


            String q = "SELECT * FROM " + ROLL_NAME_TABLE + " WHERE " + DATETIME_FOR_ROLLNAME + " = '" + rollNameDateTime + "'";

            Cursor cursor1 = sqLiteDatabase.rawQuery(q, null);
            while (cursor1.moveToNext()) {
                roll.add(cursor1.getString(0));
                name.add(cursor1.getString(1));
                attFor.add(cursor1.getString(2));
                date.add(cursor1.getString(3));

            }

            String[] stringsForRoll = roll.toArray(new String[roll.size()]);
            String[] stringsForNames = name.toArray(new String[name.size()]);
            for (int i = 0; i < roll.size(); i++) {


                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.field, null);
                // Add the new row before the add field button.

                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                editTextForField = rowView.findViewById(R.id.rollForFieldId);
                editTextNameForField = rowView.findViewById(R.id.nameForFieldId);
                spinnerForField = rowView.findViewById(R.id.spinnerForFieldId);
                editTextForField.setText(stringsForRoll[i]);
                editTextNameForField.setText(stringsForNames[i]);

            }


        }


    }

    private void getWholeInformation() {

        dataBaseHelper = new DataBaseHelper(this);

        sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + WHOLE_INFORMATION_TABLE, null);

        if (cursor.getCount() != 0) {


            while (cursor.moveToNext()) {

                emailMobilePassRollnameIndex = cursor.getString(0);
                emailMobilePassRollname = cursor.getString(1);
                emailMobilePassAttIndex = cursor.getString(2);
                emailMobilePassAtt = cursor.getString(3);
                emailMobilePassTest = cursor.getString(4);
                emailMobilePass = cursor.getString(5);

            }
        }
    }


    private void getINtentValues() {

        Bundle bundle = getIntent().getExtras();
        rollNameAttFor = bundle.getString("sAtt");
        rollNameDateTime = bundle.getString("sDateTime");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.homeId) {
            Intent intent = new Intent(this, AfterLogin.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.infoId) {
            Intent intent = new Intent(this, Informations.class);
            startActivity(intent);

        }

        if (item.getItemId() == R.id.listId) {
            Intent intent = new Intent(this, AttendancesIndex.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.openId) {
            Intent intent = new Intent(this, CreateNew1.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.localAttendances) {
            Intent intent = new Intent(this, ExistRollNames.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.summary) {
            Intent intent = new Intent(this, Percentage.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }


    private void sendToAttendanceIndex(String attendancesFor, String dateTime) {


    }

    private void setVariousValues() {


    }

    private void addValuesToList(String attendancesFor, String dateTime) {


        if (dateTime != null) {

            for (int i = 0; i < roolList.size(); i++) {
                dateTimeList.add(dateTime);

            }


            if (Network.isNetworkAvailable(this)) {

                AttendanceIndexModel attendanceIndexModel = new AttendanceIndexModel(dateTime, attendancesFor);
                databaseReferenceForattendancesIndex.child(dateTime).setValue(attendanceIndexModel);

                AttendanceModel attendanceModel = new AttendanceModel(roolList, nameList, attendanceList, dateTimeList);
                databaseReferenceForattendances.child(dateTime).setValue(attendanceModel);
                //-----------------------------------


                handlePercentage(roolList, attendanceList);


                //----------------------------------


                // go to attendance index
                Intent intent = new Intent(MainActivity.this, AttendancesIndex.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Operation Success !", Toast.LENGTH_SHORT).show();

            } else {


                dataBaseHelper.insertDataInToAttendancesTable(roolList, nameList, attendanceList, dateTimeList);


                dataBaseHelper.insertDataInToAttendancesIndexTable(dateTime, attendancesFor);
                //handle percentage
                handlePercentage(roolList, attendanceList);
                // go to local book
                Intent intent = new Intent(MainActivity.this, ExistRollNames.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Operation Success !", Toast.LENGTH_SHORT).show();

            }


        }


    }

    private void handlePercentage(List<String> roolList, List<String> attendanceList) {
        for (int i = 0; i < roolList.size(); i++) {
            attListForPercentage.add(rollNameAttFor);

        }

        List<String> attListForPer = new ArrayList<>();
        List<String> rollListForPer = new ArrayList<>();

        List<Integer> dayListForPer = new ArrayList<>();
        List<Integer> pListForPer = new ArrayList<>();
        List<Integer> aListForPer = new ArrayList<>();
        List<Integer> percentListForPer = new ArrayList<>();
        String getValues = "SELECT * FROM " + TEST_TABLE + " WHERE " + PERCENTAGE_ATT_FOR + " = '" + rollNameAttFor + "'";


        Cursor cursor = sqLiteDatabase.rawQuery(getValues, null);

        List<Integer> fDayList = new ArrayList<>();
        List<Integer> fPList = new ArrayList<>();
        List<Integer> fAList = new ArrayList<>();
        List<Integer> fPercentList = new ArrayList<>();

        if (cursor.getCount() == 0) {


            for (int i = 0; i < roolList.size(); i++) {

                fDayList.add(0);
                fPList.add(0);
                fAList.add(0);
                fPercentList.add(0);

            }


            dataBaseHelper.insertIntoPercentage(attListForPercentage, roolList, fDayList, fPList, fAList, fPercentList);

            String getValuesWhenNull = "SELECT * FROM " + TEST_TABLE + " WHERE " + PERCENTAGE_ATT_FOR + " = '" + rollNameAttFor + "'";

            Cursor c1 = sqLiteDatabase.rawQuery(getValuesWhenNull, null);
            int attendanceCount = 0;
            while (c1.moveToNext()) {
                String attFor = c1.getString(0);
                String roll = c1.getString(1);
                attListForPer.add(attFor);
                rollListForPer.add(roll);
                int day = c1.getInt(2);
                int p = c1.getInt(3);
                int a = c1.getInt(4);
                int percent = c1.getInt(5);
                int pSum = 0;
                int aSum = 0;
                int daySum = 0;
                int percentSum = 0;

                String pFromMethod = attendanceList.get(attendanceCount);
                if (pFromMethod.equals("P")) {
                    daySum = day + 1;
                    dayListForPer.add(daySum);

                    pSum = p + 1;
                    pListForPer.add(pSum);
                    aSum = a + 0;
                    aListForPer.add(aSum);

                    percentSum = pSum * 100 / daySum;
                    percentListForPer.add(percentSum);

                }
                if (pFromMethod.equals("A")) {
                    daySum = day + 1;
                    dayListForPer.add(daySum);

                    pSum = p + 0;
                    pListForPer.add(pSum);
                    aSum = a + 1;
                    aListForPer.add(aSum);

                    percentSum = pSum * 100 / daySum;
                    percentListForPer.add(percentSum);

                }
                if (pFromMethod.equalsIgnoreCase("Off")) {
                    daySum = day + 1;
                    dayListForPer.add(daySum);

                    pSum = p + 0;
                    pListForPer.add(pSum);
                    aSum = a + 0;
                    aListForPer.add(aSum);

                    percentSum = pSum * 100 / daySum;
                    percentListForPer.add(percentSum);

                }
                attendanceCount++;

            }
            dataBaseHelper.deleteSpecificFromPercentage(rollNameAttFor);
            dataBaseHelper.insertIntoPercentage(attListForPercentage, roolList, dayListForPer, pListForPer, aListForPer, percentListForPer);


        } else {


            int attendanceCount = 0;


            String getValuesWhenNotNull = "SELECT * FROM " + TEST_TABLE + " WHERE " + PERCENTAGE_ATT_FOR + " = '" + rollNameAttFor + "'";

            Cursor cursor2 = sqLiteDatabase.rawQuery(getValuesWhenNotNull, null);

            while (cursor2.moveToNext()) {
                String attFor = cursor2.getString(0);
                String roll = cursor2.getString(1);
                attListForPer.add(attFor);
                rollListForPer.add(roll);
                int day = cursor2.getInt(2);
                int p = cursor2.getInt(3);
                int a = cursor2.getInt(4);
                int percent = cursor2.getInt(5);
                int pSum = 0;
                int aSum = 0;
                int daySum = 0;
                int percentSum = 0;

                String pFromMethod = attendanceList.get(attendanceCount);


                if (pFromMethod.equals("P")) {
                    daySum = day + 1;
                    dayListForPer.add(daySum);

                    pSum = p + 1;
                    pListForPer.add(pSum);
                    aSum = a + 0;
                    aListForPer.add(aSum);

                    percentSum = pSum * 100 / daySum;
                    percentListForPer.add(percentSum);

                }
                if (pFromMethod.equals("A")) {
                    daySum = day + 1;
                    dayListForPer.add(daySum);

                    pSum = p + 0;
                    pListForPer.add(pSum);
                    aSum = a + 1;
                    aListForPer.add(aSum);

                    percentSum = pSum * 100 / daySum;
                    percentListForPer.add(percentSum);

                }
                if (pFromMethod.equals("Off")) {
                    daySum = day + 1;
                    dayListForPer.add(daySum);

                    pSum = p + 0;
                    pListForPer.add(pSum);
                    aSum = a + 0;
                    aListForPer.add(aSum);

                    percentSum = pSum * 100 / daySum;
                    percentListForPer.add(percentSum);

                }

                attendanceCount++;

            }


            dataBaseHelper.deleteSpecificFromPercentage(rollNameAttFor);
            dataBaseHelper.insertIntoPercentage(attListForPercentage, roolList, dayListForPer, pListForPer, aListForPer, percentListForPer);


        }


    }

    private void initOthers() {

        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAtt);
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAttIndex);
        databaseReferenceForRollName = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollname + "/" + rollNameDateTime);
        databaseReferenceForRollNameIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollnameIndex + "/" + rollNameDateTime);


        roll = new ArrayList<>();
        name = new ArrayList<>();
        attFor = new ArrayList<>();
        date = new ArrayList<>();


        roolList = new ArrayList<>();
        nameList = new ArrayList<>();
        attendanceList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
        attListForPercentage = new ArrayList<>();
        myBroadcastReceiver = new MyBroadcastReceiver();
    }

    //-------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(myBroadcastReceiver, intentFilter);
        unregisterReceiver(myBroadcastReceiver);

    }


    //----------------------------
    private void initView() {

        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        add_To_Firebase = findViewById(R.id.add_to_firebase_btnId);

    }


}
