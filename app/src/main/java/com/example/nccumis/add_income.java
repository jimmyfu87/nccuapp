package com.example.nccumis;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;


public class add_income extends AppCompatActivity {
    private Button lastPage;
    private Button newIncome;
    private Button comfirm;

    private TextView input_amount;
    private TextView input_date;
    private TextView input_type;
    private TextView input_book;
    private TextView input_note;

    private EditText i_price,i_note,i_fixed,i_userid;                                             //宣告需要輸入的變數的EditText
    private String i_date,i_typeid,i_bookid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_add);
        i_price =(EditText)findViewById(R.id.amount_input);
        i_note =(EditText)findViewById(R.id.note_input);

        //不儲存回首頁
        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToHome();
            }
        });

        //切換為新增收入
        newIncome = (Button)findViewById(R.id.newIncome);
        newIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //確認
        comfirm = (Button)findViewById(R.id.comfirm);
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputInfo()){

                    jumpToHome();
                }else {


                }
            }
        });
        //金額
        input_amount = (TextView)findViewById(R.id.amount_input);

        //日期
        input_date = (TextView)findViewById(R.id.date_input);
        input_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        input_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg();
                }

            }
        });

        //類別
        final Spinner input_type = (Spinner)findViewById(R.id.type_input);
        final String[] type = {"薪水", "發票中獎","樂透中獎", "其他","新增類別"};
        ArrayAdapter<String> typeList = new ArrayAdapter<>(add_income.this,
                android.R.layout.simple_spinner_dropdown_item,
                type);
        input_type.setAdapter(typeList);

        input_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                i_typeid = input_type.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?>parent){

            }
        });

        //帳本
        Spinner input_book = (Spinner)findViewById(R.id.book_input);
        final String[] book = {"現金帳本" ,"新增帳本"};
        ArrayAdapter<String> bookList = new ArrayAdapter<>(add_income.this,
                android.R.layout.simple_spinner_dropdown_item,
                book);
        input_book.setAdapter(bookList);






        //備註
        input_note = (TextView)findViewById(R.id.note_input);



    }

    //檢查輸入的值是否正確
    public boolean checkInputInfo(){
        int amount = Integer.parseInt(input_amount.getText().toString());
        if(amount < 0 || amount > Integer.MAX_VALUE){
            return false;
        }
        if(input_date.getText().toString().isEmpty()){
            return false;
        }
        if(input_note.getText().toString().isEmpty()){
            return false;
        }

        return true;
    }





    public void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(add_income.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                add_income.this.input_date.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void setdateformat(int year, int month, int day){
        String st_month;
        String st_day;
        if(month<10){
            st_month = Integer.toString(month);
            st_month = "0"+st_month;
        }
        else{
            st_month = Integer.toString(month);
        }
        if (day < 10) {
            st_day = Integer.toString(day);
            st_day = "0"+st_day;
        }
        else{
            st_day = Integer.toString(day);
        }
        i_date = year+"-"+st_month+"-"+st_day;
    }

    public void jumpToHome(){



    }
}
