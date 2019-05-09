package com.example.nccumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;


public class add_expense extends AppCompatActivity {
    private Button lastPage;
    private Button newExpense;
    private Button comfirm;

    private EditText input_amount;
    private EditText input_date;
    private Spinner input_type;
    private Spinner input_book;                          //改成final才能使用
    private EditText input_payer;
    private EditText input_note;

    private RadioButton newBookBtn;
    private RadioButton newTypeBtn;

    private Button scanInvoice;
    private Button regularExpense;


    private List<String> type = new ArrayList<String>();         //傳給ArrayAdapter的參數
    private List<String> book = new ArrayList<String>();

    public List<String> dbBookData = new ArrayList<>();         //接資料庫資料，Type還沒做!!!!!
    public List<String> dbTypeData = new ArrayList<>();

    private EditText i_price,i_note,i_userid;                  //宣告需要輸入的變數的EditText
    private String i_date,i_type_name,i_book_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_add);

        i_price=(EditText)findViewById(R.id.amount_input);  //將amount_input從View轉為EditText
        i_note=(EditText)findViewById(R.id.note_input);    //將note_input從View轉為EditText

        //Spinner ArrayAdapter 初始化
        initType();
        initBook();

        ArrayAdapter typeList = new ArrayAdapter<>(add_expense.this,
                android.R.layout.simple_spinner_dropdown_item,
                type);
        ArrayAdapter<String> bookList = new ArrayAdapter<>(add_expense.this,
                android.R.layout.simple_spinner_dropdown_item,
                book);

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
                jumpToadd_income();
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
                    dbmanager.insert_Ex(price,i_date,i_type_name,i_book_name,note,1);            //將資料放到資料庫
                    dbmanager.close();                                                                      //關閉資料庫
                    jumpToHome();
                }
            }
        });


        //金額
        input_amount = (EditText)findViewById(R.id.amount_input);

        //日期
        input_date = (EditText)findViewById(R.id.date_input);
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
        this.newTypeBtn = (RadioButton)findViewById(R.id.newTypeBtn) ;
        newTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToadd_type();
            }
        });
        this.input_type = (Spinner)findViewById(R.id.type_input);
        input_type.setAdapter(typeList);
        //取回type的值
        input_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i_type_name = input_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //帳本，已串聯Book的資料庫
        DatabaseManager dbmanager = new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
        dbmanager.open();
        this.dbBookData = dbmanager.fetchBook();           //可直接調用select_expense的資訊
        dbmanager.close();
        updateBook();

        this.newBookBtn = (RadioButton)findViewById(R.id.newBookBtn) ;
        newBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToadd_book();
            }
        });
        this.input_book = (Spinner)findViewById(R.id.book_input);
        input_book.setAdapter(bookList);

        //取回book的值
        input_book.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i_book_name = input_book.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //付款人
        //input_payer = (EditText)findViewById(R.id.payer_input);

        //備註
        input_note = (EditText)findViewById(R.id.note_input);

        //掃發票
        //scanInvoice = (Button) findViewById(R.id.scanInvoice);

        //從add_book 或 add_type 返回 填過的資料自動傳入
        Intent getSaveData = getIntent();
        Bundle getSaveBag = getSaveData.getExtras();
        if(getSaveBag != null){
            input_amount.setText(getSaveBag.getString("amount"));
            input_date.setText(getSaveBag.getString("date"));
            int typePosition = typeList.getPosition(getSaveBag.getString("type"));
            input_type.setSelection(typePosition);
            int bookPosition = bookList.getPosition(getSaveBag.getString("book"));
            input_book.setSelection(bookPosition);
            input_note.setText(getSaveBag.getString("note"));
            updateBook();
            input_book.setAdapter(bookList);

        }


        //固定支出
//        regularExpense = (Button) findViewById(R.id.regularExpense);
//        regularExpense.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    //檢查輸入的值是否正確
    //付款人沒做
    public boolean checkInputInfo(){
        int amount = 0;
        try
        {
            amount = Integer.parseInt(input_amount.getText().toString());
        }
        catch (NumberFormatException e)
        {
            // handle the exception
            if(input_amount.getText().toString().isEmpty()){
                input_amount.setError("輸入金額未填寫");
            }else{
                input_amount.setError("輸入金額太大");
            }
            return false;
        }
        if(amount < 0) {
            input_amount.setError("輸入金額小於零");
            return false;
        }
        if(input_date.getText().toString().isEmpty()){
            input_date.setError("輸入日期有誤");
            return false;
        }
        if(input_note.getText().length() > 100){
            input_note.setError("輸入備註太長");
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

    //初始化類別
    public void initType(){
        String[] typeArr = {"早餐", "午餐", "晚餐", "飲料", "零食", "交通", "投資", "醫療", "衣物", "日用品", "禮品", "購物", "娛樂", "水電費", "電話費", "房租", "其他"};
        for(int i = 0; i < typeArr.length; i++){
            this.type.add(typeArr[i]);
        }
    }

    public void updateType(){

    }

    //帳本初始設定
    public void initBook() {
        this.book.add("現金帳本");
    }

    public void updateBook(){
        for(int i = 0 ;i < dbBookData.size();i++){
            System.out.println(dbBookData.get(i));
            if(book.contains(dbBookData.get(i))){
                continue;
            }else{
                book.add(dbBookData.get(i));
            }
        }
    }

    public void jumpToHome(){
        Intent intent = new Intent(add_expense.this,Home.class);
        startActivity(intent);
    }

    public void jumpToadd_book(){
        Intent intent = new Intent(add_expense.this,add_book.class);
        Bundle saveExpenseData = new Bundle();
        saveExpenseData.putString("amount",input_amount.getText().toString());
        saveExpenseData.putString("date",input_date.getText().toString());
        saveExpenseData.putString("type",input_type.getSelectedItem().toString());
        saveExpenseData.putString("book",input_book.getSelectedItem().toString());
        saveExpenseData.putString("note",input_note.getText().toString());
        intent.putExtras(saveExpenseData);
        startActivity(intent);

    }

    public void jumpToadd_type(){
        Intent intent = new Intent(add_expense.this,add_type.class);
        Bundle saveExpenseData = new Bundle();
        saveExpenseData.putString("amount",input_amount.getText().toString());
        saveExpenseData.putString("date",input_date.getText().toString());
        saveExpenseData.putString("type",input_type.getSelectedItem().toString());
        saveExpenseData.putString("book",input_book.getSelectedItem().toString());
        saveExpenseData.putString("note",input_note.getText().toString());
        intent.putExtras(saveExpenseData);
        startActivity(intent);

    }

    public void jumpToadd_income(){
        Intent intent = new Intent(add_expense.this,add_income.class);
        startActivity(intent);
    }

}
