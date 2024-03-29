package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nccumis.Home;
import com.example.nccumis.MyListView;
import com.example.nccumis.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  onlineShoppingPath extends AppCompatActivity {
    private Button lastPage;
    protected static com.example.nccumis.MyListView ecommercePathListView;
    private EditText inputPath;
    private Button searchPath;
    private List<Channel> channellist=new ArrayList<Channel>();
    private List<Integer> idArray = new ArrayList<Integer>();
    private List<String> discountDetailArray = new ArrayList<String>();
    private List<String> nameArray = new ArrayList<String>();
    private List<String> urlArray= new ArrayList<String>();
    private List<Activity> activitylist=new ArrayList<Activity>();
    private Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlineshopping_path);

        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToHome();
            }
        });
        refresh = (Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });



        ecommercePathListView = (MyListView)findViewById(R.id.ecommercePathListView);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("NoValue")){
                    try {
                        JSONArray array = new JSONArray(response);
//                        channellist.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String channel_name = jsonObject.getString("channel_name");
                            String channel_url = jsonObject.getString("channel_url");
                            channellist.add(new Channel(id, channel_name, channel_url));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //這邊是發現許願池是空的處理方式（使用者沒卡），要改可以改
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(onlineShoppingPath.this);
                    builder.setMessage("沒有商家")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(onlineShoppingPath.this, Home.class);
                                    onlineShoppingPath.this.startActivity(intent);
                                }
                            })
                            .create()
                            .show();

                }
            }
        };

        GetallchannelRequest getRequest = new onlineShoppingPath.GetallchannelRequest(responseListener);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getRequest);

        //取得優惠商家的活動
        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
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
                            activitylist.add(new Activity(id, activity_name, channel_name, cardtype_name, Minimum_pay, Discount_ratio,Discount_limit,Discount_money,Start_time,End_time,Remarks));
                        }
                        for(int i=0;i<activitylist.size();i++){
                            for(int j=0;j<channellist.size();j++) {
                                if (activitylist.get(i).getChannel_name().equals(channellist.get(j).getChannel_name())) {
                                    channellist.get(j).getActivitylist().add(activitylist.get(i));
                                    break;
                                }
                            }
                        }
                        //拿channellist去調用
                        setChannelList();
                        setListViewHeightBasedOnChildren(ecommercePathListView);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //這邊是發現活動是空的處理方式，要改可以改
                    setChannelList();
                    setListViewHeightBasedOnChildren(ecommercePathListView);
                }
            }
        };
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        GetliveactivitywithcardRequest getRequest2 = new GetliveactivitywithcardRequest(sp.getString("member_id",null),responseListener2);
        getRequest2.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(getRequest2);

    }

    public void jumpToHome(){
        startActivity(new Intent(onlineShoppingPath.this, Home.class));
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
            totalHeight += listItem.getMeasuredHeight()+ listView.getDividerHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void initChannelList(){
        for(int i = 0; i < this.channellist.size();i++){
            String discountDetailtemp = "";
            this.idArray.add(this.channellist.get(i).getId());
            this.nameArray.add(this.channellist.get(i).getChannel_name());
            if(!this.channellist.get(i).getActivitylist().isEmpty()) {
                for(int j = 0; j < this.channellist.get(i).getActivitylist().size(); j++){
                    if(!this.channellist.get(i).getActivitylist().get(j).getRemarks().isEmpty()){
                        discountDetailtemp += this.channellist.get(i).getActivitylist().get(j).getActivity_name() +"\n";
                    }
                }
            }
            discountDetailtemp = discountDetailtemp == "" ? "無優惠資訊" : discountDetailtemp;
            this.discountDetailArray.add(discountDetailtemp);
            this.urlArray.add(this.channellist.get(i).getChannel_url());
        }
    }

    public void setChannelList(){
        initChannelList();
        onlineshoppingPathAdapter channellist_adapter = new onlineshoppingPathAdapter(onlineShoppingPath.this, discountDetailArray, idArray ,nameArray , urlArray);
        ecommercePathListView.setAdapter(channellist_adapter);
    }

    public class GetallchannelRequest extends StringRequest {
        private static final String Getallchannel_REQUEST_URL = "https://nccugo105306087.000webhostapp.com/Getallchannel.php";
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
    public class GetliveactivitywithcardRequest extends StringRequest {
        private static final String Getliveactivitywithcard_REQUEST_URL = "https://nccugo105306087.000webhostapp.com/Getliveactivitywithcard.php";
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
    private void refresh() {
        finish();
        Intent intent = new Intent(onlineShoppingPath.this, onlineShoppingPath.class);
        startActivity(intent);
    }
}
