package com.renu.attendance_apk;

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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowPercentage extends AppCompatActivity {
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
        Log.d("tt", "onCreate: " + attFromFirebaseIndex);
        Log.d("tt", "onCreate: " + dtFromFirebaseIndex);

        initView();
        handleUUID();
        initOthers();
        setValues();
        getValuesFromPercentageTable();


    }
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(myBroadcastReceiver, intentFilter);

    }
    private void getValuesFromPercentageTable() {

        List<String> attListForPer = new ArrayList<>();
        List<String> rollListForPer = new ArrayList<>();

        List<Integer> dayListForPer = new ArrayList<>();
        List<Integer> pListForPer = new ArrayList<>();
        List<Integer> aListForPer = new ArrayList<>();
        List<Integer> percentListForPer = new ArrayList<>();
        String getValues = "SELECT * FROM " + TEST_TABLE + " WHERE " + PERCENTAGE_ATT_FOR + " = '" + attFromFirebaseIndex + "'";


        Cursor cursor = sqLiteDatabase.rawQuery(getValues, null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {

                String atforPer = cursor.getString(0);
                attListForPer.add(atforPer);
                String roForPer = cursor.getString(1);
                rollListForPer.add(roForPer);
                int dayForPer = cursor.getInt(2);
                dayListForPer.add(dayForPer);
                int pForPer = cursor.getInt(3);
                pListForPer.add(pForPer);
                int aForPer = cursor.getInt(4);
                aListForPer.add(aForPer);
                int percentForPer = cursor.getInt(5);
                percentListForPer.add(percentForPer);


            }

        }

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


    }

    private void setValues() {
        textViewForClass.setText(attFromFirebaseIndex);
        textViewForDate.setText(dtFromFirebaseIndex);
    }

    private void initOthers() {
        myBroadcastReceiver = new MyBroadcastReceiver();
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
    }

    private void initView() {

        listViewShowPercentageId = findViewById(R.id.listViewShowPercentageId);
        textViewForClass = findViewById(R.id.textViewHeadingForClassId);
        textViewForDate = findViewById(R.id.textViewHeadingForDtaeId);
    }

    private void handleUUID() {
        dataBaseHelper = new DataBaseHelper(this);
        Cursor cursor = dataBaseHelper.getAllDataFromUUID();
        while (cursor.moveToNext()) {
            this.uuidForAtt = cursor.getString(0);
            this.uuidForAttIndex = cursor.getString(1);
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

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myBroadcastReceiver);
    }

}
