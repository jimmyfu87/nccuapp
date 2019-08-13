package com.example.nccumis;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

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
    private ListView TypeListView;
    private List<Integer> numberArray = new ArrayList<Integer>();
    private boolean[] checked;
    private Bundle saveBag;
    private Intent getPreSavedData;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);
        btn_newBook=(Button)findViewById(R.id.btn_newBook);
        btn_showBook=(Button)findViewById(R.id.btn_showBook);
       // BookList=(ListView) findViewById(R.id.BookList);
        TypeListView = (ListView)findViewById(R.id.TypeListView);

        saveBag = getPreSavedData.getExtras();

        if(saveBag != null){

            selectBooks = saveBag.getStringArrayList("selectBooks");



            DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
            dbmanager.open();
           // select_expense=dbmanager.fetchExpenseWithbook(start_date,end_date,selectBooks);
            System.out.println(select_expense.size()+", "+selectBooks.size());
            dbmanager.close();
            //setExpenseData(select_expense);
            setList();
            setListViewHeightBasedOnChildren(TypeListView);

        }else {
            //圖表
           // setExpenseData(select_expense);

            //ListView 類別項目、類別名稱、類別佔總額%、類別金額
            setList();
            setListViewHeightBasedOnChildren(TypeListView);
        }



        this.btn_showBook =(Button)findViewById(R.id.btn_newBook);
        btn_newBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());
                dbmanager.open();
                select_book = dbmanager.fetchBook();
                System.out.println(bookArray.size());
                dbmanager.close();

                setBookArray();
                setList();
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
    private void multiDialogEvent(){
        this.selectBooks.clear();
//        final List<Boolean> checkedStatusList = new ArrayList<>();
//        for(String s : bookArray){
//            checkedStatusList.add(false);
//        }
        new AlertDialog.Builder(BookManage.this)
                .setMultiChoiceItems(bookArray.toArray(new String[bookArray.size()]), checked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                                checked.set(which, isChecked);
                            }
                        })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuilder sb = new StringBuilder();
                        boolean isEmpty = true;
                        for(int i = 0; i < checked.length; i++){
                            if(checked[i]){
                                sb.append(bookArray.get(i));
                                sb.append(" ");
                                selectBooks.add(bookArray.get(i));
                                //System.out.println("Here"+bookArray.get(i));
                                isEmpty = false;
                            }
                        }
                        if(!isEmpty){
                            Toast.makeText(BookManage.this, "你選擇的是"+sb.toString(), Toast.LENGTH_SHORT).show();
//                            for(String s : bookArray){
//                                checkedStatusList.add(false);
//                            }
                        } else{
                            setBookArray();
                            //initSelectBooks();
                            Toast.makeText(BookManage.this, "請勾選項目，系統已自動返回預設(統計所有帳本)", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .show();
    }

    public void setBookArray() {
        DatabaseManager dbmanager = new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
        dbmanager.open();
        this.bookArray = dbmanager.fetchBook();           //可直接調用select_expense的資訊
        dbmanager.close();
        this.checked = new boolean[this.bookArray.size()];

        for(int i = 0; i < checked.length; i++){
            checked[i] = true;
        }
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

        setBookArray();





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