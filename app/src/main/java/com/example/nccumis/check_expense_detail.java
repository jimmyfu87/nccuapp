package com.example.nccumis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class check_expense_detail extends AppCompatActivity {
    private Button lastPage;
    private TextView typeField;
    private Intent getCheckExpenseData;
    private Bundle saveBag;
    private String type = "";
    private String startDate;
    private String endDate;
    private ArrayList<String> selectBooks = new ArrayList<String>();
    private List<Expense> expenseList = new ArrayList<Expense>();
    private List<Integer> idArray = new ArrayList<Integer>();
    private List<Integer> numberArray = new ArrayList<Integer>();
    private List<String> dateArray = new ArrayList<String>();
    private List<Integer> priceArray = new ArrayList<Integer>();
    private List<String> noteArray = new ArrayList<String>();
    private ArrayList<String> bookArray = new ArrayList<String>();
    private ListView DetailListView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_detail);

        //get typeName,startDate,endDate,selectBook
        getCheckExpenseData = getIntent();
        saveBag = getCheckExpenseData.getExtras();
        type = saveBag.getString("typeName");
        startDate = saveBag.getString("startDate");
        endDate = saveBag.getString("endDate");
        selectBooks = saveBag.getStringArrayList("selectBooks");

        System.out.println(startDate+", "+endDate);

        //上一頁
        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpTocheck_expense();
            }
        });

        //傳入類別名稱
        typeField = (TextView)findViewById(R.id.typefield);
        typeField.setText(type+"流水帳");

        //listview
        DetailListView = (ListView)findViewById(R.id.DetailListView);

        //Expense 資料庫 fetch 資料
        DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
        dbmanager.open();
        expenseList=dbmanager.fetchExpenseWithbookandtype(startDate,endDate,selectBooks,type);
        dbmanager.close();
        setList();
    }

    public void initListData(){
        for(int i = 0; i < this.expenseList.size();i++){
            int index = i+1;
            this.numberArray.add(index);
            this.idArray.add(this.expenseList.get(i).getEx_id());
            this.dateArray.add(this.expenseList.get(i).getEx_date());
            this.priceArray.add(this.expenseList.get(i).getEx_price());
            this.noteArray.add((this.expenseList.get(i).getEx_note().isEmpty()) ? "無備註" : this.expenseList.get(i).getEx_note());
            this.bookArray.add(this.expenseList.get(i).getBook_name());
        }
        //System.out.println(this.getPriceData.size()+" ,"+this.typeName.size());
    }

    public void setList(){
        initListData();
        ExpenseDetailListAdapter ExDetail_adapter = new ExpenseDetailListAdapter(this.idArray,startDate, endDate,check_expense_detail.this, this.numberArray, this.dateArray, this.priceArray, this.noteArray,this.bookArray,this.type);
        DetailListView.setAdapter(ExDetail_adapter);
    }

    public void jumpTocheck_expense(){
        Intent intent = new Intent(check_expense_detail.this, check_expense.class);
        intent.putExtra("startDate" ,startDate);
        intent.putExtra("endDate" , endDate);
        intent.putExtra("selectBooks" , selectBooks);
        startActivity(intent);
    }
}
