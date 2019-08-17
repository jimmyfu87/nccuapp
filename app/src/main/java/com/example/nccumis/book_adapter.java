package com.example.nccumis;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class book_adapter extends ArrayAdapter {
    private final ArrayList<String> bookArray;
    private final Activity context;

    public book_adapter(BookManage bookManage, List<String> bookArray, Activity context, ArrayList<String> bookArrayParam) {
        super(context, R.layout.activity_book_adapter,bookArrayParam);
        this.context = context;
        this.bookArray = bookArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_book_adapter, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView bookTextField = (TextView) rowView.findViewById(R.id.book);


        //this code sets the values of the objects to values from the arrays
        bookTextField.setText(bookArray.get(position).toString());

        return rowView;

    };
}
