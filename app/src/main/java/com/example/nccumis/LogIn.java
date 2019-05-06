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

public class LogIn extends AppCompatActivity {

    private CheckBox checkboxLogIn;
    private EditText et_passwordLogin;
    private Button btn_forgetPassword;
    private Button btn_registerAgain;
    private Button loginHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        checkboxLogIn = (CheckBox)findViewById(R.id.checkLogIn);
        et_passwordLogin =(EditText)findViewById(R.id.et_passwordLogin);
        btn_forgetPassword=(Button)findViewById(R.id.btn_forgetPassword);
        btn_registerAgain = (Button)findViewById(R.id.btn_registerAgain);
        loginHome = (Button) findViewById(R.id.loginHome);

        //切換到註冊頁面
        btn_registerAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpToRegistr();
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

        //登入首頁
        loginHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAccountPassword()){
                    jumpToHome();
                }
            }
        });

    }
//切換到驗證密碼的頁面
    public void JumpToPasswordCheckEmail(){
        Intent intent= new Intent(LogIn.this,PasswordCheckEmail.class);
        startActivity(intent);
    }
    //切換到註冊頁面
    public void JumpToRegistr(){
        Intent intent= new Intent(LogIn.this, Register.class);
        startActivity(intent);
    }

    //////////確認使用者輸入帳密，待補////////////////
    public boolean checkAccountPassword(){
        return true;
    }

    public void jumpToHome(){
        Intent intent = new Intent(LogIn.this,Home.class);
        startActivity(intent);
    }
}
