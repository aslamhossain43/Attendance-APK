package com.renu.attendance_apk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ManageForAttPerson extends AppCompatActivity {

    private AlertDialog.Builder alertBuilder;
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private ListView managePersonListViewId;
    private TextView textViewForAttFor, textViewForDate;
    String dateTime, attFor;
    final String ROLL_NAME_TABLE = "rollname";
    final String ROLL_FOR_ROLLNAME = "roll";
    final String NAME_FOR_ROLLNAME = "name";
    final String ATT_FOR_ROLLNAME = "attfor";
    final String DATETIME_FOR_ROLLNAME = "time";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_for_att_person);

        getIntentValues();
        initView();
        initOthers();
        setVariousText();
        handleCustomAdapture();


    }

    private void setVariousText() {
        textViewForAttFor.setText(attFor);
        textViewForDate.setText(dateTime);
    }

    private void handleCustomAdapture() {


        List<String> rollList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<String> attendancesList = new ArrayList<>();
        List<String> dateTimeList = new ArrayList<>();


        sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        String q = "SELECT * FROM " + ROLL_NAME_TABLE + " WHERE " + DATETIME_FOR_ROLLNAME + " = '" + dateTime + "'";

        Cursor cursor1 = sqLiteDatabase.rawQuery(q, null);
        while (cursor1.moveToNext()) {
            rollList.add(cursor1.getString(0));
            nameList.add(cursor1.getString(1));
            attendancesList.add(cursor1.getString(2));
            dateTimeList.add(cursor1.getString(3));

        }

        final String[] stringsRoll = rollList.toArray(new String[rollList.size()]);
        final String[] stringsNames = nameList.toArray(new String[nameList.size()]);
        final String[] stringsAtt = attendancesList.toArray(new String[attendancesList.size()]);
        final String[] stringsDate = dateTimeList.toArray(new String[dateTimeList.size()]);

        CustomAdaptureForManagingAttPerson customAdaptureForManagingAttPerson = new CustomAdaptureForManagingAttPerson(this, stringsRoll, stringsNames);
        managePersonListViewId.setAdapter(customAdaptureForManagingAttPerson);
        managePersonListViewId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                alertBuilder.setMessage("Roll No : " + stringsRoll[position] + "\nName : " + stringsNames[position]);
                alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dataBaseHelper.deleteSpecificPersonByRoll(stringsRoll[position]);
                        finish();
                        startActivity(getIntent());


                    }
                });

                alertBuilder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ManageForAttPerson.this, UpdateForAttPerson.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("roll", stringsRoll[position]);
                        bundle.putString("name", stringsNames[position]);
                        bundle.putString("attFor", stringsAtt[position]);
                        bundle.putString("dateTime", stringsDate[position]);
                        intent.putExtras(bundle);
                        startActivity(intent);


                    }
                });
                alertBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();


            }
        });


    }

    private void initOthers() {
        dataBaseHelper = new DataBaseHelper(this);
        alertBuilder = new AlertDialog.Builder(this);


    }

    private void initView() {
        managePersonListViewId = findViewById(R.id.managePersonListViewId);
        textViewForAttFor = findViewById(R.id.manageTextViewHeadingForClassId);
        textViewForDate = findViewById(R.id.manageTextViewHeadingForDtaeId);


    }

    private void getIntentValues() {
        Bundle bundle = getIntent().getExtras();
        dateTime = bundle.getString("dateTime");
        attFor = bundle.getString("attFor");


    }


}
