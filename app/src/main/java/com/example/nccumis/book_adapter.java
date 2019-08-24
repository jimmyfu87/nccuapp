package com.example.nccumis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class book_adapter extends ArrayAdapter {
    private String BookName;
    private final ArrayList<String> bookArray;
    private final Activity context;
    private Button fixBtn;
    private Button deleteBtn;


    public book_adapter(BookManage bookManage, List<String> bookArray, String bookName, Activity context, ArrayList<String> bookArrayParam) {
        super(context, R.layout.activity_book_adapter,bookArrayParam);
        BookName = bookName;
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
        fixBtn = (Button) rowView.findViewById(R.id.fixBtn);
        deleteBtn = (Button) rowView.findViewById(R.id.deleteBtn);
        //
        fixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BookName.equals("Book")){
                    jumpToadd_book(position, context);
                }else{
                    jumpToBookManage(position, context);
                }
            }
        });

        return rowView;

    }

    public void jumpToBookManage(int position, Activity context) {
        Intent intent = new Intent(context, BookManage.class);
    }

    public void jumpToadd_book(int position, Activity context) {
        Intent intent = new Intent(context, add_book.class);
        Bundle savebookdata = new Bundle();
        savebookdata.putBoolean("detail",true);

    }

    ;
}
