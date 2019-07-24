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
            + "Ex_note" + " text " + ");";                     //支出備註
    //收入資料庫
    static final String create_tb2=
            "CREATE TABLE IF NOT EXISTS " + "Income" + " ("
                    + "In_id" + " INTEGER primary key autoincrement, "
                    + "In_price" + " INTEGER , "
                    + "In_date" + " text , "
                    + "Type_name"  + " text,"                            //暫用text
                    + "Book_name"  + " text,"                         //暫用text
                    + "In_note" + " text " + ");";
    //帳本資料庫
    static final String create_tb3=
            "CREATE TABLE IF NOT EXISTS " + "Book" + " ("
                    + "Book_id" + " INTEGER primary key autoincrement, "
                    + "Book_name" + " text , "
                    + "Amount_start" + " INTEGER , "
                    + "Amount_remain" + " INTEGER , "
                    + "Currency_type" + " text " + ");";
    static final String create_tb4=
            "CREATE TABLE IF NOT EXISTS " + "Type" + " ("
                    + "Type_id" + " INTEGER primary key autoincrement, "
                    + "Type_name" + " text , "
                    + "ExpenseorIncome" + " text " + ");";
    static final String insert_default_book=
            "INSERT INTO " + "Book" + " (Book_name,Amount_start,Amount_remain,Currency_type) VALUES"
                    + "('現金帳本',0,0,'TWD')"+ ",('旅遊帳本',0,0,'TWD')"+",('購物帳本',0,0,'TWD');";
    static final String insert_default_expense_type=
            "INSERT INTO " + "Type" + " (Type_name,ExpenseorIncome) VALUES"
                    + "('早餐','Expense')"+ ",('午餐','Expense')"+ ",('晚餐','Expense')"+ ",('飲料','Expense')"
                    + ",('零食','Expense')"+ ",('交通','Expense')"+ ",('投資','Expense')"+ ",('醫療','Expense')"
                    + ",('衣物','Expense')"+ ",('日用品','Expense')"+ ",('禮品','Expense')"+ ",('購物','Expense')"
                    + ",('娛樂','Expense')"+ ",('水電費','Expense')"+ ",('電話費','Expense')"+ ",('房租','Expense')"
                    +",('其他','Expense');";
    static final String insert_default_income_type=
            "INSERT INTO " + "Type" + " (Type_name,ExpenseorIncome) VALUES"
                    + "('薪水','Income')"+ ",('投資','Income')"+ ",('樂透中獎','Income')"+ ",('發票中獎','Income')"
                    +",('其他','Income');";
    static final String insert_fake_expense=
            "INSERT INTO " + "Expense" + " (Ex_price,Ex_date,Type_name,Book_name,Ex_note) VALUES"
                    + "(500,'2019-07-01','投資','現金帳本','買東西'),(1000,'2019-07-02','交通','購物帳本','買東西')," +
                    "(300,'2019-07-03','早餐','現金帳本','買東西'),(500,'2019-07-06','投資','現金帳本','買東西')," +
                    "(600,'2019-07-08','衣物','旅遊帳本','買東西'),(3200,'2019-07-10','娛樂','現金帳本','買東西')," +
                    "(300,'2019-07-13','電話費','現金帳本','買東西'),(500,'2019-07-16','投資','購物帳本','買東西')," +
                    "(300,'2019-07-17','零食','現金帳本','買東西'),(600,'2019-07-20','早餐','現金帳本','買東西')," +
                    "(500,'2019-07-23','早餐','現金帳本','買東西'),(700,'2019-07-28','投資','旅遊帳本','買東西');";

    static final String insert_fake_income=
            "INSERT INTO " + "Income" + " (In_price,In_date,Type_name,Book_name,In_note) VALUES"
                    + "(5000,'2019-07-01','投資','現金帳本','股利'),(22000,'2019-07-02','薪水','現金帳本','公司薪水')," +
                    "(300,'2019-07-03','樂透中獎','現金帳本','運彩'),(3000,'2019-07-06','投資','現金帳本','基金')," +
                    "(600,'2019-07-08','樂透中獎','現金帳本','大樂透'),(2000,'2019-07-10','薪水','現金帳本','加班費')," +
                    "(300,'2019-07-13','樂透中獎','現金帳本','運彩'),(500,'2019-07-16','其他','購物帳本','購物禮卷')," +
                    "(300,'2019-07-17','樂透中獎','旅遊帳本','刮刮樂'),(200,'2019-07-20','發票中獎','現金帳本','4月發票');" ;

    private SQLiteDatabase db;
    //支出欄位
    static final String EX_ID = "Ex_id";
    static final String EX_PRICE = "Ex_price";
    static final String EX_DATE = "Ex_date";
    static final String TYPE_NAME = "Type_name";
    static final String BOOK_NAME = "Book_name";
    static final String EX_NOTE = "Ex_note";

    //收入欄位
    static final String IN_ID = "In_id";
    static final String IN_PRICE = "In_price";
    static final String IN_DATE = "In_date";
//    static final String TYPE_NAME = "Type_name";
//    static final String BOOK_NAME = "Book_name";
    static final String IN_NOTE = "In_note";

    //帳本欄位
    static final String BOOK_ID = "Book_id";
//    static final String BOOK_NAME = "Book_name";
    static final String AMOUNT_START = "Amount_start";
    static final String AMOUNT_REMAIN = "Amount_remain";
    static final String CURRENCY_TYPE = "Currency_type";

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
        db.execSQL(insert_fake_income);
        db.execSQL(insert_default_expense_type);
        db.execSQL(insert_default_income_type);
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
