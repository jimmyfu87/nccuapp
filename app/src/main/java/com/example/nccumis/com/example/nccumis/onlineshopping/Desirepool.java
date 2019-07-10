package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nccumis.Home;
import com.example.nccumis.LogIn;
import com.example.nccumis.LoginRequest;
import com.example.nccumis.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Desirepool extends AppCompatActivity {
    private static RequestQueue requestQueue;
    private List<Product> productlist=new ArrayList<Product>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desirepool);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("NoValue")){
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String product_name = jsonObject.getString("product_name");
                                    String product_price = jsonObject.getString("product_price");
                                    String product_url = jsonObject.getString("product_url");
                                    String member_id = jsonObject.getString("member_id");
                                    String channel_name = jsonObject.getString("channel_name");
                                    productlist.add(new Product(id, product_name, product_price, product_url, member_id, channel_name));
                                    //拿productlist去調用，包含登入使用者的所有product
                                }
//                                下面是取值方式可以參考，不用就可以刪掉
//                                for (int i = 0; i < productlist.size(); i++) {
//                                    System.out.println(productlist.get(i).getId());
//                                    System.out.println(productlist.get(i).getProduct_name());
//                                    System.out.println(productlist.get(i).getProduct_price());
//                                    System.out.println(productlist.get(i).getProduct_url());
//                                    System.out.println(productlist.get(i).getMember_id());
//                                    System.out.println(productlist.get(i).getChannel_name());
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            //這邊是發現許願池是空的處理方式，要改可以改
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Desirepool.this);
                            builder.setMessage("沒有商品")
                                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Desirepool.this, Home.class);
                                            Desirepool.this.startActivity(intent);
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
        };
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        GetallproductRequest getRequest = new GetallproductRequest(sp.getString("member_id",null),"Momo",responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getRequest);
    }
    public class GetallproductRequest extends StringRequest {
        private static final String Getallproduct_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getallproduct.php";
        private Map<String, String> params;
        //
        public GetallproductRequest(String member_id, String channel_name,Response.Listener<String> listener) {
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
}
