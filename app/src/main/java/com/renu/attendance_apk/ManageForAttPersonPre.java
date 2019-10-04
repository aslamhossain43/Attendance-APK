package com.renu.attendance_apk;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ManageForAttPersonPre extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;
    private ListView listViewForPersonPre;
    private MyBroadcastReceiver myBroadcastReceiver;

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
                Intent intent = new Intent(ManageForAttPersonPre.this, ManageForAttPerson.class);
                Bundle bundle = new Bundle();

                bundle.putString("dateTime", sDateTime[position]);
                bundle.putString("attFor", sAtt[position]);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });

    }

    private void initOthers() {
        dataBaseHelper = new DataBaseHelper(this);
        myBroadcastReceiver = new MyBroadcastReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(myBroadcastReceiver, intentFilter);
        unregisterReceiver(myBroadcastReceiver);

    }

    private void initView() {
        listViewForPersonPre = findViewById(R.id.managePersonInformarionListViewId);

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


        return super.onOptionsItemSelected(item);
    }
}
