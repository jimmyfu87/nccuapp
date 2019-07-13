package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class wishpool_momo extends AppCompatActivity {
    private static RequestQueue requestQueue;
    private static int finalprice = 0;
    private Button lastPage;
    private TextView ecommerceName;
    private ListView CreditCardListView;
    private ListView ProductListView;
    private TextView totalPrice;
    private TextView recommendcreditcard;
    private List wishpoolCreditcardDiscount;
    private List<String> discountDetailArray;
    private  List<Integer> pictureArray;    //還沒弄
    private  List<String> nameArray=new ArrayList<>();
    private  List<Integer> priceArray=new ArrayList<>();
    private List<Product> productlist=new ArrayList<Product>();
    private JSONArray array=new JSONArray();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishpool_momo);

        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToHome();
            }
        });

        ecommerceName = (TextView)findViewById(R.id.ecommerceName);
        ecommerceName.setText("Momo購物網");//之後從資料庫抓電商名稱

        //信用卡優惠
        CreditCardListView = (ListView)findViewById(R.id.CreditCardListView);
        CreditCardListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        setList();
//        setListViewHeightBasedOnChildren(CreditCardListView);
        //商品優惠
        ProductListView = (ListView)findViewById(R.id.ProductListView);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("NoValue")){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String product_name = jsonObject.getString("product_name");
                            String product_price = jsonObject.getString("product_price");
                            String product_url = jsonObject.getString("product_url");
                            String member_id = jsonObject.getString("member_id");
                            String channel_name = jsonObject.getString("channel_name");
                            productlist.add(new Product(id, product_name, product_price, product_url, member_id, channel_name));
                            //拿productlist去調用，包含登入使用者的所有product
                        }
                        //下面是取值方式可以參考，不用就可以刪掉
//                                for (int i = 0; i < productlist.size(); i++) {
//                                    System.out.println(productlist.get(i).getId());
//                                    System.out.println(productlist.get(i).getProduct_name());
//                                    System.out.println(productlist.get(i).getProduct_price());
//                                    System.out.println(productlist.get(i).getProduct_url());
//                                    System.out.println(productlist.get(i).getMember_id());
//                                    System.out.println(productlist.get(i).getChannel_name());
//                                }

                        setProductList();
//                        setListViewHeightBasedOnChildren(ProductListView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //這邊是發現許願池是空的處理方式，要改可以改
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(wishpool_momo.this);
                    builder.setMessage("沒有商品")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(wishpool_momo.this, Home.class);
                                    wishpool_momo.this.startActivity(intent);
                                }
                            })
                            .create()
                            .show();
                }
            }
        };
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        wishpool_momo.GetallproductRequest getRequest = new wishpool_momo.GetallproductRequest(sp.getString("member_id",null),"Momo",responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getRequest);

        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToHome();
            }
        });

        ecommerceName = (TextView)findViewById(R.id.ecommerceName);
        ecommerceName.setText("Momo購物網");//之後從資料庫抓電商名稱

        //信用卡優惠
        CreditCardListView = (ListView)findViewById(R.id.CreditCardListView);
        CreditCardListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        setList();
//        setListViewHeightBasedOnChildren(CreditCardListView);
        //商品優惠
        ProductListView = (ListView)findViewById(R.id.ProductListView);
        setProductList();
//        setListViewHeightBasedOnChildren(ProductListView);


        //計算後的總價
        totalPrice = findViewById(R.id.totalPrice);
//        countTotalPrice();
        totalPrice.setText("目前勾選金額:"+totalPrice.toString());

        //推薦信用卡
        recommendcreditcard = findViewById(R.id.recommendcreditcard);

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



//    public void initCreditCardListData(){
//        for(int i = 0; i < this.expenseList.size();i++){
//            int index = i+1;
//            this.numberArray.add(index);
//            this.idArray.add(this.expenseList.get(i).getEx_id());
//            this.dateArray.add(this.expenseList.get(i).getEx_date());
//            this.priceArray.add(this.expenseList.get(i).getEx_price());
//            this.noteArray.add((this.expenseList.get(i).getEx_note().isEmpty()) ? "無備註" : this.expenseList.get(i).getEx_note());
//            this.bookArray.add(this.expenseList.get(i).getBook_name());
//        }
//        //System.out.println(this.getPriceData.size()+" ,"+this.typeName.size());
//    }
//
//    public void setCreditCardList(){
//        initListData();
//        ExpenseIncomeDetailListAdapter ExDetail_adapter = new ExpenseIncomeDetailListAdapter("Expense",this.idArray,startDate, endDate,selectBooks, check_expense_detail.this, this.numberArray, this.dateArray, this.priceArray, this.noteArray,this.bookArray,this.type);
//        DetailListView.setAdapter(ExDetail_adapter);
//    }

        public void initProductList(){
        for(int i = 0; i < this.productlist.size();i++){
            if(this.productlist.get(i).getChannel_name().equals("Momo")){
                this.nameArray.add(this.productlist.get(i).getProduct_name());
                this.priceArray.add(Integer.parseInt(this.productlist.get(i).getProduct_price()));
            }
        }
        //System.out.println(this.getPriceData.size()+" ,"+this.typeName.size());
    }

    public void setProductList(){
        initProductList();
        productListAdapter productlist_adapter = new productListAdapter(wishpool_momo.this, nameArray , priceArray);
        ProductListView.setAdapter(productlist_adapter);
    }



    //計算總額
    public static void addFinalPrice(int para){
        finalprice += para;
    }

    public static void minusFinalPrice(int para){
        finalprice -= para;
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

    public void jumpToHome(){
        Intent intent = new Intent(wishpool_momo.this, Home.class);
        startActivity(intent);
    }
}
