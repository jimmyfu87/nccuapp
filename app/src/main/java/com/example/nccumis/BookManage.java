package com.example.nccumis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;


public class BookManage extends AppCompatActivity {

    private Button btn_showBook;
    private ListView BookList;
    private Button btn_newBook;
    private ArrayList<String> selectBooks = new ArrayList<String>();
    private ArrayList<String> bookArray = new ArrayList<String>();
    private List<String> select_book = new ArrayList<String>();
    private ListView TypeListView;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);
        btn_newBook=(Button)findViewById(R.id.btn_newBook);
        btn_showBook=(Button)findViewById(R.id.btn_showBook);
        BookList=(ListView) findViewById(R.id.BookList);



        this.btn_showBook =(Button)findViewById(R.id.btn_newBook);
        btn_newBook.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());
                dbmanager.open();
                select_book = dbmanager.fetchBook();
                if(select_book.isEmpty()){
                    Snackbar.make(v,"目前沒有帳本",Snackbar.LENGTH_SHORT).show();
                }
                dbmanager.close();
                setBookData(select_book);
                setList();
                setListViewHeightBasedOnChildren(TypeListView);
                }




        });






        btn_newBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToadd_book();
            }
            public void jumpToadd_book() {
                Intent intent = new Intent(BookManage.this,add_book.class);
                Bundle saveIncomeData = new Bundle();
                intent.putExtras(saveIncomeData);
                startActivity(intent);

            }
        });
        TypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jumpTobook_adapter(position);
            }
        });

    }

    public void jumpTobook_adapter(int position) {
    }

    public void setBookData(List<String> select_book){
        String getBookName = "";
        int replacePosition = 0;
        for(int i=0;i < select_book.size();i++){
            getBookName = select_book.get(i);

        }

    }

    public void setList() {
        initListData();




    }

    public void initListData() {
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


}