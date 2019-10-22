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

public class check_income_detail extends AppCompatActivity {
//    private Button lastPage;
    private TextView typeField;
    private Intent getCheckIncomeData;
    private Bundle saveBag;
    private String type = "";
    private String startDate;
    private String endDate;
    private ArrayList<String> selectBooks = new ArrayList<String>();
    private List<Income> incomeList = new ArrayList<Income>();
    private List<Integer> idArray = new ArrayList<Integer>();
    private List<Integer> numberArray = new ArrayList<Integer>();
    private List<String> dateArray = new ArrayList<String>();
    private List<Integer> priceArray = new ArrayList<Integer>();
    private List<String> noteArray = new ArrayList<String>();
    private ArrayList<String> bookArray = new ArrayList<String>();
    private ListView DetailListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_detail);

        getSupportActionBar().setTitle("收入統計");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //get typeName,startDate,endDate,selectBook
        getCheckIncomeData = getIntent();
        saveBag = getCheckIncomeData.getExtras();
        type = saveBag.getString("typeName");
        startDate = saveBag.getString("startDate");
        endDate = saveBag.getString("endDate");
        selectBooks = saveBag.getStringArrayList("selectBooks");

        //上一頁
//        lastPage = (Button)findViewById(R.id.lastPage);
//        lastPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpTocheck_income();
//            }
//        });

        //傳入類別名稱
        typeField = (TextView)findViewById(R.id.typefield);
        typeField.setText(type+"流水帳");

        //listview
        DetailListView = (ListView)findViewById(R.id.DetailListView);

        //Income 資料庫 fetch 資料
        DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
        dbmanager.open();
        incomeList=dbmanager.fetchIncomeWithbookandtype(startDate,endDate,selectBooks,type);
        dbmanager.close();
        setList();
    }

    public void initListData(){
        for(int i = 0; i < this.incomeList.size();i++){
            int index = i+1;
            this.numberArray.add(index);
            this.idArray.add(this.incomeList.get(i).getIn_id());
            this.dateArray.add(this.incomeList.get(i).getIn_date());
            this.priceArray.add(this.incomeList.get(i).getIn_price());
            this.noteArray.add((this.incomeList.get(i).getIn_note().isEmpty()) ? "無備註" : this.incomeList.get(i).getIn_note());
            this.bookArray.add(this.incomeList.get(i).getBook_name());
        }
        //System.out.println(this.getPriceData.size()+" ,"+this.typeName.size());
    }

    public void setList(){
        initListData();
        ExpenseIncomeDetailListAdapter InDetail_adapter = new ExpenseIncomeDetailListAdapter("Income",this.idArray,startDate, endDate,selectBooks,check_income_detail.this, this.numberArray, this.dateArray, this.priceArray, this.noteArray,this.bookArray,this.type);
        DetailListView.setAdapter(InDetail_adapter);
    }

//    public void jumpTocheck_income(){
//        Intent intent = new Intent(check_income_detail.this, check_income.class);
//        intent.putExtra("startDate" ,startDate);
//        intent.putExtra("endDate" , endDate);
//        intent.putExtra("selectBooks" , selectBooks);
//        startActivity(intent);
//    }

}
