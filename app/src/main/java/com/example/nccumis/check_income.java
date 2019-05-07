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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class check_income extends AppCompatActivity {
    private int[] getData = {98, 123, 161, 24, 52, 58, 35, 13, 78, 203, 240, 159};
    private String[] typeName = {"Jan" , "Feb" , "Mar" , "Apr" , "May" , "Jun" , "Jul" , "Aug" , "Sep" , "Oct" , "Nov" , "Dec"};

    private Button lastPage;
    private Button switchAccount;
    private EditText dateStart_input;
    private EditText dateEnd_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_check);

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

            }
        });

        //起始日期
        dateStart_input = (EditText)findViewById(R.id.dateStart_input);
        dateStart_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        dateStart_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg();
                }

            }
        });
        //結束日期
        dateEnd_input = (EditText)findViewById(R.id.dateEnd_input);
        dateEnd_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        dateEnd_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg();
                }

            }
        });

        //切換圖表
        setPieChart();

    }

    public void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(check_income.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                //setdateformat(year,monthOfYear,dayOfMonth);
                check_income.this.dateStart_input.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");

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

        PieChart incomeChart = (PieChart) findViewById(R.id.income_chart);
        incomeChart.setData(data);
        incomeChart.invalidate();
    }

    public void jumpToHome(){
        Intent intent = new Intent(check_income.this,Home.class);
        startActivity(intent);
    }

}
