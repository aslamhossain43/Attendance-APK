package com.renu.attendance_apk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageForAttIndex extends AppCompatActivity {
    DatabaseReference databaseReferenceForattendancesIndex, databaseReferenceForattendances;
    ValueEventListener myValueEventListner;
    List<AttendanceIndexModel> attendanceIndexModelList;
    private ListView attendancesIndexListView;
    List<String> attendancesList;
    List<String> dateTimeList;
    private AlertDialog.Builder alertDialogBuilder;
    String[] dateTimeForAttendanceIndexArray, attendanceForArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_for_att_index);


        initView();
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
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex");
        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReference("attendance");
        attendanceIndexModelList = new ArrayList<>();
        attendancesList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
        alertDialogBuilder = new AlertDialog.Builder(ManageForAttIndex.this);
    }

    private void initView() {
        attendancesIndexListView = findViewById(R.id.manageAttendancesIndexListViewId);

    }
}
