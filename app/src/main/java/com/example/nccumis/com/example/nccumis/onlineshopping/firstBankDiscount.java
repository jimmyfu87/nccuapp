package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nccumis.R;

public class firstBankDiscount extends AppCompatActivity {
    private Button btn_lastPage;
    private TextView ecommerceName;
    private String getEcommerceName;
    private TextView discountDetail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstbank_discount);
        Intent getSaveData = getIntent();
        Bundle getSaveBag = getSaveData.getExtras();

        //回webview頁
        btn_lastPage = (Button)findViewById(R.id.btn_lastPage);
        btn_lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToOnlineShopping();
            }
        });

        //電商名城
        ecommerceName = (TextView)findViewById(R.id.ecommerceName);
        getEcommerceName = getSaveBag.getString("webName");
        ecommerceName.setText(getEcommerceName+"線上購物平台");

        //一銀與電商優惠，從資料庫fetch資料
        discountDetail = (TextView)findViewById(R.id.discountDetail);
        discountDetail.setText("此處加資料庫fetch的資料");

    }

    public void jumpToOnlineShopping(){
        Intent getSaveData = getIntent();
        Bundle getSaveBag = getSaveData.getExtras();
        getSaveBag.putBoolean("firstBankDiscount",true);
        Intent intent = new Intent(firstBankDiscount.this, OnlineShopping.class);
        intent.putExtras(getSaveBag);
        startActivity(intent);
    }
}
