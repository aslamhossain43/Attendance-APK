package com.renu.attendance_apk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNew1 extends AppCompatActivity {
    private EditText editTextAttFor, editTextPersonNo;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new1);


        initView();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!editTextAttFor.getText().toString().trim().equals("") && !editTextPersonNo.getText().toString().equals("")) {

                    String attFor = editTextAttFor.getText().toString().trim();
                    String personNo = editTextPersonNo.getText().toString().trim();


                    try {

                        Integer pNo = Integer.parseInt(personNo);
                        if (pNo instanceof Integer) {

                            Intent intent = new Intent(CreateNew1.this, CreateNew2.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("attFor", attFor);
                            bundle.putInt("pNo", pNo);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(CreateNew1.this, "Insert Integer Value !", Toast.LENGTH_LONG).show();

                    }


                } else {
                    Toast.makeText(CreateNew1.this, "Insert valid values", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

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
