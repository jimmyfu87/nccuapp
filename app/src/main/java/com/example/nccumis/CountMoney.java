package com.example.nccumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.view.MotionEvent;
import android.widget.TextView;


public class CountMoney extends AppCompatActivity {
    private Spinner spin_account;
    private EditText date_start;
    private EditText date_end;
    private String startEnd_date;
    private TextView income;
    private TextView spend;
    private TextView total;
    private Button btn_checkCount;

    private String start_date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_money);

        Spinner notify =(Spinner)findViewById(R.id.spin_account);
        date_start =(EditText)findViewById(R.id.date_start);
        date_end =(EditText)findViewById(R.id.date_end);
        income =(TextView)findViewById(R.id.income);
        spend =(TextView)findViewById(R.id.spend);
        total =(TextView)findViewById(R.id.total);
        btn_checkCount=(Button)findViewById(R.id.btn_checkCount);

        //計算

        btn_checkCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                List<Expense> select_expense=new ArrayList<>();
                dbmanager.open();
                select_expense=dbmanager.fetchExpense(start_date,"2019-05-28");           //可直接調用select_expense的資訊
                dbmanager.close();

            }

        });

        //選擇帳本
        ArrayAdapter<CharSequence> nAdapter = ArrayAdapter.createFromResource(this,R.array.Account, android.R.layout.simple_spinner_dropdown_item);
        nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notify.setAdapter(nAdapter);

        //開始日期
        date_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        date_start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg();
                }

            }
        });
        //結束日期
        date_end.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent2) {
                if (motionEvent2.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePick();
                    return true;
                }
                return false;
            }
        });
        date_end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean a) {
                if (a) {
                    showDatePick();
                }

            }
        });

    }
    //開始日期
    public void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(CountMoney.this, new DatePickerDialog.OnDateSetListener() {
            @Override

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                setdateformat(year,monthOfYear,dayOfMonth);
                CountMoney.this.date_start.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    //這個是參考姵的，還沒研究好，因為他說我沒用到這個method
    public void setdateformat(int year,int month,int day){
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
        startEnd_date=year+"-"+st_month+"-"+st_day;
    }
//結束日期

    public void showDatePick() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(CountMoney.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                setdateformat(year,monthOfYear,dayOfMonth);
                CountMoney.this.date_end.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void setdateformat2(int year,int month,int day){
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
        startEnd_date=year+"-"+st_month+"-"+st_day;
    }

}
