package com.renu.attendance_apk;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateNew1 extends AppCompatActivity {
    private EditText editTextAttFor, editTextPersonNo;
    private Button nextBtn;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new1);

        initView();
        initOthers();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!editTextAttFor.getText().toString().trim().equals("") && !editTextPersonNo.getText().toString().equals("")) {

                    String attFor = editTextAttFor.getText().toString().trim();
                    String personNo = editTextPersonNo.getText().toString().trim();



                    if (new DataBaseHelper(CreateNew1.this).checkAttFor(attFor)) {


                        try {

                            Integer pNo = Integer.parseInt(personNo);
                            if (pNo instanceof Integer) {

                                Intent intent = new Intent(CreateNew1.this, CreateNew2.class);

                                Bundle bundle = new Bundle();
                                bundle.putString("attFor", attFor);
                                bundle.putInt("pNo", pNo);
                                intent.putExtras(bundle);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(CreateNew1.this, "Insert Integer Value !", Toast.LENGTH_LONG).show();

                        }


                    }else {
                        Toast.makeText(CreateNew1.this, "Already exist ! Try another", Toast.LENGTH_SHORT).show();
                    }




                } else {
                    Toast.makeText(CreateNew1.this, "Insert valid values", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void initOthers() {
        myBroadcastReceiver = new MyBroadcastReceiver();
    }
//---------------------------
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
//--------------------
    private void initView() {

        editTextAttFor = findViewById(R.id.editTextAttForId);
        editTextPersonNo = findViewById(R.id.editTextPersonNoId);
        nextBtn = findViewById(R.id.nextBtnId);

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
