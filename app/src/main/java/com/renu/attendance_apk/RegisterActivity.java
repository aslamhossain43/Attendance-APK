package com.renu.attendance_apk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterActivity extends AppCompatActivity {
    private int currentApiVersion;
    //-----------------
    int SDK_INT = android.os.Build.VERSION.SDK_INT;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;
    private EditText editTextEmail, editTextPhone, editTextPassword, editTextConfirmPassword;
    private Button registerBtn;
    TextView textViewLogin;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

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

                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String pass = editTextPassword.getText().toString().trim();
                String confirmPass = editTextConfirmPassword.getText().toString().trim();

                dataBaseHelper.deleteAllValuesFromAllTables();


                if (isValidMail(email) && isValidMobile(phone) && (pass.length() >= 8 && pass.length() <= 100)) {

                    if (pass.equals(confirmPass)) {
                        if (Network.isNetworkAvailable(RegisterActivity.this)) {


                            sendEmail(email, phone, pass);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Your network is not connected !" +
                                    " See the settings", Toast.LENGTH_SHORT).show();
                        }
                        long login = dataBaseHelper.addIntoLogin(phone, pass);
                        long l = dataBaseHelper.addUser(email, phone, pass);
                        long ll = dataBaseHelper.addWholeInformation(email, phone, pass);

                        if (l > 0 && ll > 0 && login > 0) {

                            Toast.makeText(RegisterActivity.this, "You have registered !", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, Authentication.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Error !", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(RegisterActivity.this, "Password not matching ! Try again", Toast.LENGTH_LONG).show();
                    }


                }
                if (!isValidMail(email)) {
                    Toast.makeText(RegisterActivity.this, "Email Address Not Valid !", Toast.LENGTH_SHORT).show();
                }
                if (!isValidMobile(phone)) {
                    Toast.makeText(RegisterActivity.this, "Phone Number Not Valid !", Toast.LENGTH_SHORT).show();
                }
                if (!(pass.length() >= 8 && pass.length() <= 100)) {
                    Toast.makeText(RegisterActivity.this, "Insert Minimum 8 Characters And Maximum 100 Password !", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendEmail(String email, String phone, String pass) {
        final String fromEmail = "studentsfundbd@gmail.com";
        final String toEmail = email;

        final String password = "ecehstuaslam43";
        final String subject = "V.V.I Documents To Access Attendance Application's\n Previous Information";
        final String msg = "You have to store these information to access Attendance application's previous data : \n" +
                "Email : " + email + ", Phone : " + phone + ", Password : " + pass;


        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //----------------------------------
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(fromEmail, password);
                        }
                    });

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(toEmail));
                message.setSubject(subject);
                message.setText(msg);


                Transport.send(message);

            } catch (MessagingException e) {
                throw new RuntimeException(e);

            }


        }


    }

    private void initOthers() {
        dataBaseHelper = new DataBaseHelper(this);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        myBroadcastReceiver = new MyBroadcastReceiver();
    }

    private void initView() {


        editTextEmail = findViewById(R.id.editTextEmailId);
        editTextPhone = findViewById(R.id.editTextPhoneId);
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
