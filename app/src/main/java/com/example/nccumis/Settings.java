package com.example.nccumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class Settings extends AppCompatActivity {

    private EditText et_predictAmount;
    private CheckBox ckbox_GLORY;
    private CheckBox ckbox_Green;
    private CheckBox ckbox_Wonderful;
    private EditText et_Wonderful;
    private EditText et_GREEN;
    private EditText et_GLORY;
    private Button btn_setToHome;
    private Button password_change;
    private Button book_management;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ckbox_GLORY =(CheckBox)findViewById(R.id.ckbox_GLORY);
        ckbox_Green =(CheckBox)findViewById(R.id.ckbox_Green);
        ckbox_Wonderful=(CheckBox)findViewById(R.id.ckbox_Wonderful);
        et_GLORY =(EditText)findViewById(R.id.et_GLORY);
        et_GREEN =(EditText)findViewById(R.id.et_GREEN);
        et_Wonderful =(EditText)findViewById(R.id.et_Wonderful);
        btn_setToHome =(Button)findViewById(R.id.btn_setToHome);
        book_management=(Button)findViewById(R.id.book_management) ;
        password_change=(Button)findViewById(R.id.password_change) ;


        password_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gopasswordchange();
            }

            public void gopasswordchange() {
                Intent intent = new Intent(Settings.this,password_change.class);
                startActivity(intent);
            }
        });

        btn_setToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromSettingsToHome();
            }
        });

        book_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gobookmanagement();
            }
            public void gobookmanagement(){
                Intent intent = new Intent(Settings.this,BookManage.class);
                startActivity(intent);
            }
        });


    }
    public void fromSettingsToHome(){
        Intent intent = new Intent(Settings.this,Home.class);
        startActivity(intent);
    }




}



