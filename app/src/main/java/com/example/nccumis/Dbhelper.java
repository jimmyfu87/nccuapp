package com.example.nccumis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbhelper extends SQLiteOpenHelper {

    static final String db_name="App.db";
    static final String tb_name="Expense";
    static final String tb_name2="Income";
    static final String create_tb=
            "CREATE TABLE IF NOT EXISTS " + "Expense" + " ("
            + "Ex_id" + " INTEGER primary key autoincrement, " //支出id
            + "Ex_price" + " INTEGER , "                       //支出金額
            + "Ex_date" + " text , "                           //支出日期
            + "typeID"  + " text,"                          //支出類別  //暫用text
            + "bookID"  + " INTEGER,"                          //帳本id
            + "Ex_note" + " text , "                           //支出備註
            + "Ex_fixed"+ " text ,"                            //固定支出
            + "User_ID" + " INTEGER " + ");";                  //使用者id
    //收入資料庫
    static final String create_tb2=
            "CREATE TABLE IF NOT EXISTS " + "Income" + " ("
                    + "In_id" + " INTEGER primary key autoincrement, "
                    + "In_price" + " INTEGER , "
                    + "In_date" + " text , "
                    + "typeID"  + " text,"                            //暫用text
                    + "bookID"  + " INTEGER,"                         //暫用text
                    + "In_note" + " text , "
                    + "In_fixed"+ " text ,"
                    + "User_ID" + " INTEGER " + ");";
    private SQLiteDatabase db;
    static final String EX_ID = "Ex_id";
    static final String EX_PRICE = "Ex_price";
    static final String EX_DATE = "Ex_date";
    static final String TYPEID = "typeID";   //
    static final String BOOKID = "bookID";
    static final String EX_NOTE = "Ex_note";
    static final String EX_FIXED = "Ex_fixed";
    static final String USER_ID = "User_ID";

    static final String IN_ID = "In_id";
    static final String IN_PRICE = "In_price";
    static final String IN_DATE = "In_date";
//    static final String TYPEID = "typeID";
//    static final String BOOKID = "bookID";
    static final String IN_NOTE = "In_note";
    static final String IN_FIXED = "In_fixed";
//    static final String USER_ID = "User_ID";



    Dbhelper(Context c) {
        super(c, db_name, null, 2);
    }

    @Override
    //weight
    public void onCreate(SQLiteDatabase db) {
            this.db=db;
            db.execSQL(create_tb);
            db.execSQL(create_tb2);



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
