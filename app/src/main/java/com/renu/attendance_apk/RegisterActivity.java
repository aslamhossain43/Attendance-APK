package com.renu.attendance_apk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    private EditText editTextUaserName, editTextPassword, editTextConfirmPassword;
    private Button registerBtn;
    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        initView();
        initOthers();
textViewLogin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(RegisterActivity.this,Authentication.class);
        startActivity(intent);
    }
});
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = editTextUaserName.getText().toString().trim();
                String pass = editTextPassword.getText().toString().trim();
                String confirmPass = editTextConfirmPassword.getText().toString().trim();

                if (pass.equals(confirmPass)) {
                    long l=dataBaseHelper.addUser(user, pass);
                    if (l>0){

                        Toast.makeText(RegisterActivity.this, "You have registered !", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, Authentication.class);
                        startActivity(intent);

                    }else {
                        Toast.makeText(RegisterActivity.this, "Registration Error !", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(RegisterActivity.this, "Password not matching ! Try again", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    private void initOthers() {
        dataBaseHelper = new DataBaseHelper(this);
    }

    private void initView() {


        editTextUaserName = findViewById(R.id.editTextUserNameId);
        editTextPassword = findViewById(R.id.editTextPasswordId);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPasswordId);
        registerBtn = findViewById(R.id.registerBtnid);
        textViewLogin = findViewById(R.id.textViewForLoginId);

    }
}
