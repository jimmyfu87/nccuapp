package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nccumis.R;
import com.example.nccumis.add_book;
import com.example.nccumis.add_expense;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class productListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;
//    private final List<Integer> pictureArray;   //  還沒弄
    private final List<Integer> idArray;
    private final List<String> urlArray;
    private final List<String> nameArray;
    private final List<Integer> priceArray;
    private final List<String> uploadTimeArray;
    private String channel_name;
    private String channel_webHome;
    private List<Boolean> isCheckArray;
    private CheckBox check;
    private Button deleteBtn;
    private Button productWebBtn;
    private int sizeOfList;
    private SharedPreferences sp;
    private Button record;


    public productListAdapter(Activity context, String channel_nameParam, String channel_webHomeParam, List<String> urlArrayParam ,List<Integer> idArrayParam, List<String> nameArrayParam, List<Integer> priceArrayParam, List<String> uploadTimeArrayParam){

        super(context, R.layout.product_listview_row, nameArrayParam);

        this.context=context;
        this.channel_name = channel_nameParam;
        this.channel_webHome = channel_webHomeParam;
//        this.pictureArray = pictureArrayParam;
        this.idArray = idArrayParam;
        this.urlArray = urlArrayParam;
        this.nameArray = nameArrayParam;
        this.priceArray = priceArrayParam;
        this.uploadTimeArray = uploadTimeArrayParam;
        this.isCheckArray = new ArrayList<Boolean>();
        this.sizeOfList = nameArrayParam.size();
        for(int i = 0; i < sizeOfList; i++){
            isCheckArray.add(false);
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.product_listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
//        TextView pictureTextField = (TextView) rowView.findViewById(R.id.picture);
        TextView idTextField = (TextView)rowView.findViewById(R.id.id);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.name);
        TextView priceTextField = (TextView) rowView.findViewById(R.id.price);
        TextView uploadTimeTextField = (TextView) rowView.findViewById(R.id.recentUpdateTime);

        check = (CheckBox) rowView.findViewById(R.id.check);
        deleteBtn =(Button)rowView.findViewById(R.id.deleteBtn);
        productWebBtn = (Button)rowView.findViewById(R.id.productWebBtn);
        record=(Button)rowView.findViewById(R.id.record);
//        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(context, isChecked + "", Toast.LENGTH_SHORT).show();
//                wishpool_channel.setFinalPrice(priceArray.get(position));
//            }
//        });

        check.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                //判斷CheckBox是否有勾選，同mCheckBox.isChecked()
                if(isChecked)
                {
                    //CheckBox狀態 : 已勾選
//                    Toast.makeText(context, nameArray.get(position) +" 已勾選", Toast.LENGTH_SHORT).show();
                    isCheckArray.set(position, true);
                    wishpool_channel.setisCheckedPrice(countPrice());
                    wishpool_channel.updateActivity();
                }
                else
                {
                    //CheckBox狀態 : 未勾選
//                    Toast.makeText(context, nameArray.get(position) +" 已取消勾選", Toast.LENGTH_SHORT).show();
                    isCheckArray.set(position, false);
                    wishpool_channel.setisCheckedPrice(countPrice());
                    wishpool_channel.updateActivity();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("刪除提醒")
                        .setMessage("是否確定刪除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String delete_name = nameArray.get(position);   //要刪除的product name
                                int delete_id = idArray.get(position);      //要刪除的product id
                                ///////////資料庫刪除，加這//////////////
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println(response);
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                idArray.remove(position);
                                                nameArray.remove(position);
                                                priceArray.remove(position);
                                                notifyDataSetChanged();
                                                Snackbar.make(v, "You just remove No." + delete_name +" item", Snackbar.LENGTH_SHORT).show();
                                                wishpool_channel.setListViewHeightBasedOnChildren(wishpool_channel.ProductListView);

                                            } else {
                                                Snackbar.make(v, "刪除失敗", Snackbar.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                DeleteproductRequest deleteproductRequest = new DeleteproductRequest(String.valueOf(delete_id),responseListener);
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                queue.add(deleteproductRequest);

                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make(v, "You didn't remove any item", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        productWebBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToOnlineShopping(position, context);
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("記帳提醒")
                        .setMessage("是否要將此商品記帳並移除許願池？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String record_name = nameArray.get(position);   //要刪除的product name
                                int record_id = idArray.get(position);      //要刪除的product id
                                int record_price=priceArray.get(position);
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println(response);
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                idArray.remove(position);
                                                nameArray.remove(position);
                                                priceArray.remove(position);
                                                notifyDataSetChanged();
                                                Snackbar.make(v, "You just remove No." + record_name +" item", Snackbar.LENGTH_SHORT).show();
                                                wishpool_channel.setListViewHeightBasedOnChildren(wishpool_channel.ProductListView);
                                                jumpToadd_expense_withrecord(record_price,record_name);

                                            } else {
                                                Snackbar.make(v, "刪除失敗", Snackbar.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                DeleteproductRequest deleteproductRequest = new DeleteproductRequest(String.valueOf(record_id),responseListener);
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                queue.add(deleteproductRequest);

                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make(v, "You didn't remove any item", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        //this code sets the values of the objects to values from the arrays
//        pictureTextField.setText(pictureArray.get(position).toString());
        nameTextField.setText(nameArray.get(position));
        priceTextField.setText("$"+priceArray.get(position).toString());
        uploadTimeTextField.setText("上次更新時間: "+uploadTimeArray.get(position));
        return rowView;


    }

    public int countPrice(){
        int finalPrice = 0;
        for(int i = 0; i < sizeOfList; i++){
            if(isCheckArray.get(i)){
                finalPrice += priceArray.get(i);
            }
        }
        return finalPrice;
    }
    public class DeleteproductRequest extends StringRequest {
        private static final String Deleteproduct_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Deleteproduct.php";
        private Map<String, String> params;
        //
        public DeleteproductRequest(String id,Response.Listener<String> listener) {
            super(Method.POST, Deleteproduct_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("id", id);//product_id
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    public void jumpToOnlineShopping(int position, Activity activity){
        Intent intent = new Intent(activity, OnlineShopping.class);
        Bundle saveWishpoolProductData = new Bundle();
        saveWishpoolProductData.putString("channel_url", urlArray.get(position));
        saveWishpoolProductData.putString("channel_name", channel_name);
        saveWishpoolProductData.putString("channel_webHome", channel_webHome);
        intent.putExtras(saveWishpoolProductData);
        activity.startActivity(intent);
    }
    public void jumpToadd_expense_withrecord(int product_price_int,String product_name){
        String product_price=String.valueOf(product_price_int);
        Intent intent= new Intent(this.context,add_expense.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle poolrecord = new Bundle();
        poolrecord.putString("amount",product_price);
        poolrecord.putString("note",product_name);
        intent.putExtras(poolrecord);
        this.context.startActivity(intent);
    }
}
