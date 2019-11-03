package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.nccumis.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class getactivityload extends AppCompatActivity {
    private static ArrayList<Activity> longactivitylist = new ArrayList<Activity>();
    private static ArrayList<Activity> shortactivitylist = new ArrayList<Activity>();
    private static String channel_name = "";
    private static String channel_webHome = "";
    private static int totalsize=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getactivityload);
        Intent getSaveData = getIntent();
        Bundle getSaveBag = getSaveData.getExtras();
        if (getSaveBag != null) {
            channel_name = getSaveBag.getString("channel_name");
            channel_webHome = getSaveBag.getString("channel_webHome");
        }
        //取得信用卡優惠活動(不根據使用者有的卡片)
        Response.Listener<String> responseListener3 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("NoValue")) {
                    try {
                        System.out.println(response);
                        JSONArray array =  new JSONArray(response);
                        totalsize = array.length();
                        System.out.println(totalsize);
                        longactivitylist.clear();
                        shortactivitylist.clear();
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
                            if (Discount_money == 0) {
                                longactivitylist.add(new Activity(id, activity_name, channel_name, cardtype_name, Minimum_pay, Discount_ratio, Discount_limit, Discount_money, Start_time, End_time, Remarks));
                            } else {
                                shortactivitylist.add(new Activity(id, activity_name, channel_name, cardtype_name, Minimum_pay, Discount_ratio, Discount_limit, Discount_money, Start_time, End_time, Remarks));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("錯誤");
                    }
                } else {
                    //發現沒有匹配活動的處理方式，要改可以改
                }
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                };
            }
        };

        SharedPreferences sp3 = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor3 = sp3.edit();
        //選擇的卡片名稱存在cardtypename
        GetactivityRequest getactivityRequest = new GetactivityRequest(sp3.getString("member_id", null), channel_name, responseListener3);
        RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
        requestQueue3.add(getactivityRequest);

//        while (totalsize<=(longactivitylist.size()+shortactivitylist.size()){
//
//        }


        int time=3000;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                jumpTowishpool_channel();
            }
        }, time);
    }

    public void jumpTowishpool_channel() {
        Intent saveWishpoolData = new Intent(getactivityload.this, wishpool_channel.class);
        Bundle saveBag = new Bundle();
        saveBag.putString("channel_name", channel_name);
        saveBag.putString("channel_webHome", channel_webHome);
        saveBag.putParcelableArrayList("longactivitylist", longactivitylist);
        saveBag.putParcelableArrayList("shortactivitylist", shortactivitylist);
        saveWishpoolData.putExtras(saveBag);
        startActivity(saveWishpoolData);
    }

    public class GetactivityRequest extends StringRequest {
        private static final String Getactivity_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getactivity.php";
        private Map<String, String> params;

        //
        public GetactivityRequest(String member_id, String channel_name, Response.Listener<String> listener) {
            super(Method.POST,Getactivity_REQUEST_URL, listener,null);
            params = new HashMap<>();
            params.put("member_id", member_id);
            params.put("channel_name", channel_name);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
