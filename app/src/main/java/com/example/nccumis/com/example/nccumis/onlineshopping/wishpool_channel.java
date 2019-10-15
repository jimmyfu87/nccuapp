package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nccumis.Cardtype;
import com.example.nccumis.Home;
import com.example.nccumis.R;
import com.google.api.services.drive.Drive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class wishpool_channel extends AppCompatActivity {
    private static RequestQueue requestQueue;
    private static RequestQueue requestQueue2;
    private static String channel_name = "";
    private static String channel_webHome = "";
    private Button lastPage;
    private TextView ecommerceName;
    private TextView newActivity;
    private Button changeCard;
    private Button refresh;
//    private ListView CreditCardListView;

    protected static com.example.nccumis.MyListView ProductListView;
    private static Button totalPrice;
    private static int isCheckedprice = 0;
    private static int longactivity_discount = 0;
    private static int shortactivity_discount = 0;
//    private TextView recommendcreditcard;
    private  List<Integer> pictureArray= new ArrayList<>();    //還沒弄
    private List<Integer> idArray = new ArrayList<Integer>();
    private  List<String> nameArray=new ArrayList<String>();
    private  List<Integer> priceArray=new ArrayList<Integer>();
    private List<String> urlArray = new ArrayList<String>();
    private List<String> uploadTimeArray = new ArrayList<String>();
    private List<Product> productlist=new ArrayList<Product>();
//    protected static List<String> discountDetailArray =new ArrayList<>();
//    protected static List<String> LONG_OR_SHORT_ACTIVITYArray =new ArrayList<>();
    private static List<Activity> longactivitylist=new ArrayList<Activity>();
    private static List<Activity> shortactivitylist=new ArrayList<Activity>();
    private List<Cardtype> owncardtypelist=new ArrayList<Cardtype>();
    private static List<String> owncardnamelist = new ArrayList<String>();  //alertdialog選單的卡名
    private static int singleChoiceIndex = 0;   //預設選擇第一張卡
    final Document[] doc = new Document[1];
    private static int longactivity_position = 0;
    private static int shortactivity_position = 0;
    private static List<Cardtype> othercardtypelist=new ArrayList<Cardtype>();
    private List<Activity> activitylistwithcard=new ArrayList<Activity>();  //沒用到，可刪掉！！！
    private static List<String> nocardnamelist = new ArrayList<String>();   //使用者沒卡時推薦一張信用卡
    private List<Activity> activitylistwithothercard=new ArrayList<Activity>();
    private static List<String> nocardname = new ArrayList<String>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishpool_channel);

        Intent getSaveData = getIntent();
        Bundle getSaveBag = getSaveData.getExtras();
        if(getSaveBag != null){
            channel_name = getSaveBag.getString("channel_name");
            channel_webHome = getSaveBag.getString("channel_webHome");
        }

        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToWishpool();
            }
        });

        ecommerceName = (TextView)findViewById(R.id.ecommerceName);
        ecommerceName.setText(channel_name+" 購物網");//之後從資料庫抓電商名稱

        newActivity = (TextView)findViewById(R.id.newActivity);

        changeCard = (Button)findViewById(R.id.changeCard);
        changeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(owncardnamelist.isEmpty() || owncardnamelist == null){
                    singleDialogEventWhenNocard();
                }else{
                    singleDialogEvent();
                }
            }
        });

        //商品列表
        ProductListView = (com.example.nccumis.MyListView)findViewById(R.id.ProductListView);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("NoValue")){
                    try {
                        JSONArray array = new JSONArray(response);
                        productlist.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String product_name = jsonObject.getString("product_name");
                            String product_price = jsonObject.getString("product_price");
                            String product_url = jsonObject.getString("product_url");
                            String member_id = jsonObject.getString("member_id");
                            String channel_name = jsonObject.getString("channel_name");
                            String upload_time = jsonObject.getString("upload_time");
                            productlist.add(new Product(id, product_name, product_price, product_url, member_id, channel_name,upload_time));
                            //拿productlist去調用，包含登入使用者的所有product
                        }
                        setProductList();
                        setListViewHeightBasedOnChildren(ProductListView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //這邊是發現許願池是空的處理方式，要改可以改
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(wishpool_channel.this);
                    builder.setMessage("沒有商品")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                  
                                }
                            })
                            .create()
                            .show();
                }
            }
        };
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        wishpool_channel.GetallproductRequest getRequest = new wishpool_channel.GetallproductRequest(sp.getString("member_id",null),channel_name,responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getRequest);


        //取得信用卡優惠活動(不根據使用者有的卡片)
        Response.Listener<String> responseListener3 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("NoValue")){
                    try {
                        JSONArray array = new JSONArray(response);
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
                            if(Discount_money==0){
                                longactivitylist.add(new Activity(id, activity_name, channel_name, cardtype_name, Minimum_pay, Discount_ratio,Discount_limit,Discount_money,Start_time,End_time,Remarks));
                            }
                            else{
                                shortactivitylist.add(new Activity(id, activity_name, channel_name, cardtype_name, Minimum_pay, Discount_ratio,Discount_limit,Discount_money,Start_time,End_time,Remarks));
                            }
                        }
                        //拿longactivity和shortactivity去調用，一個是長期的優惠活動一個是短期的優惠活動，下面是我測試用，看完可以刪掉
//                        System.out.println(longactivitylist.get(0).getId());
//                        System.out.println(longactivitylist.get(0).getActivity_name());
//                        System.out.println(longactivitylist.get(0).getChannel_name());
//                        System.out.println(longactivitylist.get(0).getCardtype_name());
//                        System.out.println(longactivitylist.get(0).getMinimum_pay());
//                        System.out.println(longactivitylist.get(0).getDiscount_ratio());
//                        System.out.println(longactivitylist.get(0).getDiscount_limit());
//                        System.out.println(longactivitylist.get(0).getDiscount_money());
//                        System.out.println(longactivitylist.get(0).getStart_time());
//                        System.out.println(longactivitylist.get(0).getEnd_time());
//                        System.out.println(longactivitylist.get(0).getRemarks());
//
//                        System.out.println(shortactivitylist.get(0).getId());
//                        System.out.println(shortactivitylist.get(0).getActivity_name());
//                        System.out.println(shortactivitylist.get(0).getChannel_name());
//                        System.out.println(shortactivitylist.get(0).getCardtype_name());
//                        System.out.println(shortactivitylist.get(0).getMinimum_pay());
//                        System.out.println(shortactivitylist.get(0).getDiscount_ratio());
//                        System.out.println(shortactivitylist.get(0).getDiscount_limit());
//                        System.out.println(shortactivitylist.get(0).getDiscount_money());
//                        System.out.println(shortactivitylist.get(0).getStart_time());
//                        System.out.println(shortactivitylist.get(0).getEnd_time());
//                        System.out.println(shortactivitylist.get(0).getRemarks());



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //發現沒有匹配活動的處理方式，要改可以改
                }
            }
        };
        SharedPreferences sp3 = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor3 = sp3.edit();
        //選擇的卡片名稱存在cardtypename
        GetactivityRequest getactivityRequest = new GetactivityRequest(sp3.getString("member_id",null),channel_name,responseListener3);
        RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getactivityRequest);

        //信用卡優惠listview
