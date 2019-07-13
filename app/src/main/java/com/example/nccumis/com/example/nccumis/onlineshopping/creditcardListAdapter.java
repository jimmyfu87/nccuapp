package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.nccumis.R;

import java.util.List;

public class creditcardListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;
    private final List<String> discountDetailArray;
    private CheckBox check;


    public creditcardListAdapter(Activity context,List<String> discountDetailArrayParam){

        super(context, R.layout.creditcarddiscount_listview_row);

        this.context=context;
        this.discountDetailArray = discountDetailArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.spend_listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView discountDetailTextField = (TextView) rowView.findViewById(R.id.discountDetail);
//        check = (CheckBox) rowView.findViewById(R.id.check);

        //this code sets the values of the objects to values from the arrays
        discountDetailTextField.setText(discountDetailArray.get(position));

//        check.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                //判斷CheckBox是否有勾選，同mCheckBox.isChecked()
//                if(isChecked)
//                {
//                    //CheckBox狀態 : 已勾選
//                }
//                else
//                {
//                    //CheckBox狀態 : 未勾選
//                }
//            }
//        });

        return rowView;

    }
}
