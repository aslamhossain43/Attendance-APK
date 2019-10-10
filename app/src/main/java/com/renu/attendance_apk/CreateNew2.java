package com.renu.attendance_apk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateNew2 extends AppCompatActivity {
    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    DatabaseReference databaseReferenceForRollName, databaseReferenceForRollNameIndex;


    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;
    private static final String WHOLE_INFORMATION_TABLE = "wholeinformations";


    private TextView textViewAttFor, textViewPersonNo;
    private Button saveBtn, backBtn;
    private LinearLayout parent_linear_layout_for_create_new2;
    private EditText editTextRoll, editTextName;
    private String attFor;
    private int pNo;
    private List<String> rollList, nameList, attForList, dateTimeList;
    private MyBroadcastReceiver myBroadcastReceiver;

    private String emailMobilePassRollnameIndex = null;
    private String emailMobilePassRollname = null;
    private String emailMobilePassAttIndex = null;
    private String emailMobilePassAtt = null;
    private String emailMobilePassTest = null;
    private String emailMobilePass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new2);


        getValuesFromIntent();
        initView();
        getWholeInformation();
        initOthers();
        setValues();
        handleRollNameFields();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollList.clear();
                nameList.clear();
                dateTimeList.clear();
                //--------------------------------
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd ',' hh:mm:ss a");
                Log.d("dt", "onClick: " + sim.format(new Date()));
                String dateTime = sim.format(new Date());

//------------------------------------


                for (int i = 3; i < parent_linear_layout_for_create_new2.getChildCount() - 2; i++) {
                    View ltV = parent_linear_layout_for_create_new2.getChildAt(i);


                    editTextRoll = ltV.findViewById(R.id.rollFieldId);
                    editTextName = ltV.findViewById(R.id.nameFieldId);
                    rollList.add(editTextRoll.getText().toString().trim());
                    nameList.add(editTextName.getText().toString().trim());


                }

                for (int i = 0; i < rollList.size(); i++) {
                    dateTimeList.add(dateTime);
                    attForList.add(attFor);
                }
                if (Network.isNetworkAvailable(CreateNew2.this)) {


                    dataBaseHelper.insertDataInToRollNameIndexTable(dateTime, attFor);
                    dataBaseHelper.insertRollNameIntoLocalStorage(rollList, nameList, attForList, dateTimeList);

                    RollNameIndexModel rollNameIndexModel = new RollNameIndexModel(dateTime, attFor);
                    databaseReferenceForRollNameIndex.child(dateTime).setValue(rollNameIndexModel);
                    RollNameModel rollNameModel = new RollNameModel(rollList, nameList, attForList, dateTimeList);
                    databaseReferenceForRollName.child(dateTime).setValue(rollNameModel);
                    //go to local book
                    Toast.makeText(CreateNew2.this, "Operation Success !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateNew2.this, CreateNew1.class);
                    startActivity(intent);


                } else {
                    dataBaseHelper.insertDataInToRollNameIndexTable(dateTime, attFor);
                    dataBaseHelper.insertRollNameIntoLocalStorage(rollList, nameList, attForList, dateTimeList);

                    dataBaseHelper.insertDataInToRollNameIndexTableFirebase(dateTime, attFor);
                    dataBaseHelper.insertRollNameIntoLocalStorageFirebase(rollList, nameList, attForList, dateTimeList);
                    //go to local book
                    Toast.makeText(CreateNew2.this, "Operation Success !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateNew2.this, CreateNew1.class);
                    startActivity(intent);


                }


            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNew2.this, CreateNew1.class);
                Bundle bundle = new Bundle();
                bundle.putString("attFor", attFor);
                bundle.putInt("pNo", pNo);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });


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
        attFor = bundle.getString("attFor");
        pNo = bundle.getInt("pNo");


    }

    private void handleRollNameFields() {
        for (int i = 1; i <= pNo; i++) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View fieldView = layoutInflater.inflate(R.layout.field_for_roll_name, null);

            parent_linear_layout_for_create_new2.addView(fieldView, parent_linear_layout_for_create_new2.getChildCount() - 2);
            editTextRoll = fieldView.findViewById(R.id.rollFieldId);
            editTextName = fieldView.findViewById(R.id.nameFieldId);
            editTextRoll.setText("" + i);


        }

    }

    private void setValues() {
        textViewAttFor.setText(attFor);
        textViewPersonNo.setText("" + pNo);

    }

    private void initOthers() {


        dataBaseHelper = new DataBaseHelper(this);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        databaseReferenceForRollNameIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollnameIndex);
        databaseReferenceForRollName = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollname);
        rollList = new ArrayList<>();
        nameList = new ArrayList<>();
        attForList = new ArrayList<>();
        dateTimeList = new ArrayList<>();
        myBroadcastReceiver = new MyBroadcastReceiver();

    }

    //------------------------
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

    //-----------------------------
    private void initView() {
        textViewAttFor = findViewById(R.id.textAttendancesForId);
        textViewPersonNo = findViewById(R.id.textPersonNoId);
        saveBtn = findViewById(R.id.save_button);
        backBtn = findViewById(R.id.back_btnId);
        parent_linear_layout_for_create_new2 = findViewById(R.id.parent_linear_layout_for_create_new2);


    }

    public void onDelete(View v) {
        parent_linear_layout_for_create_new2.removeView((View) v.getParent());
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
