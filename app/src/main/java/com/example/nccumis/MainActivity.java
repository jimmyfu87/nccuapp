package com.example.nccumis;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private Button lastPage;
    private Button newIncome;
    private Button comfirm;

    private TextView input_amount;
    private TextView input_date;
    private TextView input_type;
    private TextView input_book;
    private TextView input_note;

    private Button scanInvoice;
    private Button regularIncome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income);

        //不儲存回首頁
        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    //到首頁
                    jumpToHome();
                }else {
                    //把沒填好的部分填好

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
        Spinner input_type = (Spinner)findViewById(R.id.type_input);
        final String[] type = {"薪水", "發票中獎","樂透中獎", "其他","新增類別"};
        ArrayAdapter<String> typeList = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                type);
        input_type.setAdapter(typeList);

        //帳本
        Spinner input_book = (Spinner)findViewById(R.id.book_input);
        final String[] book = {"現金帳本", "一銀存摺帳本", "一銀信用卡帳本" ,"新增帳本"};
        ArrayAdapter<String> bookList = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                book);
        input_book.setAdapter(bookList);



        //備註
        input_note = (TextView)findViewById(R.id.note_input);

        //掃發票
        scanInvoice = (Button) findViewById(R.id.scanInvoice);


        //固定收入
        regularIncome = (Button) findViewById(R.id.regularIncome);


    }

    //檢查輸入的值是否正確
    public boolean checkInputInfo(){
        int amount = Integer.parseInt(input_amount.getText().toString());
        if(amount < 0 ){
            return false;
        }

        return true;
    }

    public void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                MainActivity.this.input_date.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void jumpToHome(){

        setContentView(R.layout.home);

    }
}
