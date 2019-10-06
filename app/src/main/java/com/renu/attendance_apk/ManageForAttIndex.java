package com.renu.attendance_apk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class ManageForAttIndex extends AppCompatActivity {
    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    DatabaseReference databaseReferenceForattendancesIndex, databaseReferenceForattendances;
    ValueEventListener myValueEventListner;
    List<AttendanceIndexModel> attendanceIndexModelList;
    private ListView attendancesIndexListView;
    List<String> attendancesList;
    List<String> dateTimeList;
    private AlertDialog.Builder alertDialogBuilder;
    String[] dateTimeForAttendanceIndexArray, attendanceForArray;
    private MyBroadcastReceiver myBroadcastReceiver;
    String uuidForAtt, uuidForAttIndex;
    private DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;


    private static final String WHOLE_INFORMATION_TABLE = "wholeinformations";
    private String emailMobilePassRollnameIndex = null;
    private String emailMobilePassRollname = null;
    private String emailMobilePassAttIndex = null;
    private String emailMobilePassAtt = null;
    private String emailMobilePassTest = null;
    private String emailMobilePass = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_for_att_index);


        initView();

        getWholeInformation();
        initOthers();


        databaseReferenceForattendancesIndex.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> dtForIndex = new ArrayList<>();
                List<String> attForIndex = new ArrayList<>();
                for (DataSnapshot dnapshot : dataSnapshot.getChildren()) {
                    AttendanceIndexModel attendanceIndexModel = dnapshot.getValue(AttendanceIndexModel.class);
                    attendanceIndexModelList.add(attendanceIndexModel);


                }

                for (AttendanceIndexModel attendanceIndexModel : attendanceIndexModelList) {
                    dtForIndex.add(attendanceIndexModel.getDateTime());
                    attForIndex.add(attendanceIndexModel.getAttendanceFor());


                }


                dateTimeForAttendanceIndexArray = dtForIndex.toArray(new String[dtForIndex.size()]);
                attendanceForArray = attForIndex.toArray(new String[attForIndex.size()]);


                listViewHandleForAttendancesIndex(dateTimeForAttendanceIndexArray, attendanceForArray);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(myBroadcastReceiver, intentFilter);
        unregisterReceiver(myBroadcastReceiver);

    }

    private void listViewHandleForAttendancesIndex(final String[] dateTimeForAttendanceIndexArray, final String[] attendanceForArray) {


        final CustomAdupterForManagingAttIndex customAdupterForAttendancesIndex = new CustomAdupterForManagingAttIndex(this, dateTimeForAttendanceIndexArray, attendanceForArray);
        attendancesIndexListView.setAdapter(customAdupterForAttendancesIndex);

        attendancesIndexListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                alertDialogBuilder.setMessage("Index Name : " + attendanceForArray[position] + "\nDate : " + dateTimeForAttendanceIndexArray[position]);

                alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReferenceForattendancesIndex.child(dateTimeForAttendanceIndexArray[position]).getRef().removeValue();
                        databaseReferenceForattendances.child(dateTimeForAttendanceIndexArray[position]).getRef().removeValue();
                        //Load activity
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(ManageForAttIndex.this, "You Have Deleted Successfully !", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialogBuilder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ManageForAttIndex.this, UpdateForManageAttIndex.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("index", attendanceForArray[position]);
                        bundle.putString("dateTime", dateTimeForAttendanceIndexArray[position]);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });
                alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


    }

    private void initOthers() {
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAttIndex);
        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAtt);

        attendanceIndexModelList = new ArrayList<>();
        attendancesList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
        alertDialogBuilder = new AlertDialog.Builder(ManageForAttIndex.this);
        myBroadcastReceiver = new MyBroadcastReceiver();
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


    private void initView() {
        attendancesIndexListView = findViewById(R.id.manageAttendancesIndexListViewId);

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

}
