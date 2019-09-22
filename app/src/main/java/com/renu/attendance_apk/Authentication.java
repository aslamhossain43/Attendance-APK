package com.renu.attendance_apk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Authentication extends AppCompatActivity {
    private EditText editTextUaserName, editTextPassword;
    private Button loginBtn;
    TextView textViewForRegister;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);


        initView();
        initOther();
        textViewForRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Authentication.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = editTextUaserName.getText().toString();
                String pass = editTextPassword.getText().toString();

                Boolean b = dataBaseHelper.checkUser(user, pass);
                if (b == true) {
                    Toast.makeText(Authentication.this, "Successfully logged in", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Authentication.this,AfterLogin.class);
                startActivity(intent);



                } else {
                    Toast.makeText(Authentication.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    private void initOther() {
        dataBaseHelper = new DataBaseHelper(this);
    }

    private void initView() {
        editTextUaserName = findViewById(R.id.editTextUserNameId);
        editTextPassword = findViewById(R.id.editTextPasswordId);
        loginBtn = findViewById(R.id.loginBtnid);
        textViewForRegister = findViewById(R.id.textViewForRegisterId);


    }


}
