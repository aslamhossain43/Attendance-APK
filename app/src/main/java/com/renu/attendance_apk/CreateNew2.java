package com.renu.attendance_apk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateNew2 extends AppCompatActivity {
    DatabaseReference databaseReferenceForRollName,databaseReferenceForRollNameIndex;
    private DataBaseHelper dataBaseHelper;
    private TextView textViewAttFor, textViewPersonNo;
    private Button saveBtn, backBtn;
    private LinearLayout parent_linear_layout_for_create_new2;
    private EditText editTextRoll, editTextName;
    private String attFor;
    private int pNo;
    private List<String> rollList, nameList,attForList,dateTimeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new2);


        getValuesFromIntent();
        initView();
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

                for (int i=0;i<rollList.size();i++){
                    dateTimeList.add(dateTime);
                    attForList.add(attFor);
                }

               // if (Network.isNetworkAvailable(CreateNew2.this)) {
                  /*  RollNameIndexModel rollNameIndexModel=new RollNameIndexModel(dateTime,attFor);
                    databaseReferenceForRollNameIndex.child(dateTime).setValue(rollNameIndexModel);

                    RollNameModel rollNameModel = new RollNameModel(rollList, nameList,attForList,dateTimeList);
                    databaseReferenceForRollName.child(dateTime).setValue(rollNameModel);

                    Log.d("nn", "addValuesToList:  Neeeeeeeeeeeeeeeeet");
*/

              // }else {
                    Log.d("rr", "onClick: "+rollList);
                    Log.d("rr", "onClick: "+nameList);
                    Log.d("rr", "onClick: "+attForList);
                    Log.d("rr", "onClick: "+dateTimeList);
                    dataBaseHelper.insertRollNameIntoLocalStorage(rollList,nameList,attForList,dateTimeList);
                    dataBaseHelper.insertDataInToRollNameIndexTable(dateTime,attFor);
                    Log.d("nn", "addValuesToList:  Nooooooo Neeeeeeet");

               // }


            }
        });


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
        dataBaseHelper=new DataBaseHelper(this);
        databaseReferenceForRollName = FirebaseDatabase.getInstance().getReference("rollnames");
        databaseReferenceForRollNameIndex = FirebaseDatabase.getInstance().getReference("rollnamesindex");
        rollList = new ArrayList<>();
        nameList = new ArrayList<>();
        attForList = new ArrayList<>();
        dateTimeList=new ArrayList<>();

    }

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

}
