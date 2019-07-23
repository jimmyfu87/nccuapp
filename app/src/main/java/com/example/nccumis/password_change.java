package com.example.nccumis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class password_change<ModifyPswActivity> extends AppCompatActivity {
    private Button btn_lastPage;
    private Button btn_confirm;
    private EditText ex_password;
    private EditText new_password;
    private EditText check_password;
    private TextView et_wrongPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        ex_password = findViewById(R.id.ex_password);
        new_password = findViewById(R.id.new_password);
        check_password = findViewById(R.id.check_password);
        et_wrongPassword = findViewById(R.id.et_wrongPassword);

       // ex_password.addTextChangedListener(changeTextWatcher);
       // new_password.addTextChangedListener(changeTextWatcher);
        //check_password.addTextChangedListener(changeTextWatcher);


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
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ex_password.getText().toString().isEmpty() && new_password.getText().toString().isEmpty() && check_password.getText().toString().isEmpty()) {
                    et_wrongPassword.setText("請趕快填空格");
                }else if(new_password.getText().toString().isEmpty()){
                    et_wrongPassword.setText("輸入新密碼");
                }else if(check_password.getText().toString().isEmpty()) {
                    et_wrongPassword.setText("確認新密碼");
                }else if(ex_password.getText().toString().equals("12345") && new_password.getText().toString().isEmpty()){
                    et_wrongPassword.setText("輸入新密碼");
                }else if(ex_password.getText().toString().isEmpty() && new_password.getText().toString().equals(check_password.getText().toString())){
                    et_wrongPassword.setText("請輸入舊密碼");
               // }else if(ex_password.getText().toString().isEmpty() && new_password.getText().toString().isEmpty() && check_password.getText().toString().isEmpty()) {
                   // et_wrongPassword.setText("趕快填空格!!!");
                }else if(ex_password.getText().toString().equals("12345") && !new_password.getText().toString().equals(check_password.getText().toString())){
                    et_wrongPassword.setText("請盡速確認密碼");
                }else if(!ex_password.getText().toString().equals("12345") && new_password.getText().toString().equals(check_password.getText().toString())){
                    et_wrongPassword.setText("舊密碼錯誤");
                }
                else if (ex_password.getText().toString().equals("12345") && new_password.getText().toString().equals(check_password.getText().toString())) {
                    Intent intent = new Intent(password_change.this, Settings.class);
                    startActivity(intent);
                    Toast.makeText(password_change.this,"密碼已更新",Toast.LENGTH_SHORT).show();
                }

            }



        });


    }














    //private TextWatcher changeTextWatcher = new TextWatcher() {
       // @Override
       // public void beforeTextChanged(CharSequence s, int start, int count, int after) {

       // }

        //@Override
       // public void onTextChanged(CharSequence s, int start, int before, int count) {
        //    String ex_passwordInput = ex_password.getText().toString().trim();
        //    String new_passwordInput = new_password.getText().toString().trim();
        //    String check_passwordInput = check_password.getText().toString().trim();

        //    btn_confirm.setEnabled(!ex_passwordInput.isEmpty() && !new_passwordInput.isEmpty() && !check_passwordInput.isEmpty());

       // }

       // @Override
       // public void afterTextChanged(Editable s) {

       // }

   // };
   // private View.OnClickListener ClickIntHere = new View.OnClickListener() {

        //@Override
        //public void onClick(View v) {
          //  final String expassword = ex_password.getText().toString();
         //   final String newpassword = new_password.getText().toString();
         //   final String checkpassword = check_password.getText().toString();

         //   if (new_password.getText().toString().isEmpty()) {
         //       et_wrongPassword.setText("請輸入新密碼");
          //  } else if (new_password.getText().toString().equals(check_password.getText().toString())) {
          //      et_wrongPassword.setText("確定變更密碼");
          //  }


      //  }
   // };
    }
