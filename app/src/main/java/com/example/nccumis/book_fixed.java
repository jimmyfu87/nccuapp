package com.example.nccumis;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class book_fixed extends AppCompatActivity {
    private Button btn_changebookname;
    private Button btn_amountchange;
    private Button btn_check_detail;
    private Button btn_lastpage;
    private TextView et_bookname;
    private EditText et_amount;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_fixed);
        btn_changebookname=(Button)findViewById(R.id.btn_changebookname);
        btn_amountchange=(Button)findViewById(R.id.btn_amountchange);
        btn_check_detail=(Button)findViewById(R.id.btn_check_detail);
        btn_lastpage=(Button)findViewById(R.id.btn_lastpage);
        et_bookname=(TextView)findViewById(R.id.et_bookname);
        et_amount=(EditText)findViewById(R.id.et_amount);

        btn_lastpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backtolastpage();
            }
        });
        btn_changebookname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebookname();
            }
        });



    }

    public void changebookname() {
        final EditText input = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("請輸入你的名稱")
                .setView(input)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String inputName = input.getText().toString();
                        Log.d("Main", "你輸入的名稱為：" + inputName);
                        et_bookname.setText(inputName);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Main", "click cancel");
                    }
                })
                .create();
        dialog.show();


    }

    public void backtolastpage() {
        Intent intent = new Intent(book_fixed.this,BookManage.class);
    }


}
