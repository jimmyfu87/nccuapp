package com.example.nccumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.view.MotionEvent;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private Button lastPage;
    private Button newExpense;
    private Button comfirm;

    private TextView input_amount;
    private TextView input_date;
    private TextView input_type;
    private TextView input_book;
    private TextView input_payer;
    private TextView input_note;

    private Button scanInvoice;
    private Button regularExpense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //不儲存回首頁
        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //切換為新增支出
        newExpense = (Button)findViewById(R.id.newExpense);
        newExpense.setOnClickListener(new View.OnClickListener() {
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
        final String[] type = {"早餐", "午餐", "晚餐", "飲料", "零食", "交通", "投資", "醫療", "衣物", "日用品", "禮品", "購物", "娛樂", "水電費", "電話費", "房租", "其他","新增類別"};
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

        //付款人
        input_payer = (TextView)findViewById(R.id.payer_input);

        //備註
        input_note = (TextView)findViewById(R.id.note_input);

        //掃發票
        scanInvoice = (Button) findViewById(R.id.scanInvoice);


        //固定支出
        regularExpense = (Button) findViewById(R.id.regularExpense);


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
