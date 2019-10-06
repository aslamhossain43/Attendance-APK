package com.renu.attendance_apk;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowPercentage extends AppCompatActivity {

    DatabaseReference databaseReferenceForPercentage;
    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    List<String> attListForTest, rollListForTest;
    List<Integer> dayList, pList, aList, percentList;


    private static final String WHOLE_INFORMATION_TABLE = "wholeinformations";
    private String emailMobilePassRollnameIndex = null;
    private String emailMobilePassRollname = null;
    private String emailMobilePassAttIndex = null;
    private String emailMobilePassAtt = null;
    private String emailMobilePassTest = null;
    private String emailMobilePass = null;


    private ListView listViewShowPercentageId;
    private String attFromFirebaseIndex, dtFromFirebaseIndex;
    private TextView textViewForClass, textViewForDate;
    String[] percentageFinalRoll;
    int[] percentageFinalDay, percentageFinalP, percentageFinalA;
    int[] percentageFinalPercent;
    private MyBroadcastReceiver myBroadcastReceiver;
    String uuidForAtt, uuidForAttIndex;
    DataBaseHelper dataBaseHelper;
    private static final String TEST_TABLE = "test";
    private static final String PERCENTAGE_ATT_FOR = "attfor";
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_percentage);


        getValuesFromIntent();

        initView();
        getWholeInformation();

        initOthers();
        setValues();
        getValuesFromPercentageTable();


    }
//-----------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myBroadcastReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(myBroadcastReceiver);


    }


    //------------------------------------------------------------
    private void getValuesFromPercentageTable() {
        if (Network.isNetworkAvailable(ShowPercentage.this)) {


            databaseReferenceForPercentage.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    try {
                        List<String> attListForPer = new ArrayList<>();
                        List<String> rollListForPer = new ArrayList<>();

                        List<Integer> dayListForPer = new ArrayList<>();
                        List<Integer> pListForPer = new ArrayList<>();
                        List<Integer> aListForPer = new ArrayList<>();
                        List<Integer> percentListForPer = new ArrayList<>();


                        PercentageModel percentageModel = dataSnapshot.getValue(PercentageModel.class);


                        attListForPer.addAll(percentageModel.getAttList());
                        rollListForPer.addAll(percentageModel.getRollList());
                        dayListForPer.addAll(percentageModel.getDayList());
                        pListForPer.addAll(percentageModel.getpList());
                        aListForPer.addAll(percentageModel.getaList());
                        percentListForPer.addAll(percentageModel.getPercentList());


                        percentageFinalRoll = rollListForPer.toArray(new String[rollListForPer.size()]);
//convert List<Integer> into int[]
                        int daysize = dayListForPer.size();
                        int[] dayresult = new int[daysize];
                        Integer[] daytemp = dayListForPer.toArray(new Integer[daysize]);
                        for (int n = 0; n < daysize; ++n) {
                            dayresult[n] = daytemp[n];
                        }
                        percentageFinalDay = dayresult;


                        //convert List<Integer> into int[]
                        int psize = pListForPer.size();
                        int[] presult = new int[psize];
                        Integer[] ptemp = pListForPer.toArray(new Integer[psize]);
                        for (int n = 0; n < psize; ++n) {
                            presult[n] = ptemp[n];
                        }
                        percentageFinalP = presult;

                        //convert List<Integer> into int[]
                        int asize = aListForPer.size();
                        int[] aresult = new int[asize];
                        Integer[] atemp = aListForPer.toArray(new Integer[asize]);
                        for (int n = 0; n < asize; ++n) {
                            aresult[n] = atemp[n];
                        }
                        percentageFinalA = aresult;

                        //convert List<Integer> into int[]
                        int percentsize = percentListForPer.size();
                        int[] percentresult = new int[percentsize];
                        Integer[] percenttemp = percentListForPer.toArray(new Integer[percentsize]);
                        for (int n = 0; n < percentsize; ++n) {
                            percentresult[n] = percenttemp[n];
                        }

                        percentageFinalPercent = percentresult;


                        CustomAdupterForShowPercentageDetails customAdupterForShowPercentageDetails = new CustomAdupterForShowPercentageDetails(ShowPercentage.this, percentageFinalRoll, percentageFinalDay, percentageFinalP,
                                percentageFinalA, percentageFinalPercent);
                        listViewShowPercentageId.setAdapter(customAdupterForShowPercentageDetails);


                    } catch (Exception e) {

                        Intent intent = new Intent(ShowPercentage.this, Percentage.class);
                        startActivity(intent);


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(ShowPercentage.this, "Connect internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void setValues() {
        textViewForClass.setText(attFromFirebaseIndex);
        textViewForDate.setText(dtFromFirebaseIndex);
    }

    private void initOthers() {
        myBroadcastReceiver = new MyBroadcastReceiver();
        databaseReferenceForPercentage = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassTest + "/" + attFromFirebaseIndex);

    }

    private void initView() {

        listViewShowPercentageId = findViewById(R.id.listViewShowPercentageId);
        textViewForClass = findViewById(R.id.textViewHeadingForClassId);
        textViewForDate = findViewById(R.id.textViewHeadingForDtaeId);
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

    private void getValuesFromIntent() {


        Bundle bundle = getIntent().getExtras();
        attFromFirebaseIndex = bundle.getString("sAtt");
        dtFromFirebaseIndex = bundle.getString("sDateTime");

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
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            startActivity(intent);

        }
        if (item.getItemId() == R.id.infoId) {
            Intent intent = new Intent(this, Informations.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            startActivity(intent);

        }

        if (item.getItemId() == R.id.listId) {
            Intent intent = new Intent(this, AttendancesIndex.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            startActivity(intent);

        }
        if (item.getItemId() == R.id.openId) {
            Intent intent = new Intent(this, CreateNew1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            startActivity(intent);

        }
        if (item.getItemId() == R.id.localAttendances) {
            Intent intent = new Intent(this, ExistRollNames.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            startActivity(intent);

        }
        if (item.getItemId() == R.id.summary) {
            Intent intent = new Intent(this, Percentage.class);
            startActivity(intent);

        }

        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, Settings.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }


}
