package com.renu.attendance_apk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateForManageAttIndex extends AppCompatActivity {

    DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex;

    private LinearLayout updateParentLinearLayout;
    private EditText editTextForIndex;
    private String dateTime,index;
    private Button updateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_for_manage_att_index);


        getIntentValues();
        initView();
        initOthers();


        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View liv = layoutInflater.inflate(R.layout.update_for_manage_att_index_layout, null);
        updateParentLinearLayout.addView(liv, updateParentLinearLayout.getChildCount());
        editTextForIndex = liv.findViewById(R.id.editUpdateForManageAttIndexId);
        editTextForIndex.setText(index);
        updateButton = liv.findViewById(R.id.updateButtonId);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String index = editTextForIndex.getText().toString().trim();

                databaseReferenceForattendancesIndex.child(dateTime).child("attendanceFor").getRef().setValue(index);
                Intent intent=new Intent(UpdateForManageAttIndex.this,ManageForAttIndex.class);
                startActivity(intent);
            }
        });




    }



    private void initOthers() {
        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReference("attendance");
        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReference("attendanceindex");

    }

    private void initView() {

        updateParentLinearLayout = findViewById(R.id.updateParentLinearLayoutForManageAttIndexId);
        editTextForIndex = findViewById(R.id.editUpdateForManageAttIndexId);

    }

    private void getIntentValues() {

        Bundle bundle = getIntent().getExtras();
        dateTime = bundle.getString("dateTime");
        index=bundle.getString("index");
    }
}
