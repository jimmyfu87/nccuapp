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
    public void insert_Ex(int ex_price,String ex_date,String type_name,String book_name,String ex_note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.EX_PRICE, ex_price);
        contentValues.put(dbHelper.EX_DATE, ex_date);
        contentValues.put(dbHelper.TYPE_NAME, type_name);
        contentValues.put(dbHelper.BOOK_NAME, book_name);
        contentValues.put(dbHelper.EX_NOTE, ex_note);
        database.insert(dbHelper.tb_name, null, contentValues);

    }
    public void insert_In(int In_price,String In_date,String type_name,String book_name,String In_note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.IN_PRICE, In_price);
        contentValues.put(dbHelper.IN_DATE, In_date);
        contentValues.put(dbHelper.TYPE_NAME, type_name);
        contentValues.put(dbHelper.BOOK_NAME, book_name);
        contentValues.put(dbHelper.IN_NOTE, In_note);
        database.insert(dbHelper.tb_name2, null, contentValues);

    }
    public void updateExpense(int ex_id,int ex_price,String ex_date,String type_name,String book_name,String ex_note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.EX_PRICE, ex_price);
        contentValues.put(dbHelper.EX_DATE, ex_date);
        contentValues.put(dbHelper.TYPE_NAME, type_name);
        contentValues.put(dbHelper.BOOK_NAME, book_name);
        contentValues.put(dbHelper.EX_NOTE, ex_note);
        database.update(dbHelper.tb_name, contentValues, dbHelper.EX_ID + "=" +"'"+ex_id+"'", null);

    }

    public void updateIncome(int in_id,int in_price,String in_date,String type_name,String book_name,String in_note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.IN_PRICE, in_price);
        contentValues.put(dbHelper.IN_DATE, in_date);
        contentValues.put(dbHelper.TYPE_NAME, type_name);
        contentValues.put(dbHelper.BOOK_NAME, book_name);
        contentValues.put(dbHelper.IN_NOTE, in_note);
        database.update(dbHelper.tb_name2, contentValues, dbHelper.IN_ID + "=" +"'"+in_id+"'", null);

    }
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
    public List<Expense> fetchExpense(String starttime, String endtime) {
        Cursor Expense=database.rawQuery
                ("select * from Expense where dateTime(Ex_date) between datetime('"+starttime+"') and datetime('"+endtime+"')",null);
        List<Expense> Expenselist=new ArrayList<>();
        while (Expense.moveToNext()){
            Expenselist.add(new Expense(Expense.getInt(0),Expense.getInt(1),Expense.getString(2),Expense.getString(3),Expense.getString(4),Expense.getString(5)));
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
            Expenselist.add(new Expense(Expense.getInt(0),Expense.getInt(1),Expense.getString(2),Expense.getString(3),Expense.getString(4),Expense.getString(5)));
        }
        return  Expenselist;
    }
    public List<Expense> fetchExpenseWithbookandtype(String starttime, String endtime, List<String> booklist,String type) {
        String result="select distinct * from Expense where (dateTime(Ex_date) between datetime('"+starttime+"') and datetime('"+endtime+"'))"+" AND(Type_name='"+type+"') AND( "+"Book_name="+"'"+booklist.get(0)+"'";
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
            Expenselist.add(new Expense(Expense.getInt(0),Expense.getInt(1),Expense.getString(2),Expense.getString(3),Expense.getString(4),Expense.getString(5)));
        }
        return  Expenselist;
    }

    public List<Income> fetchIncome(String starttime, String endtime) {
        Cursor Income=database.rawQuery
                ("select * from Income where dateTime(Ex_date) between datetime('"+starttime+"') and datetime('"+endtime+"')",null);
        List<Income> Incomelist=new ArrayList<>();
        while (Income.moveToNext()){
            Incomelist.add(new Income(Income.getInt(0),Income.getInt(1),Income.getString(2),Income.getString(3),Income.getString(4),Income.getString(5)));
        }
        return  Incomelist;
    }
    public List<Income> fetchIncomeWithbook(String starttime, String endtime, List<String> booklist) {
        String result="select distinct * from Income where (dateTime(In_date) between datetime('"+starttime+"') and datetime('"+endtime+"'))"+" AND( "+"Book_name="+"'"+booklist.get(0)+"'";
        if(booklist.size()>1){
            for(int i=1;i<booklist.size();i++) {
                result = result + " OR " + "Book_name='"+booklist.get(i)+"'";
            }
        }
        result=result+");";
        Cursor Income=database.rawQuery
                (result,null);
        List<Income> Incomelist=new ArrayList<>();
        while (Income.moveToNext()){
            Incomelist.add(new Income(Income.getInt(0),Income.getInt(1),Income.getString(2),Income.getString(3),Income.getString(4),Income.getString(5)));
        }
        return  Incomelist;
    }
    public List<Income> fetchIncomeWithbookandtype(String starttime, String endtime, List<String> booklist,String type) {
        String result="select distinct * from Income where (dateTime(In_date) between datetime('"+starttime+"') and datetime('"+endtime+"'))"+" AND(Type_name='"+type+"') AND( "+"Book_name="+"'"+booklist.get(0)+"'";
        if(booklist.size()>1){
            for(int i=1;i<booklist.size();i++) {
                result = result + " OR " + "Book_name='"+booklist.get(i)+"'";
            }
        }
        result=result+");";
        Cursor Income=database.rawQuery
                (result,null);
        List<Income> Incomelist=new ArrayList<>();
        while (Income.moveToNext()){
            Incomelist.add(new Income(Income.getInt(0),Income.getInt(1),Income.getString(2),Income.getString(3),Income.getString(4),Income.getString(5)));
        }
        return  Incomelist;
    }

    public void deleteExpense(int ex_id) {
        database.delete(dbHelper.tb_name,dbHelper.EX_ID + " ='" + ex_id + "'",null);
    }

    public void deleteIncome(int In_id) {
        database.delete(dbHelper.tb_name2,dbHelper.IN_ID + " ='" + In_id + "'",null);
    }
    public void insert_Type(String type_name,String ExorIn) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.TYPE_NAME, type_name);
        contentValues.put(dbHelper.EXPENSEORINCOME ,ExorIn);
        database.insert(dbHelper.tb_name4, null, contentValues);

    }
    public List<Type> fetchType(String ExpenseOrIncome) {
        List<Type> typelist = new ArrayList<>();
        String result="";
        if(ExpenseOrIncome.equals("Expense")){
            result="select * from Type WHERE ExpenseorIncome='Expense'";
        }
        else if(ExpenseOrIncome.equals("Income")){
            result="select * from Type WHERE ExpenseorIncome='Income'";
        }
        Cursor alltype=database.rawQuery
                (result ,null);

        while(alltype.moveToNext()){
            typelist.add(new Type(alltype.getInt(0),alltype.getString(1),alltype.getString(2)));
        }
        return typelist;
    }
    public void updateType(int type_id,String type_name,String ExorIn) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.TYPE_NAME, type_name);
        contentValues.put(dbHelper.EXPENSEORINCOME, ExorIn);
        database.update(dbHelper.tb_name4, contentValues, dbHelper.TYPE_ID + "=" +"'"+type_id+"'", null);

    }
    public void deleteType(int type_id) {
        database.delete(dbHelper.tb_name4,dbHelper.TYPE_ID + " ='" + type_id + "'",null);
    }

    public void insert_Book(String book_name,int amount_start,int amount_remain,String currency_type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.BOOK_NAME, book_name);
        contentValues.put(dbHelper.AMOUNT_START, amount_start);
        contentValues.put(dbHelper.AMOUNT_REMAIN, amount_remain);
        contentValues.put(dbHelper.CURRENCY_TYPE, currency_type);
        database.insert(dbHelper.tb_name3, null, contentValues);

    }
    public List<String> fetchBook() {
        List<String> book = new ArrayList<>();
        Cursor allbook=database.rawQuery
                ("select Book_name from Book" ,null);

        while(allbook.moveToNext()){
            book.add(allbook.getString(0));
        }
        return  book;
    }

    public void updateBook(int book_id,String book_name,int amount_start,String currency_type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.BOOK_NAME, book_name);
        contentValues.put(dbHelper.AMOUNT_START, amount_start);
        contentValues.put(dbHelper.CURRENCY_TYPE, currency_type);
        database.update(dbHelper.tb_name3, contentValues, dbHelper.BOOK_ID + "=" +"'"+book_id+"'", null);

    }
    public List<Book> fetchBookallattribute(List<String> booklist){
        for(int i=0;i<booklist.size();i++){
            update_book_remain_amount(booklist.get(i));
        }
        String result="select distinct * from book where "+"Book_name="+"'"+booklist.get(0)+"'";
        if(booklist.size()>1){
            for(int i=1;i<booklist.size();i++) {
                result = result + " OR " + "Book_name='"+booklist.get(i)+"'";
            }
        }
        result=result+";";
        Cursor Books=database.rawQuery
                (result,null);
        List<Book> Booklist=new ArrayList<>();
        while (Books.moveToNext()){
            Booklist.add(new Book(Books.getInt(0),Books.getString(1),Books.getInt(2),Books.getInt(3),Books.getString(4)));
        }
        return  Booklist;
    }
    public void deleteBook(int book_id) {
        database.delete(dbHelper.tb_name3,dbHelper.BOOK_ID + " ='" + book_id + "'",null);
    }
    public void update_book_remain_amount(String book_name){
        String income_query="select In_price from Income where "+"Book_name="+"'"+book_name+"'"+";";
        String expense_query="select Ex_price from Expense where "+"Book_name="+"'"+book_name+"'"+";";
        String start_query="select Amount_start from Book where "+"Book_name="+"'"+book_name+"'"+";";

        Cursor start_amount=database.rawQuery(start_query,null);
        Cursor Income=database.rawQuery(income_query,null);
        Cursor Expense=database.rawQuery(expense_query,null);
        int total_start_amount=0;
        int total_income=0;
        int total_expense=0;
        int total_remain_amount=0;
        while (start_amount.moveToNext()){
            total_start_amount=total_start_amount+start_amount.getInt(0);
        }
        while (Income.moveToNext()){
            total_income=total_income+Income.getInt(0);
        }
        while (Expense.moveToNext()){
            total_expense=total_expense+Expense.getInt(0);
        }
        total_remain_amount=total_start_amount+total_income-total_expense;

        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.AMOUNT_REMAIN, total_remain_amount);
        database.update(dbHelper.tb_name3, contentValues, dbHelper.BOOK_NAME + "=" +"'"+book_name+"'", null);

    }

}
