package com.renu.attendance_apk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ManageForAttPersonPre extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;
    private ListView listViewForPersonPre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_for_att_person_pre);

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


        CustomAdupterForManagingLocalAttIndexPre customAdupterForLocalAttIndexPre = new CustomAdupterForManagingLocalAttIndexPre(this, sAtt, sDateTime);
        listViewForPersonPre.setAdapter(customAdupterForLocalAttIndexPre);
        listViewForPersonPre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent intent=new Intent(ManageForAttPersonPre.this,ManageForAttPerson.class);
                Bundle bundle=new Bundle();
                Log.d("dd", "onItemClick: DATE : "+sDateTime[position]);

                bundle.putString("dateTime",sDateTime[position]);
                bundle.putString("attFor",sAtt[position]);
                intent.putExtras(bundle);
                startActivity(intent);










            }
        });

    }

    private void initOthers() {
        dataBaseHelper = new DataBaseHelper(this);

    }

    private void initView() {
        listViewForPersonPre = findViewById(R.id.managePersonInformarionListViewId);

    }


}
