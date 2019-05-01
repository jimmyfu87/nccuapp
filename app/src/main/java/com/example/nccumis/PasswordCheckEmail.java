package com.example.nccumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class PasswordCheckEmail extends AppCompatActivity {

    private Button btn_sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_check_email);

        btn_sendEmail=(Button) findViewById(R.id.btn_sendEmail);
    }

    public void JumpToLogInn(){
        Intent intent = new Intent();
    }
}
