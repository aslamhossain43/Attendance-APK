
package com.renu.attendance_apk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Update extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex;

    private LinearLayout updateParentLinearLayout;
    private EditText editTextForId, editTextForName, editTextForPAOff;
    private Spinner spinner;
    private String roll, name, paoff, dateTime, position, attFor;
    private Button updateButton;
    private MyBroadcastReceiver myBroadcastReceiver;
    DataBaseHelper dataBaseHelper;
    String uuidForAtt, uuidForAttIndex;
    SQLiteDatabase sqLiteDatabase;
    private static final String PERCENTAGE_ATT_FOR = "attfor";
    private static final String PERCENTAGE_ROLL = "roll";
    private static final String TEST_TABLE = "test";


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
        setContentView(R.layout.activity_update);


        getIntentValues();
        initView();

        getWholeInformation();

        initOthers();
        handleLayoutInflater();


    }

    private void handleLayoutInflater() {


        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View liv = layoutInflater.inflate(R.layout.update_specific_layout, null);
        updateParentLinearLayout.addView(liv, updateParentLinearLayout.getChildCount());
        editTextForId = liv.findViewById(R.id.editUpdateSpecificLayoutForIdId);
        editTextForName = liv.findViewById(R.id.editUpdateSpecificLayoutForNameId);
        spinner = liv.findViewById(R.id.updateSpecificLayoutForspinnerId);
        editTextForId.setText(roll);
        editTextForName.setText(name);
        updateButton = liv.findViewById(R.id.updateButtonId);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rollFromEditText = editTextForId.getText().toString().trim();
                String nameFromEditText = editTextForName.getText().toString().trim();
                String paoffFromSpinner = spinner.getSelectedItem().toString().trim();

                databaseReferenceForattendances.child(dateTime).child("rollList").child(position).setValue(rollFromEditText);
                databaseReferenceForattendances.child(dateTime).child("nameList").child(position).setValue(nameFromEditText);
                databaseReferenceForattendances.child(dateTime).child("attendanceList").child(position).setValue(paoffFromSpinner);
                updatePercentage(roll, attFor, paoff, rollFromEditText, paoffFromSpinner);


            }
        });


    }

    private void updatePercentage(String oldRoll, String attFor, String oldPaoff, String newRollFromEditText, String paoffFromSpinner) {

        String q = "SELECT * FROM " + TEST_TABLE + " WHERE " + PERCENTAGE_ATT_FOR + " = '" + attFor + "'" + " AND " + PERCENTAGE_ROLL + " = '" + oldRoll + "'";

        Cursor cursor = sqLiteDatabase.rawQuery(q, null);

        String gettingRoll = null;
        int gettingDaySum = 0;
        int gettingPSum = 0;
        int gettingASum = 0;
        int gettingDay = 0, gettingP = 0, gettingA = 0;
        int gettingPercentSum = 0;
        int gettingPercent;


        while (cursor.moveToNext()) {
            gettingRoll = cursor.getString(1);
            gettingDay = cursor.getInt(2);
            gettingP = cursor.getInt(3);
            gettingA = cursor.getInt(4);
            gettingPercent = cursor.getInt(5);


        }


        if (paoff.equals("Off")) {

            if (paoffFromSpinner.equals("P")) {
                gettingDaySum = gettingDay;
                gettingPSum = gettingP + 1;
                gettingASum = gettingA;


                try {

                    gettingPercentSum = gettingPSum * 100 / gettingDaySum;
                } catch (ArithmeticException ae) {
                    gettingPercentSum = 0;
                }

            }
            if (paoffFromSpinner.equals("A")) {
                gettingDaySum = gettingDay;
                gettingASum = gettingA + 1;
                gettingPSum = gettingP;
                try {

                    gettingPercentSum = gettingPSum * 100 / gettingDaySum;
                } catch (ArithmeticException ae) {
                    gettingPercentSum = 0;
                }

            }
            if (paoffFromSpinner.equals("Off")) {
                gettingDaySum = gettingDay;
                gettingASum = gettingA;
                gettingPSum = gettingP;
                try {

                    gettingPercentSum = gettingPSum * 100 / gettingDaySum;
                } catch (ArithmeticException ae) {
                    gettingPercentSum = 0;
                }

            }


        }

        if (paoff.equals("P")) {
            if (paoffFromSpinner.equals("P")) {
                gettingDaySum = gettingDay;
                gettingPSum = gettingP;
                gettingASum = gettingA;
                try {

                    gettingPercentSum = gettingPSum * 100 / gettingDaySum;
                } catch (ArithmeticException ae) {
                    gettingPercentSum = 0;
                }

            }

            if (paoffFromSpinner.equals("A")) {
                gettingDaySum = gettingDay;
                gettingASum = gettingA + 1;
                gettingPSum = gettingP - 1;
                try {

                    gettingPercentSum = gettingPSum * 100 / gettingDaySum;
                } catch (ArithmeticException ae) {
                    gettingPercentSum = 0;
                }

            }
            if (paoffFromSpinner.equals("Off")) {
                gettingDaySum = gettingDay;
                gettingASum = gettingA + 0;
                gettingPSum = gettingP - 1;
                try {

                    gettingPercentSum = gettingPSum * 100 / gettingDaySum;
                } catch (ArithmeticException ae) {
                    gettingPercentSum = 0;
                }

            }


        }
        if (paoff.equals("A")) {


            if (paoffFromSpinner.equals("P")) {
                gettingDaySum = gettingDay;
                gettingPSum = gettingP + 1;
                gettingASum = gettingA - 1;
                try {

                    gettingPercentSum = gettingPSum * 100 / gettingDaySum;
                } catch (ArithmeticException ae) {
                    gettingPercentSum = 0;
                }

            }
            if (paoffFromSpinner.equals("A")) {
                gettingDaySum = gettingDay;
                gettingASum = gettingA;
                gettingPSum = gettingP;
                try {

                    gettingPercentSum = gettingPSum * 100 / gettingDaySum;
                } catch (ArithmeticException ae) {
                    gettingPercentSum = 0;
                }

            }

            if (paoffFromSpinner.equals("Off")) {
                gettingDaySum = gettingDay;
                gettingASum = gettingA - 1;
                gettingPSum = gettingP + 0;
                try {

                    gettingPercentSum = gettingPSum * 100 / gettingDaySum;
                } catch (ArithmeticException ae) {
                    gettingPercentSum = 0;
                }

            }


        }


        dataBaseHelper.updatePercentage(oldRoll, attFor, gettingRoll, gettingDaySum, gettingPSum, gettingASum, gettingPercentSum);
        BackgroundUpdate.getInstance().handlePercentage(Update.this);





        Toast.makeText(Update.this, "Update Success !", Toast.LENGTH_SHORT).show();
        //go to attendancesindex
        Intent intent = new Intent(Update.this, SpecificAttendancesFromFirebase.class);
        Bundle bundle = new Bundle();
        bundle.putString("attFor", attFor);
        bundle.putString("dt", dateTime);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);


    }

    private void getIntentValues() {
        Bundle bundle = getIntent().getExtras();
        roll = bundle.getString("roll");
        name = bundle.getString("name");
        paoff = bundle.getString("paoff");
        dateTime = bundle.getString("dateTime");
        position = bundle.getString("position");
        attFor = bundle.getString("attFor");


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


    private void initOthers() {
        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAtt);
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAttIndex);
        myBroadcastReceiver = new MyBroadcastReceiver();

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

    private void initView() {
        updateParentLinearLayout = findViewById(R.id.updateParentLinearLayoutId);
        editTextForId = findViewById(R.id.editUpdateSpecificLayoutForIdId);
        editTextForName = findViewById(R.id.editUpdateSpecificLayoutForNameId);
        spinner = findViewById(R.id.updateSpecificLayoutForspinnerId);

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
