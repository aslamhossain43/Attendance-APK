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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SpecificAttendancesFromFirebase extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    private String attFromFirebaseIndex, dtFromFirebaseIndex;
    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex;
    private ListView listViewSpecificAttFromFirebase;
    private TextView textViewForClass, textViewForDate;
    private AlertDialog.Builder alertDialogBuilder;
    CustomAdupterForIndexFromFirebase customAdupterForIndexFromFirebase;
    String[] specificFinalRoll, specificFinalName, specificFinalAttendances, specificFinalDateTime;
    private MyBroadcastReceiver myBroadcastReceiver;
    String uuidForAtt, uuidForAttIndex;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;

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
        setContentView(R.layout.activity_specific_attendances_from_firebase);
        getValuesFromIntent();


        initView();
        getWholeInformation();


        initOthers();
        setValues();


        databaseReferenceForattendances.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> rollList = new ArrayList<>();
                final List<String> nameList = new ArrayList<>();
                final List<String> attList = new ArrayList<>();
                final List<String> dtList = new ArrayList<>();


                try {


                    AttendanceModel attendanceModel = new AttendanceModel();
                    attendanceModel = dataSnapshot.getValue(AttendanceModel.class);
                    rollList.addAll(attendanceModel.getRollList());
                    nameList.addAll(attendanceModel.getNameList());
                    attList.addAll(attendanceModel.getAttendanceList());
                    dtList.addAll(attendanceModel.getDateTimeList());

                } catch (Exception e) {


                }


                specificFinalRoll = rollList.toArray(new String[rollList.size()]);
                specificFinalName = nameList.toArray(new String[nameList.size()]);
                specificFinalAttendances = attList.toArray(new String[attList.size()]);
                specificFinalDateTime = dtList.toArray(new String[dtList.size()]);
                customAdupterForIndexFromFirebase = new CustomAdupterForIndexFromFirebase(SpecificAttendancesFromFirebase.this, specificFinalRoll, specificFinalName, specificFinalAttendances);
                listViewSpecificAttFromFirebase.setAdapter(customAdupterForIndexFromFirebase);


                listViewSpecificAttFromFirebase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        alertDialogBuilder.setMessage("ID : " + specificFinalRoll[position] + "\n" + "Name : "
                                + specificFinalName[position] + "\n" + "P/A/Off : " + specificFinalAttendances[position]);

                        /*alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                Query qForAtt = databaseReferenceForattendances;
                                qForAtt.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataBaseHelper.updatePercentageWhenDeleteOne(rollList.get(position), attFromFirebaseIndex,
                                                dataSnapshot.child("attendanceList").child("" + position).getValue().toString());
                                        new BackgroundUpdate().handlePercentage(SpecificAttendancesFromFirebase.this);


                                        rollList.remove(position);
                                        nameList.remove(position);
                                        attList.remove(position);
                                        dtList.remove(position);
                                        if (rollList.size() < 1) {
                                            Query qForAttIndex = databaseReferenceForattendancesIndex;
                                            qForAttIndex.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    dataSnapshot.getRef().removeValue();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            Toast.makeText(SpecificAttendancesFromFirebase.this, "You Have Deleted Successfully !", Toast.LENGTH_SHORT).show();

                                        }
                                        if (rollList.size() > 0) {

                                            dataSnapshot.child("rollList").getRef().removeValue();
                                            dataSnapshot.child("nameList").getRef().removeValue();
                                            dataSnapshot.child("dateTimeList").getRef().removeValue();
                                            dataSnapshot.child("attendanceList").getRef().removeValue();

                                            AttendanceModel attendanceModel = new AttendanceModel(rollList, nameList, attList, dtList);

                                            databaseReferenceForattendances.setValue(attendanceModel);
                                            Toast.makeText(SpecificAttendancesFromFirebase.this, "You Have Deleted Successfully !", Toast.LENGTH_SHORT).show();
                                        } else {

                                            dataSnapshot.getRef().removeValue();

                                            Intent intent = new Intent(SpecificAttendancesFromFirebase.this, AttendancesIndex.class);
                                            startActivity(intent);
                                            Toast.makeText(SpecificAttendancesFromFirebase.this, "You Have Deleted Successfully !", Toast.LENGTH_SHORT).show();

                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        databaseError.toException();
                                    }
                                });

                            }
                        });

                        */
                        alertDialogBuilder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SpecificAttendancesFromFirebase.this, Update.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("roll", specificFinalRoll[position]);
                                bundle.putString("name", specificFinalName[position]);
                                bundle.putString("paoff", specificFinalAttendances[position]);
                                bundle.putString("dateTime", specificFinalDateTime[position]);
                                bundle.putString("position", String.valueOf(position));
                                bundle.putString("attFor", attFromFirebaseIndex);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });

                        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
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

        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAtt).child(dtFromFirebaseIndex);
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAttIndex).child(dtFromFirebaseIndex);
        alertDialogBuilder = new AlertDialog.Builder(SpecificAttendancesFromFirebase.this);
        myBroadcastReceiver = new MyBroadcastReceiver();

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
