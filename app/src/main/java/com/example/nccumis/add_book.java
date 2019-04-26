package com.example.nccumis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

public class add_book extends AppCompatActivity {
    private Button lastPage;
    private Button comfirm;

    private EditText input_bookName;
    private EditText input_startBudget;
    private Spinner input_currency;
    private Switch default_book;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_add);

        //不儲存回 新增支出 或 新增收入
        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToaddExpense();
            }
        });


        //確認
        comfirm = (Button)findViewById(R.id.comfirm);
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputInfo()){
                    //到下一頁
                    jumpToaddExpense();
                }
            }
        });
        //帳本名稱
        input_bookName = (EditText) findViewById(R.id.bookName_input);

        //起始金額
        input_startBudget = (EditText) findViewById(R.id.startBudget_input);

        //貨幣
        //預設世界前幾常用的貨幣
        Spinner input_currency = (Spinner)findViewById(R.id.currency_input);
        final String[] currency = {"TWD", "USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "RMB", "SEK", "NZD", "MXN", "SGD", "HDK", "KRW", "TRY", "RUB", "BRL", "INR", "ZAR", "DKK", "PLN", "NOK"};
        ArrayAdapter<String> currencyList = new ArrayAdapter<>(add_book.this,
                android.R.layout.simple_spinner_dropdown_item,
                currency);
        input_currency.setAdapter(currencyList);

        //預設帳本
        default_book = (Switch) findViewById(R.id.default_book);

    }

    //檢查輸入的值是否正確
    public boolean checkInputInfo(){
        int amount = Integer.parseInt(input_startBudget.getText().toString());
        if(amount < 0 || amount > Integer.MAX_VALUE){
            return false;
        }
        if(input_bookName.getText().toString().isEmpty()){
            return false;
        }
        if(input_currency.getAdapter().toString().isEmpty()){
            return false;
        }
        return true;
    }

    public void jumpToaddExpense(){
        Intent intent = new Intent(this,add_expense.class);
        startActivity(intent);
    }
}
