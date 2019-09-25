package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nccumis.R;

public class totalPriceDetail extends AppCompatActivity {
    private Button lastPage;
    private TextView isCheckedPrice;
    private TextView totalprice;
    private TextView amountLong;
    private TextView amountShort;
    private TextView nameLong;
    private TextView nameShort;
    private Button remarkLong;
    private Button remarkShort;
    private String remarkLongText;
    private String remarkShortText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalprice_detail);

        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpTowishpool();
            }
        });

        Intent getSaveData = getIntent();
        Bundle getSaveBag = getSaveData.getExtras();
        isCheckedPrice = (TextView)findViewById(R.id.isCheckedPrice);
        isCheckedPrice.setText(Integer.toString(getSaveBag.getInt("isCheckedprice")));

        amountLong = (TextView)findViewById(R.id.activityamountLong);
        nameLong = (TextView)findViewById(R.id.activitynameLong);
        remarkLong = (Button) findViewById(R.id.remarkLong);
        if(getSaveBag.getInt("longactivity_discount") != 0){
            amountLong.setText(Integer.toString(getSaveBag.getInt("longactivity_discount")));
            nameLong.setText(getSaveBag.getString("longactivity_name"));
            remarkLongText = getSaveBag.getString("longactivity_remark");
        }else {
            amountLong.setText("長期優惠為零");
        }

        amountShort = (TextView)findViewById(R.id.activityamountShort);
        nameShort = (TextView)findViewById(R.id.activitynameShort);
        remarkShort = (Button) findViewById(R.id.remarkShort);

        if(getSaveBag.getInt("shortactivity_discount") != 0){
            amountShort.setText(Integer.toString(getSaveBag.getInt("shortactivity_discount")));
            nameShort.setText(getSaveBag.getString("shortactivity_name"));
            remarkShortText = getSaveBag.getString("shortactivity_remark");
        }else {
            amountShort.setText("短期優惠為零");
        }

        remarkLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkLongText = (remarkLongText == null) ? "無長期優惠備註" : remarkLongText ;
                Toast.makeText(totalPriceDetail.this,
                        remarkLongText, Toast.LENGTH_LONG).show();
            }
        });

        remarkShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkShortText = (remarkShortText == null) ? "無短期優惠備註" : remarkShortText ;
                Toast.makeText(totalPriceDetail.this,
                        remarkShortText, Toast.LENGTH_LONG).show();
            }
        });

        totalprice = (TextView)findViewById(R.id.totalprice);
        totalprice.setText(Integer.toString(getSaveBag.getInt("isCheckedprice")-getSaveBag.getInt("longactivity_discount")-getSaveBag.getInt("shortactivity_discount")));
    }

    public void jumpTowishpool(){
        Intent intent = new Intent(totalPriceDetail.this, wishpool_channel.class);
        startActivity(intent);
    }

}
