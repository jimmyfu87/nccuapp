package com.example.nccumis;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordCheckEmail extends AppCompatActivity {

    private Button btn_sendEmail;
    private EditText et_useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_check_email);
        et_useremail=(EditText)findViewById(R.id.Email);

        btn_sendEmail=(Button) findViewById(R.id.btn_sendEmail);
        btn_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String member_email = et_useremail.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            System.out.println(response);
                            JSONObject jsonResponse = new JSONObject(response);
                            String condition = jsonResponse.getString("condition");

                            if (condition .equals("sent")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordCheckEmail.this);
                                builder.setMessage("信件已寄發")
                                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(PasswordCheckEmail.this, LogIn.class);
                                                PasswordCheckEmail.this.startActivity(intent);
                                            }
                                        })
                                        .create()
                                        .show();

                            } else if(condition.equals("wrongformat")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordCheckEmail.this);
                                builder.setMessage("帳號為非信箱格式，寄發失敗")
                                        .setPositiveButton("知道了", null)
                                        .create()
                                        .show();
                            }
                            else  {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordCheckEmail.this);
                                builder.setMessage("此帳號未註冊，寄發失敗")
                                        .setPositiveButton("知道了", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                PasswordCheckEmailRequest forgetRequest = new PasswordCheckEmailRequest(member_email, responseListener);
                RequestQueue queue = Volley.newRequestQueue(PasswordCheckEmail.this);
                queue.add(forgetRequest);
                //JumpToLogInn();
            }
        });
    }

    public void JumpToLogInn(){
        Intent intent = new Intent(PasswordCheckEmail.this, LogIn.class);
        startActivity(intent);
    }
}
