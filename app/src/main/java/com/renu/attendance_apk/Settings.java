package com.renu.attendance_apk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Settings extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    private Button btnForLocalAttTypeIndex, btnForLocalAttPerson, btnForAttIndex,
            btnAllPersonInfoIndex, btndeleteAttIndex;
    private DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex,
            databaseReferenceForRollnameIndex, databaseReferenceForRollname, databaseReferenceForPercentage;
    AlertDialog.Builder alertDialogBuilder;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;
    private MyBroadcastReceiver myBroadcastReceiver;
    String uuidForAtt, uuidForAttIndex;


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
        setContentView(R.layout.activity_settings);

        initView();
        getWholeInformation();
        initOthers();

        btnForLocalAttTypeIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, ManageForLocalAttTypeIndex.class);
                startActivity(intent);
            }
        });
        btnForLocalAttPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, ManageForAttPersonPre.class);
                startActivity(intent);
            }
        });
        btnForAttIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, ManageForAttIndex.class);
                startActivity(intent);
            }
        });
        btnAllPersonInfoIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.setMessage("Do You Want To Delete All Information From Your App ?");
                alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Network.isNetworkAvailable(Settings.this)) {


                            databaseReferenceForattendancesIndex.getRef().removeValue();
                            databaseReferenceForattendances.getRef().removeValue();
                            databaseReferenceForRollnameIndex.getRef().removeValue();
                            databaseReferenceForRollname.getRef().removeValue();
                            databaseReferenceForPercentage.getRef().removeValue();


                            dataBaseHelper.deleteAllValuesFromAllTables();
                            Intent intent = new Intent(Settings.this, RegisterActivity.class);
                            startActivity(intent);
                            Toast.makeText(Settings.this, "You Have Deleted All Information From Your App !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Settings.this, "Connect internet !", Toast.LENGTH_SHORT).show();
                        }


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
        btndeleteAttIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialogBuilder.setMessage("Do You Want To Delete All Attendances From Your App ?");
                alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Network.isNetworkAvailable(Settings.this)) {


                            databaseReferenceForattendancesIndex.getRef().removeValue();
                            databaseReferenceForattendances.getRef().removeValue();
                            //databaseReferenceForPercentage.getRef().removeValue();

                            dataBaseHelper.deleteAllFromPercentage();
                            Intent intent = new Intent(Settings.this, ExistRollNames.class);
                            startActivity(intent);
                            Toast.makeText(Settings.this, "You Have Deleted All Attendances From Your App !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Settings.this, "Connect network !", Toast.LENGTH_SHORT).show();
                        }

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
        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAtt);
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAttIndex);

        databaseReferenceForRollname = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollname);
        databaseReferenceForRollnameIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollnameIndex);


        databaseReferenceForPercentage = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassTest);


        alertDialogBuilder = new AlertDialog.Builder(this);
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

        btnForLocalAttTypeIndex = findViewById(R.id.btnLocalAttTypeIndexId);
        btnForLocalAttPerson = findViewById(R.id.btnLocalAttPersonId);
        btnForAttIndex = findViewById(R.id.btnAttIndexId);
        btnAllPersonInfoIndex = findViewById(R.id.btnAllPersonInfoIndexId);
        btndeleteAttIndex = findViewById(R.id.btndeleteAttIndexId);


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
        if (item.getItemId() == R.id.summary) {
            Intent intent = new Intent(this, Percentage.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.localAttendances) {
            Intent intent = new Intent(this, ExistRollNames.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }
}
