package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nccumis.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;


public class OnlineShopping extends AppCompatActivity {
    private WebView OnlineShopping_webView;
    private String getWebHomeURL;       //從優惠店家取得首頁的URL，目前先塞Momo首頁
    private String getWebName;             //從優惠店家取得名稱，目前先塞Momo首頁
    private Button btn_lastPage;
    private Button btn_webHome;
    private Button btn_webPrePage;
    private Button btn_webNextPage;
    private Button btn_FirstBankDiscount;
    private Button btn_addInCart;
    final Document[] doc = new Document[1];
    private RequestQueue queue;
    private String member_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_shopping);

        //回App上一頁
        btn_lastPage = (Button)findViewById(R.id.btn_lastPage);
        btn_lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToOnlineShoppingPath();
            }
        });

        //到網購首頁
        btn_webHome = (Button)findViewById(R.id.btn_webHome);
        btn_webHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToWebHome();
            }
        });

        //到網購前頁
        btn_webPrePage = (Button) findViewById(R.id.btn_webPrePage);
        btn_webPrePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OnlineShopping_webView.canGoBack()){
                    OnlineShopping_webView.goBack();
                }else{
                    finish();
                }
            }
        });

        //到網購後頁
        btn_webNextPage = (Button) findViewById(R.id.btn_webNextPage);
        btn_webNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OnlineShopping_webView.canGoForward()){
                    OnlineShopping_webView.goForward();
                }else{
                    finish();
                }
            }
        });

        //webview
        getWebName = "Momo";
        getWebHomeURL = "https://www.momoshop.com.tw/main/Main.jsp?cid=mtab&oid=logo&mdiv=1000100000-bt_0_199_01-bt_0_199_01_P1_1_e1&ctype=B"; //此行的URL會根據使用者上一頁點的優惠店家而決定，目前先測試Momo購物網

        OnlineShopping_webView = (WebView) findViewById(R.id.OnlineShopping_webView);
        OnlineShopping_webView.setVerticalScrollBarEnabled(true);
        OnlineShopping_webView.setWebViewClient(new WebViewClient());
        jumpToWebHome();

        //跳到一銀優惠資訊頁
        btn_FirstBankDiscount = (Button)findViewById(R.id.btn_FirstBankDiscount);
        btn_FirstBankDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToFirstBankDiscountPage();
            }
        });

        //加入許願池
        btn_addInCart = (Button)findViewById(R.id.btn_addInCart);
        btn_addInCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentWebURL = getCurrentWebURL();//給你們爬蟲的URL
                webcrawl(currentWebURL);
                //jumpToDemo(currentWebURL);
                ///////////////爬蟲加這之後//////////////////

            }
        });

        //檢查是否從firstbankDiscount回來 或從 onlineShoppingPath/wishpool_channel 回來
        Intent getSaveData = getIntent();
        Bundle getSaveBag = getSaveData.getExtras();
        if(getSaveBag != null){
            if(getSaveBag.getBoolean("firstBankDiscount")){
                getWebHomeURL = getSaveBag.getString("webHomeURL");
                getWebName = getSaveBag.getString("webName");
                OnlineShopping_webView.loadUrl(getSaveBag.getString("webURL"));
            }else{
                getWebHomeURL = getSaveBag.getString("channel_webHome");
                getWebName = getSaveBag.getString("channel_name");
                OnlineShopping_webView.loadUrl(getSaveBag.getString("channel_url"));
            }
            WebSettings webSettings = OnlineShopping_webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            OnlineShopping_webView.setVerticalScrollBarEnabled(true);
            OnlineShopping_webView.setHorizontalScrollBarEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (OnlineShopping_webView.canGoBack()) {
            OnlineShopping_webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    //取得目前web的URL
    public String getCurrentWebURL(){
        return OnlineShopping_webView.getUrl();
    }

    public void jumpToWebHome(){
        OnlineShopping_webView.loadUrl(getWebHomeURL);
        WebSettings webSettings = OnlineShopping_webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

//    public void jumpToLastPage(){
//        startActivity(new Intent(OnlineShopping.this, discountEcommerce.class));
//    }

    public void jumpToOnlineShoppingPath(){
        startActivity(new Intent(OnlineShopping.this, onlineShoppingPath.class));

    }

    private void jumpToFirstBankDiscountPage(){
        Intent intent = new Intent(OnlineShopping.this, firstBankDiscount.class);
        Bundle savedWebData = new Bundle();
        savedWebData.putString("webName", getWebName);
        savedWebData.putString("webURL", getCurrentWebURL());
        savedWebData.putString("webHomeURL", getWebHomeURL);
        intent.putExtras(savedWebData);
        startActivity(intent);
    }
    private void jumpToDemo(String urldemo){
        Intent intent = new Intent(OnlineShopping.this, Webcrawler.class);
        Bundle savedWebData = new Bundle();
        savedWebData.putString("webURL",urldemo);
        intent.putExtras(savedWebData);
        startActivity(intent);
    }
    public void webcrawl(String inputurl){
        String finalInputurl = inputurl;
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
            Toast.makeText(getApplicationContext(), "無法解析", Toast.LENGTH_SHORT).show();
            return;
        }
        queue = Volley.newRequestQueue(this);

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
                                    //行動版
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
                                        InsertIntoDatabase(sb, sb2, finalInputurl, "Momo");
                                        break;
                                    //電腦版
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
                                        InsertIntoDatabase(sb, sb2, finalInputurl, "Momo");
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

                                    InsertIntoDatabase(product_name,product_price,finalInputurl,"Pchome");
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "無法解析", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

    }
    public void InsertIntoDatabase(String product_name,String product_price,String product_url,String channel_name){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String success = jsonResponse.getString("success");
                    if (success.equals("success")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OnlineShopping.this);
                        builder.setMessage("成功加入許願池")
                                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();

                    }
                    else if(success.equals("same")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(OnlineShopping.this);
                        builder.setMessage("已加入過許願池")
                                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(OnlineShopping.this);
                        builder.setMessage("加入許願池失敗")
                                .setPositiveButton("知道了", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        member_id=sp.getString("member_id",null);
        AddtopoolRequest addtopoolRequest = new AddtopoolRequest(product_name,product_price, product_url,member_id,channel_name,responseListener);
        RequestQueue queue = Volley.newRequestQueue(OnlineShopping.this);
        queue.add(addtopoolRequest);

    }
    public class AddtopoolRequest extends StringRequest {
        private static final String Addtopool_REQUEST_URL="https://nccugo105306.000webhostapp.com/Addtopool.php";
        private Map<String,String> params;

        public AddtopoolRequest(String product_name,String product_price, String product_url,String member_id,String channel_name, Response.Listener<String> listener){
            super(Method.POST, Addtopool_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("product_name", product_name);
            params.put("product_price", product_price);
            params.put("product_url",  product_url);
            params.put("member_id",member_id );
            params.put("channel_name", channel_name);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }


    }
}