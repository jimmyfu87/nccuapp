package com.example.nccumis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class BookManage extends AppCompatActivity {



    private Button btn_newBook;
    private ArrayList<Integer> idArray = new ArrayList<Integer>();
    private List<String> nameArray = new ArrayList<String>();
    private List<Integer> amount_startArray = new ArrayList<Integer>();
    private List<Integer> amount_remainArray = new ArrayList<Integer>();
    private List<String> i_currencyidArray = new ArrayList<String>();
    private List<String> startdateArray = new ArrayList<String>();
    private List<String> enddateArray = new ArrayList<String>();
    private List<String> getBookNameFromDB = new ArrayList<String>();   //接資料庫用
    private List<Book> getBookArrayFromDB = new ArrayList<Book>();
    private ListView BookListView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);
        btn_newBook=(Button)findViewById(R.id.btn_newBook);
        BookListView = (ListView)findViewById(R.id.book_listview);
        getSupportActionBar().setTitle("帳本管理");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setBookFromDBList();
        setList();
        setListViewHeightBasedOnChildren(BookListView);


        btn_newBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToadd_book();
            }
        });
    }




    public void setBookFromDBList(){
        getBookNameFromDB.clear();
        DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
        dbmanager.open();
        getBookNameFromDB=dbmanager.fetchBook();
        getBookArrayFromDB=dbmanager.fetchBookallattribute(getBookNameFromDB);
        dbmanager.close();
    }

    public void clearList(){
        this.idArray.clear();
        this.amount_remainArray.clear();
        this.nameArray.clear();
        this.amount_startArray.clear();
        this.i_currencyidArray.clear();
        this.startdateArray.clear();
        this.enddateArray.clear();
    }


    public void initialData(){
        clearList();
        for(int i=0; i<this.getBookArrayFromDB.size();i++){
            this.idArray.add(getBookArrayFromDB.get(i).getId());
            this.nameArray.add(getBookArrayFromDB.get(i).getName());
            this.amount_startArray.add(getBookArrayFromDB.get(i).getAmount_start());
            this.amount_remainArray.add(getBookArrayFromDB.get(i).getAmount_remain());
            this.i_currencyidArray.add(getBookArrayFromDB.get(i).getCurrency_type());
            this.startdateArray.add(getBookArrayFromDB.get(i).getStart_date());
            this.enddateArray.add(getBookArrayFromDB.get(i).getEnd_date());
        }
    }

    public void setList() {
        initialData();
        book_detail_adapter bk_adapter = new book_detail_adapter(this,idArray,nameArray,amount_startArray,amount_remainArray,i_currencyidArray,startdateArray,enddateArray);
        BookListView.setAdapter(bk_adapter);
    }


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

    public void jumpToadd_book() {
        Intent intent = new Intent(BookManage.this,add_book.class);
        Bundle saveBookData = new Bundle();
        saveBookData.putBoolean("FromBookManage", true);
        saveBookData.putBoolean("newBook", true);
        intent.putExtras(saveBookData);
        startActivity(intent);
    }
}