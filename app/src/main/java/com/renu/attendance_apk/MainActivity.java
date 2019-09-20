package com.renu.attendance_apk;

import android.content.Context;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    private EditText editTextForMain, editTextForField, editTextAttendanceFor;
    private Button add_FieldButton, add_To_Firebase;
    private Spinner spinnerForMain, spinnerForField;
    int c = 1;
    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex;
    List<String> roolList;
    List<String> attendanceList;
    List<String> dateTimeList;
    DataBaseHelper dataBaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setVariousValues();
        initOthers();


        add_FieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.field, null);
                // Add the new row before the add field button.

                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 2);
                editTextForField = rowView.findViewById(R.id.rollForFieldId);
                spinnerForField = rowView.findViewById(R.id.spinnerForFieldId);
                editTextForField.setText("" + c);
                c++;


            }
        });
        add_To_Firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roolList.clear();
                attendanceList.clear();
                //--------------------------------
                String attendancesFor = editTextAttendanceFor.getText().toString();
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd ',' hh:mm a");
                Log.d("dt", "onClick: " + sim.format(new Date()));
                String dateTime = sim.format(new Date());
                sendToAttendanceIndex(attendancesFor, dateTime);
                //------------------------------------
                for (int i = 1; i < parentLinearLayout.getChildCount() - 2; i++) {
                    View ltV = parentLinearLayout.getChildAt(i);


                    editTextForField = ltV.findViewById(R.id.rollForFieldId);
                    spinnerForField = ltV.findViewById(R.id.spinnerForFieldId);
                    roolList.add(editTextForField.getText().toString().trim());
                    attendanceList.add(spinnerForField.getSelectedItem().toString().trim());


                }
                addValuesToList(attendancesFor, dateTime);

            }
        });


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


        return super.onOptionsItemSelected(item);
    }

    private void sendToAttendanceIndex(String attendancesFor, String dateTime) {


    }

    private void setVariousValues() {


    }

    private void addValuesToList(String attendancesFor, String dateTime) {

        dateTimeList.clear();

        if (dateTime != null) {

            for (int i = 0; i < roolList.size(); i++) {
                dateTimeList.add(dateTime);
            }

            Log.d("rr", "addValuesToList: " + roolList);
            Log.d("aa", "addValuesToList: " + attendanceList);
            Log.d("dt", "addValuesToList: " + dateTimeList);


            if (Network.isNetworkAvailable(this)) {

                String pushKeyForAttendancesIndex = dateTime;
                AttendanceIndexModel attendanceIndexModel = new AttendanceIndexModel(dateTime, attendancesFor);
                databaseReferenceForattendancesIndex.child(dateTime).setValue(attendanceIndexModel);

                String pushKey = dateTime;
                AttendanceModel attendanceModel = new AttendanceModel(roolList, attendanceList, dateTimeList);
                databaseReferenceForattendances.child(pushKey).setValue(attendanceModel);


            } else {
                dataBaseHelper.insertDataInToAttendancesTable(roolList, attendanceList, dateTimeList);
                dataBaseHelper.insertDataInToAttendancesIndexTable(dateTime, attendancesFor);
            }


        }


    }

    private void initOthers() {

        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReference("attendance");
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex");
        roolList = new ArrayList<>();
        attendanceList = new ArrayList<>();
        dateTimeList = new ArrayList<>();

        dataBaseHelper = new DataBaseHelper(this);

    }

    private void initView() {

        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        add_FieldButton = findViewById(R.id.add_field_button);
        add_To_Firebase = findViewById(R.id.add_to_firebase_btnId);
        editTextAttendanceFor = findViewById(R.id.editTextAttendancesForId);

    }


    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }


}
