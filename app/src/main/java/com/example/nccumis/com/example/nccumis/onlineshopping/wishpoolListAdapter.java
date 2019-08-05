package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nccumis.R;

import java.util.List;

public class wishpoolListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final android.app.Activity context;
    private final List<String> PathNameArray;


    public wishpoolListAdapter(Activity context, List<String> pathNameArrayArrayParam){

        super(context, R.layout.wishpool_listview_row);

        this.context = context;
        this.PathNameArray = pathNameArrayArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.wishpool_listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView PathNameTextField = (TextView) rowView.findViewById(R.id.pathname);


        //this code sets the values of the objects to values from the arrays
        PathNameTextField.setText(PathNameArray.get(position));

        return rowView;

    }
}
