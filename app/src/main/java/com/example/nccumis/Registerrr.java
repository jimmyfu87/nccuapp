package com.example.nccumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Registerrr extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerrr);

        EditText et_userName = (EditText)findViewById(R.id.et_userName);
        EditText et_useAccount = (EditText)findViewById(R.id.et_userAccount);
        EditText et_userPhone = (EditText)findViewById(R.id.et_userPhone);
        EditText et_userEmail = (EditText)findViewById(R.id.et_userEmail);
        EditText et_userPassword1 = (EditText)findViewById(R.id.et_userPassword1);
        EditText et_userPasswaord2 = (EditText)findViewById(R.id.et_userPassword2);
        EditText et_userBirth = (EditText)findViewById(R.id.et_userBirth);
        Button btn_checkRegister = (Button) findViewById(R.id.btn_checkRegister);
    }


    }

