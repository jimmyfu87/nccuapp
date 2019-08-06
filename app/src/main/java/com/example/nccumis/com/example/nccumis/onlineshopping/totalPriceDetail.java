package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nccumis.R;

public class totalPriceDetail extends AppCompatActivity {
    private Button lastPage;
    private TextView isCheckedPrice;
    private TextView totalprice;
    private TextView amountLong;
    private TextView amountShort;
    private TextView nameLong;
    private TextView nameShort;
    private TextView remarkLong;
    private TextView remarkShort;

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
        remarkLong = (TextView)findViewById(R.id.remarkLong);
        if(getSaveBag.getInt("longactivity_discount") != 0){
            amountLong.setText(Integer.toString(getSaveBag.getInt("longactivity_discount")));
            nameLong.setText(getSaveBag.getString("longactivity_name"));
            remarkLong.setText(getSaveBag.getString("longactivity_remark"));
        }else {
            amountLong.setText("長期優惠為零");
        }

        amountShort = (TextView)findViewById(R.id.activityamountShort);
        nameShort = (TextView)findViewById(R.id.activitynameShort);
        remarkShort = (TextView)findViewById(R.id.remarkShort);

        if(getSaveBag.getInt("shortactivity_discount") != 0){
            amountShort.setText(Integer.toString(getSaveBag.getInt("shortactivity_discount")));
            nameShort.setText(getSaveBag.getString("shortactivity_name"));
            remarkShort.setText(getSaveBag.getString("shortactivity_remark"));
        }else {
            amountShort.setText("短期優惠為零");
        }

        totalprice = (TextView)findViewById(R.id.totalprice);
        totalprice.setText(Integer.toString(getSaveBag.getInt("isCheckedprice")-getSaveBag.getInt("longactivity_discount")-getSaveBag.getInt("shortactivity_discount")));
    }

    public void jumpTowishpool(){
        Intent intent = new Intent(totalPriceDetail.this, wishpool_channel.class);
        startActivity(intent);
    }

}
