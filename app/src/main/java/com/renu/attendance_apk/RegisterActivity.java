package com.renu.attendance_apk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private int currentApiVersion;
    //-----------------

    DataBaseHelper dataBaseHelper;
    private EditText editTextUaserName, editTextPassword, editTextConfirmPassword;
    private Button registerBtn;
    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//-------------------------------------------
        //for full screen
        currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }


        //--------------------------------
        initView();
        initOthers();
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, Authentication.class);
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
                    long l = dataBaseHelper.addUser(user, pass);
                    if (l > 0) {

                        Toast.makeText(RegisterActivity.this, "You have registered !", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, Authentication.class);
                        startActivity(intent);

                    } else {
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
    //----------------------------------------------------
    // for full screen
    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
