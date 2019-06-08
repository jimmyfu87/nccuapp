package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.nccumis.R;


public class OnlineShopping extends AppCompatActivity {
    private WebView OnlineShopping_webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_shopping);

        OnlineShopping_webView = (WebView) findViewById(R.id.OnlineShopping_webView);
        OnlineShopping_webView.setWebViewClient(new WebViewClient());
        OnlineShopping_webView.loadUrl("https://shopee.tw/");

        WebSettings webSettings = OnlineShopping_webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    @Override
    public void onBackPressed() {
        if (OnlineShopping_webView.canGoBack()) {
            OnlineShopping_webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}