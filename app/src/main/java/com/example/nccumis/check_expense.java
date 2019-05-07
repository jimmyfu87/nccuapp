package com.example.nccumis;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class check_expense extends AppCompatActivity {
    private final int startDate = -1;
    private final int endDate = 1;

    private int[] getData = {98, 123, 161, 24, 52, 58, 35, 13, 78, 203, 240, 159};
    private String[] typeName = {"Jan" , "Feb" , "Mar" , "Apr" , "May" , "Jun" , "Jul" , "Aug" , "Sep" , "Oct" , "Nov" , "Dec"};

    private Button lastPage;
    private Button switchAccount;
    private EditText dateStart_input;
    private EditText dateEnd_input;
    private String start_date,end_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_check);
        //上一頁
        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToHome();
            }
        });

        //切換帳戶
        switchAccount = (Button) findViewById(R.id.switchAccount);
        switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                List<Expense> select_expense=new ArrayList<>();
                dbmanager.open();
                select_expense=dbmanager.fetchExpense(start_date,"2019-05-28");           //可直接調用select_expense的資訊
                dbmanager.close();
                jumpToHome();

            }
        });

        //起始日期
        dateStart_input = (EditText)findViewById(R.id.dateStart_input);
        dateStart_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg(startDate);
                    return true;
                }
                return false;
            }
        });
        dateStart_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg(startDate);
                }

            }
        });
        //結束日期
        dateEnd_input = (EditText)findViewById(R.id.dateEnd_input);
        dateEnd_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg(endDate);
                    return true;
                }
                return false;
            }
        });
        dateEnd_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg(1);
                }

            }
        });

        //圖表
        setPieChart();

    }

    public void showDatePickDlg(final int checknum) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(check_expense.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                set_start_dateformat(year,monthOfYear,dayOfMonth);
                // -1是startDate  1是EndDate
                if(checknum == startDate){
                    check_expense.this.dateStart_input.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");
                }else{
                    check_expense.this.dateEnd_input.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");
                }

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }



    public void setPieChart(){
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i = 0; i < getData.length; i++){
            pieEntries.add(new PieEntry(getData[i] , typeName[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries , "類別");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart expenseChart = (PieChart) findViewById(R.id.expense_chart);
        expenseChart.setData(data);
        expenseChart.invalidate();
    }

    public void jumpToHome(){
        Intent intent = new Intent(check_expense.this,Home.class);
        startActivity(intent);
    }
    public void set_start_dateformat(int year,int month,int day){
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
    public void set_end_dateformat(int year,int month,int day){
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




    //不用重拉另一個xml 資料庫傳該帳本資料進來
//    public void jumpToAnotherBook(){
//        Intent intent = new Intent(check_expense.this,Home.class);
//        startActivity(intent);
//    }
}
