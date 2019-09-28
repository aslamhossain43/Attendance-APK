package com.renu.attendance_apk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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

        Log.d("ii", "onCreate: infate for local att index");
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


}
