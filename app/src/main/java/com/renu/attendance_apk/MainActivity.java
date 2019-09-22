package com.renu.attendance_apk;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private LinearLayout parentLinearLayout;
    private EditText editTextForMain, editTextForField, editTextAttendanceFor, editTextNameForField;
    private Button add_FieldButton, add_To_Firebase;
    private Spinner spinnerForMain, spinnerForField;


    private static final String ROLL_NAME_TABLE = "rollname";
    private static final String ROLL_FOR_ROLLNAME = "roll";
    private static final String NAME_FOR_ROLLNAME = "name";
    private static final String ATT_FOR_ROLLNAME = "attfor";
    private static final String DATETIME_FOR_ROLLNAME = "time";


    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex,
            databaseReferenceForRollNames;
    List<String> roolList;
    List<String> nameList;
    List<String> attendanceList;
    List<String> dateTimeList;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;
    String rollNameAttFor;
    String rollNameDateTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getINtentValues();


        initView();
        setVariousValues();
        initOthers();


       /* add_FieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



*/

       /* if (Network.isNetworkAvailable(this)) {


//---------------------------

            databaseReferenceForRollNames.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> roll = new ArrayList<>();
                    List<String> name = new ArrayList<>();
                    RollNameModel rollNameModel = new RollNameModel();
                    rollNameModel = dataSnapshot.getValue(RollNameModel.class);
                    roll.addAll(rollNameModel.getRollNo());
                    name.addAll(rollNameModel.getName());
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

*/
        List<String> roll = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<String> attFor = new ArrayList<>();
        List<String> date = new ArrayList<>();
        Log.d("tt", "onCreate: ROLL NAME TIME :" + rollNameDateTime);
        Log.d("tt", "onCreate: ROLL NAME TIME :" + rollNameAttFor);
            /*Map<String, List<String>> map = dataBaseHelper.getAllDataFromRollNameTable();

            roll.addAll(map.get("rollList"));
            name.addAll(map.get("nameList"));
*/

//--------------------------
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
        Log.d("rr", "onCreate: " + roll);
        Log.d("rr", "onCreate: " + name);
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


        //}
//-----------------------------------------------------

       /*

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.field, null);
                // Add the new row before the add field button.

                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 2);
                editTextForField = rowView.findViewById(R.id.rollForFieldId);
                spinnerForField = rowView.findViewById(R.id.spinnerForFieldId);
                editTextForField.setText("" + c);
                c++;*/


            /*}
        });*/
        add_To_Firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roolList.clear();
                nameList.clear();
                attendanceList.clear();
                dateTimeList.clear();
                //--------------------------------
                //String attendancesFor = editTextAttendanceFor.getText().toString();
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd ',' hh:mm:ss a");
                Log.d("dt", "onClick: " + sim.format(new Date()));
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

        if (item.getItemId() == R.id.listId) {
            Intent intent = new Intent(this, AttendancesIndex.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.openId) {
            Intent intent = new Intent(this, CreateNew1.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.localAttendances) {
            Intent intent = new Intent(this, AfterLogin.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

    private void sendToAttendanceIndex(String attendancesFor, String dateTime) {


    }

    private void setVariousValues() {


    }

    private void addValuesToList(String attendancesFor, String dateTime) {

        //dateTimeList.clear();

        if (dateTime != null) {

            for (int i = 0; i < roolList.size(); i++) {
                dateTimeList.add(dateTime);
            }

            Log.d("rr", "addValuesToList: " + roolList);
            Log.d("aa", "addValuesToList: " + attendanceList);
            Log.d("dt", "addValuesToList: " + dateTimeList);


            if (Network.isNetworkAvailable(this)) {

                AttendanceIndexModel attendanceIndexModel = new AttendanceIndexModel(dateTime, attendancesFor);
                databaseReferenceForattendancesIndex.child(dateTime).setValue(attendanceIndexModel);

                AttendanceModel attendanceModel = new AttendanceModel(roolList, nameList, attendanceList, dateTimeList);
                databaseReferenceForattendances.child(dateTime).setValue(attendanceModel);
                Log.d("nn", "addValuesToList:  neeeeeeeeeeeeeeeeet");


            } else {


                Log.d("nn", "addValuesToList: noooooooooooooooooo net");
                dataBaseHelper.insertDataInToAttendancesTable(roolList, nameList, attendanceList, dateTimeList);


                dataBaseHelper.insertDataInToAttendancesIndexTable(dateTime, attendancesFor);
            }


        }


    }

    private void initOthers() {

        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReference("attendance");
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex");
        databaseReferenceForRollNames = FirebaseDatabase.getInstance().getReference("rollnames").child(rollNameDateTime);
        roolList = new ArrayList<>();
        nameList = new ArrayList<>();
        attendanceList = new ArrayList<>();
        dateTimeList = new ArrayList<>();

        dataBaseHelper = new DataBaseHelper(this);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
    }

    private void initView() {

        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        //editTextAttendanceFor = findViewById(R.id.editTextAttendancesForId);
        add_To_Firebase = findViewById(R.id.add_to_firebase_btnId);
        /*add_FieldButton = findViewById(R.id.add_field_button);*/

    }


    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }


}
