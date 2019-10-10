package com.renu.attendance_apk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageForAttPerson extends AppCompatActivity {


    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";

    DatabaseReference databaseReferenceForRollnameIndex, databaseReferenceForRollName, databaseReferenceForPercentage;


    private static final String WHOLE_INFORMATION_TABLE = "wholeinformations";
    private String emailMobilePassRollnameIndex = null;
    private String emailMobilePassRollname = null;
    private String emailMobilePassAttIndex = null;
    private String emailMobilePassAtt = null;
    private String emailMobilePassTest = null;
    private String emailMobilePass = null;


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
    private MyBroadcastReceiver myBroadcastReceiver;
    String[] stringsRoll, stringsNames, stringsAtt, stringsDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_for_att_person);

        getIntentValues();
        initView();
        getWholeInformation();
        initOthers();
        setVariousText();
        handleCustomAdapture();


    }

    private void setVariousText() {
        textViewForAttFor.setText(attFor);
        textViewForDate.setText(dateTime);
    }

    private void handleCustomAdapture() {
if (Network.isNetworkAvailable(ManageForAttPerson.this)) {


    databaseReferenceForRollName.child(dateTime).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<String> rollList = new ArrayList<>();
            List<String> nameList = new ArrayList<>();
            List<String> attendancesList = new ArrayList<>();
            List<String> dateTimeList = new ArrayList<>();


            try {
                RollNameModel rollNameModel = new RollNameModel();
                rollNameModel = dataSnapshot.getValue(RollNameModel.class);

                rollList.addAll(rollNameModel.getRollNo());
                nameList.addAll(rollNameModel.getName());
                attendancesList.addAll(rollNameModel.getAttFor());
                dateTimeList.addAll(rollNameModel.getDateTime());
            } catch (Exception e) {
               }
            stringsRoll = rollList.toArray(new String[rollList.size()]);
            stringsNames = nameList.toArray(new String[nameList.size()]);
            stringsAtt = attendancesList.toArray(new String[attendancesList.size()]);
            stringsDate = dateTimeList.toArray(new String[dateTimeList.size()]);

            CustomAdaptureForManagingAttPerson customAdaptureForManagingAttPerson = new CustomAdaptureForManagingAttPerson(ManageForAttPerson.this, stringsRoll, stringsNames);
            managePersonListViewId.setAdapter(customAdaptureForManagingAttPerson);
            managePersonListViewId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                    alertBuilder.setMessage("Roll No : " + stringsRoll[position] + "\nName : " + stringsNames[position]);
                    alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dataBaseHelper.deleteSpecificPersonByRoll(stringsRoll[position], stringsAtt[position]);
                            databaseReferenceForPercentage.child(attFor).child("aList").child("" + position).getRef().removeValue();
                            databaseReferenceForPercentage.child(attFor).child("attList").child("" + position).getRef().removeValue();
                            databaseReferenceForPercentage.child(attFor).child("pList").child("" + position).getRef().removeValue();
                            databaseReferenceForPercentage.child(attFor).child("dayList").child("" + position).getRef().removeValue();
                            databaseReferenceForPercentage.child(attFor).child("percentList").child("" + position).getRef().removeValue();
                            databaseReferenceForPercentage.child(attFor).child("rollList").child("" + position).getRef().removeValue();


                            databaseReferenceForRollName.child(dateTime).child("attFor").child("" + position).getRef().removeValue();
                            databaseReferenceForRollName.child(dateTime).child("dateTime").child("" + position).getRef().removeValue();
                            databaseReferenceForRollName.child(dateTime).child("name").child("" + position).getRef().removeValue();
                            databaseReferenceForRollName.child(dateTime).child("rollNo").child("" + position).getRef().removeValue();


                            Toast.makeText(ManageForAttPerson.this, "You have deleted successfully !", Toast.LENGTH_SHORT).show();
                           intentHandle();

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
                            bundle.putString("position", "" + position);
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

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


}else {
    Toast.makeText(this, "Connect internet !", Toast.LENGTH_SHORT).show();
}


    }

    private void intentHandle() {
        Intent intent = new Intent(ManageForAttPerson.this, ManageForAttPersonPre.class);

        startActivity(intent);
    }

    private void getWholeInformation() {


        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
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
            sqLiteDatabase.close();
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            sqLiteDatabase.close();
        }
    }

    private void initOthers() {
        databaseReferenceForPercentage = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassTest);
        databaseReferenceForRollnameIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollnameIndex);
        databaseReferenceForRollName = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollname);

        dataBaseHelper = new DataBaseHelper(this);
        alertBuilder = new AlertDialog.Builder(this);
        myBroadcastReceiver = new MyBroadcastReceiver();
        Log.d("rr", "initOthers: " + emailMobilePassRollname);


    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(myBroadcastReceiver, intentFilter);
        unregisterReceiver(myBroadcastReceiver);

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

        if (item.getItemId() == R.id.logout) {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            dataBaseHelper.delete_Login();

            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}
