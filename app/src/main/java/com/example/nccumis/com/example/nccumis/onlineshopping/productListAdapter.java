package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.nccumis.DatabaseManager;
import com.example.nccumis.R;

import java.util.ArrayList;
import java.util.List;

public class productListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;
//    private final List<Integer> pictureArray;   //  還沒弄
    private final List<String> nameArray;
    private final List<Integer> priceArray;
    private List<Boolean> isCheckArray;
    private CheckBox check;
    private Button deleteBtn;
    private int sizeOfList;


    public productListAdapter(Activity context, List<String> nameArrayParam, List<Integer> priceArrayParam){

        super(context, R.layout.product_listview_row, nameArrayParam);

        this.context=context;
//        this.pictureArray = pictureArrayParam;
        this.nameArray = nameArrayParam;
        this.priceArray = priceArrayParam;
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
        TextView nameTextField = (TextView) rowView.findViewById(R.id.name);
        TextView priceTextField = (TextView) rowView.findViewById(R.id.price);
        check = (CheckBox) rowView.findViewById(R.id.check);
        deleteBtn =(Button)rowView.findViewById(R.id.deleteBtn);
//        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(context, isChecked + "", Toast.LENGTH_SHORT).show();
//                wishpool_momo.setFinalPrice(priceArray.get(position));
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
                    Toast.makeText(context, nameArray.get(position) +" 已勾選", Toast.LENGTH_SHORT).show();
                    isCheckArray.set(position, true);
                    wishpool_momo.setisCheckedPrice(countPrice());
                }
                else
                {
                    //CheckBox狀態 : 未勾選
                    Toast.makeText(context, nameArray.get(position) +" 已取消勾選", Toast.LENGTH_SHORT).show();
                    isCheckArray.set(position, false);
                    wishpool_momo.setisCheckedPrice(countPrice());
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
                                String name = nameArray.get(position); //要刪除的product name
                                ///////////資料庫刪除，加這//////////////
                                nameArray.remove(position);
                                priceArray.remove(position);
                                notifyDataSetChanged();
                                Snackbar.make(v, "You just remove No." + name +" item", Snackbar.LENGTH_SHORT).show();
                                wishpool_momo.setListViewHeightBasedOnChildren(wishpool_momo.ProductListView);
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
        priceTextField.setText(priceArray.get(position).toString());
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
}
