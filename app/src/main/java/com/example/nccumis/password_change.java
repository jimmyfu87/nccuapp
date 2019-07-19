package com.example.nccumis;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class password_change extends AppCompatActivity {
    private Button btn_lastPage;
    private EditText ex_password;
    private EditText new_password;
    private EditText check_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        ex_password = findViewById(R.id.ex_password);
        new_password = findViewById(R.id.new_password);
        check_password = findViewById(R.id.check_password);

        ex_password.addTextChangedListener(changeTextWatcher);
        new_password.addTextChangedListener(changeTextWatcher);
        check_password.addTextChangedListener(changeTextWatcher);


        btn_lastPage = (Button) findViewById(R.id.btn_lastPage);
        btn_lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backlastpage();
            }

            public void backlastpage() {
                Intent intent = new Intent(password_change.this, Settings.class);
                startActivity(intent);
            }
        });

    }

    private TextWatcher changeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };
}