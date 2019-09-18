package com.example.nccumis;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Settings extends AppCompatActivity {

//    private EditText et_predictAmount;

//    private ListView listView;
//    private ListAdapter listAdapter;

    private Button btn_setToHome;
    private RequestQueue queue;
    private List<Cardtype> owncardtype_list=new ArrayList<Cardtype>();
    private List<Cardtype> othercardtype_list=new ArrayList<Cardtype>();
    private Button password_change;
    private Button addcardrelation;
    private Button deletecardrelation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        ckbox_GLORY = (CheckBox) findViewById(R.id.ckbox_GLORY);
//        ckbox_Green = (CheckBox) findViewById(R.id.ckbox_Green);
//        ckbox_Wonderful = (CheckBox) findViewById(R.id.ckbox_Wonderful);
//        et_GLORY = (EditText) findViewById(R.id.et_GLORY);
//        et_GREEN = (EditText) findViewById(R.id.et_GREEN);
//        et_Wonderful = (EditText) findViewById(R.id.et_Wonderful);
//        listView = (ListView) findViewById(R.id.listView);
//        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, new String[]{"GLORY+世界卡", "Living Green綠活卡", "Wonderful星璨卡"});
//        listView.setAdapter(listAdapter);

        btn_setToHome = (Button) findViewById(R.id.btn_setToHome);
        password_change = (Button) findViewById(R.id.password_change);


        password_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordchange();
            }

            public void passwordchange() {
                Intent intent = new Intent(Settings.this, password_change.class);
                startActivity(intent);
            }

        });

        btn_setToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromSettingsToHome();
            }
        });

        //owncardtype_list使用者沒有的卡
        Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String cardtype_name = jsonObject.getString("cardtype_name");
                                    owncardtype_list.add(new Cardtype(id, cardtype_name));
                                    //拿cardtype_list去調用

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
        };
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        GetcardRequest getRequest = new GetcardRequest(sp.getString("member_id",null),responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getRequest);

        //othercardtype_list使用者沒有的卡
        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String cardtype_name = jsonObject.getString("cardtype_name");
                        othercardtype_list.add(new Cardtype(id, cardtype_name));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        SharedPreferences sp2 = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sp2.edit();
        GetothercardRequest getRequest2 = new GetothercardRequest(sp2.getString("member_id",null),responseListener2);
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(getRequest2);

        addcardrelation=(Button)findViewById(R.id.addcardrelation);
        addcardrelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener3 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                                builder.setMessage("新增成功")
                                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .create()
                                        .show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                                builder.setMessage("新增失敗")
                                        .setPositiveButton("知道了", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //cardtype_name要放入新增的卡名
                String cardtype_name="i-Fun愛玩樂卡";
                SharedPreferences sp3 = getSharedPreferences("User", MODE_PRIVATE);
                SharedPreferences.Editor editor3 = sp3.edit();
                AddcardrelationRequest getRequest3 = new AddcardrelationRequest(sp3.getString("member_id",null),cardtype_name,responseListener3);
                RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
                requestQueue3.add(getRequest3);

            }
        });
        deletecardrelation=(Button)findViewById(R.id.deletecardrelation);
        deletecardrelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener4 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                                builder.setMessage("刪除成功")
                                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .create()
                                        .show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                                builder.setMessage("刪除失敗")
                                        .setPositiveButton("知道了", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //id要放入card_relationship的id
                String id="6";
                SharedPreferences sp4 = getSharedPreferences("User", MODE_PRIVATE);
                SharedPreferences.Editor editor4 = sp4.edit();
                DeletecardrelationRequest getRequest4 = new DeletecardrelationRequest(id,responseListener4);
                RequestQueue requestQueue4 = Volley.newRequestQueue(getApplicationContext());
                requestQueue4.add(getRequest4);

            }
        });
    }
    public void fromSettingsToHome(){
        Intent intent = new Intent(Settings.this,Home.class);
        startActivity(intent);
    }
    public class GetcardRequest extends StringRequest {
        private static final String Getcard_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getcard.php";
        private Map<String, String> params;
        //
        public GetcardRequest(String member_id,Response.Listener<String> listener) {
            super(Method.POST,  Getcard_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("member_id", member_id);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
    public class GetothercardRequest extends StringRequest {
        private static final String Getothercard_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getothercard.php";
        private Map<String, String> params;
        //
        public GetothercardRequest(String member_id,Response.Listener<String> listener) {
            super(Method.POST,  Getothercard_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("member_id", member_id);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
    public class AddcardrelationRequest extends StringRequest {
        private static final String Addcardrelation_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Addcardrelation.php";
        private Map<String, String> params;
        public AddcardrelationRequest(String member_id,String cardtype_name,Response.Listener<String> listener) {
            super(Method.POST,  Addcardrelation_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("member_id", member_id);
            params.put("cardtype_name", cardtype_name);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
    public class DeletecardrelationRequest extends StringRequest {
        private static final String Deletecardrelation_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Deletecardrelation.php";
        private Map<String, String> params;
        public DeletecardrelationRequest(String id , Response.Listener<String> listener) {
            super(Method.POST,  Deletecardrelation_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("id", id);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

}

