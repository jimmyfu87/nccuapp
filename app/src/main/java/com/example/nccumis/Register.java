package com.example.nccumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    private EditText et_userName ;
    private EditText et_userAccount ;
    private EditText et_userPhone ;
    private EditText et_userEmail ;
    private EditText et_userPassword1 ;
    private EditText et_userPassword2 ;
    private EditText et_userBirth ;
    private TextView et_wrongPassword;
    private Button btn_checkRegister ;
    private CheckBox checkBoxPassword1 ;
    private CheckBox checkBoxPassword2 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //被findViewById抓下來的type是view ,要再轉string
        et_userName = findViewById(R.id.et_userName);
         et_userAccount = findViewById(R.id.et_userAccount);
         et_userPhone = findViewById(R.id.et_userPhone);
         et_userEmail = findViewById(R.id.et_userEmail);
         et_userPassword1 = findViewById(R.id.et_userPassword1);
         et_userPassword2 = findViewById(R.id.et_userPassword2);
         et_wrongPassword =findViewById(R.id.et_wrongPassword);
         et_userBirth = findViewById(R.id.et_userBirth);
         btn_checkRegister =  findViewById(R.id.btn_checkRegister);
         checkBoxPassword1 = findViewById(R.id.checkBoxPassword1);
         checkBoxPassword2 =  findViewById(R.id.checkBoxPassword2);

         et_userName.addTextChangedListener(registerTextWatcher );
        et_userAccount.addTextChangedListener(registerTextWatcher );
        et_userPhone.addTextChangedListener(registerTextWatcher );
        et_userEmail.addTextChangedListener(registerTextWatcher );
        et_userPassword1.addTextChangedListener(registerTextWatcher );
        et_userPassword2.addTextChangedListener(registerTextWatcher );
        et_userBirth.addTextChangedListener(registerTextWatcher );

        btn_checkRegister.setOnClickListener(ClickIntHere);

        //讓密碼顯示的打勾框框
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
   }

   //核對密碼


   private View.OnClickListener ClickIntHere = new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           if(et_userPassword1.getText().toString().isEmpty()){
               et_wrongPassword.setText("密碼不能空白");
           }else if(et_userPassword1.getText().toString() .equals(et_userPassword2.getText().toString())){
               et_wrongPassword.setText("密碼正確");
           }else {
               et_wrongPassword.setText("密碼不一致");
           }

       }
   };
    //空白沒輸入東西的話，會警告你





    //空白沒輸入東西的話，會警告你
   private TextWatcher registerTextWatcher = new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence s, int start, int count, int after) {
       }

       @Override
       public void onTextChanged(CharSequence s, int start, int before, int count) {
           String usernameInput =et_userName.getText().toString().trim();
           String userAccountInput =et_userAccount.getText().toString().trim();
           String userEmailInput =et_userEmail.getText().toString().trim();
           String userPassword1Input =et_userPassword1.getText().toString().trim();
           String userPassword2Input =et_userPassword2.getText().toString().trim();
           String userPhoneInput =et_userPhone.getText().toString().trim();
           String userBirthInput =et_userBirth.getText().toString().trim();

           btn_checkRegister.setEnabled(!usernameInput.isEmpty()&&!userAccountInput.isEmpty()&&!userEmailInput.isEmpty()&&!userBirthInput.isEmpty()&&!userPassword1Input.isEmpty()&&!userPassword2Input.isEmpty()&&!userPhoneInput.isEmpty());

       }

       @Override
       public void afterTextChanged(Editable s) {


       }
   };






}
