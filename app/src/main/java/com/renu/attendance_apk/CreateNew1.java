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

public class CreateNew1 extends AppCompatActivity {
    private EditText editTextAttFor,editTextPersonNo;
    private Button nextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new1);


initView();

nextBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String attFor=editTextAttFor.getText().toString().trim();
        String personNo=editTextPersonNo.getText().toString();
        int pNo=Integer.parseInt(personNo);

        Intent intent=new Intent(CreateNew1.this,CreateNew2.class);

        Bundle bundle=new Bundle();
        bundle.putString("attFor",attFor);
        bundle.putInt("pNo",pNo);
        intent.putExtras(bundle);
        startActivity(intent);




    }
});








    }

    private void initView() {

        editTextAttFor=findViewById(R.id.editTextAttForId);
        editTextPersonNo=findViewById(R.id.editTextPersonNoId);
        nextBtn=findViewById(R.id.nextBtnId);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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

        }if (item.getItemId()==R.id.logoutId){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

}
