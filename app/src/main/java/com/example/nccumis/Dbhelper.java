package com.example.nccumis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbhelper extends SQLiteOpenHelper {

    static final String db_name="App.db";
    static final String tb_name="Expense";
    static final String tb_name2="Income";
    static final String tb_name3="Book";
    static final String tb_name4="Type";
    static final String create_tb=
            "CREATE TABLE IF NOT EXISTS " + "Expense" + " ("
            + "Ex_id" + " INTEGER primary key autoincrement, " //支出id
            + "Ex_price" + " INTEGER , "                       //支出金額
            + "Ex_date" + " text , "                           //支出日期
            + "Type_name"  + " text,"                          //支出類別  //暫用text
            + "Book_name"  + " text,"                          //帳本id
            + "Ex_note" + " text , "                           //支出備註
            + "User_id" + " INTEGER " + ");";                  //使用者id
    //收入資料庫
    static final String create_tb2=
            "CREATE TABLE IF NOT EXISTS " + "Income" + " ("
                    + "In_id" + " INTEGER primary key autoincrement, "
                    + "In_price" + " INTEGER , "
                    + "In_date" + " text , "
                    + "Type_name"  + " text,"                            //暫用text
                    + "Book_name"  + " text,"                         //暫用text
                    + "In_note" + " text , "
                    + "User_id" + " INTEGER " + ");";
    //帳本資料庫
    static final String create_tb3=
            "CREATE TABLE IF NOT EXISTS " + "Book" + " ("
                    + "Book_id" + " INTEGER primary key autoincrement, "
                    + "Book_name" + " text , "
                    + "Amount_start" + " INTEGER , "
                    + "Amount_remain" + " INTEGER , "
                    + "Currency_type" + " text , "
                    + "User_id" + " INTEGER " + ");";
    static final String create_tb4=
            "CREATE TABLE IF NOT EXISTS " + "Type" + " ("
                    + "Type_id" + " INTEGER primary key autoincrement, "
                    + "Type_name" + " text , "
                    + "ExpenseorIncome" + " text " + ");";
    static final String insert_default_book=
            "INSERT INTO " + "Book" + " (Book_name,Amount_start,Amount_remain,Currency_type,User_id) VALUES"
                    + "('現金帳本',0,0,'TWD',1)"+ ",('美金帳本',0,0,'TWD',1)"+",('支票帳本',0,0,'TWD',1);";
    static final String insert_fake_expense=
            "INSERT INTO " + "Expense" + " (Ex_price,Ex_date,Type_name,Book_name,Ex_note,User_id) VALUES"
                    + "(500,'2019-05-01','投資','現金帳本','買東西',1),(1000,'2019-05-02','交通','美金帳本','買東西',1)," +
                    "(300,'2019-05-03','早餐','現金帳本','買東西',1),(500,'2019-05-06','投資','現金帳本','買東西',1)," +
                    "(600,'2019-05-08','衣物','支票帳本','買東西',1),(3200,'2019-05-10','娛樂','現金帳本','買東西',1)," +
                    "(300,'2019-05-13','電話費','現金帳本','買東西',1),(500,'2019-05-16','投資','現金帳本','買東西',1)," +
                    "(300,'2019-05-17','零食','現金帳本','買東西',1),(600,'2019-05-20','早餐','現金帳本','買東西',1)," +
                    "(500,'2019-05-23','早餐','現金帳本','買東西',1),(700,'2019-05-28','投資','現金帳本','買東西',1);";

    private SQLiteDatabase db;
    //支出欄位
    static final String EX_ID = "Ex_id";
    static final String EX_PRICE = "Ex_price";
    static final String EX_DATE = "Ex_date";
    static final String TYPE_NAME = "Type_name";
    static final String BOOK_NAME = "Book_name";
    static final String EX_NOTE = "Ex_note";
    static final String USER_ID = "User_id";

    //收入欄位
    static final String IN_ID = "In_id";
    static final String IN_PRICE = "In_price";
    static final String IN_DATE = "In_date";
//    static final String TYPE_NAME = "Type_name";
//    static final String BOOK_NAME = "Book_name";
    static final String IN_NOTE = "In_note";
//    static final String USER_ID = "User_id";

    //帳本欄位
    static final String BOOK_ID = "Book_id";
//    static final String BOOK_NAME = "Book_name";
    static final String AMOUNT_START = "Amount_start";
    static final String AMOUNT_REMAIN = "Amount_remain";
    static final String CURRENCY_TYPE = "Currency_type";
//    static final String USER_ID = "User_id";

    //類別欄位
    static final String TYPE_ID = "Type_id";
    //    static final String TYPE_NAME = "Type_name";
    static final String EXPENSEORINCOME= "ExpenseorIncome";



    Dbhelper(Context c) {
        super(c, db_name, null, 2);
    }

    @Override
    //weight
    public void onCreate(SQLiteDatabase db) {
            this.db=db;
            db.execSQL(create_tb);
            db.execSQL(create_tb2);
            db.execSQL(create_tb3);
            db.execSQL(create_tb4);
            db.execSQL(insert_default_book);
            db.execSQL(insert_fake_expense);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //更新資料版本


    }
//    public void insert(ContentValues values){
//        SQLiteDatabase db=getWritableDatabase();
//        db.insert(tb_name,null,values);
//        db.close();
//    }
//    public Cursor query(){
//        String[] columns = new String[]{dbHelper._ID, dbHelper.TITLE, dbHelper.DESC};
//        Cursor cursor = database.query(dbHelper.TABLE_NAME, columns, null, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//        return cursor;
//        SQLiteDatabase db=getWritableDatabase();
//        Cursor c=db.query(tb_name,null,null,null,null,null,null);
//        return c;
//          return db.rawQuery("SELECT * FROM "+db_name,null);
//    }
//    public void del(int id){
//        if (db==null){
//            db=getWritableDatabase();
//        }
//        db.delete(tb_name,"_id=?",new String[]{String.valueOf(id)});
//    }

    public void close(){
        if(db!=null){
            db.close();
        }
    }
}
