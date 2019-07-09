package com.example.nccumis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.GestureDetector;
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

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;


public class add_expense extends AppCompatActivity {
    private static final int EXPENSE = -1;
    private boolean detail = false;
    private Button lastPage;
    private Button newExpense;
    private Button confirm;

    private EditText input_amount;
    private EditText input_date;
    private Spinner input_type;
    private Spinner input_book;                          //改成final才能使用
    private EditText input_note;

    private RadioButton newBookBtn;
    private RadioButton newTypeBtn;

//    private Button scanInvoice;
//    private Button regularExpense;


    private List<String> type = new ArrayList<String>();         //傳給ArrayAdapter的參數
    private List<String> book = new ArrayList<String>();

    public List<Type> dbTypeData = new ArrayList<>();
    public List<String> dbBookData = new ArrayList<>();       //接資料庫資料

    private String saveDetailStartdate ="";
    private String saveDetailEnddate ="";
    private ArrayList<String> saveDetailBooksArray = new ArrayList<String>();
    private String saveDetailDate="";
    private int saveDetailId =0;

    ViewPager pager;
    ArrayList<View> pagerList;

    private EditText i_userid;                  //宣告需要輸入的變數的EditText
    private String i_price,i_note,i_date,i_type_name,i_book_name;

    private GestureDetectorCompat geatureObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_add);

        geatureObject = new GestureDetectorCompat(this, new LearnGesture());
