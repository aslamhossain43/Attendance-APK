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

public class ManageForLocalAttTypeIndex extends AppCompatActivity {


    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";

    DatabaseReference databaseReferenceForRollnameIndex,databaseReferenceForRollName, databaseReferenceForPercentage;


    private static final String WHOLE_INFORMATION_TABLE = "wholeinformations";
    private String emailMobilePassRollnameIndex = null;
    private String emailMobilePassRollname = null;
    private String emailMobilePassAttIndex = null;
    private String emailMobilePassAtt = null;
    private String emailMobilePassTest = null;
    private String emailMobilePass = null;


    private AlertDialog.Builder alertDialogBuilder;
    DataBaseHelper dataBaseHelper;
    private ListView listViewExistAttTypes;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_for_local_att_type_index);


        initView();

        getWholeInformation();
        initOthers();
listViewHandleForLocalAttIndex();



    }

    private void listViewHandleForLocalAttIndex() {

        databaseReferenceForRollnameIndex.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<RollNameIndexModel>rollNameIndexModelList=new ArrayList<>();
                for (DataSnapshot dnapshot : dataSnapshot.getChildren()) {
                    RollNameIndexModel rollNameIndexModel = dnapshot.getValue(RollNameIndexModel.class);
                    rollNameIndexModelList.add(rollNameIndexModel);


                }

                List<String> stringAttFor = new ArrayList<>();
                List<String> stringDate = new ArrayList<>();
                for (RollNameIndexModel rollNameIndexModel:rollNameIndexModelList) {
                    stringAttFor.add(rollNameIndexModel.getAttendanceFor());
                    stringDate.add(rollNameIndexModel.getDateTime());


                }

                final String[] sAtt = stringAttFor.toArray(new String[stringAttFor.size()]);
                final String[] sDateTime = stringDate.toArray(new String[stringDate.size()]);


                CustomAdupterForManagingLocalAttIndex customAdupterForLocalAttIndex = new CustomAdupterForManagingLocalAttIndex(ManageForLocalAttTypeIndex.this, sAtt, sDateTime);
                listViewExistAttTypes.setAdapter(customAdupterForLocalAttIndex);
                listViewExistAttTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        alertDialogBuilder.setMessage("Index Name : " + sAtt[position] + "\nDate : " + sDateTime[position]);
                        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataBaseHelper.deleteRollNameIndex(sDateTime[position]);
                                dataBaseHelper.deleteRollNameByDate(sDateTime[position]);

                                databaseReferenceForRollnameIndex.child(sDateTime[position]).getRef().removeValue();
                                databaseReferenceForRollName.child(sDateTime[position]).getRef().removeValue();

                                finish();
                                startActivity(getIntent());
                                Toast.makeText(ManageForLocalAttTypeIndex.this, "You Have Deleted Successfully !", Toast.LENGTH_SHORT).show();


                            }
                        });
                        alertDialogBuilder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(ManageForLocalAttTypeIndex.this, UpdateForManagingLocalAttTypeIndex.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("index", sAtt[position]);
                                bundle.putString("dateTime", sDateTime[position]);
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

       /*
        Cursor cursor = dataBaseHelper.getAllDataFromRollNameIndex();
        List<String> stringAttFor = new ArrayList<>();
        List<String> stringDate = new ArrayList<>();
        while (cursor.moveToNext()) {
            stringAttFor.add(cursor.getString(0));
            stringDate.add(cursor.getString(1));
        }

        final String[] sAtt = stringAttFor.toArray(new String[stringAttFor.size()]);
        final String[] sDateTime = stringDate.toArray(new String[stringDate.size()]);


        CustomAdupterForManagingLocalAttIndex customAdupterForLocalAttIndex = new CustomAdupterForManagingLocalAttIndex(ManageForLocalAttTypeIndex.this, sAtt, sDateTime);
        listViewExistAttTypes.setAdapter(customAdupterForLocalAttIndex);
        listViewExistAttTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                alertDialogBuilder.setMessage("Index Name : " + sAtt[position] + "\nDate : " + sDateTime[position]);
                alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataBaseHelper.deleteRollNameIndex(sDateTime[position]);
                        dataBaseHelper.deleteRollNameByDate(sDateTime[position]);


                        finish();
                        startActivity(getIntent());
                        Toast.makeText(ManageForLocalAttTypeIndex.this, "You Have Deleted Successfully !", Toast.LENGTH_SHORT).show();


                    }
                });
                alertDialogBuilder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(ManageForLocalAttTypeIndex.this, UpdateForManagingLocalAttTypeIndex.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("index", sAtt[position]);
                        bundle.putString("dateTime", sDateTime[position]);
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
*/

    }

    private void initOthers() {
        alertDialogBuilder = new AlertDialog.Builder(this);
        dataBaseHelper = new DataBaseHelper(this);
        myBroadcastReceiver = new MyBroadcastReceiver();
        databaseReferenceForPercentage = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassTest);

        databaseReferenceForRollnameIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollnameIndex);
        databaseReferenceForRollName = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollname);


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

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(myBroadcastReceiver, intentFilter);
        unregisterReceiver(myBroadcastReceiver);

    }

    private void initView() {
        listViewExistAttTypes = findViewById(R.id.manageLocalExistanceAttendanceListViewId);
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
        if (item.getItemId() == R.id.summary) {
            Intent intent = new Intent(this, Percentage.class);
            startActivity(intent);

        }

        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }
}
