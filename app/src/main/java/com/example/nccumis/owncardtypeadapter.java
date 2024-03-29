package com.example.nccumis;

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
import com.example.nccumis.com.example.nccumis.onlineshopping.productListAdapter;
import com.example.nccumis.com.example.nccumis.onlineshopping.wishpool_channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class owncardtypeadapter extends ArrayAdapter{
    //to reference the Activity
    private final Activity context;
    private final List<Integer> idArray;
    private final List<String> nameArray;
//    private List<Boolean> isCheckArray;
    private Button deleteBtn;
    private SharedPreferences sp;
//    private int sizeOfList;


    public owncardtypeadapter(Activity context, List<Integer> idArrayParam , List<String> nameArrayParam){

        super(context, R.layout.setting_owncardtype_listview_row, nameArrayParam);

        this.context=context;
//        this.pictureArray = pictureArrayParam;
        this.idArray = idArrayParam;
        this.nameArray = nameArrayParam;
//        this.isCheckArray = new ArrayList<Boolean>();
//        this.sizeOfList = nameArrayParam.size();
//        for(int i = 0; i < sizeOfList; i++){
//            isCheckArray.add(false);
//        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.setting_owncardtype_listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
//        TextView pictureTextField = (TextView) rowView.findViewById(R.id.picture);
        TextView idTextField = (TextView)rowView.findViewById(R.id.id);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.cardtypename);

        deleteBtn =(Button)rowView.findViewById(R.id.deleteBtn);


//        check.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                //判斷CheckBox是否有勾選，同mCheckBox.isChecked()
//                if(isChecked)
//                {
//                    //CheckBox狀態 : 已勾選
////                    Toast.makeText(context, nameArray.get(position) +" 已勾選", Toast.LENGTH_SHORT).show();
//                    isCheckArray.set(position, true);
//                }
//                else
//                {
//                    //CheckBox狀態 : 未勾選
////                    Toast.makeText(context, nameArray.get(position) +" 已取消勾選", Toast.LENGTH_SHORT).show();
//                    isCheckArray.set(position, false);
//                }
//            }
//        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("刪除提醒")
                        .setMessage("是否確定刪除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String delete_name = nameArray.get(position);   //要加入的信用卡 name
                                int delete_id = idArray.get(position);      //  要加入的信用卡 id
                                ///////////資料庫othercard新增，owncard刪除//////////////

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
                                                notifyDataSetChanged();

                                                refresh(context);
                                            } else {
                                                Snackbar.make(v, "刪除失敗", Snackbar.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                SharedPreferences sp = context.getSharedPreferences("User", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                DeletecardrelationRequest deleteproductRequest = new DeletecardrelationRequest(sp.getString("member_id",null),delete_name,responseListener);
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
        return rowView;


    }

    public class AddcardrelationRequest extends StringRequest {
        private static final String Addcardrelation_REQUEST_URL = "https://nccugo105306087.000webhostapp.com/Addcardrelation.php";
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
        private static final String Deletecardrelation_REQUEST_URL = "https://nccugo105306087.000webhostapp.com/Deletecardrelation.php";
        private Map<String, String> params;
        public DeletecardrelationRequest(String member_id ,String cardtype_name, Response.Listener<String> listener) {
            super(Method.POST,  Deletecardrelation_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("member_id", member_id);
            params.put("cardtype_name", cardtype_name);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    private void refresh(Activity activity) {
        activity.finish();
        Intent intent = new Intent(activity, activity.getClass());
        activity.startActivity(intent);
    }
}
