package com.example.nccumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.view.MotionEvent;
import java.util.Calendar;


public class add_expense extends AppCompatActivity {
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

    private EditText i_price,i_note,i_fixed,i_userid;                                             //宣告需要輸入的變數的EditText
    private String i_date,i_typeid,i_bookid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_add);
        i_price=(EditText)findViewById(R.id.amount_input);  //將amount_input從View轉為EditText
        i_note=(EditText)findViewById(R.id.note_input);    //將note_input從View轉為EditText



        //不儲存回首頁
        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToHome();
            }
        });

        //切換為新增支出
        newExpense = (Button)findViewById(R.id.newExpense);
        newExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jumpToAddIncome();
            }
        });

        //確認
        comfirm = (Button)findViewById(R.id.comfirm);
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputInfo()){
                    //到首頁
                    int price=Integer.parseInt(i_price.getText().toString());                               //將price轉為int
                    String note=i_note.getText().toString();
                    DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());
                    dbmanager.open();                                                                       //開啟、建立資料庫(if not exists)
                    dbmanager.insert(price,i_date,i_typeid,i_bookid,note,"",1);            //將資料放到資料庫
                    dbmanager.close();                                                                      //關閉資料庫
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
        final Spinner input_type = (Spinner)findViewById(R.id.type_input);
        final String[] type = {"早餐", "午餐", "晚餐", "飲料", "零食", "交通", "投資", "醫療", "衣物", "日用品", "禮品", "購物", "娛樂", "水電費", "電話費", "房租", "其他","新增類別"};
        ArrayAdapter<String> typeList = new ArrayAdapter<>(add_expense.this,
                android.R.layout.simple_spinner_dropdown_item,
                type);
        input_type.setAdapter(typeList);
        //取回type的值
        input_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i_typeid = input_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       /*
        if(input_type.getAdapter().equals("新增類別")){
            setContentView(R.layout.type_add);
        }
        */

        //帳本
        final Spinner input_book = (Spinner)findViewById(R.id.book_input);                  //改成final才能使用
        final String[] book = {"現金帳本", "新增帳本"};
        ArrayAdapter<String> bookList = new ArrayAdapter<>(add_expense.this,
                android.R.layout.simple_spinner_dropdown_item,
                book);
        input_book.setAdapter(bookList);
        //取回book的值
        input_book.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i_bookid = input_book.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(input_book.getAdapter().equals("新增帳本")){
            setContentView(R.layout.book_add);
        }

        //付款人
        input_payer = (TextView)findViewById(R.id.payer_input);

        //備註
        input_note = (TextView)findViewById(R.id.note_input);

        //掃發票
        scanInvoice = (Button) findViewById(R.id.scanInvoice);


        //固定支出
        regularExpense = (Button) findViewById(R.id.regularExpense);
        regularExpense.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                }
                return false;
            }
        });

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(add_expense.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                setdateformat(year,monthOfYear,dayOfMonth);
                add_expense.this.input_date.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
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
        i_date=year+"-"+st_month+"-"+st_day;
    }

    public void jumpToHome(){

        setContentView(R.layout.home);

    }

    //////////
    public void setRegularExpense(){

    }
}
