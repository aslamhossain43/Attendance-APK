package com.renu.attendance_apk;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ManageForLocalAttTypeIndex extends AppCompatActivity {

    private AlertDialog.Builder alertDialogBuilder;
    DataBaseHelper dataBaseHelper;
    private ListView listViewExistAttTypes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_for_local_att_type_index);


        initView();
        initOthers();


        Cursor cursor = dataBaseHelper.getAllDataFromRollNameIndex();
        List<String> stringAttFor = new ArrayList<>();
        List<String> stringDate = new ArrayList<>();
        while (cursor.moveToNext()) {
            stringAttFor.add(cursor.getString(0));
            stringDate.add(cursor.getString(1));
        }

        final String[] sAtt = stringAttFor.toArray(new String[stringAttFor.size()]);
        final String[] sDateTime = stringDate.toArray(new String[stringDate.size()]);

        listViewHandleForLocalAttIndex(sAtt, sDateTime);


    }

    private void listViewHandleForLocalAttIndex(final String[] sAtt, final String[] sDateTime) {


        CustomAdupterForManagingLocalAttIndex customAdupterForLocalAttIndex = new CustomAdupterForManagingLocalAttIndex(ManageForLocalAttTypeIndex.this, sAtt, sDateTime);
        listViewExistAttTypes.setAdapter(customAdupterForLocalAttIndex);
        listViewExistAttTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                alertDialogBuilder.setMessage("Index Name : " + sAtt[position] + "\nDate : " + sDateTime[position]);
                alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataBaseHelper.deleteRollNameIndex(sDateTime[position]);
                        dataBaseHelper.deleteRollNameByDate(sDateTime[position]);
                        fileList();
                        startActivity(getIntent());


                    }
                });
                alertDialogBuilder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(ManageForLocalAttTypeIndex.this, UpdateForManagingLocalAttTypeIndex.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("index", sAtt[position]);
                        bundle.putString("dateTime", sDateTime[position]);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });
                alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


    }

    private void initOthers() {
        alertDialogBuilder = new AlertDialog.Builder(this);
        dataBaseHelper = new DataBaseHelper(this);

    }

    private void initView() {
        listViewExistAttTypes = findViewById(R.id.manageLocalExistanceAttendanceListViewId);
    }


}