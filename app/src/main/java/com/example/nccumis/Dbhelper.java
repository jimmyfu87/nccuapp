package com.example.nccumis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbhelper extends SQLiteOpenHelper {

    static final String db_name="bmi.db";
    static final String tb_name="Bmi";
    static final String create_tb=
            "CREATE TABLE IF NOT EXISTS " + "Bmi" + " ("
            + "_id" + " INTEGER primary key autoincrement, "
            + "name" + " text , "
            + "height" + " text , "
            + "weight" + " text "+ ");";
    private SQLiteDatabase db;
    static final String _ID = "_id";
    static final String NAME = "name";
    static final String HEIGHT = "height";
    static final String WEIGHT = "weight";

    Dbhelper(Context c) {
        super(c, db_name, null, 2);
    }

    @Override
    //weight
    public void onCreate(SQLiteDatabase db) {
            this.db=db;
            db.execSQL(create_tb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


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
    public void del(int id){
        if (db==null){
            db=getWritableDatabase();
        }
        db.delete(tb_name,"_id=?",new String[]{String.valueOf(id)});
    }

    public void close(){
        if(db!=null){
            db.close();
        }
    }
}
