package com.renu.attendance_apk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendancesIndex extends AppCompatActivity {
    private static final String FIREBASE_URL="https://attendance-apk.firebaseio.com/";
    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex;
    List<AttendanceIndexModel> attendanceIndexModelList;
    List<String> rollList;
    List<String> attendancesList;
    List<String> dateTimeList;
    private ListView attendancesIndexListView;
    private MyBroadcastReceiver myBroadcastReceiver;
    private String uuidForAtt,uuidForAttIndex;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendances_index);


        initView();
        handleUUID();
        intitOthers();


        databaseReferenceForattendancesIndex.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void handleUUID() {

        dataBaseHelper=new DataBaseHelper(this);
        Cursor cursor=dataBaseHelper.getAllDataFromUUID();
        while (cursor.moveToNext()){
            this.uuidForAtt=cursor.getString(0);
            this.uuidForAttIndex=cursor.getString(1);
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
                startActivity(intent);


            }
        });

    }

    //-------------------------------------
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

    //----------------------------


    private void intitOthers() {

        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL+uuidForAttIndex);
        attendanceIndexModelList = new ArrayList<>();
        rollList = new ArrayList<>();
        attendancesList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
myBroadcastReceiver=new MyBroadcastReceiver();

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

        if (item.getItemId() == R.id.logoutId) {
            Intent intent = new Intent(this, Authentication.class);
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


        return super.onOptionsItemSelected(item);
    }

}
