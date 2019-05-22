package com.example.nccumis;

import android.app.Activity;
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


public class ExpenseDetailListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final int DETAIL = 1;
    private final Activity context;
    private final List<Integer> numberArray;
    private final List<String> dateArray;
    private final List<Integer> priceArray;
    private final List<String> noteArray;
    private final ArrayList<String> bookArray;
    private String typeName;
    private Button fixBtn;
    private Button deleteBtn;

    //, Button fixParam, Button deleteParam
    public ExpenseDetailListAdapter(Activity context, List<Integer> numberArrayParam, List<String> dateArrayParam, List<Integer> priceArrayParam, List<String> noteArrayParam, ArrayList<String> bookArrayParam , String typeName){

        super(context, R.layout.detail_listview_row, dateArrayParam);

        this.context=context;
        this.numberArray = numberArrayParam;
        this.dateArray = dateArrayParam;
        this.priceArray = priceArrayParam;
        this.noteArray = noteArrayParam;
        this.bookArray = bookArrayParam;
        this.typeName = typeName;
    }

    public View getView(final int position, final View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.detail_listview_row, null,true);

        //this code gets references to objects in the detail_listview_row.xml file
        TextView numberTextField = (TextView) rowView.findViewById(R.id.number);
        TextView dateTextField = (TextView) rowView.findViewById(R.id.date);
        TextView priceTextField = (TextView) rowView.findViewById(R.id.price);
        TextView noteTextField = (TextView) rowView.findViewById(R.id.note);
        TextView bookTextField = (TextView) rowView.findViewById(R.id.book) ;
        fixBtn = (Button) rowView.findViewById(R.id.fixBtn);
        deleteBtn = (Button) rowView.findViewById(R.id.deleteBtn);

        //this code sets the values of the objects to values from the arrays
        numberTextField.setText(numberArray.get(position).toString());
        dateTextField.setText(dateArray.get(position));
        priceTextField.setText(priceArray.get(position).toString());
        noteTextField.setText(noteArray.get(position));
        bookTextField.setText(bookArray.get(position));

        fixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToadd_expense(position, context);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = numberArray.get(position);
                numberArray.remove(position);
                dateArray.remove(position);
                priceArray.remove(position);
                noteArray.remove(position);
                bookArray.remove(position);
                notifyDataSetChanged();
                Snackbar.make(v, "You just remove No." + number +" item", Snackbar.LENGTH_SHORT).show();
            }
        });

        return rowView;
    };

    public void jumpToadd_expense(int position,Activity activity){
        Intent intent = new Intent(activity, add_expense.class);
        Bundle saveExpenseData = new Bundle();
        saveExpenseData.putBoolean("detail",true);
        saveExpenseData.putString("amount", priceArray.get(position).toString());
        saveExpenseData.putString("date", resetDateformat(dateArray.get(position)));
        saveExpenseData.putString("type", typeName);
        saveExpenseData.putString("book", dateArray.get(position));
        saveExpenseData.putString("note", noteArray.get(position));
        saveExpenseData.putString("saveDetailStartdate",dateArray.get(0));
        saveExpenseData.putString("saveDetailEnddate",dateArray.get(dateArray.size()-1));
        saveExpenseData.putStringArrayList("saveDetailBooksArray",bookArray);
        intent.putExtras(saveExpenseData);
        activity.startActivity(intent);

    }

    public String resetDateformat(String date){
        String resetDate = "";
        int index = 0;

        for(String str : date.split("-")){
            resetDate += (Integer.parseInt(str) < 10) ? str.substring(1): str;
            if(index == 0){
                resetDate += "年";
            }else if(index == 1){
                resetDate +="月";
            }else if(index == 2){
                resetDate += "日";
            }
            index++;
        }

        return resetDate;
    }
}
