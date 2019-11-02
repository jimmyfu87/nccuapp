package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nccumis.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

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
    private Button shareLongTextToline;
    private Button shareShortTextToline;
    private String nameLongText;
    private String nameShortText;
    private String applyURL;


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
        shareLongTextToline=(Button)findViewById(R.id.shareLongTextToline);
        shareShortTextToline=(Button)findViewById(R.id.shareShortTextToline);

        applyURL = getSaveBag.getString("apply_url");///////////////辦卡連結在這////////////////
        if(getSaveBag.getInt("longactivity_discount") != 0){
            nameLongText=getSaveBag.getString("longactivity_name");
            remarkLongText = getSaveBag.getString("longactivity_remark");
            amountLong.setText(Integer.toString(getSaveBag.getInt("longactivity_discount")));
            nameLong.setText(nameLongText);
        }else {
            amountLong.setText("長期優惠為零");
        }

        amountShort = (TextView)findViewById(R.id.activityamountShort);
        nameShort = (TextView)findViewById(R.id.activitynameShort);
        remarkShort = (Button) findViewById(R.id.remarkShort);

        if(getSaveBag.getInt("shortactivity_discount") != 0){
            nameShortText=getSaveBag.getString("shortactivity_name");
            remarkShortText = getSaveBag.getString("shortactivity_remark");
            amountShort.setText(Integer.toString(getSaveBag.getInt("shortactivity_discount")));
            nameShort.setText(nameShortText);
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
        shareLongTextToline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkLongText = (remarkLongText == null) ? "無長期優惠備註" : remarkLongText ;
                if(nameLongText!=null&&remarkLongText!=null){
                    shareTextToLine("活動名稱："+nameLongText+"\n"+"活動內容："+remarkLongText+"\n"+"辦卡網址："+applyURL);
                }
                else{
                    Toast.makeText(totalPriceDetail.this,
                            "無優惠可分享", Toast.LENGTH_LONG).show();
                }
            }
        });
        shareShortTextToline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkShortText = (remarkShortText == null) ? "無短期優惠備註" : remarkShortText ;
                if(nameShortText!=null&&remarkShortText!=null) {
                    shareTextToLine("活動名稱："+nameShortText+"\n"+"活動內容："+remarkShortText+"\n"+"辦卡網址："+applyURL);
                }
                else{
                    Toast.makeText(totalPriceDetail.this,
                            "無優惠可分享", Toast.LENGTH_LONG).show();
                }
            }
        });
        totalprice = (TextView)findViewById(R.id.totalprice);
        totalprice.setText(Integer.toString(getSaveBag.getInt("isCheckedprice")-getSaveBag.getInt("longactivity_discount")-getSaveBag.getInt("shortactivity_discount")));
    }

    public void jumpTowishpool(){
        Intent intent = new Intent(totalPriceDetail.this, wishpool_channel.class);
        startActivity(intent);
    }
    public void shareTextToLine(String content){
        String newscheme= null;
        try {
            newscheme = URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String scheme = "line://msg/text/"+newscheme;
        Uri uri = Uri.parse(scheme);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

}
