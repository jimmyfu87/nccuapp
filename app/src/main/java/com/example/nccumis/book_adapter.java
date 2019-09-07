package com.example.nccumis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("刪除提醒")
                        .setMessage("是否確定刪除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseManager dbmanager=new DatabaseManager(getContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                                dbmanager.open();
                                if(BookName.equals("Book")){
                                 //   dbmanager.deleteBook();
                                }else{
                                 //   dbmanager.deleteIncome(idArray.get(position));
                                }
                                dbmanager.close();


                                bookArray.remove(position);
                                ///////////資料庫刪除，加這//////////////
                                notifyDataSetChanged();
                                Snackbar.make(v, "You just remove No." + BookName +" item", Snackbar.LENGTH_SHORT).show();
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
        fixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BookName.equals("Book")){
                    jumpTobook_fixed(position, context);
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

    public void jumpTobook_fixed(int position, Activity context) {
        Intent intent = new Intent(context, book_fixed.class);
       // Bundle savebookdata = new Bundle();
      //  savebookdata.putBoolean("detail",true);

    }

    ;
}
