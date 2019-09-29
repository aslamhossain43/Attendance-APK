package com.renu.attendance_apk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExistRollNames extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
     private ListView listViewExistAttTypes;
     private MyBroadcastReceiver myBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist_roll_names);

        initView();
        initOthers();


    SQLiteDatabase sqLiteDatabase=dataBaseHelper.getWritableDatabase();
    Cursor cursor=dataBaseHelper.getAllDataFromRollNameIndex();
    List<String>stringAttFor=new ArrayList<>();
    List<String>stringDate=new ArrayList<>();
    while (cursor.moveToNext()){
        stringAttFor.add(cursor.getString(0));
        stringDate.add(cursor.getString(1));
    }

    final String[] sAtt = stringAttFor.toArray(new String[stringAttFor.size()]);
    final String[] sDateTime = stringDate.toArray(new String[stringDate.size()]);
    CustomAdupterForAttendanceTypes customAdupterForAttendanceTypes = new CustomAdupterForAttendanceTypes(ExistRollNames.this, sAtt,sDateTime);
    listViewExistAttTypes.setAdapter(customAdupterForAttendanceTypes);
    listViewExistAttTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ExistRollNames.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("sAtt", sAtt[position]);
            bundle.putString("sDateTime", sDateTime[position]);
            intent.putExtras(bundle);
            startActivity(intent);


        }
    });



    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(myBroadcastReceiver, intentFilter);

    }

    private void initOthers() {
  dataBaseHelper=new DataBaseHelper(this);

myBroadcastReceiver=new MyBroadcastReceiver();
    }

    private void initView() {

        listViewExistAttTypes = findViewById(R.id.existanceAttendanceListViewId);
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

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myBroadcastReceiver);
    }
}
