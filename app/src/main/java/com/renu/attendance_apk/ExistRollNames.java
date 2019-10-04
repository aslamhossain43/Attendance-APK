package com.renu.attendance_apk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";

    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;
    DatabaseReference databaseReferenceForRollNameIndex;
    private ListView listViewExistAttTypes;
    private MyBroadcastReceiver myBroadcastReceiver;


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
        setContentView(R.layout.activity_exist_roll_names);

        initView();
        getWholeInformation();
        initOthers();

        handleRollNameIndex();

    }

    private void handleRollNameIndex() {


        final List<RollNameIndexModel>rollNameIndexModelList=new ArrayList<>();
        final List<String> stringAttFor = new ArrayList<>();
        final List<String> stringDate = new ArrayList<>();
        if (Network.isNetworkAvailable(this)) {

            databaseReferenceForRollNameIndex.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {





                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {



                        RollNameIndexModel rollNameIndexModel = dataSnapshot1.getValue(RollNameIndexModel.class);
                       rollNameIndexModelList.add(rollNameIndexModel);



                    }
                    for (RollNameIndexModel rollNameIndexModel:rollNameIndexModelList){
                        stringAttFor.add(rollNameIndexModel.getAttendanceFor());
                        stringDate.add(rollNameIndexModel.getDateTime());

                    }


                    final String[] sAtt = stringAttFor.toArray(new String[stringAttFor.size()]);
                    final String[] sDateTime = stringDate.toArray(new String[stringDate.size()]);
                    CustomAdupterForAttendanceTypes customAdupterForAttendanceTypes = new CustomAdupterForAttendanceTypes(ExistRollNames.this, sAtt, sDateTime);
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


        } else {


            Cursor cursor = dataBaseHelper.getAllDataFromRollNameIndex();
            while (cursor.moveToNext()) {
                stringAttFor.add(cursor.getString(0));
                stringDate.add(cursor.getString(1));
            }

            final String[] sAtt = stringAttFor.toArray(new String[stringAttFor.size()]);
            final String[] sDateTime = stringDate.toArray(new String[stringDate.size()]);
            CustomAdupterForAttendanceTypes customAdupterForAttendanceTypes = new CustomAdupterForAttendanceTypes(ExistRollNames.this, sAtt, sDateTime);
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

    private void initOthers() {



       databaseReferenceForRollNameIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollnameIndex);
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

        listViewExistAttTypes = findViewById(R.id.existanceAttendanceListViewId);
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

        if (item.getItemId() == R.id.logoutId) {
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }


}
