package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.nccumis.R;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_shopping);

        //回App上一頁
        btn_lastPage = (Button)findViewById(R.id.btn_lastPage);
        btn_lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                jumpToLastPage();
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
                ///////////////爬蟲加這之後//////////////////

            }
        });

        //檢查是否從firstbankDiscount回來
        Intent getSaveData = getIntent();
        Bundle getSaveBag = getSaveData.getExtras();
        if(getSaveBag != null){
            getWebHomeURL = getSaveBag.getString("webHomeURL");
            getWebName = getSaveBag.getString("webName");

            OnlineShopping_webView.loadUrl(getSaveBag.getString("webURL"));
            WebSettings webSettings = OnlineShopping_webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
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

    private void jumpToFirstBankDiscountPage(){
        Intent intent = new Intent(OnlineShopping.this, firstBankDiscount.class);
        Bundle savedWebData = new Bundle();
        savedWebData.putString("webName", getWebName);
        savedWebData.putString("webURL", getCurrentWebURL());
        savedWebData.putString("webHomeURL", getWebHomeURL);
        intent.putExtras(savedWebData);
        startActivity(intent);
    }
}