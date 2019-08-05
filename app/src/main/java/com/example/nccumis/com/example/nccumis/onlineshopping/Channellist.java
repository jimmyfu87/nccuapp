package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class Channellist extends AppCompatActivity {
    private List<Channel> channellist=new ArrayList<Channel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channellist);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("NoValue")){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String channel_name = jsonObject.getString("channel_name");
                            String channel_url = jsonObject.getString("channel_url");
                            channellist.add(new Channel(id, channel_name, channel_url));

                            //拿channellist去調用

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //這邊是發現許願池是空的處理方式，要改可以改
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Channellist.this);
                    builder.setMessage("沒有商家")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Channellist.this, Home.class);
                                    Channellist.this.startActivity(intent);
                                }
                            })
                            .create()
                            .show();
                }
//                System.out.println(channellist.get(0).getId());
//                System.out.println(channellist.get(0).getChannel_name());
//                System.out.println(channellist.get(0).getChannel_url());
            }
        };
        GetallchannelRequest getRequest = new GetallchannelRequest(responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getRequest);

    }
    public class GetallchannelRequest extends StringRequest {
        private static final String Getallchannel_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getallchannel.php";
        private Map<String, String> params;
        //
        public GetallchannelRequest(Response.Listener<String> listener) {
            super(Method.GET, Getallchannel_REQUEST_URL, listener, null);
            params = new HashMap<>();

        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
}
