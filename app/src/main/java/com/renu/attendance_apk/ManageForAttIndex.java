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

public class ManageForAttIndex extends AppCompatActivity {
    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    DatabaseReference databaseReferenceForattendancesIndex, databaseReferenceForattendances, databaseReferenceForPercentage;
    ValueEventListener myValueEventListner;
    List<AttendanceIndexModel> attendanceIndexModelList;
    private ListView attendancesIndexListView;
    List<String> attendancesList;
    List<String> dateTimeList;
    private AlertDialog.Builder alertDialogBuilder;
    String[] dateTimeForAttendanceIndexArray, attendanceForArray;

    private MyBroadcastReceiver myBroadcastReceiver;
    String uuidForAtt, uuidForAttIndex;
    private DataBaseHelper dataBaseHelper;
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
        setContentView(R.layout.activity_manage_for_att_index);


        initView();

        getWholeInformation();
        initOthers();
        handleAttendanceIndex();


    }

    private void handleAttendanceIndex() {


        databaseReferenceForattendancesIndex.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> dtForIndex = new ArrayList<>();
                List<String> attForIndex = new ArrayList<>();
                for (DataSnapshot dnapshot : dataSnapshot.getChildren()) {
                    AttendanceIndexModel attendanceIndexModel = dnapshot.getValue(AttendanceIndexModel.class);
                    attendanceIndexModelList.add(attendanceIndexModel);


                }

                for (AttendanceIndexModel attendanceIndexModel : attendanceIndexModelList) {
                    dtForIndex.add(attendanceIndexModel.getDateTime());
                    attForIndex.add(attendanceIndexModel.getAttendanceFor());


                }


                dateTimeForAttendanceIndexArray = dtForIndex.toArray(new String[dtForIndex.size()]);
                attendanceForArray = attForIndex.toArray(new String[attForIndex.size()]);


                listViewHandleForAttendancesIndex(dateTimeForAttendanceIndexArray, attendanceForArray);


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

    private void listViewHandleForAttendancesIndex(final String[] dateTimeForAttendanceIndexArray, final String[] attendanceForArray) {


        final CustomAdupterForManagingAttIndex customAdupterForAttendancesIndex = new CustomAdupterForManagingAttIndex(this, dateTimeForAttendanceIndexArray, attendanceForArray);
        attendancesIndexListView.setAdapter(customAdupterForAttendancesIndex);

        attendancesIndexListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                alertDialogBuilder.setMessage("Index Name : " + attendanceForArray[position] + "\nDate : " + dateTimeForAttendanceIndexArray[position]);

                alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        final List<String> rollList = new ArrayList<>();
                        final List<String> nameList = new ArrayList<>();
                        final List<String> attList = new ArrayList<>();
                        final List<String> dtList = new ArrayList<>();


                        Log.d("rr", "onDataChange: BEFORE========================" + dateTimeForAttendanceIndexArray[position]);

                        //-----------------------------------------------------------
                        databaseReferenceForattendances.child(dateTimeForAttendanceIndexArray[position]).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("rr", "onDataChange: AFTER====================" + dateTimeForAttendanceIndexArray[position]);


                                try {


                                    AttendanceModel attendanceModel = new AttendanceModel();
                                    attendanceModel = dataSnapshot.getValue(AttendanceModel.class);
                                    rollList.addAll(attendanceModel.getRollList());
                                    nameList.addAll(attendanceModel.getNameList());
                                    attList.addAll(attendanceModel.getAttendanceList());
                                    dtList.addAll(attendanceModel.getDateTimeList());


                                    Log.d("rr", "onDataChange: " + rollList + ", " + "" + nameList + ", " + attList + ", " + dtList);

                                    handlePercentage(rollList, nameList, attList, dtList, position);

                                } catch (Exception e) {

                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });


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

    private void handlePercentage(final List<String> rollList, List<String> nameList, final List<String> attList, List<String> dtList, final int position) {


        databaseReferenceForPercentage.child(attendanceForArray[position]).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("tt", "onDataChange:=========== " + rollList);
                Log.d("tt", "onDataChange:=========== " + attendanceForArray[position]);

                final List<String> rollListFORPer = new ArrayList<>();
                final List<String> attListFORPer = new ArrayList<>();
                final List<Integer> dayListFORPer = new ArrayList<>();
                final List<Integer> pListFORPer = new ArrayList<>();
                final List<Integer> aListFORPer = new ArrayList<>();
                final List<Integer> percentageListFORPer = new ArrayList<>();


                try {

                    PercentageModel percentageModel = new PercentageModel();
                    percentageModel = dataSnapshot.getValue(PercentageModel.class);
                    attListFORPer.addAll(percentageModel.getAttList());
                    rollListFORPer.addAll(percentageModel.getRollList());
                    dayListFORPer.addAll(percentageModel.getDayList());
                    pListFORPer.addAll(percentageModel.getpList());
                    aListFORPer.addAll(percentageModel.getaList());
                    percentageListFORPer.addAll(percentageModel.getPercentList());


                    Log.d("rr", "onDataChange: " + attListFORPer + ", " + "" + rollListFORPer + ", " + dayListFORPer + ", " + pListFORPer + ", " + aListFORPer + ", " + percentageListFORPer);


                    Log.d("rr", "onDataChange: =========================" + rollList.size());
                    for (int i = 0; i < rollList.size(); i++) {
                        int finalday = 0, finalP = 0, finalA = 0, finalPercent = 0;

                        if (attList.get(i).equals("P")) {

                            finalday = dayListFORPer.get(i) - 1;
                            finalP = pListFORPer.get(i) - 1;
                            finalA = aListFORPer.get(i);
                            finalPercent = finalP * 100 / finalday;

                            Log.d("rr", "onDataChange: " + finalday + ", " + finalP + ", " + finalA + ", " + finalPercent);

                        }
                        if (attList.get(i).equals("A")) {
                            finalday = dayListFORPer.get(i) - 1;
                            finalP = pListFORPer.get(i);
                            finalA = aListFORPer.get(i) - 1;
                            finalPercent = finalP * 100 / finalday;

                            Log.d("rr", "onDataChange: " + finalday + ", " + finalP + ", " + finalA + ", " + finalPercent);

                        }
                        if (attList.get(i).equals("Off")) {

                            finalday = dayListFORPer.get(i) - 1;
                            finalP = pListFORPer.get(i);
                            finalA = aListFORPer.get(i);
                            finalPercent = finalP * 100 / finalday;

                            Log.d("rr", "onDataChange: " + finalday + ", " + finalP + ", " + finalA + ", " + finalPercent);


                        }
                        Log.d("rr", "onDataChange: " + finalday + ", " + finalP + ", " + finalA + ", " + finalPercent);

                        databaseReferenceForPercentage.child(attendanceForArray[position]).child("dayList").child("" + i).getRef().removeValue();
                        databaseReferenceForPercentage.child(attendanceForArray[position]).child("dayList").child("" + i).getRef().setValue(finalday);

                        databaseReferenceForPercentage.child(attendanceForArray[position]).child("pList").child("" + i).getRef().removeValue();
                        databaseReferenceForPercentage.child(attendanceForArray[position]).child("pList").child("" + i).getRef().setValue(finalP);

                        databaseReferenceForPercentage.child(attendanceForArray[position]).child("aList").child("" + i).getRef().removeValue();
                        databaseReferenceForPercentage.child(attendanceForArray[position]).child("aList").child("" + i).getRef().setValue(finalA);

                        databaseReferenceForPercentage.child(attendanceForArray[position]).child("percentList").child("" + i).getRef().removeValue();
                        databaseReferenceForPercentage.child(attendanceForArray[position]).child("percentList").child("" + i).getRef().setValue(finalPercent);

                        dataBaseHelper.updatePercentageOneTitle(attendanceForArray[position], finalday, finalP, finalA, finalPercent);


                    }
                    databaseReferenceForattendancesIndex.child(dateTimeForAttendanceIndexArray[position]).getRef().removeValue();
                    databaseReferenceForattendances.child(dateTimeForAttendanceIndexArray[position]).getRef().removeValue();


                    Toast.makeText(ManageForAttIndex.this, "You have deleted successfully !", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());


                } catch (Exception e) {

                    Log.d("rr", "onDataChange: Exception============"+attendanceForArray[position]);
                    dataBaseHelper.deleteSpecificFromPercentage(attendanceForArray[position]);
                    databaseReferenceForPercentage.child(attendanceForArray[position]).getRef().removeValue();

                    databaseReferenceForattendancesIndex.child(dateTimeForAttendanceIndexArray[position]).getRef().removeValue();
                    databaseReferenceForattendances.child(dateTimeForAttendanceIndexArray[position]).getRef().removeValue();
                    Toast.makeText(ManageForAttIndex.this, "You have deleted successfully !", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initOthers() {
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAttIndex);
        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAtt);
        databaseReferenceForPercentage = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassTest);

        attendanceIndexModelList = new ArrayList<>();
        attendancesList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
        alertDialogBuilder = new AlertDialog.Builder(ManageForAttIndex.this);
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
        attendancesIndexListView = findViewById(R.id.manageAttendancesIndexListViewId);

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
        if (item.getItemId() == R.id.logout) {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            dataBaseHelper.delete_Login();

            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

}
