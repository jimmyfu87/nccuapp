package com.example.nccumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class LogInn extends AppCompatActivity {

    CheckBox checkboxLogIn;
    EditText et_passwordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_inn);

        checkboxLogIn = (CheckBox)findViewById(R.id.checkLogIn);
        et_passwordLogin =(EditText)findViewById(R.id.et_passwordLogin);
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
}
