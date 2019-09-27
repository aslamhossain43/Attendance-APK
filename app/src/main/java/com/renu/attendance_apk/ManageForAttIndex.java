package com.renu.attendance_apk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageForAttIndex extends AppCompatActivity {
    DatabaseReference databaseReferenceForattendancesIndex;
    List<AttendanceIndexModel> attendanceIndexModelList;
    private ListView attendancesIndexListView;
    List<String> attendancesList;
    List<String> dateTimeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_for_att_index);


        initView();
        initOthers();


        databaseReferenceForattendancesIndex.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("hmmm", "onDataChange: hmmmmm");
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
                Log.d("dt", "onCreate: " + dtForIndex);
                Log.d("at", "onCreate: " + attForIndex);


                listViewHandleForAttendancesIndex(dateTimeForAttendanceIndexArray, attendanceForArray);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void listViewHandleForAttendancesIndex(String[] dateTimeForAttendanceIndexArray, String[] attendanceForArray) {


        CustomAdupterForManagingAttIndex customAdupterForAttendancesIndex = new CustomAdupterForManagingAttIndex(this, dateTimeForAttendanceIndexArray, attendanceForArray);
        attendancesIndexListView.setAdapter(customAdupterForAttendancesIndex);

        attendancesIndexListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {









            }
        });


    }

    private void initOthers() {
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex");
        attendanceIndexModelList = new ArrayList<>();
        attendancesList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
    }

    private void initView() {
        attendancesIndexListView = findViewById(R.id.manageAttendancesIndexListViewId);

    }
}
