package com.example.nccumis.com.example.nccumis.onlineshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nccumis.ExpenseIncomeDetailListAdapter;
import com.example.nccumis.Home;
import com.example.nccumis.R;
import com.example.nccumis.add_expense;
import com.example.nccumis.check_expense_detail;

public class ecommerce1 extends AppCompatActivity {
    private Button lastPage;
    private TextView ecommerceName;
    private ListView CreditCardListView;
    private ListView ProductListView;
    private TextView totalPrice;
    private TextView recommendcreditcard;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecommerce1);

        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToHome();
            }
        });

        ecommerceName = (TextView)findViewById(R.id.ecommerceName);
        ecommerceName.setText("Momo購物網");//之後從資料庫抓電商名稱

        //信用卡優惠
        CreditCardListView = (ListView)findViewById(R.id.CreditCardListView);
        CreditCardListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        setList();
        setListViewHeightBasedOnChildren(CreditCardListView);
        //商品優惠
        ProductListView = (ListView)findViewById(R.id.ProductListView);
//        setList();
        setListViewHeightBasedOnChildren(ProductListView);
        //計算後的總價
        totalPrice = findViewById(R.id.totalPrice);
//        countTotalPrice();

        //推薦信用卡
        recommendcreditcard = findViewById(R.id.recommendcreditcard);

    }

//    public void initCreditCardListData(){
//        for(int i = 0; i < this.expenseList.size();i++){
//            int index = i+1;
//            this.numberArray.add(index);
//            this.idArray.add(this.expenseList.get(i).getEx_id());
//            this.dateArray.add(this.expenseList.get(i).getEx_date());
//            this.priceArray.add(this.expenseList.get(i).getEx_price());
//            this.noteArray.add((this.expenseList.get(i).getEx_note().isEmpty()) ? "無備註" : this.expenseList.get(i).getEx_note());
//            this.bookArray.add(this.expenseList.get(i).getBook_name());
//        }
//        //System.out.println(this.getPriceData.size()+" ,"+this.typeName.size());
//    }
//
//    public void setCreditCardList(){
//        initListData();
//        ExpenseIncomeDetailListAdapter ExDetail_adapter = new ExpenseIncomeDetailListAdapter("Expense",this.idArray,startDate, endDate,selectBooks, check_expense_detail.this, this.numberArray, this.dateArray, this.priceArray, this.noteArray,this.bookArray,this.type);
//        DetailListView.setAdapter(ExDetail_adapter);
//    }

    //計算總額
    public int countTotalPrice(){
        int price = 0;
        return price;
    }

    /**
     * 動態設定ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void jumpToHome(){
        Intent intent = new Intent(ecommerce1.this, Home.class);
        startActivity(intent);
    }
}
