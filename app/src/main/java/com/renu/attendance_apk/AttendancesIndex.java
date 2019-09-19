package com.renu.attendance_apk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendancesIndex extends AppCompatActivity {
    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex;
    List<AttendanceIndexModel> attendanceIndexModelList;
    List<String> rollList;
    List<String> attendancesList;
    List<String> dateTimeList;
private ListView attendancesIndexListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendances_index);
        initView();
        intitOthers();

        databaseReferenceForattendancesIndex.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("hmmm", "onDataChange: hmmmmm");
                for (DataSnapshot dnapshot:dataSnapshot.getChildren()){
                AttendanceIndexModel attendanceIndexModel=dnapshot.getValue(AttendanceIndexModel.class);
                attendanceIndexModelList.add(attendanceIndexModel);


                }
                Log.d("ati", "onDataChange:"+attendanceIndexModelList.get(0).getAttendanceFor());





                List<String>dtForIndex=new ArrayList<>();
                List<String>attForIndex=new ArrayList<>();
                for (AttendanceIndexModel attendanceIndexModel:attendanceIndexModelList){
                    dtForIndex.add(attendanceIndexModel.getDateTime());
                    attForIndex.add(attendanceIndexModel.getAttendanceFor());



                }

                String[]dateTimeForAttendanceIndexArray;
                String[]attendanceForArray;
                dateTimeForAttendanceIndexArray=dtForIndex.toArray(new String[dtForIndex.size()]);
                attendanceForArray=attForIndex.toArray(new String[attForIndex.size()]);
                Log.d("dt", "onCreate: "+dtForIndex);
                Log.d("at", "onCreate: "+attForIndex);



                listViewHandleForAttendancesIndex(dateTimeForAttendanceIndexArray,attendanceForArray);











            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void listViewHandleForAttendancesIndex(String[] dateTimeForAttendanceIndexArray, String[] attendanceForArray) {

    CustomAdupterForAttendancesIndex customAdupterForAttendancesIndex=new CustomAdupterForAttendancesIndex(this,dateTimeForAttendanceIndexArray,attendanceForArray);
attendancesIndexListView.setAdapter(customAdupterForAttendancesIndex);


    }


    private void intitOthers() {

        //databaseReferenceForattendances = FirebaseDatabase.getInstance().getReference("attendance");
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex");
        attendanceIndexModelList = new ArrayList<>();
        rollList = new ArrayList<>();
        attendancesList = new ArrayList<>();
        dateTimeList = new ArrayList<>();


    }

    private void initView() {
attendancesIndexListView=findViewById(R.id.attendancesIndexListViewId);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logoutId) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);


    }
}
