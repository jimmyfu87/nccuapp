package com.example.nccumis;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Dbhelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context c) {
        this.context = c;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper = new Dbhelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    //typeid,bookid暫用text
    public void insert_Ex(int ex_price,String ex_date,String type_name,String book_name,String ex_note,int user_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.EX_PRICE, ex_price);
        contentValues.put(dbHelper.EX_DATE, ex_date);
        contentValues.put(dbHelper.TYPE_NAME, type_name);
        contentValues.put(dbHelper.BOOK_NAME, book_name);
        contentValues.put(dbHelper.EX_NOTE, ex_note);
        contentValues.put(dbHelper.USER_ID, user_id);
        database.insert(dbHelper.tb_name, null, contentValues);

    }
    public void insert_Book(String book_name,int amount_start,int amount_remain,String currency_type,int user_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.BOOK_NAME, book_name);
        contentValues.put(dbHelper.AMOUNT_START, amount_start);
        contentValues.put(dbHelper.AMOUNT_REMAIN, amount_remain);
        contentValues.put(dbHelper.CURRENCY_TYPE, currency_type);
        contentValues.put(dbHelper.USER_ID, user_id);
        database.insert(dbHelper.tb_name3, null, contentValues);

    }

//    public void update(String name, String height,String weight) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(dbHelper.HEIGHT, height);
//        contentValues.put(dbHelper.WEIGHT, weight);
//        database.update(dbHelper.tb_name, contentValues, dbHelper.NAME + "=" +"'"+name+"'", null);
//
//    }
//
//    public void delete(String name) {
//        database.delete(dbHelper.tb_name,dbHelper.NAME + " ='" + name + "'",null);
//    }

//    public Cursor fetch() {
//        String[] columns = new String[]{dbHelper._ID, dbHelper.NAME, dbHelper.HEIGHT,dbHelper.WEIGHT};
//        Cursor cursor = database.query(dbHelper.tb_name, columns, null, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//        return cursor;
//    }
        public List<String> fetchBook() {
            List<String> book = new ArrayList<>();
            Cursor allbook=database.rawQuery
                    ("select Book_name from Book" ,null);

            while(allbook.moveToNext()){
                book.add(allbook.getString(0));
            }
            return  book;
        }



    public List<Expense> fetchExpense(String starttime, String endtime) {
        Cursor Expense=database.rawQuery
                ("select * from Expense where dateTime(Ex_date) between datetime('"+starttime+"') and datetime('"+endtime+"')",null);
        List<Expense> Expenselist=new ArrayList<>();
        while (Expense.moveToNext()){
            Expenselist.add(new Expense(Expense.getInt(0),Expense.getInt(1),Expense.getString(2),Expense.getString(3),Expense.getString(4),Expense.getString(5),Expense.getInt(6)));
        }
        return  Expenselist;
    }
    public List<Expense> fetchExpenseWithbook(String starttime, String endtime, List<String> booklist) {
        String result="select distinct * from Expense where (dateTime(Ex_date) between datetime('"+starttime+"') and datetime('"+endtime+"'))"+" AND( "+"Book_name="+"'"+booklist.get(0)+"'";
        if(booklist.size()>1){
            for(int i=1;i<booklist.size();i++) {
                result = result + " OR " + "Book_name='"+booklist.get(i)+"'";
            }
        }
        result=result+");";
        Cursor Expense=database.rawQuery
                (result,null);
        List<Expense> Expenselist=new ArrayList<>();
        while (Expense.moveToNext()){
            Expenselist.add(new Expense(Expense.getInt(0),Expense.getInt(1),Expense.getString(2),Expense.getString(3),Expense.getString(4),Expense.getString(5),Expense.getInt(6)));
        }
        return  Expenselist;
    }

}
