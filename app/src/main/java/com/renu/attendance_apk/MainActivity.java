package com.renu.attendance_apk;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    private EditText editTextForMain, editTextForField;
    private Button add_FieldButton,add_To_Firebase;
    private Spinner spinnerForMain, spinnerForField;
    int c = 1;
    DatabaseReference databaseReference;
List<String>roolList;
List<String>attendanceList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initOthers();


        add_FieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean key=false;

                if (parentLinearLayout.getChildCount() == 3) {
                    View ltoV = parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 2);


                    Log.d("vv", "onClick: " + editTextForMain.getText());
                    Log.d("vv", "onClick: " + spinnerForMain.getSelectedItem().toString());
                addValuesToList(editTextForMain.getText().toString().trim(),spinnerForMain.getSelectedItem().toString().trim(),key);



                }
                if (parentLinearLayout.getChildCount() >= 4) {
                    View ltV = parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 3);


                    editTextForField = ltV.findViewById(R.id.rollForFieldId);
                    spinnerForField = ltV.findViewById(R.id.spinnerForFieldId);


                    Log.d("vv", "onClick: " + editTextForField.getText());
                    Log.d("vv", "onClick: " + spinnerForField.getSelectedItem().toString());
                    addValuesToList(editTextForField.getText().toString().trim(),spinnerForField.getSelectedItem().toString().trim(),key);



                }


                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.field, null);
                // Add the new row before the add field button.

                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 2);
                editTextForField = rowView.findViewById(R.id.rollForFieldId);
                spinnerForField = rowView.findViewById(R.id.spinnerForFieldId);
                c++;
                editTextForField.setText("" + c);


            }
        });
        add_To_Firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean key=true;
                if (parentLinearLayout.getChildCount() >= 4) {
                    View ltV = parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 3);


                    editTextForField = ltV.findViewById(R.id.rollForFieldId);
                    spinnerForField = ltV.findViewById(R.id.spinnerForFieldId);


                    Log.d("vv", "onClick: " + editTextForField.getText());
                    Log.d("vv", "onClick: " + spinnerForField.getSelectedItem().toString());
                    addValuesToList(editTextForField.getText().toString().trim(),spinnerForField.getSelectedItem().toString().trim(),key);



                }
            }
        });



    }

    private void addValuesToList(String roll, String attendance,boolean key) {


        roolList.add(roll);
        attendanceList.add(attendance);

        Log.d("rr", "addValuesToList: "+roolList);
        Log.d("aa", "addValuesToList: "+attendanceList);
        if (key==true){

            if (Network.isNetworkAvailable(this)){

                String pushKey=databaseReference.push().getKey();
                AttendanceModel attendanceModel=new AttendanceModel(roolList,attendanceList);
                databaseReference.child(pushKey).setValue(attendanceModel);
            }else {


            }
        }


    }

    private void initOthers() {

        databaseReference = FirebaseDatabase.getInstance().getReference("attendance");
        roolList=new ArrayList<>();
        attendanceList=new ArrayList<>();

    }

    private void initView() {

        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        editTextForMain = findViewById(R.id.rollForMainId);
        spinnerForMain = findViewById(R.id.spinnerForMainId);
        add_FieldButton = findViewById(R.id.add_field_button);
        add_To_Firebase = findViewById(R.id.add_to_firebase_btnId);
        editTextForMain.setText("" + c);
    }


    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }
}
