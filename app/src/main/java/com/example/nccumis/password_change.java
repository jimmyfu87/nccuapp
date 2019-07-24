package com.example.nccumis;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class password_change<ModifyPswActivity> extends AppCompatActivity {
    private Button btn_lastPage;
    private Button btn_confirm;
    private EditText new_password;
    private EditText check_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        new_password = findViewById(R.id.new_password);
        check_password = findViewById(R.id.check_password);


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
                if (new_password.getText().toString().isEmpty() || check_password.getText().toString().isEmpty()) {
                    Toast.makeText(password_change.this,"請趕快填空格",Toast.LENGTH_SHORT).show();
                }else if(new_password.getText().toString().isEmpty()){
                    Toast.makeText(password_change.this,"請輸入新密碼",Toast.LENGTH_SHORT).show();
                }
                else if (new_password.getText().toString().equals(check_password.getText().toString())) {
                    SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    String member_id=sp.getString("member_id",null);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(password_change.this);
                                    builder.setMessage("密碼更新成功")
                                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(password_change.this, Settings.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .create()
                                            .show();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(password_change.this);
                                    builder.setMessage("密碼更新失敗")
                                            .setPositiveButton("知道了", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    String newpassword=new_password.getText().toString();
                    ChangepasswordRequest changepasswordRequest = new ChangepasswordRequest(member_id, newpassword,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(password_change.this);
                    queue.add(changepasswordRequest);
                }
                else{
                    Toast.makeText(password_change.this,"密碼不一致",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public class ChangepasswordRequest extends StringRequest {
        private static final String Changepassword_REQUEST_URL="https://nccugo105306.000webhostapp.com/changepassword.php";
        private Map<String,String> params;
        public ChangepasswordRequest(String member_id,String newpassword, Response.Listener<String> listener){
            super(Method.POST, Changepassword_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("member_id", member_id);
            params.put("newpassword", newpassword);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    }
