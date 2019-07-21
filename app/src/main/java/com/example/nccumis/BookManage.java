package com.example.nccumis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;


public class BookManage extends AppCompatActivity {

    private Button btn_showBook;
    private ListView BookList;
    private Button btn_newBook;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);
        btn_newBook=(Button)findViewById(R.id.btn_newBook);
        btn_showBook=(Button)findViewById(R.id.btn_showBook);
        BookList=(ListView) findViewById(R.id.BookList);

        this.btn_newBook = (RadioButton)findViewById(R.id.book_management);
        btn_newBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToadd_book();
            }
        });

    }

    public void jumpToadd_book() {
        Intent intent = new Intent(BookManage.this,add_book.class);
        Bundle saveIncomeData = new Bundle();
        intent.putExtras(saveIncomeData);
        startActivity(intent);

    }


}