//        CreditCardListView = (ListView)findViewById(R.id.CreditCardListView);
//        CreditCardListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        setCreditCardDiscountList();
//        setListViewHeightBasedOnChildren(CreditCardListView);


        //計算後的總價
        isCheckedprice = 0;
        longactivity_discount = 0;
        shortactivity_discount = 0;
        totalPrice = (Button) findViewById(R.id.totalPrice);
        totalPrice.setText("最終結算金額: \n"+0 +"(所有勾選商品金額)" +
                " - " + 0 +"(長期優惠) - " + 0 +"(短期優惠) \n\t= " + 0);
        totalPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToTotalPriceDetail();
            }
        });


        //推薦信用卡
//        recommendcreditcard = (TextView)findViewById(R.id.recommendcreditcard);

        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "更新中請稍候...", Toast.LENGTH_SHORT).show();
                for(int i=0;i<productlist.size();i++){
                    rewebcrawl(productlist.get(i).getProduct_url(),String.valueOf(productlist.get(i).getId()));
                }
                int time=productlist.size()*1300;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        SharedPreferences sp = getSharedPreferences("changeamount", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        System.out.println(sp.getInt("changeamount",0));
                        if(sp.getInt("changeamount",0)==0){
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(wishpool_channel.this);
                            builder.setMessage("所有商品皆無變動")
                                    .setPositiveButton("刷新列表", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.putInt("changeamount",0).commit();
                                            refresh();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                        else{
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(wishpool_channel.this);
                            builder.setMessage("已變動了"+sp.getInt("changeamount",0)+"項商品")
                                    .setPositiveButton("刷新列表", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.putInt("changeamount",0).commit();
                                            refresh();
                                        }
                                    })
                                    .create()
                                    .show();

                        }
                    }
                }, time);


            }
        });
        //取得使用者沒有的信用卡
        Response.Listener<String> responseListener4 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("NoValue")){
                    try {
                        JSONArray array = new JSONArray(response);
                        othercardtypelist.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String cardtype_name = jsonObject.getString("cardtype_name");
                            String apply_url = jsonObject.getString("apply_url");
                            othercardtypelist.add(new Cardtype(id, cardtype_name,apply_url));
                            //拿othercardtypelist去調用
                        }

                        for(int i = 0; i < othercardtypelist.size();i++){
                            System.out.println("使用者沒的信用卡卡名："+othercardtypelist.get(i).getCardtype_name());

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //發現使用者辦了所有信用卡的處理方式，要改可以改

                }
            }
        };
        SharedPreferences sp4 = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor4 = sp4.edit();
        GetothercardRequest getothercardRequest = new GetothercardRequest(sp4.getString("member_id",null),responseListener4);
        RequestQueue requestQueue4 = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getothercardRequest);

        //取得使用者擁有的信用卡
        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("NoValue")){
                    try {
                        JSONArray array = new JSONArray(response);
                        owncardtypelist.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String cardtype_name = jsonObject.getString("cardtype_name");
                            String apply_url = jsonObject.getString("apply_url");
                            owncardtypelist.add(new Cardtype(id, cardtype_name,apply_url));
                            //拿owncardtypelist去調用

                        }
                        set_owncardnamelist();   //丟進alertdialog 的 String list
                        for(int i = 0; i < owncardtypelist.size();i++){
                            System.out.println("使用者有的信用卡卡名："+owncardtypelist.get(i).getCardtype_name());

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //發現使用者沒有信用卡的處理方式，要改可以改

                }
            }
        };
        SharedPreferences sp2 = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sp2.edit();
        System.out.println("member_id"+sp2.getString("member_id",null));
        GetcardRequest getcardRequest = new GetcardRequest(sp2.getString("member_id",null),responseListener2);
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getcardRequest);


        //取得使用者有的信用卡的活動
        Response.Listener<String> responseListener5 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("NoValue")){
                    try {
                        JSONArray array = new JSONArray(response);
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
                            activitylistwithcard.add(new Activity(id, activity_name, channel_name, cardtype_name, Minimum_pay, Discount_ratio,Discount_limit,Discount_money,Start_time,End_time,Remarks));
                        }
                        //拿Activitylist調用
                        updatePrice();
                        updateActivity();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        SharedPreferences sp5 = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor5 = sp5.edit();
        GetliveactivitywithcardRequest getRequest5 = new GetliveactivitywithcardRequest(sp5.getString("member_id",null),responseListener5);
        RequestQueue requestQueue5 = Volley.newRequestQueue(getApplicationContext());
        requestQueue5.add(getRequest5);




    }

    public class GetallproductRequest extends StringRequest {
        private static final String Getallproduct_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getallproduct.php";
        private Map<String, String> params;
        //
        public GetallproductRequest(String member_id, String channel_name, Response.Listener<String> listener) {
            super(Method.POST, Getallproduct_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("member_id", member_id);
            params.put("channel_name", channel_name);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
    public class GetactivityRequest extends StringRequest {
        private static final String Getactivity_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getactivity.php";
        private Map<String, String> params;
        //
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
    public class GetcardRequest extends StringRequest {
        private static final String Getcard_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getcard.php";
        private Map<String, String> params;
        //
        public GetcardRequest(String member_id, Response.Listener<String> listener) {
            super(Method.POST, Getcard_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("member_id", member_id);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
    public class GetliveactivitywithcardRequest extends StringRequest {
        private static final String Getliveactivitywithcard_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getliveactivitywithcard.php";
        private Map<String, String> params;
        //
        public GetliveactivitywithcardRequest(String member_id,Response.Listener<String> listener) {
            super(Method.POST,  Getliveactivitywithcard_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("member_id", member_id);

        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    public class GetliveactivitywithothercardRequest extends StringRequest {
        private static final String Getliveactivitywithcard_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getliveactivitywithothercard.php";
        private Map<String, String> params;
        //
        public GetliveactivitywithothercardRequest(String member_id,Response.Listener<String> listener) {
            super(Method.POST,  Getliveactivitywithcard_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("member_id", member_id);

        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    public void initProductList(){
        for(int i = 0; i < this.productlist.size();i++){
            if(this.productlist.get(i).getChannel_name().equals(channel_name)){
                this.idArray.add(this.productlist.get(i).getId());
                this.nameArray.add(this.productlist.get(i).getProduct_name());
                this.urlArray.add(this.productlist.get(i).getProduct_url());
                this.priceArray.add(Integer.parseInt(this.productlist.get(i).getProduct_price()));
                this.uploadTimeArray.add(this.productlist.get(i).getUpload_time());
            }
        }
        //System.out.println(this.getPriceData.size()+" ,"+this.typeName.size());
    }

    public void setProductList(){
        initProductList();
        productListAdapter productlist_adapter = new productListAdapter(wishpool_channel.this, channel_name, channel_webHome, urlArray, idArray ,nameArray , priceArray, uploadTimeArray);
        ProductListView.setAdapter(productlist_adapter);
    }

    //長期&短期 優惠活動 各別擇一 （可能優惠最多0~2種）
    public static void setLongShortActivity(){
        longactivity_discount = 0;
        shortactivity_discount = 0;
        longactivity_position = 0;
        shortactivity_position = 0;

        //檢查使用者是否有信用卡
        String creditcardname = owncardnamelist.isEmpty() ?
                getMaxdiscountInothercardtypelist()  : owncardnamelist.get(singleChoiceIndex) ;

        for(int i = 0; i < longactivitylist.size();i++){
            if(longactivitylist.get(i).getChannel_name().equals(channel_name)){
                if(longactivitylist.get(i).getCardtype_name().equals(creditcardname)){
                    int longactivity_discount_temp = 0;
                    if(longactivitylist.get(i).getDiscount_limit() > isCheckedprice*longactivitylist.get(i).getDiscount_ratio() ){
                        longactivity_discount_temp = Double.valueOf(isCheckedprice*longactivitylist.get(i).getDiscount_ratio()).intValue();
                    }else {
                        longactivity_discount_temp = longactivitylist.get(i).getDiscount_limit();
                    }
                    if(longactivity_discount_temp > longactivity_discount){
                        longactivity_discount = longactivity_discount_temp;
                        longactivity_position = i;
                    }
                }
//                else{
//                    int longactivity_discount_temp = 0;
//                    if(longactivitylist.get(i).getDiscount_limit() > isCheckedprice*longactivitylist.get(i).getDiscount_ratio() ){
//                        longactivity_discount_temp = Double.valueOf(isCheckedprice*longactivitylist.get(i).getDiscount_ratio()).intValue();
//                    }else {
//                        longactivity_discount_temp = longactivitylist.get(i).getDiscount_limit();
//                    }
//                    if(longactivity_discount_temp > longactivity_discount){
//                        longactivity_discount = longactivity_discount_temp;
//                        longactivity_position = i;
//                    }
//                }

            }
        }


        for(int i = 0; i < shortactivitylist.size();i++){
            if(shortactivitylist.get(i).getChannel_name().equals(channel_name)){
                if(shortactivitylist.get(i).getCardtype_name().equals(creditcardname)){
                    int shortactivity_discount_temp = 0;
                    if(isCheckedprice > shortactivitylist.get(i).getMinimum_pay()){
                        shortactivity_discount_temp = shortactivitylist.get(i).getDiscount_money();
                    }

                    if(shortactivity_discount_temp > shortactivity_discount){
                        shortactivity_discount = shortactivity_discount_temp;
                        shortactivity_position = i;
                    }

                }

//                else {
//                    int shortactivity_discount_temp = 0;
//                    if(isCheckedprice > shortactivitylist.get(i).getMinimum_pay()){
//                        shortactivity_discount_temp = shortactivitylist.get(i).getDiscount_money();
//                    }
//                    if(shortactivity_discount_temp > shortactivity_discount){
//                        shortactivity_discount = shortactivity_discount_temp;
//                        shortactivity_position = i;
//                    }
//                }
            }
        }



    }

    //計算目前勾選總額
    public static void setisCheckedPrice(int para){
        isCheckedprice = para;
        setLongShortActivity();
        int totalPriceData = isCheckedprice - longactivity_discount - shortactivity_discount;
        totalPrice.setText("最終結算金額: \n"+isCheckedprice +"(所有勾選商品金額)" +
                " - " + longactivity_discount +"(長期優惠) - " + shortactivity_discount +"(短期優惠) \n\t= " + totalPriceData);
    }

    //改了勾選信用卡後更新總額
    public static void updatePrice(){
        setLongShortActivity();
        int totalPriceData = isCheckedprice - longactivity_discount - shortactivity_discount;
        totalPrice.setText("最終結算金額: \n"+isCheckedprice +"(所有勾選商品金額)" +
                " - " + longactivity_discount +"(長期優惠) - " + shortactivity_discount +"(短期優惠) \n\t= " + totalPriceData);
    }

    //更新使用者有的信用卡中的最新活動訊息
    public void updateActivity(){
        if(!hasActivity() || owncardnamelist.isEmpty()){
            newActivity.setText("卡片目前無任何優惠");
            return;
        }
        String longRemark ="";
        for (int i= 0; i < longactivitylist.size();i++){
            if(owncardnamelist.get(singleChoiceIndex).equals(longactivitylist.get(i).getCardtype_name())){
                if(longactivitylist.get(longactivity_position).getRemarks().equals(null) || longactivitylist.get(longactivity_position).getRemarks().equals("")){
                    longRemark = "目前無長期優惠";
                    break;
                }else {
                    longRemark = longactivitylist.get(longactivity_position).getRemarks();
                }
            }
        }
        String shortRemark = "";
        for (int i= 0; i < shortactivitylist.size();i++){
            if(owncardnamelist.get(singleChoiceIndex).equals(shortactivitylist.get(i).getCardtype_name())){
                if(shortactivitylist.get(shortactivity_position).getRemarks().equals(null) || shortactivitylist.get(shortactivity_position).getRemarks().equals("")){
                    shortRemark = "目前無短期優惠";
                    break;
                }else {
                    shortRemark = shortactivitylist.get(shortactivity_position).getRemarks();
                }
            }
        }

        newActivity.setText("最新長期優惠: " + longRemark +"\n"
                    + "最新短期優惠: " + shortRemark);

//        for(int i = 0; i < activitylistwithcard.size(); i++){
//            if(activitylistwithcard.get(i).getCardtype_name().equals(owncardnamelist.get(singleChoiceIndex))){
//                newActivity.setText("最新優惠: " + activitylistwithcard.get(i).getRemarks());
//                return;
//            }
//        }
    }

    //檢查勾選信用卡是否有長期或短期活動
    public boolean hasActivity(){
        //檢查使用者是否有信用卡
        String creditcardname = owncardnamelist.isEmpty() ?
                getMaxdiscountInothercardtypelist() : owncardnamelist.get(singleChoiceIndex);

        for(int i = 0; i < longactivitylist.size(); i++){
            if(longactivitylist.get(i).getCardtype_name().equals(creditcardname)){
                return true;
            }
        }

        for(int i = 0; i < shortactivitylist.size(); i++){
            if(shortactivitylist.get(i).getCardtype_name().equals(creditcardname)){
                return true;
            }
        }

        return false;
    }

    //抓取要丟進alertdialog的選單 && 依照優惠程度排序 (own && other cardtypelist)
    public void set_owncardnamelist(){
        this.owncardnamelist.clear();   //歸零list
        set_owncarddiscount();  //  設定每張卡的優惠
        set_othercarddiscount();

        List<Cardtype> tempcardtypelist = new ArrayList<Cardtype>();
        for(int i = 0; i <owncardtypelist.size(); i++) {
            tempcardtypelist.add(new Cardtype(this.owncardtypelist.get(i).getId(),this.owncardtypelist.get(i).getCardtype_name(),this.owncardtypelist.get(i).getApply_url()));
        }
        int count = 0;

        while (tempcardtypelist.size() != 0){
            if(count == 1){
                owncardnamelist.add(getMaxdiscountInothercardtypelist()+"\n(以上為推薦使用者之卡)");
                count++;
                continue;
            }
            int curMax = 0;
            int position = 0;
            for(int i = 0; i < tempcardtypelist.size(); i++){
                int discount = tempcardtypelist.get(i).getdiscountMax();
                if(discount > curMax){
                    curMax = discount;
                    position = i;
                }
            }
            owncardnamelist.add(tempcardtypelist.get(position).getCardtype_name());
            tempcardtypelist.remove(position);
            count++;
        }


    }


    //算出 owncardtypelist 中每張的優惠程度
    public void set_owncarddiscount(){
        int totalPriceinProductList = countProductListTotalPrice();

        for(int i = 0; i < owncardtypelist.size(); i++){
            String getcardtypeName = owncardtypelist.get(i).getCardtype_name();

            int discountlongMax = 0;    //試算最大的長期優惠金額
            for(int j = 0; j < longactivitylist.size();j++){
//                System.out.println("longactivitylistsize: "+longactivitylist.size());
                if(longactivitylist.get(j).getChannel_name().equals(channel_name) && longactivitylist.get(j).getCardtype_name().equals(getcardtypeName)){
                    int longactivity_discount_temp = 0;
                    if(longactivitylist.get(j).getDiscount_limit() > totalPriceinProductList*longactivitylist.get(j).getDiscount_ratio() ){
                        longactivity_discount_temp = Double.valueOf(totalPriceinProductList*longactivitylist.get(j).getDiscount_ratio()).intValue();
                    }else {
                        longactivity_discount_temp = longactivitylist.get(j).getDiscount_limit();
                    }

                    discountlongMax = (longactivity_discount_temp > discountlongMax) ?
                            longactivity_discount_temp : discountlongMax ;

                }

            }

            int discountshortMax = 0;    //試算最大的短期優惠金額
            for(int j = 0; j < shortactivitylist.size();j++){
//                System.out.println("shortactivitylistsize: "+shortactivitylist.size());
                if(shortactivitylist.get(j).getChannel_name().equals(channel_name) && shortactivitylist.get(j).getCardtype_name().equals(getcardtypeName)){
                    int shortactivity_discount_temp = 0;

                    if(totalPriceinProductList > shortactivitylist.get(j).getMinimum_pay()){
                        shortactivity_discount_temp = shortactivitylist.get(j).getDiscount_money();
                    }

                    discountshortMax = (shortactivity_discount_temp > discountshortMax) ?
                            shortactivity_discount_temp : discountshortMax ;

                }
            }

//            System.out.println("hhhhhhhh: "+discountlongMax + discountshortMax);
            owncardtypelist.get(i).setdiscountmax(discountlongMax + discountshortMax);
        }

    }

    //算出 othercardtypelist 每張的優惠程度
    public void set_othercarddiscount(){
        int totalPriceinProductList = countProductListTotalPrice();

        for(int i = 0; i < othercardtypelist.size(); i++){
            String getcardtypeName = othercardtypelist.get(i).getCardtype_name();

            int discountlongMax = 0;
            for(int j = 0; j < longactivitylist.size();j++){
                if(longactivitylist.get(j).getChannel_name().equals(channel_name) && longactivitylist.get(j).getCardtype_name().equals(getcardtypeName)){
                    int longactivity_discount_temp = 0;
                    if(longactivitylist.get(j).getDiscount_limit() > totalPriceinProductList*longactivitylist.get(j).getDiscount_ratio() ){
                        longactivity_discount_temp = Double.valueOf(totalPriceinProductList*longactivitylist.get(j).getDiscount_ratio()).intValue();
                    }else {
                        longactivity_discount_temp = longactivitylist.get(j).getDiscount_limit();
                    }

                    discountlongMax = (longactivity_discount_temp > discountlongMax) ?
                            longactivity_discount_temp : discountlongMax;
                }
            }

            int discountshortMax = 0;
            for(int j = 0; j < shortactivitylist.size();j++){
                if(shortactivitylist.get(j).getChannel_name().equals(channel_name) && shortactivitylist.get(j).getCardtype_name().equals(getcardtypeName)){
                    int shortactivity_discount_temp = 0;

                    if(totalPriceinProductList > shortactivitylist.get(j).getMinimum_pay()){
                        shortactivity_discount_temp = shortactivitylist.get(j).getDiscount_money();
                    }

                    discountshortMax = (shortactivity_discount_temp > discountshortMax) ?
                            shortactivity_discount_temp : discountshortMax;
                }
            }
            othercardtypelist.get(i).setdiscountmax(discountlongMax + discountshortMax);
        }

    }

    //取得othercardtypelist中值最大的
    public static String getMaxdiscountInothercardtypelist(){
        int discountMax = 0;
        String cardnameMaxdiscount = "";
        for(int i = 0; i < othercardtypelist.size(); i++){
            if(discountMax < othercardtypelist.get(i).getdiscountMax()){
                discountMax = othercardtypelist.get(i).getdiscountMax();
                cardnameMaxdiscount = othercardtypelist.get(i).getCardtype_name();
            }
        }
        System.out.println("cardnameMaxdiscount:"+cardnameMaxdiscount);
        return cardnameMaxdiscount;
    }

    //算出productlist中商品的總額
    public int countProductListTotalPrice(){
        int totalprice = 0;
        for(int i = 0; i < productlist.size(); i++){
            totalprice += Integer.parseInt(productlist.get(i).getProduct_price());
        }
//        System.out.println("許願池所有商品加總："+totalprice);
        return totalprice;
    }

    //當使用者勾選信用卡時 && 紀錄選了哪張卡
    public void singleDialogEvent(){
        new AlertDialog.Builder(wishpool_channel.this)
                .setSingleChoiceItems(owncardnamelist.toArray(new String[owncardnamelist.size()]), singleChoiceIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                singleChoiceIndex = which;
                            }
                        })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updatePrice();
                        updateActivity();
                        Toast.makeText(wishpool_channel.this, "你選擇的是"+owncardnamelist.get(singleChoiceIndex), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //當使用者無信用卡可選時，直接推薦使用者可辦的卡
    public void singleDialogEventWhenNocard(){
        set_othercarddiscount();

        nocardnamelist.clear();
        nocardnamelist.add(getMaxdiscountInothercardtypelist());
        new AlertDialog.Builder(wishpool_channel.this)
                .setSingleChoiceItems(nocardnamelist.toArray(new String[nocardnamelist.size()]), singleChoiceIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                singleChoiceIndex = which;
                            }
                        })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updatePrice();
                        updateActivity();
                        Toast.makeText(wishpool_channel.this, "你選擇的是"+nocardnamelist.get(singleChoiceIndex), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //找信用卡辦卡URL
    public String getCreditCardURL(){
        String creditcardurl = "";
        if(owncardnamelist.isEmpty()){
            for(int i = 0; i < othercardtypelist.size(); i++){
                if(nocardnamelist.get(0).equals(othercardtypelist.get(i).getCardtype_name())){
                    creditcardurl = othercardtypelist.get(i).getApply_url();
//                    System.out.println("沒卡");
                    return creditcardurl;
                }
            }
        }else {
            for(int i = 0; i < owncardtypelist.size(); i++){
                if(owncardnamelist.get(singleChoiceIndex).equals(owncardtypelist.get(i).getCardtype_name())){
                    creditcardurl = owncardtypelist.get(i).getApply_url();
//                    System.out.println("有卡");
                    return creditcardurl;
                }
            }
        }

        System.out.println("信用卡URL："+creditcardurl);
        return creditcardurl;
    }

    /**
     * 動態設定ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + listView.getDividerHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }



    public void jumpToWishpool(){
        Intent intent = new Intent(wishpool_channel.this, wishpool.class);
        startActivity(intent);
    }

    public void jumpToTotalPriceDetail(){
        Intent intent = new Intent(wishpool_channel.this, totalPriceDetail.class);
        Bundle saveCheckPriceData = new Bundle();
        saveCheckPriceData.putString("ecommerceName", ecommerceName.getText().toString());
        saveCheckPriceData.putInt("isCheckedprice", isCheckedprice);
        saveCheckPriceData.putInt("longactivity_discount", longactivity_discount);
        saveCheckPriceData.putString("apply_url", getCreditCardURL());
        if(longactivity_discount != 0){
            saveCheckPriceData.putString("longactivity_name",longactivitylist.get(longactivity_position).getActivity_name());
            saveCheckPriceData.putString("longactivity_remark",longactivitylist.get(longactivity_position).getRemarks());
//            saveCheckPriceData.putString("applyurl",owncardtypelist.get());

        }
        saveCheckPriceData.putInt("shortactivity_discount", shortactivity_discount);
        if(shortactivity_discount != 0){
            saveCheckPriceData.putString("shortactivity_name",shortactivitylist.get(shortactivity_position).getActivity_name());
            saveCheckPriceData.putString("shortactivity_remark",shortactivitylist.get(shortactivity_position).getRemarks());
//            saveCheckPriceData.putString("applyurl",);

        }



        intent.putExtras(saveCheckPriceData);
        startActivity(intent);
    }

    public void rewebcrawl(String inputurl,String product_id){
        if(inputurl.contains("pchome.com.tw")){
            if(inputurl.contains("?fq")){
                int pos=inputurl.indexOf("?fq");
                inputurl=inputurl.substring(0,pos);
            }
            if(inputurl.contains("/prod")){
                int pos=inputurl.indexOf("/prod");
                char[] str=inputurl.toCharArray();
                StringBuilder sb=new StringBuilder();
                sb.append(str);
                sb.insert(pos,"/ecapi/ecshop/prodapi/v2");
                sb.append("&fields=Name,Price&_callback=jsonp_prod");
                inputurl=sb.toString();
            }
        }
        else if(inputurl.contains("momoshop.com.tw/goods")){

        }
        else{
            //無法解析
            UpdateDatabase(product_id,"delete","delete");
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, inputurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.contains("momoshop.com.tw")) {
                                doc[0] = Jsoup.parse(response);
                                Elements title = doc[0].getElementsByTag("title");
                                String titles = "";
                                int edition = 0;
                                String sb = "";
                                String sb2 = "";
                                for (Element element : title) {
                                    titles = titles + element;
                                    //System.out.println(titles);
                                }

                                if (titles.contains("momo購物網行動版")) {
                                    edition = 1;
                                } else {
                                    edition = 2;
                                }
                                //System.out.println(edition);
                                switch (edition) {
                                    //Momo行動版
                                    case 1:
                                        Elements element2 = doc[0].getElementsByTag("title");
                                        for (Element element : element2) {
                                            sb = sb + element;
                                            sb = sb.replace("<title>", "");
                                            sb = sb.replace("</title>", "");
                                            sb = sb.replace("- momo購物網行動版", "");
                                        }
                                        Elements elements3 = doc[0].getElementsByClass("priceArea").first().getElementsByTag("b");
                                        for (Element element : elements3) {
                                            sb2 = sb2 + element;
                                            sb2 = sb2.replace("<b>", "");
                                            sb2 = sb2.replace("</b>", "");
                                        }

                                        sb2 = sb2.replace(",", "");//移除價錢的逗號
                                        UpdateDatabase(product_id, sb, sb2);
                                        break;
                                    //Momo電腦版
                                    case 2:
                                        Elements element4 = doc[0].getElementsByTag("title");
                                        for (Element element : element4) {
                                            sb = "";
                                            sb = sb + element;
                                            sb = sb.replace("<title>", "");
                                            sb = sb.replace("</title>", "");
                                            sb = sb.replace("-momo購物網", "");
                                        }
                                        Elements elements5 = doc[0].getElementsByClass("special").first().getElementsByTag("span");
                                        for (Element element : elements5) {
                                            sb2 = "";
                                            sb2 = sb2 + element;
                                            sb2 = sb2.replace("<span>", "");
                                            sb2 = sb2.replace("</span>", "");
                                        }

                                        sb2 = sb2.replace(",", "");//移除價錢的逗號
                                        UpdateDatabase(product_id, sb, sb2);
                                        break;

                                }
                            }
                            //Pchome爬蟲
                            else {
                                response=response.replace("try{jsonpcb_prodecshop(","");
                                response=response.replace("}}catch(e){if(window.console){console.log(e);}}","");
                                int pos=response.indexOf(":");
                                response=response.substring(pos+1,response.length()-1);
                                JSONObject jsonresponse=new JSONObject(response);

                                String product_name=jsonresponse.getString("Name");
                                String fprice=jsonresponse.getString("Price");
                                JSONObject jsonresponse2=new JSONObject(fprice);
                                String product_price=jsonresponse2.getString("P");

                                UpdateDatabase(product_id,product_name,product_price);
                            }

                        } catch (Exception e) {
                            UpdateDatabase(product_id,"delete","delete");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UpdateDatabase(product_id, "delete", "delete");
            }
        });
        queue.add(stringRequest);

    }
    public void UpdateDatabase(String product_id,String product_name,String product_price){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String change = jsonResponse.getString("change");
                    if (change.equals("change")) {
                        SharedPreferences sp = getSharedPreferences("changeamount", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        int changeamount=sp.getInt("changeamount",0)+1;
                        editor.putInt("changeamount",changeamount).commit();
                    } else if(change.equals("same")){
                    }
                    else{
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        UpdatepoolRequest updatepoolRequest = new UpdatepoolRequest(product_id,product_name,product_price,responseListener);
        RequestQueue queue = Volley.newRequestQueue(wishpool_channel.this);
        queue.add(updatepoolRequest);

    }
    public class UpdatepoolRequest extends StringRequest {
        private static final String UpdatepoolRequest_REQUEST_URL="https://nccugo105306.000webhostapp.com/Updatepool.php";
        private Map<String,String> params;

        public UpdatepoolRequest(String id,String product_name, String product_price, Response.Listener<String> listener){
            super(Method.POST, UpdatepoolRequest_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("id", id);
            params.put("product_name", product_name);
            params.put("product_price", product_price);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
    private void refresh() {
        finish();
        Intent intent = new Intent(wishpool_channel.this, wishpool_channel.class);
        startActivity(intent);
    }
    public class GetothercardRequest extends StringRequest {
        private static final String Getothercard_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getothercard.php";
        private Map<String, String> params;
        //
        public GetothercardRequest(String member_id, Response.Listener<String> listener) {
            super(Method.POST, Getothercard_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("member_id", member_id);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
}
