package com.renu.attendance_apk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AfterLogin extends AppCompatActivity {
    private Button createNewBtn, exist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);


        initView();

        createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterLogin.this, CreateNew1.class);
                startActivity(intent);
            }
        });
        exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterLogin.this, ExistRollNames.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        createNewBtn = findViewById(R.id.createNewBtnId);
        exist = findViewById(R.id.existBtnId);

    }


}
