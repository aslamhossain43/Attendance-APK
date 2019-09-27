package com.renu.attendance_apk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {
    private Button btnForLocalAttTypeIndex, btnForLocalAttPerson, btnForAttIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();


        btnForLocalAttTypeIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, ManageForLocalAttTypeIndex.class);
                startActivity(intent);
            }
        });
        btnForLocalAttPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, ManageForAttPerson.class);
                startActivity(intent);
            }
        });
        btnForAttIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, ManageForAttIndex.class);
                startActivity(intent);
            }
        });


    }

    private void initView() {

        btnForLocalAttTypeIndex = findViewById(R.id.btnLocalAttTypeIndexId);
        btnForLocalAttPerson = findViewById(R.id.btnLocalAttPersonId);
        btnForAttIndex = findViewById(R.id.btnAttIndexId);


    }
}
