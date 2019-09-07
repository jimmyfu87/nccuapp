package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

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

public class wishpool extends AppCompatActivity {
    private Button lastPage;
    private ListView ecommercePathListView;
    private List<Channel> channellist=new ArrayList<Channel>();
    private List<String> nameArray = new ArrayList<String>();
    private List<String> homeurlArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishpool);

        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToHome();
            }
        });

        ecommercePathListView = (ListView)findViewById(R.id.ecommercePathListView);
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
                        System.out.println("channelsize: "+channellist.size());
                        setChannelList();
                        setListViewHeightBasedOnChildren(ecommercePathListView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //這邊是發現許願池是空的處理方式，要改可以改
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(wishpool.this);
                    builder.setMessage("沒有商家")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(wishpool.this, Home.class);
                                    wishpool.this.startActivity(intent);
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
        wishpool.GetallchannelRequest getRequest = new wishpool.GetallchannelRequest(responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getRequest);

        ecommercePathListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //到個別通路的許願池
                jumpTowishpool_channel(position);
            }
        });
    }

    public void initChannelList(){
//        System.out.println("channellist size:333 "+this.channellist.size());
        for(int i = 0; i < this.channellist.size();i++){
//            this.idArray.add(this.btn_webHome.get(i).getId());
            this.nameArray.add(this.channellist.get(i).getChannel_name());
            this.homeurlArray.add(this.channellist.get(i).getChannel_url());
        }
        //System.out.println(this.getPriceData.size()+" ,"+this.typeName.size());
    }

    public void setChannelList(){
        initChannelList();
        wishpoolListAdapter channellist_adapter = new wishpoolListAdapter(this,nameArray);
        ecommercePathListView.setAdapter(channellist_adapter);
    }

    public void jumpTowishpool_channel(int position){
        Intent saveWishpoolData = new Intent(wishpool.this, wishpool_channel.class);
        Bundle saveBag = new Bundle();
        saveBag.putString("channel_name", nameArray.get(position));
        saveBag.putString("channel_webHome", homeurlArray.get(position));
        saveWishpoolData.putExtras(saveBag);
        startActivity(saveWishpoolData);
    }

    public void jumpToHome(){
        startActivity(new Intent(wishpool.this, Home.class));
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
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
