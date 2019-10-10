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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdateForManagingLocalAttTypeIndex extends AppCompatActivity {


    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";

    DatabaseReference databaseReferenceForRollnameIndex, databaseReferenceForRollName, databaseReferenceForPercentage;


    private static final String WHOLE_INFORMATION_TABLE = "wholeinformations";
    private String emailMobilePassRollnameIndex = null;
    private String emailMobilePassRollname = null;
    private String emailMobilePassAttIndex = null;
    private String emailMobilePassAtt = null;
    private String emailMobilePassTest = null;
    private String emailMobilePass = null;


    DataBaseHelper dataBaseHelper;
    private LinearLayout updateParentLinearLayout;
    private EditText editTextForIndex;
    private String dateTime, index;
    private Button updateButton;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_for_managing_local_att_type_index);


        getIntentValues();
        initView();
        getWholeInformation();
        initOthers();
        handleUpdate();


    }

    private void handleUpdate() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View liv = layoutInflater.inflate(R.layout.upadte_for_manage_local_att_index_layout, null);
        updateParentLinearLayout.addView(liv, updateParentLinearLayout.getChildCount());
        editTextForIndex = liv.findViewById(R.id.editUpdateForManageLocalAttIndexId);
        editTextForIndex.setText(index);
        updateButton = liv.findViewById(R.id.updateButtonId);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String attFromEditTerxt = editTextForIndex.getText().toString().trim();
                if (dataBaseHelper.checkAttFor(attFromEditTerxt)) {

                    databaseReferenceForRollName.child(dateTime).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd ',' hh:mm:ss a");
                            Log.d("dt", "onClick: " + sim.format(new Date()));
                            String date = sim.format(new Date());


                            RollNameModel rollNameModel = new RollNameModel();
                            List<String> roll = new ArrayList<>();
                            List<String> name = new ArrayList<>();
                            List<String> attFor = new ArrayList<>();
                            List<String> dateTimeList = new ArrayList<>();
                            try {

                                rollNameModel = dataSnapshot.getValue(RollNameModel.class);
                                roll.addAll(rollNameModel.getRollNo());
                                name.addAll(rollNameModel.getName());


                                for (int i = 0; i < roll.size(); i++) {
                                    attFor.add(attFromEditTerxt);
                                    dateTimeList.add(date);
                                }
                            } catch (Exception e) {

                            }
                            if (Network.isNetworkAvailable(UpdateForManagingLocalAttTypeIndex.this)) {


                                dataBaseHelper.insertDataInToRollNameIndexTable(date, attFromEditTerxt);
                                dataBaseHelper.insertRollNameIntoLocalStorage(roll, name, attFor, dateTimeList);
                                databaseReferenceForRollnameIndex.child(date).getRef().setValue(new RollNameIndexModel(date, attFromEditTerxt));
                                databaseReferenceForRollName.child(date).getRef().setValue(new RollNameModel(roll, name, attFor, dateTimeList));

                                Toast.makeText(UpdateForManagingLocalAttTypeIndex.this, "Update success !", Toast.LENGTH_SHORT).show();
                                intentHandle();
                            } else {

                                dataBaseHelper.insertDataInToRollNameIndexTable(date, attFromEditTerxt);
                                dataBaseHelper.insertRollNameIntoLocalStorage(roll, name, attFor, dateTimeList);

                                dataBaseHelper.insertDataInToRollNameIndexTableFirebase(date, attFromEditTerxt);
                                dataBaseHelper.insertRollNameIntoLocalStorageFirebase(roll, name, attFor, dateTimeList);

                                Toast.makeText(UpdateForManagingLocalAttTypeIndex.this, "Update success !", Toast.LENGTH_SHORT).show();
                                intentHandle();
                            }


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {

                    Toast.makeText(UpdateForManagingLocalAttTypeIndex.this, "Already exist ! Try another", Toast.LENGTH_SHORT).show();
                }


            }

        });


    }

    private void intentHandle() {
        Intent intent = new Intent(UpdateForManagingLocalAttTypeIndex.this, ExistRollNames.class);
        startActivity(intent);
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

    private void initOthers() {

        dataBaseHelper = new DataBaseHelper(this);
        myBroadcastReceiver = new MyBroadcastReceiver();
        databaseReferenceForPercentage = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassTest);

        databaseReferenceForRollnameIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollnameIndex);
        databaseReferenceForRollName = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollname);

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
        updateParentLinearLayout = findViewById(R.id.updateParentLinearLayoutForManageLocalAttIndexId);


    }

    private void getIntentValues() {
        Bundle bundle = getIntent().getExtras();
        dateTime = bundle.getString("dateTime");
        index = bundle.getString("index");


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
