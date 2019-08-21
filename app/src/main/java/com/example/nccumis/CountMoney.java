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
    private TextView date_start;
    private TextView date_end;
    private Button switchBook;
    private String end_date;
    private String start_date;
    private int yearStart,monthStart,dayStart;
    private int yearEnd,monthEnd,dayEnd;
    private List<String> bookArray = new ArrayList<String>();
    private List<String> selectBooks = new ArrayList<String>();
    private List<Expense> select_expense = new ArrayList<Expense>();
    private List<Income> select_income = new ArrayList<Income>();

    private TextView income;
    private TextView expense;
    private TextView remain;
    private Button btn_checkCount;
    private boolean[] checked;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_money);
        //  抓資料庫的所有帳本
        setBookArray();
        initSelectBooks();

        date_start =(TextView) findViewById(R.id.date_start);
        date_end =(TextView) findViewById(R.id.date_end);
        income =(TextView)findViewById(R.id.income);
        expense =(TextView)findViewById(R.id.expense);
        remain =(TextView)findViewById(R.id.Remain);
        btn_checkCount=(Button)findViewById(R.id.btn_checkCount);
        switchBook = (Button)findViewById(R.id.switchBook);

        //拉資料庫的支出進來
        btn_checkCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDateInput(v)){
                    //Expense 資料庫
                    DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                    dbmanager.open();
                    select_expense=dbmanager.fetchExpenseWithbook(start_date,end_date,selectBooks);
                    select_income=dbmanager.fetchIncomeWithbook(start_date,end_date,selectBooks);
                    dbmanager.close();
                    expense.setText(Integer.toString(countTotalExpensePrice())); //支出
                    income.setText(Integer.toString(countTotalIncomePrice()));  //收入
                    remain.setText(Integer.toString(countRemain()));    //期間花費餘額
                }
            }
        });

        //開始日期
        date_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePick(START_DATE);
                    date_start.setInputType(InputType.TYPE_NULL);//讓使用者無法打東西
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
                    date_start.setInputType(InputType.TYPE_NULL); //讓使用者無法打東西

                }

            }
        });
        //結束日期
        date_end.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent2) {
                if (motionEvent2.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePick(END_DATE);
                    date_end.setInputType(InputType.TYPE_NULL);//讓使用者無法打東西

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
                    date_end.setInputType(InputType.TYPE_NULL);//讓使用者無法打東西

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

    //把(開始日期)年月日轉換成---
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

    //把(結束日期)年月日轉換成---
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

    //確認日期是否有效，可否查詢
    public boolean checkDateInput(View view){
        if(this.yearStart > this.yearEnd || (this.yearStart <= this.yearEnd && this.monthStart > monthEnd) || (this.yearStart <= this.yearEnd && this.monthStart <= monthEnd && this.dayStart > dayEnd)){
            this.date_end.setError("結束日期小於開始日期");
            Snackbar.make(view,"結束日期小於開始日期，請重新修改",Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(date_start.getText().toString().isEmpty()||date_end.getText().toString().isEmpty()){
            this.date_end.setError("開始或結束日期未填寫");
            Snackbar.make(view,"請填寫開始和結束日期",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        expense.setText("");
        income.setText("");
        remain.setText("");
        this.date_end.setError(null);
        return true;
    }

    //拉資料庫的帳本
    public void setBookArray(){
        DatabaseManager dbmanager = new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
        dbmanager.open();
        this.bookArray = dbmanager.fetchBook();           //可直接調用select_expense的資訊
        dbmanager.close();
        this.checked = new boolean[this.bookArray.size()];

        for(int i = 0; i < checked.length; i++){
            checked[i] = true;
        }
    }

    //使用者選擇帳本時會跳出來的小框框
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

    //計算總支出
    public int countTotalExpensePrice(){
        int total = 0;
        for(int i = 0; i < this.select_expense.size(); i++){
            total += this.select_expense.get(i).getEx_price();
        }
        return total;
    }

    //計算總收入
    public int countTotalIncomePrice(){
        int total = 0;
        for(int i = 0; i < this.select_income.size(); i++){
            total += this.select_income.get(i).getIn_price();
        }
        return total;
    }

    //計算餘額
    public int countRemain(){
        return countTotalIncomePrice() - countTotalExpensePrice();
    }
}