//        pager = (ViewPager) findViewById(R.id.pager);
//
//        LayoutInflater li = getLayoutInflater().from(this);
//        View v1 = li.inflate(R.layout.income_check,null);
//        pagerList = new ArrayList<View>();
//        pagerList.add(v1);
//
//        pager.setAdapter(new myViewPagerAdapter(pagerList));
//        pager.setCurrentItem(0);


        //Spinner ArrayAdapter 初始化
        updateType();
        updateBook();

        for(int i = 0; i < type.size(); i++){
            System.out.println(type.get(i));
        }
        ArrayAdapter typeList = new ArrayAdapter<>(add_expense.this,
                android.R.layout.simple_spinner_dropdown_item,
                type);
        ArrayAdapter<String> bookList = new ArrayAdapter<>(add_expense.this,
                android.R.layout.simple_spinner_dropdown_item,
                book);

        //不儲存回首頁 或 回流水帳
        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detail){
                    jumpTocheck_expense_detail();
                }else{
                    jumpToHome();
                }
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
        confirm = (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputInfo()){
                    if(detail){
                        i_price = input_amount.getText().toString();
                        int price=Integer.parseInt(i_price);                               //將price轉為int
                        i_note = input_note.getText().toString();
                        new AlertDialog.Builder(add_expense.this)
                                .setTitle("記帳資訊確認")
                                .setMessage("金額："+i_price+"\n"+"日期："+i_date+"\n"+"類別："+i_type_name+"\n"+"帳本："+i_book_name+"\n"+"備註："+i_note)
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseManager dbmanager = new DatabaseManager(getApplication());
                                        dbmanager.open();
                                        dbmanager.updateExpense(saveDetailId, price, i_date, i_type_name, i_book_name, i_note);
                                        dbmanager.close();
                                        //回查帳
                                        jumpTocheck_expense_detail();
                                    }
                                })
                                .setNeutralButton("返回", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Snackbar.make(v, "返回記帳頁面", Snackbar.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    }else{
                        i_price = input_amount.getText().toString();
                        int price=Integer.parseInt(i_price);                               //將price轉為int
                        i_note = input_note.getText().toString();
                        new AlertDialog.Builder(add_expense.this)
                                .setTitle("記帳資訊確認")
                                .setMessage("金額："+i_price+"\n"+"日期："+i_date+"\n"+"類別："+i_type_name+"\n"+"帳本："+i_book_name+"\n"+"備註："+i_note)
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Snackbar.make(v, "完成記帳", Snackbar.LENGTH_SHORT).show();
                                        DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());
                                        dbmanager.open();                                                                       //開啟、建立資料庫(if not exists)
                                        dbmanager.insert_Ex(price,i_date,i_type_name,i_book_name,i_note);            //將資料放到資料庫
//                                      BackupManager bm = new BackupManager(add_expense.this);
//                                      bm.dataChanged();
                                        dbmanager.close();                                                                     //關閉資料庫
                                        //到首頁
                                        jumpToHome();
                                    }
                                })
                                .setNeutralButton("返回", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Snackbar.make(v, "返回記帳頁面", Snackbar.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    }
                }
            }
        });


        //金額
        input_amount = (EditText)findViewById(R.id.amount_input);
        i_price = input_amount.getText().toString();


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
        this.newBookBtn = (RadioButton)findViewById(R.id.newBookBtn) ;
        newBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToadd_book();
                newBookBtn.setChecked(false);
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
        i_note = input_note.getText().toString();

        //掃發票
        //scanInvoice = (Button) findViewById(R.id.scanInvoice);

        //從add_book 或 add_type 返回 填過的資料自動傳入
        Intent getSaveData = getIntent();
        Bundle getSaveBag = getSaveData.getExtras();
        if(getSaveBag != null){
            this.detail = getSaveBag.getBoolean("detail");
            input_amount.setText(getSaveBag.getString("amount"));
            i_price = getSaveBag.getString("amount");
            input_date.setText((detail)?resetDateformat(getSaveBag.getString("date")):getSaveBag.getString("date"));
            i_date = getSaveBag.getString("date");
            int typePosition = typeList.getPosition(getSaveBag.getString("type"));
            input_type.setSelection(typePosition);
            int bookPosition = bookList.getPosition(getSaveBag.getString("book"));
            input_book.setSelection(bookPosition);
            input_note.setText(getSaveBag.getString("note"));
            i_note = getSaveBag.getString("note");
            updateBook();
            updateType();
            input_book.setAdapter(bookList);
            input_type.setAdapter(typeList);
            if(detail){
                saveDetailId=getSaveBag.getInt("id");
                saveDetailStartdate = getSaveBag.getString("saveDetailStartdate");
                saveDetailEnddate = getSaveBag.getString("saveDetailEnddate");
                saveDetailBooksArray = getSaveBag.getStringArrayList("selectBooks");
                saveDetailDate=getSaveBag.getString("date");
            }

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

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.geatureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            if(event2.getX() > event1.getX()){
                Intent intent = new Intent(add_expense.this, add_income.class);
                finish();
                startActivity(intent);
            }
            return true;
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

    public String resetDateformat(String date){
        String resetDate = "";
        int index = 0;

        for(String str : date.split("-")){
            resetDate += (Integer.parseInt(str) < 10) ? str.substring(1): str;
            if(index == 0){
                resetDate += "年";
            }else if(index == 1){
                resetDate +="月";
            }else if(index == 2){
                resetDate += "日";
            }
            index++;
        }

        return resetDate;
    }


    public void updateType(){
        DatabaseManager dbmanager = new DatabaseManager(getApplicationContext());
        dbmanager.open();
        this.dbTypeData = dbmanager.fetchType("Expense");
        dbmanager.close();
        for(int i = 0; i < this.dbTypeData.size(); i++){
            if(type.contains(this.dbTypeData.get(i).getTypeName())){
                continue;
            }else{
                this.type.add(dbTypeData.get(i).getTypeName());
            }
        }
    }


    public void updateBook(){
        DatabaseManager dbmanager = new DatabaseManager(getApplicationContext());
        dbmanager.open();
        this.dbBookData = dbmanager.fetchBook();
        dbmanager.close();
        for(int i = 0; i<dbBookData.size(); i++){
            if(book.contains(dbBookData.get(i))){
                continue;
            }else{
                this.book.add(dbBookData.get(i));
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
        saveExpenseData.putInt("FromExpenseOrIncome",EXPENSE);
        intent.putExtras(saveExpenseData);
        startActivity(intent);

    }

    public void jumpToadd_income(){
        Intent intent = new Intent(add_expense.this,add_income.class);
        startActivity(intent);
    }

    public void jumpTocheck_expense_detail(){
        Intent intent = new Intent(add_expense.this, check_expense_detail.class);
        Bundle detailData = new Bundle();
        detailData.putString("typeName", input_type.getSelectedItem().toString());
        detailData.putString("startDate", saveDetailStartdate);
        detailData.putString("endDate", saveDetailEnddate);
        detailData.putStringArrayList("selectBooks", saveDetailBooksArray);
        intent.putExtras(detailData);
        startActivity(intent);
    }

}
