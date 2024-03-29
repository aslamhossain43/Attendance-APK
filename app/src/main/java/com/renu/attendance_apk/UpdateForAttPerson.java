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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UpdateForAttPerson extends AppCompatActivity {




    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";

    DatabaseReference databaseReferenceForRollnameIndex, databaseReferenceForRollName, databaseReferenceForPercentage;


    private static final String WHOLE_INFORMATION_TABLE = "wholeinformations";
    private String emailMobilePassRollnameIndex = null;
    private String emailMobilePassRollname = null;
    private String emailMobilePassAttIndex = null;
    private String emailMobilePassAtt = null;
    private String emailMobilePassTest = null;
    private String emailMobilePass = null;




    private LinearLayout updateParentLinearLayoutForManageAttId;
    private EditText editUpdateForManagePersonNameId, editUpdateForManagePersonRollId;
    private String roll, name, dateTime, index,position;
    private Button updateButton;
    private DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;


    final String ROLL_NAME_TABLE = "rollname";
    final String ROLL_FOR_ROLLNAME = "roll";
    final String NAME_FOR_ROLLNAME = "name";
    final String ATT_FOR_ROLLNAME = "attfor";
    final String DATETIME_FOR_ROLLNAME = "time";
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_for_att_person);


        getIntentValues();
        initView();
        getWholeInformation();
        initOthers();


        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View liv = layoutInflater.inflate(R.layout.update_for_manage_person_att_layout, null);
        updateParentLinearLayoutForManageAttId.addView(liv, updateParentLinearLayoutForManageAttId.getChildCount());
        editUpdateForManagePersonNameId = liv.findViewById(R.id.editUpdateForManagePersonNameId);
        editUpdateForManagePersonRollId = liv.findViewById(R.id.editUpdateForManagePersonRollId);

        editUpdateForManagePersonNameId.setText(name);
        editUpdateForManagePersonRollId.setText(roll);
        updateButton = liv.findViewById(R.id.updateButtonId);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rol = editUpdateForManagePersonRollId.getText().toString().trim();
                String nam = editUpdateForManagePersonNameId.getText().toString().trim();


databaseReferenceForRollName.child(dateTime).child("rollNo").child(position).getRef().setValue(rol);
databaseReferenceForRollName.child(dateTime).child("name").child(position).getRef().setValue(nam);



databaseReferenceForPercentage.child(index).child("rollList").child(""+position).getRef().removeValue();
databaseReferenceForPercentage.child(index).child("rollList").child(""+position).getRef().setValue(rol);



                //--------------------------


                List<String> rollList = new ArrayList<>();
                List<String> nameList = new ArrayList<>();
                List<String> attendancesList = new ArrayList<>();
                List<String> dateTimeList = new ArrayList<>();


                sqLiteDatabase = dataBaseHelper.getWritableDatabase();

                String q = "SELECT * FROM " + ROLL_NAME_TABLE + " WHERE " + DATETIME_FOR_ROLLNAME + " = '" + dateTime + "'";

                Cursor cursor1 = sqLiteDatabase.rawQuery(q, null);
                while (cursor1.moveToNext()) {
                    rollList.add(cursor1.getString(0));
                    nameList.add(cursor1.getString(1));
                    attendancesList.add(cursor1.getString(2));
                    dateTimeList.add(cursor1.getString(3));

                }


                //--------------------------------------------


                if (!rollList.contains(rol)) {
                    Toast.makeText(UpdateForAttPerson.this, "Roll No :" + rol + " has updated successfully !", Toast.LENGTH_LONG).show();
                    dataBaseHelper.updateForRollName(UpdateForAttPerson.this.roll, rol, name, index, dateTime);

                    Intent intent = new Intent(UpdateForAttPerson.this, ManageForAttPerson.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("dateTime", dateTime);
                    bundle.putString("attFor", index);
                    intent.putExtras(bundle);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                } else {

                    Toast.makeText(UpdateForAttPerson.this, "Roll No : " + rol + " exists already !", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(UpdateForAttPerson.this, ManageForAttPerson.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("dateTime", dateTime);
                    bundle.putString("attFor", index);
                    intent.putExtras(bundle);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);


                }





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
    private void initOthers() {
        dataBaseHelper = new DataBaseHelper(this);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
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

        updateParentLinearLayoutForManageAttId = findViewById(R.id.updateParentLinearLayoutForManageAttId);
        updateButton = findViewById(R.id.updateButtonId);

    }

    private void getIntentValues() {
        Bundle bundle = getIntent().getExtras();
        roll = bundle.getString("roll");
        name = bundle.getString("name");
        dateTime = bundle.getString("dateTime");
        index = bundle.getString("attFor");
        position=bundle.getString("position");
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
