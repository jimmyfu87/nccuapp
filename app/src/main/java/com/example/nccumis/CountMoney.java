package com.example.nccumis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.Toast;


public class CountMoney extends AppCompatActivity {
    private final int START_DATE = 1;
    private final int END_DATE = -1;
    private EditText date_start;
    private EditText date_end;
    private Button switchBook;
    private String end_date;
    private String start_date;
    private int yearStart,monthStart,dayStart;
    private int yearEnd,monthEnd,dayEnd;
    private List<String> bookArray = new ArrayList<String>();
    private List<String> selectBooks = new ArrayList<String>();
    private List<Expense> select_expense = new ArrayList<Expense>();

    private TextView income;
    private TextView spend;
    private TextView total;
    private Button btn_checkCount;
    private boolean[] checked;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_money);
        //  抓資料庫的所有帳本
        setBookArray();
        initSelectBooks();

        //multidiolog 勾選確認欄位
        checked=new boolean[bookArray.size()];

        date_start =(EditText)findViewById(R.id.date_start);
        date_end =(EditText)findViewById(R.id.date_end);
        income =(TextView)findViewById(R.id.income);
        spend =(TextView)findViewById(R.id.spend);
        total =(TextView)findViewById(R.id.total);
        btn_checkCount=(Button)findViewById(R.id.btn_checkCount);
        switchBook = (Button)findViewById(R.id.switchBook);

        //計算

        btn_checkCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDateInput(v)){
                    //Expense 資料庫
                    DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                    dbmanager.open();
                    select_expense=dbmanager.fetchExpenseWithbook(start_date,end_date,selectBooks);
                    dbmanager.close();

                    spend.setText(Integer.toString(countTotalExpensePrice()));
                }
            }
        });

        //開始日期
        date_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePick(START_DATE);
                    date_start.setInputType(InputType.TYPE_NULL);
                    return true;
                }
                return false;
            }
        });
        date_start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePick(START_DATE);
                    date_start.setInputType(InputType.TYPE_NULL);

                }

            }
        });
        //結束日期
        date_end.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent2) {
                if (motionEvent2.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePick(END_DATE);
                    date_end.setInputType(InputType.TYPE_NULL);

                    return true;
                }
                return false;
            }
        });
        date_end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean a) {
                if (a) {
                    showDatePick(END_DATE);
                    date_end.setInputType(InputType.TYPE_NULL);

                }

            }
        });

        //選擇帳本
        switchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiDialogEvent();
            }
        });
    }
    //這個是參考姵的，還沒研究好，因為他說我沒用到這個method
    public void setStartdateformat(int year,int month,int day){
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
        start_date=year+"-"+st_month+"-"+st_day;
    }
//結束日期

    public void showDatePick(final int checkNum) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(CountMoney.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                if(checkNum == START_DATE){
                    CountMoney.this.date_start.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");
                    setStartdateformat(year,monthOfYear,dayOfMonth);
                }else{
                    CountMoney.this.date_end.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");
                    setEnddateformat(year,monthOfYear,dayOfMonth);
                }
                setdateInfo(checkNum, year,monthOfYear,dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void setEnddateformat(int year,int month,int day){
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
        end_date=year+"-"+st_month+"-"+st_day;
    }

    //暫存日期
    private void setdateInfo(int StartOrEndDate, int year, int month, int day){
        if(StartOrEndDate == this.START_DATE){
            this.yearStart = year;
            this.monthStart = month;
            this.dayStart = day;
        }else{
            this.yearEnd = year;
            this.monthEnd = month;
            this.dayEnd = day;
        }
    }

    public boolean checkDateInput(View view){
        if(this.yearStart > this.yearEnd || this.monthStart > monthEnd || this.dayStart > dayEnd){
            this.date_end.setError("結束日期小於開始日期");
            Snackbar.make(view,"結束日期小於開始日期，請重新修改",Snackbar.LENGTH_SHORT).show();
            spend.setText("");
            return false;
        }
        this.date_end.setError(null);
        return true;
    }

    public void setBookArray(){
        DatabaseManager dbmanager = new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
        dbmanager.open();
        this.bookArray = dbmanager.fetchBook();           //可直接調用select_expense的資訊
        dbmanager.close();
    }

    private void multiDialogEvent(){
        this.selectBooks.clear();
//        final List<Boolean> checkedStatusList = new ArrayList<>();
//        for(String s : bookArray){
//            checkedStatusList.add(false);
//        }
        new AlertDialog.Builder(CountMoney.this)
                .setMultiChoiceItems(bookArray.toArray(new String[bookArray.size()]), checked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                //checkedStatusList.set(which, isChecked);
                            }
                        })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuilder sb = new StringBuilder();
                        boolean isEmpty = true;
                        for(int i = 0; i < checked.length; i++){
                            if(checked[i]){
                                sb.append(bookArray.get(i));
                                sb.append(" ");
                                selectBooks.add(bookArray.get(i));
                                //System.out.println("Here"+bookArray.get(i));
                                isEmpty = false;
                            }
                        }
                        if(!isEmpty){
                            Toast.makeText(CountMoney.this, "你選擇的是"+sb.toString(), Toast.LENGTH_SHORT).show();
//                            for(String s : bookArray){
//                                checkedStatusList.add(false);
//                            }
                        } else{
                            initSelectBooks();
                            Toast.makeText(CountMoney.this, "請勾選項目，系統已自動返回預設(統計所有帳本)", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .show();
    }

    public void initSelectBooks(){
        for(int i = 0;i < this.bookArray.size(); i++){
            this.selectBooks.add(this.bookArray.get(i));
        }
    }

    public int countTotalExpensePrice(){
        int total = 0;
        for(int i = 0; i < this.select_expense.size(); i++){
            total += this.select_expense.get(i).getEx_price();
        }
        return total;
    }
}
