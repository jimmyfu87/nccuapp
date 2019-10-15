package com.example.nccumis;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.nccumis.com.example.nccumis.onlineshopping.wishpool_channel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//
public class add_book extends AppCompatActivity {
    private Button lastPage;
    private Button confirm;

    private EditText input_bookName;
    private EditText input_startBudget;
    private Spinner input_currency;
    private Switch default_book;

    private Intent savedDataFromExpense;
    private Bundle saveBag;
    private int JumpToWhere = 0;

    private String i_currencyid;
    private EditText input_startdate;
    private EditText input_enddate;
    private String i_startdate;
    private String i_enddate;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_add);
        savedDataFromExpense = getIntent();
        saveBag = savedDataFromExpense.getExtras();
        JumpToWhere = saveBag.getInt("FromExpenseOrIncome");
        saveBag.remove("FromExpenseOrIncome");
        saveBag.putBoolean("detail",false);

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
        confirm = (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputInfo()){
                    //到下一頁
                    String i_bookName=input_bookName.getText().toString();
                    int i_startBudget=Integer.parseInt(input_startBudget.getText().toString());
                    int i_remain=i_startBudget;
                    DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());
                    dbmanager.open();                                                                       //開啟、建立資料庫(if not exists)
                    dbmanager.insert_Book(i_bookName,i_startBudget,i_remain,i_currencyid,i_startdate,i_enddate,0);            //將資料放到資料庫
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

        //開始日期
        input_startdate = (EditText)findViewById(R.id.startdate_input);
        input_startdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    input_startdate.setInputType(InputType.TYPE_NULL);      // disable soft input
                    showDatePickDlg(true);
                    return true;
                }
                return false;
            }
        });
        input_startdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    input_startdate.setInputType(InputType.TYPE_NULL);      // disable soft input
                    showDatePickDlg(true);
                }

            }
        });

        //結束日期
        input_enddate = (EditText)findViewById(R.id.enddate_input);
        input_enddate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    input_enddate.setInputType(InputType.TYPE_NULL);      // disable soft input
                    showDatePickDlg(false);
                    return true;
                }
                return false;
            }
        });
        input_enddate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    input_enddate.setInputType(InputType.TYPE_NULL);      // disable soft input
                    showDatePickDlg(false);
                }

            }
        });


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
                return false;
            }else{
                input_startBudget.setError("起始金額太大");
                return false;
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
        if(input_startdate.getText().toString().isEmpty()){
            input_startdate.setError("輸入日期有誤");
            return false;
        }
        if(input_enddate.getText().toString().isEmpty()){
            input_enddate.setError("輸入日期有誤");
            return false;
        }
        if(isDate2Bigger(input_startdate.getText().toString(),input_enddate.getText().toString())){
            input_startdate.setError("輸入日期有誤");
            input_enddate.setError("輸入日期有誤");
            Toast.makeText(add_book.this, "結束日期大於起始日期", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void jumpToaddSpend(Bundle prelayoutData){
        //確定jump回哪裡
        if(this.JumpToWhere == add_income.INCOME){
            Intent intent = new Intent(add_book.this, add_income.class);
            intent.putExtras(prelayoutData);
            startActivity(intent);
        }else {
            Intent intent = new Intent(add_book.this, add_expense.class);
            intent.putExtras(prelayoutData);
            startActivity(intent);
        }
    }
    public void showDatePickDlg(boolean isstart) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(add_book.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                setdateformat(year,monthOfYear,dayOfMonth,isstart);
                if(isstart)
                    add_book.this.input_startdate.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");
                else
                    add_book.this.input_enddate.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void setdateformat(int year,int month,int day,boolean isstart){
        String st_month;
        String st_day;
        if(month<10){
            st_month=Integer.toString(month);
            st_month="0"+st_month;
        }
        else{
            st_month=Integer.toString(month);
        }
        if(day<10){
            st_day=Integer.toString(day);
            st_day="0"+st_day;
        }
        else{
            st_day=Integer.toString(day);
        }
        if(isstart)
            i_startdate=year+"-"+st_month+"-"+st_day;
        else
            i_enddate=year+"-"+st_month+"-"+st_day;
    }
    public static boolean isDate2Bigger(String str1, String str2) {
        boolean isBigger = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        try {
          Date  dt1 = sdf.parse(str1);
          Date  dt2 = sdf.parse(str2);
          if (dt1.getTime() > dt2.getTime()) {
                isBigger = true;
            } else if (dt1.getTime() <= dt2.getTime()) {
                isBigger = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isBigger;
    }

}
