package com.renu.attendance_apk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class ExistRollNames extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    DatabaseReference databaseReferenceForRollNameIndex;
    private ListView listViewExistAttTypes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist_roll_names);

        Log.d("ee", "onCreate: ExistRollName");
        initView();
        initOthers();

/*
if (Network.isNetworkAvailable(this)) {


    databaseReferenceForRollNameIndex.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<String> stringAttFor = new ArrayList<>();
            List<String> stringDate = new ArrayList<>();
            RollNameIndexModel rollNameIndexModel=new RollNameIndexModel();
            for (DataSnapshot dsnapShot : dataSnapshot.getChildren()) {
               rollNameIndexModel=dsnapShot.getValue(RollNameIndexModel.class);
               stringAttFor.add(rollNameIndexModel.getAttendanceFor());
               stringDate.add(rollNameIndexModel.getDateTime());
            }
            final String[] sAtt = stringAttFor.toArray(new String[stringAttFor.size()]);
            final String[] sDateTime = stringDate.toArray(new String[stringDate.size()]);
            CustomAdupterForAttendanceTypes customAdupterForAttendanceTypes = new CustomAdupterForAttendanceTypes(ExistRollNames.this, sAtt,sDateTime);
            listViewExistAttTypes.setAdapter(customAdupterForAttendanceTypes);
            listViewExistAttTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ExistRollNames.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("sAtt", sAtt[position]);
                    bundle.putString("sDateTime", sDateTime[position]);
                    intent.putExtras(bundle);
                    startActivity(intent);


                }
            });

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}else {
*/
    SQLiteDatabase sqLiteDatabase=dataBaseHelper.getWritableDatabase();
    Cursor cursor=dataBaseHelper.getAllDataFromRollNameIndex();
    List<String>stringAttFor=new ArrayList<>();
    List<String>stringDate=new ArrayList<>();
    while (cursor.moveToNext()){
        stringAttFor.add(cursor.getString(0));
        stringDate.add(cursor.getString(1));
    }

    final String[] sAtt = stringAttFor.toArray(new String[stringAttFor.size()]);
    final String[] sDateTime = stringDate.toArray(new String[stringDate.size()]);
    Log.d("rr", "onCreate: attFor"+stringAttFor);
    Log.d("rr", "onCreate: date for roll"+stringDate);
    CustomAdupterForAttendanceTypes customAdupterForAttendanceTypes = new CustomAdupterForAttendanceTypes(ExistRollNames.this, sAtt,sDateTime);
    listViewExistAttTypes.setAdapter(customAdupterForAttendanceTypes);
    listViewExistAttTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ExistRollNames.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("sAtt", sAtt[position]);
            bundle.putString("sDateTime", sDateTime[position]);
            intent.putExtras(bundle);
            startActivity(intent);


        }
    });


    /*final String[]strings=list.toArray(new String[list.size()]);

    CustomAdupterForAttendanceTypes customAdupterForAttendanceTypes = new CustomAdupterForAttendanceTypes(ExistRollNames.this, strings);
    listViewExistAttTypes.setAdapter(customAdupterForAttendanceTypes);
    listViewExistAttTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ExistRollNames.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("key", strings[position]);
            intent.putExtras(bundle);
            startActivity(intent);


        }
    });*/


//}

    }

    private void initOthers() {
        databaseReferenceForRollNameIndex = FirebaseDatabase.getInstance().getReference("rollnamesindex");
dataBaseHelper=new DataBaseHelper(this);


    }

    private void initView() {

        listViewExistAttTypes = findViewById(R.id.existanceAttendanceListViewId);
    }
}
