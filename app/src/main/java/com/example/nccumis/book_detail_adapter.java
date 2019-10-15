package com.example.nccumis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import java.util.concurrent.ConcurrentHashMap;

//流水帳
public class book_detail_adapter extends ArrayAdapter {
    //to reference the Activity
    private ArrayList<String> selectBooks;
    private final Activity context;
    private final List<Integer> idArray;
    private final List<String> nameArray;
    private final List<Integer> amount_startArray;
    private final List<Integer> amount_remainArray;
    private final List<String> i_currencyidArray;
    private Button fixBtn;
    private Button deleteBtn;


    //, Button fixParam, Button deleteParam
    public book_detail_adapter (Activity context, List<Integer> idArrayParam , List<String> nameArrayParam, List<Integer> Amount_startArrayParam, List<Integer> Amount_remainArrayParam, List<String> currency_typeArrayParam){
        super(context, R.layout.activity_book_adapter,nameArrayParam);

        this.idArray = idArrayParam;
        this.context=context;
        this.nameArray = nameArrayParam;
        this.amount_remainArray = Amount_remainArrayParam;
        this.amount_startArray = Amount_startArrayParam;
        this.i_currencyidArray = currency_typeArrayParam;
    }
    public View getView(final int position, final View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_book_adapter, null,true);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.name);
        TextView Amount_startTextField = (TextView) rowView.findViewById(R.id.amount_start);
        TextView Amount_remainTextField = (TextView) rowView.findViewById(R.id.amount_remain);
        TextView currency_typeTextField = (TextView) rowView.findViewById(R.id.currency_type);
        fixBtn = (Button) rowView.findViewById(R.id.fixBtn);
        deleteBtn = (Button) rowView.findViewById(R.id.deleteBtn);

        nameTextField.setText(nameArray.get(position));
        Amount_startTextField.setText(amount_startArray.get(position).toString());
        Amount_remainTextField.setText(amount_remainArray.get(position).toString());
        currency_typeTextField.setText(i_currencyidArray.get(position));

        fixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                jumpToadd_book(position, context);
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
                                DatabaseManager dbmanager=new DatabaseManager(getContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                                dbmanager.open();
                                dbmanager.deleteBook(idArray.get(position));
                                dbmanager.close();
                                String bookName = nameArray.get(position);
                                idArray.remove(position);
                                nameArray.remove(position);
                                amount_startArray.remove(position);
                                amount_remainArray.remove(position);
                                i_currencyidArray.remove(position);
                                notifyDataSetChanged();
                                Snackbar.make(v, "You just remove  " + bookName , Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make(v, "You didn't remove any book", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
        return rowView;
    };


    public void jumpToadd_book(int position,Activity activity){
        Intent intent = new Intent(activity, add_book.class);
        Bundle saveBookData = new Bundle();
        saveBookData.putInt("id",idArray.get(position));
        saveBookData.putString("name", nameArray.get(position).toString());
        saveBookData.putString("amount_start",amount_startArray.get(position).toString());
        saveBookData.putInt("amount_remain", amount_remainArray.get(position));
        saveBookData.putString("currency_type", i_currencyidArray.get(position));
        saveBookData.putBoolean("FromBookManage", true);
        intent.putExtras(saveBookData);
        activity.startActivity(intent);

    }




    }

