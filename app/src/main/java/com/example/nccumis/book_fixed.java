package com.example.nccumis;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class book_fixed extends AppCompatActivity {
    private Button btn_changebookname;
    private Button btn_amountchange;
    private Button btn_check_detail;
    private Button btn_lastpage;
    private TextView et_bookname;
    private TextView et_amount;
    private List<Expense> select_expense = new ArrayList<Expense>();
    private ArrayList<String> selectBooks = new ArrayList<String>();
    private String start_date,end_date;
    private ListView TypeListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_fixed);
        btn_changebookname = (Button) findViewById(R.id.btn_changebookname);
        btn_amountchange = (Button) findViewById(R.id.btn_amountchange);
        btn_check_detail = (Button) findViewById(R.id.btn_check_detail);
        btn_lastpage = (Button) findViewById(R.id.btn_lastpage);
        et_bookname = (TextView) findViewById(R.id.et_bookname);
        et_amount = (TextView) findViewById(R.id.et_amount);

        btn_lastpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backtolastpage();
            }
        });
        btn_changebookname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebookname();
            }
        });
        btn_amountchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountchange();
            }
        });
        btn_check_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                dbmanager.open();
                select_expense=dbmanager.fetchExpenseWithbook(start_date,end_date,selectBooks);
                dbmanager.close();
                setExpenseData(select_expense);
                setList();
                setListViewHeightBasedOnChildren(TypeListView);

            }
        });
    }

    public void setList() {
    }

    private void setExpenseData(List<Expense> select_expense) {
    }


    public void changebookname() {
        final EditText input = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("請輸入你的名稱")
                .setView(input)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String inputName = input.getText().toString();
                        Log.d("Main", "你輸入的名稱為：" + inputName);
                        et_bookname.setText(inputName);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Main", "click cancel");
                    }
                })
                .create();
        dialog.show();


    }
    public void amountchange(){
        final EditText input2 = new EditText(this);
        AlertDialog dialog2 = new AlertDialog.Builder(this)
                .setTitle("重新輸入你的預算")
                .setView(input2)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String inputAmount = input2.getText().toString();
                        Log.d("Main", "新預算：" + inputAmount);
                        et_amount.setText(inputAmount);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Main", "click cancel");
                    }
                })
                .create();
        dialog2.show();


    }

    public void backtolastpage() {
        Intent intent = new Intent(book_fixed.this,BookManage.class);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
