package com.renu.attendance_apk;

import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Percentage extends AppCompatActivity {


    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";

    DatabaseReference databaseReferenceForRollnameIndex, databaseReferenceForAttIndex, databaseReferenceForPercentage;

    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;
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
        setContentView(R.layout.activity_percentage);

BackgroundUpdate.getInstance().handlePercentage(this);

        initView();
        getWholeInformation();
        initOthers();

        handlePercentageIndex();


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

    private void handlePercentageIndex() {


        if (Network.isNetworkAvailable(this)) {



            databaseReferenceForPercentage.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        getPercentageFromFirebase();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {


            Toast.makeText(this, "Connect internet !", Toast.LENGTH_SHORT).show();


        }

    }

    private void getPercentageFromFirebase() {


        final List<String> stringAttFor = new ArrayList<>();



        databaseReferenceForPercentage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    stringAttFor.add(dataSnapshot1.getKey());

                }

                final String[] sAtt = stringAttFor.toArray(new String[stringAttFor.size()]);



                        CustomAdupterForShowPercentage customAdupterForShowPercentage = new CustomAdupterForShowPercentage(Percentage.this, sAtt);
                        listViewExistAttTypes.setAdapter(customAdupterForShowPercentage);
                        listViewExistAttTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(Percentage.this, ShowPercentage.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("sAtt", sAtt[position]);
                                intent.putExtras(bundle);

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }
                        });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }

    private void initOthers() {

        dataBaseHelper = new DataBaseHelper(this);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        databaseReferenceForPercentage = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassTest);

        databaseReferenceForRollnameIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollnameIndex);
        databaseReferenceForAttIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAttIndex);


        myBroadcastReceiver = new MyBroadcastReceiver();
    }

    private void initView() {

        listViewExistAttTypes = findViewById(R.id.percentageListViewId);
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
        if (item.getItemId() == R.id.localAttendances) {
            Intent intent = new Intent(this, ExistRollNames.class);

            startActivity(intent);

        }

        if (item.getItemId() == R.id.settings) {

            Intent intent = new Intent(this, Settings.class);

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
