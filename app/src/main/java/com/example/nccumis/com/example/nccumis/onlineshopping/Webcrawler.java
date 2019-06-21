package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nccumis.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Webcrawler extends AppCompatActivity {

    private Bundle saveBag;
    private Intent getPreSavedData;
    private String inputurl;
    private RequestQueue queue;
    private TextView name;
    private TextView price;
    final Document[] doc = new Document[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webcrawler);
        queue = Volley.newRequestQueue(this);
        name = (TextView) findViewById(R.id.originalcode);
        price = (TextView) findViewById(R.id.price);
        getPreSavedData = getIntent();
        saveBag = getPreSavedData.getExtras();
        inputurl=saveBag.getString("webURL");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, inputurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                            if(!title.contains("momoshop.com.tw/goods")){
                                name.setText("未知品名");
                                price.setText("未知價格");
                                Toast.makeText(getApplicationContext(), "無法解析", Toast.LENGTH_SHORT).show();
                                return;
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
                                    name.setText(sb);
                                    Elements elements3 = doc[0].getElementsByClass("priceArea").first().getElementsByTag("b");
                                    for (Element element : elements3) {
                                        sb2 = sb2 + element;
                                        sb2 = sb2.replace("<b>", "");
                                        sb2 = sb2.replace("</b>", "");
                                    }
                                    price.setText(sb2);
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
                                    //電腦版
                                    name.setText(sb);
                                    Elements elements5 = doc[0].getElementsByClass("special").first().getElementsByTag("span");
                                    for (Element element : elements5) {
                                        sb2 = "";
                                        sb2 = sb2 + element;
                                        sb2 = sb2.replace("<span>", "");
                                        sb2 = sb2.replace("</span>", "");
                                    }
                                    price.setText(sb2);
                                    break;
                            }
                        } catch (Exception e) {
                            name.setText("未知品名");
                            price.setText("未知價格");
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
}

