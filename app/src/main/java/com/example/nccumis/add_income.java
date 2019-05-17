package com.example.nccumis;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class add_income extends AppCompatActivity {
    public static final int income = 1;
    private Button lastPage;
    private Button newIncome;
    private Button comfirm;

    private EditText input_amount;
    private EditText input_date;
    private Spinner input_type;
    private Spinner input_book;
    private EditText input_note;

    private RadioButton newBookBtn;
    private RadioButton newTypeBtn;

    private List<String> type = new ArrayList<String>();        //傳給ArrayAdapter的參數
    private List<String> book = new ArrayList<String>();

    public List<String> dbBookData = new ArrayList<String>();         //接資料庫資料，Type還沒做!!!!!
    public List<String> dbTypeData = new ArrayList<String>();

    private EditText i_price,i_note,i_fixed,i_userid;                                             //宣告需要輸入的變數的EditText
    private String i_date,i_typeid,i_bookid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_add);

        //Spinner ArrayAdapter 初始化
        initType();
        initBook();

        ArrayAdapter typeList = new ArrayAdapter<String>(add_income.this,
                android.R.layout.simple_spinner_dropdown_item,
                type);
        ArrayAdapter<String> bookList = new ArrayAdapter<String>(add_income.this,
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

        //切換為新增收入
        newIncome = (Button)findViewById(R.id.newIncome);
        newIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToadd_expense();
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
                    input_date.setInputType(InputType.TYPE_NULL);      // disable soft input
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
                    input_date.setInputType(InputType.TYPE_NULL);      // disable soft input
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
                newTypeBtn.setChecked(false);
            }
        });
        input_type = (Spinner)findViewById(R.id.type_input);
        input_type.setAdapter(typeList);

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
                jumpToadd_type();
                newBookBtn.setChecked(false);
            }
        });
        input_book = (Spinner)findViewById(R.id.book_input);
        input_book.setAdapter(bookList);



        //備註
        input_note = (EditText)findViewById(R.id.note_input);

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(add_income.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                add_income.this.input_date.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //初始化類別
    public void initType(){
        String[] typeArr = {"薪水", "發票中獎","樂透中獎", "其他"};
        for(int i = 0; i < typeArr.length; i++){
            this.type.add(typeArr[i]);
        }
    }

//    public void updateType(){
//
//    }

    //帳本初始設定
    public void initBook() {
        this.book.add("現金帳本");
    }

    public void updateBook(){
        for(int i = 0 ;i < dbBookData.size();i++){
            //System.out.println(dbBookData.get(i));
            if(book.contains(dbBookData.get(i))){
                continue;
            }else{
                book.add(dbBookData.get(i));
            }
        }
    }

    public void jumpToHome(){
        Intent intent = new Intent(add_income.this,Home.class);
        startActivity(intent);
    }

    public void jumpToadd_book(){
        Intent intent = new Intent(add_income.this,add_book.class);
        Bundle saveIncomeData = new Bundle();
        saveIncomeData.putString("amount",input_amount.getText().toString());
        saveIncomeData.putString("date",input_date.getText().toString());
        saveIncomeData.putString("type",input_type.getSelectedItem().toString());
        saveIncomeData.putString("book",input_book.getSelectedItem().toString());
        saveIncomeData.putString("note",input_note.getText().toString());
        intent.putExtras(saveIncomeData);
        startActivity(intent);

    }

    public void jumpToadd_type(){
        Intent intent = new Intent(add_income.this,add_book.class);
        Bundle saveIncomeData = new Bundle();
        saveIncomeData.putString("amount",input_amount.getText().toString());
        saveIncomeData.putString("date",input_date.getText().toString());
        saveIncomeData.putString("type",input_type.getSelectedItem().toString());
        saveIncomeData.putString("book",input_book.getSelectedItem().toString());
        saveIncomeData.putString("note",input_note.getText().toString());
        saveIncomeData.putInt("FromExpenseOrIncome",income);
        intent.putExtras(saveIncomeData);
        startActivity(intent);

    }

    public void jumpToadd_expense(){
        Intent intent = new Intent(add_income.this,add_expense.class);
        startActivity(intent);
    }
}
