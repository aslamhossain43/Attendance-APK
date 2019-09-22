package com.renu.attendance_apk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SpecificAttendancesFromFirebase extends AppCompatActivity {
    private String attFromFirebaseIndex;
    private String dtFromFirebaseIndex;
    DatabaseReference databaseReferenceForattendances;
    private ListView listViewSpecificAttFromFirebase;
    private TextView textViewForClass,textViewForDate;

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
                List<String> rollList = new ArrayList<>();
                List<String> nameList = new ArrayList<>();
                List<String> attList = new ArrayList<>();
                AttendanceModel attendanceModel=new AttendanceModel();
                attendanceModel=dataSnapshot.getValue(AttendanceModel.class);

                    rollList.addAll(attendanceModel.getRollList());
                    nameList.addAll(attendanceModel.getNameList());
                    attList.addAll(attendanceModel.getAttendanceList());


                Log.d("rr", "onDataChange: " + rollList);
                Log.d("att", "onDataChange: " + attList);

                String[] specificFinalRoll = rollList.toArray(new String[rollList.size()]);
                String[] specificFinalName = nameList.toArray(new String[nameList.size()]);
                String[] specificFinalAttendances = attList.toArray(new String[attList.size()]);
                CustomAdupterForIndexFromFirebase customAdupterForIndexFromFirebase = new CustomAdupterForIndexFromFirebase(SpecificAttendancesFromFirebase.this, specificFinalRoll,specificFinalName, specificFinalAttendances);
                listViewSpecificAttFromFirebase.setAdapter(customAdupterForIndexFromFirebase);


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

    }

    private void initView() {
        listViewSpecificAttFromFirebase = findViewById(R.id.listViewSpecificAttFromFirebaseId);
textViewForClass=findViewById(R.id.textViewHeadingForClassId);
textViewForDate=findViewById(R.id.textViewHeadingForDtaeId);
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

        }if (item.getItemId()==R.id.logoutId){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

}
