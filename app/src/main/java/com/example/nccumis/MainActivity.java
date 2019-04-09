package com.example.nccumis;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.misproject.R;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout textInputUserName;
    private TextInputLayout textInputPhone;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputUserId;
    private TextInputLayout textInputUserBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputUserName = findViewById(R.id.text_input_userName);
        textInputPhone = findViewById(R.id.text_input_phone);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputPassword = findViewById(R.id.text_input_password);
        textInputUserId = findViewById(R.id.text_input_userId);
        textInputUserBirth = findViewById(R.id.text_input_userBirth);
    }
    private boolean validateUserNamel() {
        String userNameInput = textInputUserName.getEditText().getText().toString().trim();

        if (userNameInput.isEmpty()) {
            textInputUserName.setError("不能空白");
            return false;
        } else {
            textInputUserName.setError(null);
            return true;
        }
    }
    private boolean validatePhone() {
        String PhoneInput = textInputPhone.getEditText().getText().toString().trim();

        if (PhoneInput.isEmpty()) {
            textInputPhone.setError("不能空白");
            return false;
        } else {
            textInputPhone.setError(null);
            return true;
        }
    }
    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("不能空白");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }
    private boolean validatePasswordl() {
        String PasswordInput = textInputPassword.getEditText().getText().toString().trim();

        if (PasswordInput.isEmpty()) {
            textInputPassword.setError("不能空白");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }
    private boolean validateUserId() {
        String userIdInput = textInputUserId.getEditText().getText().toString().trim();

        if (userIdInput.isEmpty()) {
            textInputUserId.setError("不能空白");
            return false;
        } else {
            textInputUserId.setError(null);
            return true;
        }
    }
    private boolean validateUserBirth() {
        String userBithInput = textInputUserBirth.getEditText().getText().toString().trim();

        if (userBithInput.isEmpty()) {
            textInputUserBirth.setError("不能空白");
            return false;
        } else {
            textInputUserBirth.setError(null);
            return true;
        }
    }

        public void confirmInput (View v){
           if(!validateUserNamel() | !validatePhone() | !validateEmail() | !validatePasswordl() | !validateUserId() | !validateUserBirth()){
               return;
           }
           String input  ="使用者名稱: "+textInputUserName.getEditText().getText().toString();
            input += "\n";
            input +="手機: "+textInputPhone.getEditText().getText().toString();
            input += "\n";
            input +="郵件信箱: "+textInputEmail.getEditText().getText().toString();
           input += "\n";
           input +="Email: "+textInputPassword.getEditText().getText().toString();

            Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
        }
    }

