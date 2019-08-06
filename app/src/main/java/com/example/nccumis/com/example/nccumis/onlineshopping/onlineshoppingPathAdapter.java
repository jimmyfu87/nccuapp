package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.nccumis.R;

import java.util.List;

public class onlineshoppingPathAdapter extends ArrayAdapter {
    //to reference the Activity
    private final android.app.Activity context;
    private final List<Integer> idArray;
    private final List<String> PathNameArray;
//    private final List<String> discountDetailArray;
    private final List<String> urlArray;
    private Button enterPath_btn;


    public onlineshoppingPathAdapter(Activity context, List<Integer> idArrayParam, List<String> PathNameArrayParam, List<String>urlArrayParam){

        super(context, R.layout.onlineshoppingpath_listview_row,PathNameArrayParam);

        this.context = context;
        this.idArray = idArrayParam;
        this.PathNameArray = PathNameArrayParam;
        this.urlArray = urlArrayParam;
//        this.discountDetailArray = discountDetailArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.onlineshoppingpath_listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView PathNameTextField = (TextView) rowView.findViewById(R.id.pathname);
//        TextView discountDetailTextField = (TextView) rowView.findViewById(R.id.discountDetail);
        enterPath_btn = (Button)rowView.findViewById(R.id.enterPath);


        //this code sets the values of the objects to values from the arrays
        PathNameTextField.setText(PathNameArray.get(position));
//        discountDetailTextField.setText(discountDetailArray.get(position));

        //到各通路網購(webview)
        enterPath_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToOnlineShopping(position, context);
            }
        });
        return rowView;

    }

    public void jumpToOnlineShopping(int position,Activity activity){
        Intent intent = new Intent(activity, onlineShopping.class);
        Bundle saveChannelData = new Bundle();
        saveChannelData.putString("channel_url",urlArray.get(position));
        saveChannelData.putString("channel_name", PathNameArray.get(position));
        saveChannelData.putBoolean("firstBankDiscount", false);
        intent.putExtras(saveChannelData);
        activity.startActivity(intent);

    }
}
