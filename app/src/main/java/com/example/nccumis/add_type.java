package com.example.nccumis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class add_type extends AppCompatActivity {
    private Button lastPage;
    private Button confirm;

    private EditText input_typeName;
    private Spinner input_ExpenseOrIncome;

    private Intent savedDataFromSpend;
    private Bundle saveBag;
    private int JumpToWhere = 0;

    private String i_typeName="";
    private String i_expenseOrincome="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fix_type_add);
        savedDataFromSpend = getIntent();
        saveBag = savedDataFromSpend.getExtras();
        JumpToWhere = saveBag.getInt("FromExpenseOrIncome");
        saveBag.remove("FromExpenseOrIncome");
        saveBag.putBoolean("detail",false);
        getSupportActionBar().setTitle("新增類別");


        //不儲存回 新增支出 或 新增收入
        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToaddSpend(saveBag);
            }
        });


        //確認
        confirm = (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputInfo()){
                    //到下一頁
                    i_typeName = input_typeName.getText().toString();
                    DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());
                    dbmanager.open();                                                                       //開啟、建立資料庫(if not exists)
                    dbmanager.insert_Type(i_typeName,i_expenseOrincome);            //將資料放到資料庫
                    dbmanager.close();                                                                      //關閉資料庫
                    jumpToaddSpend(saveBag);
                }
            }
        });


        //帳本名稱
        input_typeName = (EditText) findViewById(R.id.typeName_input);


        //收入或支出
        input_ExpenseOrIncome = (Spinner)findViewById(R.id.ExpenseOrIncome_input);
        final String[] ExpenseOrIncomeArr = {"支出", "收入" };
        ArrayAdapter<String> ExpenseOrIncomeList = new ArrayAdapter<>(add_type.this,
                android.R.layout.simple_spinner_dropdown_item,
                ExpenseOrIncomeArr);
        input_ExpenseOrIncome.setAdapter(ExpenseOrIncomeList);
        input_ExpenseOrIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i_expenseOrincome = input_ExpenseOrIncome.getSelectedItem().toString().equals("支出") ? "Expense" : "Income" ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //檢查輸入的值是否正確
    public boolean checkInputInfo(){
        if(input_typeName.getText().length() > 10){
            input_typeName.setError("輸入名稱太長");
            return false;
        }
        if(input_typeName.getText().toString().isEmpty()){
            input_typeName.setError("輸入名稱未填寫");
            return false;
        }
        return true;
    }

    public void jumpToaddSpend(Bundle prelayoutData) {
        //確定jump回哪裡
        if(this.JumpToWhere == add_income.INCOME){
            Intent intent = new Intent(add_type.this, add_income.class);
            intent.putExtras(prelayoutData);
            startActivity(intent);
        }else {
            Intent intent = new Intent(add_type.this, add_expense.class);
            intent.putExtras(prelayoutData);
            startActivity(intent);
        }
    }
}
