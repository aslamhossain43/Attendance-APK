package com.renu.attendance_apk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
    private Button btnForLocalAttTypeIndex, btnForLocalAttPerson, btnForAttIndex,
            btnAllPersonInfoIndex, btndeleteAttIndex;
    private DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex;
    AlertDialog.Builder alertDialogBuilder;
    DataBaseHelper dataBaseHelper;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
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
                        dataBaseHelper.deleteAllValuesFromAllTables();
                        Intent intent = new Intent(Settings.this, CreateNew1.class);
                        startActivity(intent);
                        Toast.makeText(Settings.this, "You Have Deleted All Information From Your App !", Toast.LENGTH_SHORT).show();


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


                        databaseReferenceForattendancesIndex.getRef().removeValue();
                        databaseReferenceForattendances.getRef().removeValue();
                        Intent intent = new Intent(Settings.this, ExistRollNames.class);
                        startActivity(intent);
                        Toast.makeText(Settings.this, "You Have Deleted All Attendances From Your App !", Toast.LENGTH_SHORT).show();

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
        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReference("attendance");
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex");
        dataBaseHelper = new DataBaseHelper(this);
        alertDialogBuilder = new AlertDialog.Builder(this);
        myBroadcastReceiver=new MyBroadcastReceiver();
    }
    //-------------------------------------
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

    //----------------------------

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
