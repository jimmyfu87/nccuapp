package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        //回webview頁
        btn_lastPage = (Button)findViewById(R.id.btn_lastPage);

        //電商名城
        ecommerceName = (TextView)findViewById(R.id.ecommerceName);
        ecommerceName.setText(getEcommerceName+"購物網");

        //一銀與電商優惠，從資料庫fetch資料
        discountDetail = (TextView)findViewById(R.id.discountDetail);

    }
}
