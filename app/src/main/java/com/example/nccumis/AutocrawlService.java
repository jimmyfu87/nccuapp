package com.example.nccumis;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nccumis.com.example.nccumis.onlineshopping.Product;
import com.example.nccumis.com.example.nccumis.onlineshopping.wishpool;
import com.example.nccumis.com.example.nccumis.onlineshopping.wishpool_channel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.constraint.motion.utils.Oscillator.TAG;

public class AutocrawlService extends Service {
    final Document[] doc = new Document[1];
    private List<Product> productlist=new ArrayList<Product>();
    public AutocrawlService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //startMyOwnForeground();
            //startOwnForeground();
            //startForeground(1,new Notification());
        }
        else{
            startForeground(1, new Notification());
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("NoValue")){
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                   // productlist.clear();
                                    int id = jsonObject.getInt("id");
                                    String product_name = jsonObject.getString("product_name");
                                    String product_price = jsonObject.getString("product_price");
                                    String product_url = jsonObject.getString("product_url");
                                    String member_id = jsonObject.getString("member_id");
                                    String channel_name = jsonObject.getString("channel_name");
                                    String upload_time = jsonObject.getString("upload_time");
                                    productlist.add(new Product(id, product_name, product_price, product_url, member_id, channel_name,upload_time));
                                    //拿productlist去調用，包含登入使用者的所有product
                                }
                                System.out.println(productlist.size());
                                for(int i=0;i<productlist.size();i++){
                                    rewebcrawl(productlist.get(i).getProduct_url(),String.valueOf(productlist.get(i).getId()));
                                    System.out.println("Execute");
                                }
                                SharedPreferences sp2 = getSharedPreferences("changeamount", MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sp2.edit();
                                Log.i(TAG, "run: executed at "+new Date().toString());
                                Log.i(TAG, "變動的商品"+String.valueOf(sp2.getInt("changeamount",0)));

                                startMyOwnForeground();

                                editor2.putInt("changeamount",0).commit();
                                productlist.clear();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            startMyOwnForeground();
                        }
                    }
                };
                SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                GetallproductRequest getRequest = new GetallproductRequest(sp.getString("member_id",null),"Momo",responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(getRequest);


            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8*60*60*10*1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
    public void rewebcrawl(String inputurl,String product_id){
        RequestQueue queue = Volley.newRequestQueue(this);
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
                            if(!inputurl.contains("momoshop.com.tw/goods")){
                                UpdateDatabase(product_id,"delete","delete");
                                //Toast.makeText(getApplicationContext(), "無法解析", Toast.LENGTH_SHORT).show();
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
                                    Elements elements3 = doc[0].getElementsByClass("priceArea").first().getElementsByTag("b");
                                    for (Element element : elements3) {
                                        sb2 = sb2 + element;
                                        sb2 = sb2.replace("<b>", "");
                                        sb2 = sb2.replace("</b>", "");
                                    }

                                    sb2=sb2.replace(",","");//移除價錢的逗號
                                    UpdateDatabase(product_id,sb,sb2);
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

                                    sb2=sb2.replace(",","");//移除價錢的逗號
                                    UpdateDatabase(product_id,sb,sb2);
                                    break;
                            }
                        } catch (Exception e) {
                            UpdateDatabase(product_id,"delete","delete");

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UpdateDatabase(product_id, "delete", "delete");
            }
        });
        queue.add(stringRequest);

    }
    public void UpdateDatabase(String product_id,String product_name,String product_price){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String change = jsonResponse.getString("change");
                    if (change.equals("change")) {
                        SharedPreferences sp = getSharedPreferences("changeamount", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        int changeamount=sp.getInt("changeamount",0)+1;
                        editor.putInt("changeamount",changeamount).commit();
                    } else if(change.equals("NoValue")){
                    }
                    else{
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        UpdatepoolRequest updatepoolRequest = new UpdatepoolRequest(product_id,product_name,product_price,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(updatepoolRequest);

    }
    public class UpdatepoolRequest extends StringRequest {
        private static final String UpdatepoolRequest_REQUEST_URL="https://nccugo105306.000webhostapp.com/Updatepool.php";
        private Map<String,String> params;

        public UpdatepoolRequest(String id,String product_name, String product_price, Response.Listener<String> listener){
            super(Method.POST, UpdatepoolRequest_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("id", id);
            params.put("product_name", product_name);
            params.put("product_price", product_price);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
    public class GetallproductRequest extends StringRequest {
        private static final String Getallproduct_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Getallproduct.php";
        private Map<String, String> params;
        //
        public GetallproductRequest(String member_id, String channel_name, Response.Listener<String> listener) {
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "自動更新價格";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW);

        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        SharedPreferences sp3 = getSharedPreferences("changeamount", MODE_PRIVATE);
        SharedPreferences.Editor editor3 = sp3.edit();
        Intent intent = new Intent(this,Home.class);
        PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0);

        Notification notification = notificationBuilder
                 .setSmallIcon(R.drawable.ic_stat_name)
                //.setContentText("價格更新通知執行中")
                .setAutoCancel(false)
                .setContentIntent(pIntent)
                .build();
        if(sp3.getInt("changeamount",0)>0) {
            Notification notification2 = notificationBuilder
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_stat_name))
                    .setContentTitle("有商品更新了喔")
                     .setContentText("快來查看吧")
                    .setLights(Color.GREEN, 1000, 1000)
                    .setAutoCancel(false)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setContentIntent(pIntent)
                    .build();
            manager.notify(1, notification2);
        }
        startForeground(2,notification);

    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void startOwnForeground(){
//        NotificationChannel channelLove = new NotificationChannel(
//                "idLove",
//                "Channel Love",
//                NotificationManager.IMPORTANCE_HIGH);
//        channelLove.setDescription("最重要的人");
//        channelLove.enableLights(true);
//        channelLove.enableVibration(true);
//
//
//        SharedPreferences sp3 = getSharedPreferences("changeamount", MODE_PRIVATE);
//        SharedPreferences.Editor editor3 = sp3.edit();
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.createNotificationChannel(channelLove);
//        Notification.Builder builder =
//                new Notification.Builder(this)
//                        .setSmallIcon(R.drawable.love)
//                        .setContentTitle("My Love")
//                        .setContentText("Hi, my love!")
//                        .setChannelId("idLove");
//        manager.notify(1, builder.build());
//    }
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        private void startOwnForeground(){
//            String NOTIFICATION_CHANNEL_ID = "com.example.update";
//            String channelName = "通知";
//            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
//            chan.setLightColor(Color.BLUE);
//            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            assert manager != null;
//            manager.createNotificationChannel(chan);
//
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//            SharedPreferences sp3 = getSharedPreferences("changeamount", MODE_PRIVATE);
//            if(true) {
//                Notification.Builder builder =
//                        new Notification.Builder(this)
//                                .setSmallIcon(R.drawable.love)
//                                .setContentTitle("My Love")
//                                .setContentText("Hi, my love!")
//                                .setChannelId("idLove");
//                manager.notify(2, builder.build());
//            }
//
//        }

}
