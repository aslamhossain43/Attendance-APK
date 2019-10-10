package com.renu.attendance_apk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Settings extends AppCompatActivity {

    int SDK_INT = android.os.Build.VERSION.SDK_INT;
    private static final String FIREBASE_URL = "https://attendance-apk.firebaseio.com/";
    private Button btnForLocalAttTypeIndex, btnForLocalAttPerson, btnForAttIndex, btnForPercentageIndex,
            btnAllPersonInfoIndex, btndeleteAttIndex, btnResendId;
    private DatabaseReference databaseReferenceForattendances, databaseReferenceForattendancesIndex,
            databaseReferenceForRollnameIndex, databaseReferenceForRollname, databaseReferenceForPercentage;
    AlertDialog.Builder alertDialogBuilder;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;
    private MyBroadcastReceiver myBroadcastReceiver;
    String uuidForAtt, uuidForAttIndex;


    private static final String WHOLE_INFORMATION_TABLE = "wholeinformations";
    private String emailMobilePassRollnameIndex = null;
    private String emailMobilePassRollname = null;
    private String emailMobilePassAttIndex = null;
    private String emailMobilePassAtt = null;
    private String emailMobilePassTest = null;
    private String emailMobilePass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        getWholeInformation();
        initOthers();


        btnResendId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialogBuilder.setMessage("Do you want to send your backup information to your email ?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if (Network.isNetworkAvailable(Settings.this)) {


                            handEmail();

                        } else {
                            Toast.makeText(Settings.this, "Connect internet !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });


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
                Intent intent = new Intent(Settings.this, ManageForAttPersonPre.class);

                startActivity(intent);
            }
        });

        btnForPercentageIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, ManageSpecificPercentage.class);

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
        btnAllPersonInfoIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.setMessage("Do you want to delete all information from your application ?");
                alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Network.isNetworkAvailable(Settings.this)) {

                           deleteFirstHalf();
                           deleteSecondHalf();


                            dataBaseHelper.deleteAllValuesFromAllTables();

                            Toast.makeText(Settings.this, "You have deleted all information from your application !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Settings.this, RegisterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Settings.this, "Connect internet !", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        btndeleteAttIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialogBuilder.setMessage("Do you want to delete all attendances from your application ?");
                alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Network.isNetworkAvailable(Settings.this)) {


                            databaseReferenceForattendancesIndex.getRef().removeValue();
                            databaseReferenceForattendances.getRef().removeValue();

                            Toast.makeText(Settings.this, "You have deleted all attendances from your app !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Settings.this, ExistRollNames.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                        } else {
                            Toast.makeText(Settings.this, "Connect network !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });


    }

    private void deleteSecondHalf() {
        databaseReferenceForRollname.getRef().removeValue();
        databaseReferenceForattendancesIndex.getRef().removeValue();
        databaseReferenceForattendances.getRef().removeValue();
        databaseReferenceForPercentage.getRef().removeValue();

    }

    private void deleteFirstHalf() {

        databaseReferenceForRollnameIndex.getRef().removeValue();


    }

    private void handEmail() {


        String email = null, phone = null, password = null;
        Cursor cursor1 = dataBaseHelper.getAllRegister();
        while (cursor1.moveToNext()) {
            email = cursor1.getString(1);
            phone = cursor1.getString(2);
            password = cursor1.getString(3);
            sendEmail(email, phone, password);
            Toast.makeText(Settings.this, "Email has been sent successfully !", Toast.LENGTH_SHORT).show();
        }


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

    private void initOthers() {

        databaseReferenceForRollnameIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollnameIndex);
        databaseReferenceForRollname = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassRollname);

        databaseReferenceForattendancesIndex = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAttIndex);
        databaseReferenceForattendances = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassAtt);


        databaseReferenceForPercentage = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL + emailMobilePassTest);


        alertDialogBuilder = new AlertDialog.Builder(this);
        myBroadcastReceiver = new MyBroadcastReceiver();
    }

    private void getWholeInformation() {


        dataBaseHelper = new DataBaseHelper(this);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + WHOLE_INFORMATION_TABLE, null);

        if (cursor.getCount() != 0) {


            while (cursor.moveToNext()) {

                emailMobilePassRollnameIndex = cursor.getString(0);
                emailMobilePassRollname = cursor.getString(1);
                emailMobilePassAttIndex = cursor.getString(2);
                emailMobilePassAtt = cursor.getString(3);
                emailMobilePassTest = cursor.getString(4);
                emailMobilePass = cursor.getString(5);

            }
        }
    }


    private void initView() {

        btnForLocalAttTypeIndex = findViewById(R.id.btnLocalAttTypeIndexId);
        btnForLocalAttPerson = findViewById(R.id.btnLocalAttPersonId);
        btnForAttIndex = findViewById(R.id.btnAttIndexId);
        btnForPercentageIndex = findViewById(R.id.btnPercentageIndexId);
        btnAllPersonInfoIndex = findViewById(R.id.btnAllPersonInfoIndexId);
        btndeleteAttIndex = findViewById(R.id.btndeleteAttIndexId);
        btnResendId = findViewById(R.id.btnResendId);

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
        if (item.getItemId() == R.id.summary) {

            Intent intent = new Intent(this, Percentage.class);

            startActivity(intent);

        }
        if (item.getItemId() == R.id.localAttendances) {
            Intent intent = new Intent(this, ExistRollNames.class);

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
