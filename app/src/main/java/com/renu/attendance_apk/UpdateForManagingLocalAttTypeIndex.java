package com.renu.attendance_apk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateForManagingLocalAttTypeIndex extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    private LinearLayout updateParentLinearLayout;
    private EditText editTextForIndex;
    private String dateTime, index;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_for_managing_local_att_type_index);


        getIntentValues();
        initView();
        initOthers();

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View liv = layoutInflater.inflate(R.layout.upadte_for_manage_local_att_index_layout, null);
        updateParentLinearLayout.addView(liv, updateParentLinearLayout.getChildCount());
        editTextForIndex = liv.findViewById(R.id.editUpdateForManageLocalAttIndexId);
        editTextForIndex.setText(index);
        updateButton = liv.findViewById(R.id.updateButtonId);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String index = editTextForIndex.getText().toString().trim();
                dataBaseHelper.updateForAttIndex(dateTime, index);
                Intent intent = new Intent(UpdateForManagingLocalAttTypeIndex.this, ManageForLocalAttTypeIndex.class);
                startActivity(intent);
                Toast.makeText(UpdateForManagingLocalAttTypeIndex.this, "Update Success !", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void initOthers() {

        dataBaseHelper = new DataBaseHelper(this);
    }

    private void initView() {
        updateParentLinearLayout = findViewById(R.id.updateParentLinearLayoutForManageLocalAttIndexId);


    }

    private void getIntentValues() {
        Bundle bundle = getIntent().getExtras();
        dateTime = bundle.getString("dateTime");
        index = bundle.getString("index");


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
