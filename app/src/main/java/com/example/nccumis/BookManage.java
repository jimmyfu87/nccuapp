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

    private Button btn_showBook;
    private ListView BookList;
    private Button btn_newBook;
    private ArrayList<String> selectBooks = new ArrayList<String>();
    private List<String> bookArray = new ArrayList<String>();
    private List<String> select_book = new ArrayList<String>();
    private List<Integer> getPriceData = new ArrayList<Integer>();
    private List<String> getTypeName = new ArrayList<String>();
    private List<Expense> select_expense = new ArrayList<Expense>();
    private String start_date,end_date;
    private ListView TypeListView;
    private List<Integer> numberArray = new ArrayList<Integer>();
    private boolean[] checked;
    private Bundle saveBag;
    private Intent getPreSavedData;
    private Activity context;
    private ArrayList<String> bookArrayParam;
    private String bookName;
    private TextView book1;
    private TextView book2;
    private TextView book3;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);
        btn_newBook=(Button)findViewById(R.id.btn_newBook);
        btn_showBook=(Button)findViewById(R.id.btn_showBook);
       // BookList=(ListView) findViewById(R.id.BookList);
        TypeListView = (ListView)findViewById(R.id.TypeListView);
        book1=(TextView)findViewById(R.id.book1);
        book2=(TextView)findViewById(R.id.book2);
        book3=(TextView)findViewById(R.id.book3);

       // saveBag = getPreSavedData.getExtras();

        if(saveBag != null){

            selectBooks = saveBag.getStringArrayList("selectBooks");



            DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
            dbmanager.open();
            select_expense=dbmanager.fetchExpenseWithbook(start_date,end_date,selectBooks);
            System.out.println(select_expense.size()+", "+selectBooks.size());
            dbmanager.close();
            //setExpenseData(select_expense);
            setList();
            setListViewHeightBasedOnChildren(TypeListView);

        }else {
            //圖表
           // setExpenseData(select_expense);

            //ListView 類別項目、類別名稱、類別佔總額%、類別金額
            //setList();
            setListViewHeightBasedOnChildren(TypeListView);
        }

        btn_showBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book1.setText("旅遊帳本");
                book2.setText("現金帳本");
                book3.setText("購物帳本");


                    //Expense 資料庫
                    //DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                    //dbmanager.open();
                    // select_expense=dbmanager.fetchExpense(start_date,end_date);           //可直接調用select_expense的資訊
                    //System.out.println("Size of select books"+selectBooks.size());
                    //select_book=dbmanager.fetchBook();
                    //dbmanager.close();
                   // setList();
            }

        });

        book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookManage.this);
                builder.setMessage("管理帳本");
                builder.setPositiveButton("編輯", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(BookManage.this,book_fixed.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("刪除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                       book1.setText("");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        book2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookManage.this);
                builder.setMessage("管理帳本");
                builder.setPositiveButton("編輯", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(BookManage.this,book_fixed.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("刪除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        book2.setText("");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        book3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookManage.this);
                builder.setMessage("管理帳本");
                builder.setPositiveButton("編輯", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(BookManage.this,book_fixed.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("刪除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        book3.setText("");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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

//        TypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
 //           @Override
  //          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
   //             jumpTobook_adapter(position);
    //        }
     //   });

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
        book_adapter bk_adapter = new book_adapter(this, this.bookArray, bookName, context, bookArrayParam);
        TypeListView.setAdapter(bk_adapter);
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