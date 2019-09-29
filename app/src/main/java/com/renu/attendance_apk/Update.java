
package com.renu.attendance_apk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Update extends AppCompatActivity {

    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex;

    private LinearLayout updateParentLinearLayout;
    private EditText editTextForId, editTextForName, editTextForPAOff;
    private Spinner spinner;
    private String roll, name, paoff, dateTime, position,attFor;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        getIntentValues();
        initView();
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
                //go to attendancesindex
                Intent intent=new Intent(Update.this,SpecificAttendancesFromFirebase.class);
                Bundle bundle = new Bundle();
                bundle.putString("attFor", attFor);
                bundle.putString("dt", dateTime);
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(Update.this, "Update Success !", Toast.LENGTH_SHORT).show();
            }
        });


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

    private void initOthers() {
        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReference("attendance");
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex");


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
