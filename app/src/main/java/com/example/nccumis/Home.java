package com.example.nccumis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity {
    private Button addSpend;
    private Button checkSpend;
    private  String dateinStart;
    private String dateinEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        autoSetFirstandEndMonth();
        DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
        List<Expense> select_expense=new ArrayList<>();
        dbmanager.open();
        select_expense=dbmanager.fetchExpense(dateinStart,dateinEnd);           //可直接調用select_expense的資訊
        dbmanager.close();


        setContentView(R.layout.home);


        //到記帳
        addSpend = (Button) findViewById(R.id.addSpend);
        addSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToadd_expense();
            }
        });

        //到查帳
        checkSpend = (Button) findViewById(R.id.checkSpend);
        checkSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpTocheck_expense();
            }
        });
    }



    public void jumpToadd_expense(){
        Intent intent = new Intent(Home.this,add_expense.class);
        startActivity(intent);
    }

    public void jumpTocheck_expense(){
        Intent intent = new Intent(Home.this,check_expense.class);
        startActivity(intent);
    }

    public void autoSetFirstandEndMonth(){
        List<String> oddMonth = new ArrayList<String>();
        oddMonth.add("1");
        oddMonth.add("3");
        oddMonth.add("5");
        oddMonth.add("7");
        oddMonth.add("8");
        oddMonth.add("10");
        oddMonth.add("12");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String year = Integer.toString(cal.get(Calendar.YEAR));
        String month = Integer.toString(cal.get(Calendar.MONTH) +1);

        this.dateinStart = year+"-"+month+"-"+1;
        if(month == "2"){
            int intYear = Integer.parseInt(year);
            if ((intYear % 4 == 0 && intYear % 100 != 0) || (intYear % 400 == 0 && intYear % 3200 != 0)){
                this.dateinEnd = year+"-"+month+"-"+29;
            }else {
                this.dateinEnd = year+"-"+month+"-"+28;
            }
        }else if(oddMonth.contains(month)){
            this.dateinEnd = year+"-"+month+"-"+31;
        }else {
            this.dateinEnd = year+"-"+month+"-"+30;
        }

        //System.out.println(this.dateinStart+" ,"+this.dateinEnd);
    }

}

