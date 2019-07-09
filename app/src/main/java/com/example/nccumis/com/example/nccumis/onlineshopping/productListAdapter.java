package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.nccumis.R;

import java.util.List;

public class productListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;
//    private final List<Integer> pictureArray;   //  還沒弄
    private final List<String> nameArray;
    private final List<Integer> priceArray;
    private CheckBox check;


    public productListAdapter(Activity context, List<String> nameArrayParam, List<Integer> priceArrayParam){

        super(context, R.layout.product_listview_row, nameArrayParam);

        this.context=context;
//        this.pictureArray = pictureArrayParam;
        this.nameArray = nameArrayParam;
        this.priceArray = priceArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.spend_listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
//        TextView pictureTextField = (TextView) rowView.findViewById(R.id.picture);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.name);
        TextView priceTextField = (TextView) rowView.findViewById(R.id.price);
        check = (CheckBox) rowView.findViewById(R.id.check);

        //this code sets the values of the objects to values from the arrays
//        pictureTextField.setText(pictureArray.get(position).toString());
        nameTextField.setText(nameArray.get(position));
        priceTextField.setText(priceArray.get(position).toString());
        return rowView;
    }
}
