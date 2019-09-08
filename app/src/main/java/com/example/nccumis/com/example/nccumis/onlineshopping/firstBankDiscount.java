package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nccumis.Home;
import com.example.nccumis.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class firstBankDiscount extends AppCompatActivity {
    private Button btn_lastPage;
    private TextView ecommerceName;
    private String getEcommerceName;
    private TextView discountDetail;
    private List<Activity> longactivitylist=new ArrayList<Activity>();
    private List<Activity> shortactivitylist=new ArrayList<Activity>();
    private String dicountLong;
    private String dicountShort;

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
        discountDetail.setText("優惠資訊：");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("NoValue")){
                    try {
                        JSONArray array = new JSONArray(response);
                        //longactivitylist.clear();
                        //shortactivitylist.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String activity_name = jsonObject.getString("activity_name");
                            String channel_name = jsonObject.getString("channel_name");
                            String cardtype_name = jsonObject.getString("cardtype_name");
                            int Minimum_pay = jsonObject.getInt("Minimum_pay");
                            double Discount_ratio = jsonObject.getDouble("Discount_ratio");
                            int Discount_limit = jsonObject.getInt("Discount_limit");
                            int Discount_money = jsonObject.getInt("Discount_money");
                            String Start_time = jsonObject.getString("Start_time");
                            String End_time = jsonObject.getString("End_time");
                            String Remarks = jsonObject.getString("Remarks");
                            if(Discount_money==0){
                                longactivitylist.add(new Activity(id, activity_name, channel_name, cardtype_name, Minimum_pay, Discount_ratio,Discount_limit,Discount_money,Start_time,End_time,Remarks));
                            }
                            else{
                                shortactivitylist.add(new Activity(id, activity_name, channel_name, cardtype_name, Minimum_pay, Discount_ratio,Discount_limit,Discount_money,Start_time,End_time,Remarks));
                            }
                        }
                        setLongactivity();
                        setShortactivity();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        };
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //這邊要放渠道名字
//        String channel_name="Momo";
        GetactivityRequest getactivityRequest = new GetactivityRequest(sp.getString("member_id",null),getEcommerceName,responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getactivityRequest);


        System.out.println("heeee"+getEcommerceName);
        discountDetail.setText("長期優惠資訊： \n"+dicountLong + "\n 短期優惠資訊： \n" + dicountShort);

    }

    public void jumpToOnlineShopping(){
        Intent getSaveData = getIntent();
        Bundle getSaveBag = getSaveData.getExtras();
        getSaveBag.putBoolean("firstBankDiscount",true);
        Intent intent = new Intent(firstBankDiscount.this, OnlineShopping.class);
        intent.putExtras(getSaveBag);
        startActivity(intent);
    }

    public class GetactivityRequest extends StringRequest {
        private static final String Getactivity_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getactivity.php";
        private Map<String, String> params;

        public GetactivityRequest(String member_id, String channel_name,Response.Listener<String> listener) {
            super(Method.POST, Getactivity_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("member_id", member_id);
            params.put("channel_name", channel_name);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    public void setLongactivity(){
        for(int i = 0; i < longactivitylist.size(); i++){
            if(longactivitylist.get(i).getChannel_name().equals(getEcommerceName)){
                dicountLong += longactivitylist.get(i).getRemarks() + "\n";
                System.out.println(longactivitylist.get(i).getRemarks());
            }
        }
    }

    public void setShortactivity(){
        for(int i = 0; i < shortactivitylist.size(); i++){
            if(shortactivitylist.get(i).getChannel_name().equals(getEcommerceName)){
                dicountShort += shortactivitylist.get(i).getRemarks() + "\n";
            }
        }
    }
}
