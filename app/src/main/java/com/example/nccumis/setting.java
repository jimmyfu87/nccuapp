package com.example.nccumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;


public class setting extends AppCompatActivity {

    private EditText et_predictAmount;
    private CheckBox ckbox_GLORY;
    private CheckBox ckbox_Green;
    private CheckBox ckbox_Wonderful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        et_predictAmount =(EditText)findViewById(R.id.et_predictAmount);
        ckbox_GLORY =(CheckBox)findViewById(R.id.ckbox_GLORY);
        ckbox_Green =(CheckBox)findViewById(R.id.ckbox_Green);
        ckbox_Wonderful=(CheckBox)findViewById(R.id.ckbox_Wonderful);


    }
}

