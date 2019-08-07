package com.example.nccumis;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nccumis.com.example.nccumis.onlineshopping.wishpool_momo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Settings extends AppCompatActivity {

    private EditText et_predictAmount;
    private CheckBox ckbox_GLORY;
    private CheckBox ckbox_Green;
    private CheckBox ckbox_Wonderful;
    private EditText et_Wonderful;
    private EditText et_GREEN;
    private EditText et_GLORY;
    private Button btn_setToHome;
    private RequestQueue queue;
    private String Getallcard_url="https://nccugo105306.000webhostapp.com/Getallcard.php";
    private List<Cardtype> cardtype_list=new ArrayList<Cardtype>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ckbox_GLORY =(CheckBox)findViewById(R.id.ckbox_GLORY);
        ckbox_Green =(CheckBox)findViewById(R.id.ckbox_Green);
        ckbox_Wonderful=(CheckBox)findViewById(R.id.ckbox_Wonderful);
        et_GLORY =(EditText)findViewById(R.id.et_GLORY);
        et_GREEN =(EditText)findViewById(R.id.et_GREEN);
        et_Wonderful =(EditText)findViewById(R.id.et_Wonderful);
        btn_setToHome =(Button)findViewById(R.id.btn_setToHome);

        btn_setToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromSettingsToHome();
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String cardtype_name = jsonObject.getString("cardtype_name");
                                    cardtype_list.add(new Cardtype(id, cardtype_name));
                                    //拿cardtype_list去調用

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
        };
        GetallcardRequest getRequest = new GetallcardRequest(responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getRequest);
    }
    public void fromSettingsToHome(){
        Intent intent = new Intent(Settings.this,Home.class);
        startActivity(intent);
    }
    public class GetallcardRequest extends StringRequest {
        private static final String Getallcard_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getallcard.php";
        private Map<String, String> params;
        //
        public GetallcardRequest(Response.Listener<String> listener) {
            super(Method.GET,  Getallcard_REQUEST_URL, listener, null);
            params = new HashMap<>();
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

}

