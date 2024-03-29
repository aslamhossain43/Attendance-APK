package com.renu.attendance_apk;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendancesIndex extends AppCompatActivity {
    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex;
    List<AttendanceIndexModel> attendanceIndexModelList;
    List<String> rollList;
    List<String> attendancesList;
    List<String> dateTimeList;
    private ListView attendancesIndexListView;
    private MyBroadcastReceiver myBroadcastReceiver;
    private String uuidForAtt, uuidForAttIndex;
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
        setContentView(R.layout.activity_attendances_index);


        initView();

        getWholeInformation();


        intitOthers();
handleAttendanceIndex();



    }

    private void handleAttendanceIndex() {

        if (Network.isNetworkAvailable(this)) {


            databaseReferenceForattendancesIndex.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {


                        for (DataSnapshot dnapshot : dataSnapshot.getChildren()) {
                            AttendanceIndexModel attendanceIndexModel = dnapshot.getValue(AttendanceIndexModel.class);
                            attendanceIndexModelList.add(attendanceIndexModel);


                        }


                        List<String> dtForIndex = new ArrayList<>();
                        List<String> attForIndex = new ArrayList<>();
                        for (AttendanceIndexModel attendanceIndexModel : attendanceIndexModelList) {
                            dtForIndex.add(attendanceIndexModel.getDateTime());
                            attForIndex.add(attendanceIndexModel.getAttendanceFor());


                        }

                        String[] dateTimeForAttendanceIndexArray;
                        String[] attendanceForArray;
                        dateTimeForAttendanceIndexArray = dtForIndex.toArray(new String[dtForIndex.size()]);
                        attendanceForArray = attForIndex.toArray(new String[attForIndex.size()]);


                        listViewHandleForAttendancesIndex(dateTimeForAttendanceIndexArray, attendanceForArray);


                    } catch (Exception e) {
                        Toast.makeText(AttendancesIndex.this, "Attendances not available !", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }else {
            Toast.makeText(this, "Connect internet !", Toast.LENGTH_SHORT).show();
        }

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


    private void listViewHandleForAttendancesIndex(final String[] dateTimeForAttendanceIndexArray, final String[] attendanceForArray) {

        CustomAdupterForAttendancesIndex customAdupterForAttendancesIndex = new CustomAdupterForAttendancesIndex(this, dateTimeForAttendanceIndexArray, attendanceForArray);
        attendancesIndexListView.setAdapter(customAdupterForAttendancesIndex);

        attendancesIndexListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(AttendancesIndex.this, SpecificAttendancesFromFirebase.class);
                String attFor = attendanceForArray[position];
                String dt = dateTimeForAttendanceIndexArray[position];
                Bundle bundle = new Bundle();
                bundle.putString("attFor", attFor);
                bundle.putString("dt", dt);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myBroadcastReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myBroadcastReceiver);


    }

    private void intitOthers() {

        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAttIndex);
        attendanceIndexModelList = new ArrayList<>();
        rollList = new ArrayList<>();
        attendancesList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
        myBroadcastReceiver = new MyBroadcastReceiver();

    }

    private void initView() {
        attendancesIndexListView = findViewById(R.id.attendancesIndexListViewId);

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

        if (item.getItemId() == R.id.openId) {
            Intent intent = new Intent(this, CreateNew1.class);
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


        if (item.getItemId() == R.id.localAttendances) {
            Intent intent = new Intent(this, ExistRollNames.class);
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
