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
    DatabaseReference databaseReferenceForExistAttTypes;
    private ListView listViewExistAttTypes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist_roll_names);

        Log.d("ee", "onCreate: ExistRollName");
        initView();
        initOthers();

if (Network.isNetworkAvailable(this)) {


    databaseReferenceForExistAttTypes.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<String> stringListForKey = new ArrayList<>();
            for (DataSnapshot dsnapShot : dataSnapshot.getChildren()) {
                stringListForKey.add(dsnapShot.getKey());
            }
            final String[] keys = stringListForKey.toArray(new String[stringListForKey.size()]);
            CustomAdupterForAttendanceTypes customAdupterForAttendanceTypes = new CustomAdupterForAttendanceTypes(ExistRollNames.this, keys);
            listViewExistAttTypes.setAdapter(customAdupterForAttendanceTypes);
            listViewExistAttTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ExistRollNames.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("key", keys[position]);
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
    SQLiteDatabase sqLiteDatabase=dataBaseHelper.getWritableDatabase();
    Cursor cursor=dataBaseHelper.getAllDataFromAttendancesIndex();
    List<String>list=new ArrayList<>();
    while (cursor.moveToNext()){
        list.add(cursor.getString(1));
    }
    final String[]strings=list.toArray(new String[list.size()]);

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
    });


}

    }

    private void initOthers() {
        databaseReferenceForExistAttTypes = FirebaseDatabase.getInstance().getReference("rollnames");
dataBaseHelper=new DataBaseHelper(this);


    }

    private void initView() {

        listViewExistAttTypes = findViewById(R.id.existanceAttendanceListViewId);
    }
}
