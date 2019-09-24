package com.renu.attendance_apk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SpecificAttendancesFromFirebase extends AppCompatActivity {
    private String attFromFirebaseIndex;
    private String dtFromFirebaseIndex;
    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex;
    private ListView listViewSpecificAttFromFirebase;
    private TextView textViewForClass, textViewForDate;
    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_attendances_from_firebase);
        getValuesFromIntent();


        initView();
        initOthers();
        setValues();


        databaseReferenceForattendances.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> rollList = new ArrayList<>();
                List<String> nameList = new ArrayList<>();
                List<String> attList = new ArrayList<>();
                List<String> dtList = new ArrayList<>();
                AttendanceModel attendanceModel = new AttendanceModel();
                attendanceModel = dataSnapshot.getValue(AttendanceModel.class);


                try {
                    rollList.addAll(attendanceModel.getRollList());
                    nameList.addAll(attendanceModel.getNameList());
                    attList.addAll(attendanceModel.getAttendanceList());
                    dtList.addAll(attendanceModel.getDateTimeList());

                    Log.d("rr", "onDataChange: " + rollList);
                    Log.d("att", "onDataChange: " + attList);
                } catch (Exception e) {
                    Query qForAttIndex = databaseReferenceForattendancesIndex;
                    qForAttIndex.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.child("attendanceFor").getRef().removeValue();
                            dataSnapshot.child("dateTime").getRef().removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


                final String[] specificFinalRoll = rollList.toArray(new String[rollList.size()]);
                final String[] specificFinalName = nameList.toArray(new String[nameList.size()]);
                final String[] specificFinalAttendances = attList.toArray(new String[attList.size()]);
                final String[] specificFinalDateTime = dtList.toArray(new String[dtList.size()]);
                CustomAdupterForIndexFromFirebase customAdupterForIndexFromFirebase = new CustomAdupterForIndexFromFirebase(SpecificAttendancesFromFirebase.this, specificFinalRoll, specificFinalName, specificFinalAttendances);
                listViewSpecificAttFromFirebase.setAdapter(customAdupterForIndexFromFirebase);


                listViewSpecificAttFromFirebase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        alertDialogBuilder.setMessage("ID : " + specificFinalRoll[position] + "\n" + "Name : "
                                + specificFinalName[position] + "\n" + "P/A/Off : " + specificFinalAttendances[position]);

                        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String dateTime = specificFinalDateTime[position];

                                Query qForAtt = databaseReferenceForattendances;
                                qForAtt.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        dataSnapshot.child("rollList").child(String.valueOf(position)).getRef().removeValue();
                                        dataSnapshot.child("nameList").child(String.valueOf(position)).getRef().removeValue();
                                        dataSnapshot.child("dateTimeList").child(String.valueOf(position)).getRef().removeValue();
                                        dataSnapshot.child("attendanceList").child(String.valueOf(position)).getRef().removeValue();


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        });
                        alertDialogBuilder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SpecificAttendancesFromFirebase.this, Update.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("roll", specificFinalRoll[position]);
                                bundle.putString("name", specificFinalName[position]);
                                bundle.putString("paoff", specificFinalAttendances[position]);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setValues() {
        textViewForClass.setText(attFromFirebaseIndex);
        textViewForDate.setText(dtFromFirebaseIndex);

    }

    private void getValuesFromIntent() {


        Bundle bundle = getIntent().getExtras();
        attFromFirebaseIndex = bundle.getString("attFor");
        dtFromFirebaseIndex = bundle.getString("dt");

    }

    private void initOthers() {

        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReference("attendance").child(dtFromFirebaseIndex);
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex").child(dtFromFirebaseIndex);
        alertDialogBuilder = new AlertDialog.Builder(SpecificAttendancesFromFirebase.this);


    }

    private void initView() {
        listViewSpecificAttFromFirebase = findViewById(R.id.listViewSpecificAttFromFirebaseId);
        textViewForClass = findViewById(R.id.textViewHeadingForClassId);
        textViewForDate = findViewById(R.id.textViewHeadingForDtaeId);
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
            Intent intent = new Intent(this, ExistRollNames.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.logoutId) {
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

}
