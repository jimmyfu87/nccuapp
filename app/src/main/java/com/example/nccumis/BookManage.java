package com.example.nccumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;


public class BookManage extends AppCompatActivity {

    private Button btn_showBook;
    private ListView BookList;
    private Button btn_newBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);
        btn_newBook=(Button)findViewById(R.id.btn_newBook);
        btn_showBook=(Button)findViewById(R.id.btn_showBook);
        BookList=(ListView) findViewById(R.id.BookList);



    }


}
