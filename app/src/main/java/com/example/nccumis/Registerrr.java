package com.example.nccumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class Registerrr extends AppCompatActivity {

    private EditText et_userName = (EditText)findViewById(R.id.et_userName);
    private EditText et_userAccount = (EditText)findViewById(R.id.et_userAccount);
    private EditText et_userPhone = (EditText)findViewById(R.id.et_userPhone);
    private EditText et_userEmail = (EditText)findViewById(R.id.et_userEmail);
    private EditText et_userPassword1 = (EditText)findViewById(R.id.et_userPassword1);
    private EditText et_userPassword2 = (EditText)findViewById(R.id.et_userPassword2);
    private EditText et_userBirth = (EditText)findViewById(R.id.et_userBirth);
    private Button btn_checkRegister = (Button) findViewById(R.id.btn_checkRegister);
    private CheckBox checkBoxPassword1 = (CheckBox) findViewById(R.id.checkBoxPassword1);
    private CheckBox checkBoxPassword2 = (CheckBox) findViewById(R.id.checkBoxPassword2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerrr);

        checkBoxPassword1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_userPassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    et_userPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        checkBoxPassword2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_userPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    et_userPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btn_checkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if()
            }
        });

    }

   public boolean isInputEmpty (EditText editText){
        if(editText.getText().toString().isEmpty()){
            editText.setError("不能為空白");
            return false;
        }else{
            return true;
        }

   }


    }

