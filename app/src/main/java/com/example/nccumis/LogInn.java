package com.example.nccumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class LogInn extends AppCompatActivity {

    CheckBox checkboxLogIn;
    EditText et_passwordLogin;
    Button btn_forgetPassword;
    Button btn_registerAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_inn);

        checkboxLogIn = (CheckBox)findViewById(R.id.checkLogIn);
        et_passwordLogin =(EditText)findViewById(R.id.et_passwordLogin);
        btn_forgetPassword=(Button)findViewById(R.id.btn_forgetPassword);
        btn_registerAgain = (Button)findViewById(R.id.btn_registerAgain);

        //切換到註冊頁面
        btn_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpToRegistrrr();
            }
        });

        //切換到驗證密碼頁面
        btn_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpToPasswordCheckEmail();
            }
        });


        //顯示隱藏密碼
        checkboxLogIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_passwordLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    et_passwordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }
//切換到驗證密碼的頁面
    public void JumpToPasswordCheckEmail(){
        Intent intent= new Intent(LogInn.this,PasswordCheckEmail.class);
        startActivity(intent);
    }
    //切換到註冊頁面
    public void JumpToRegistrrr(){
        Intent intent= new Intent(LogInn.this,Registerrr.class);
        startActivity(intent);
    }
}
