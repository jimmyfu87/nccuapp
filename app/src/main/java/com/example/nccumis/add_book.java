package com.example.nccumis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

//
public class add_book extends AppCompatActivity {
    private Button lastPage;
    private Button comfirm;

    private EditText input_bookName;
    private EditText input_startBudget;
    private Spinner input_currency;
    private Switch default_book;

    private Intent savedDataFromExpense;
    private Bundle saveBag;

    private String i_currencyid;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_add);
        savedDataFromExpense = getIntent();
        saveBag = savedDataFromExpense.getExtras();


        //帳本名稱
        input_bookName = (EditText) findViewById(R.id.bookName_input);

        //起始金額
        input_startBudget = (EditText) findViewById(R.id.startBudget_input);


        //不儲存回 新增支出 或 新增收入
        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToaddSpend(saveBag);
            }
        });


        //確認
        comfirm = (Button)findViewById(R.id.comfirm);
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputInfo()){
                    //到下一頁
                    String i_bookName=input_bookName.getText().toString();
                    int i_startBudget=Integer.parseInt(input_startBudget.getText().toString());
                    int i_remain=i_startBudget;
                    DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());
                    dbmanager.open();                                                                       //開啟、建立資料庫(if not exists)
                    dbmanager.insert_Book(i_bookName,i_startBudget,i_remain,i_currencyid,1);            //將資料放到資料庫
                    dbmanager.close();                                                                      //關閉資料庫
                    jumpToaddSpend(saveBag);
                }
            }
        });


        //貨幣
        //預設世界前幾常用的貨幣
        input_currency = (Spinner)findViewById(R.id.currency_input);
        final String[] currency = {"TWD", "USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "RMB", "SEK", "NZD", "MXN", "SGD", "HDK", "KRW", "TRY", "RUB", "BRL", "INR", "ZAR", "DKK", "PLN", "NOK"};
        ArrayAdapter<String> currencyList = new ArrayAdapter<>(add_book.this,
                android.R.layout.simple_spinner_dropdown_item,
                currency);
        input_currency.setAdapter(currencyList);
        input_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i_currencyid = input_currency.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //預設帳本
        default_book = (Switch) findViewById(R.id.default_book);

    }

    //檢查輸入的值是否正確
    public boolean checkInputInfo(){
        int amount = 0;
        try
        {
            amount = Integer.parseInt(input_startBudget.getText().toString());
        }
        catch (NumberFormatException e)
        {
            // handle the exception
            if(input_startBudget.getText().toString().isEmpty()){
                input_startBudget.setError("起始金額未填寫");
            }else{
                input_startBudget.setError("起始金額太大");
            }

        }
        if(amount < 0){
            input_startBudget.setError("輸入金額小於零");
            return false;
        }
        if(input_bookName.getText().length() > 20){
            input_bookName.setError("輸入名稱太長");
            return false;
        }
        if(input_bookName.getText().toString().isEmpty()){
            input_bookName.setError("輸入名稱未填寫");
            return false;
        }
        return true;
    }

    public void jumpToaddSpend(Bundle prelayoutData){
        Intent intent = new Intent(add_book.this,add_expense.class);
        intent.putExtras(prelayoutData);
        startActivity(intent);
    }
}
