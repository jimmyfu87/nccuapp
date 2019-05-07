package com.example.nccumis;

public class Expense {
    private  int Ex_id;
    private  int    Ex_price;
    private  String Ex_date;
    private  String Type_name;
    private  String Book_name;
    private  String Ex_note;
    private  int User_id;

    public Expense(int id,int price ,String date,String typename,String bookname,String note,int user_id)
    {
        Ex_id=id;
        Ex_price=price;
        Ex_date=date;
        Type_name=typename;
        Book_name=bookname;
        Ex_note=note;
        User_id=user_id;
    }

    public int getEx_id() {
        return Ex_id;
    }

    public void setEx_id(int ex_id) {
        Ex_id = ex_id;
    }

    public int getEx_price() {
        return Ex_price;
    }

    public void setEx_price(int ex_price) {
        Ex_price = ex_price;
    }

    public String getEx_date() {
        return Ex_date;
    }

    public void setEx_date(String ex_date) {
        Ex_date = ex_date;
    }

    public String getType_name() {
        return Type_name;
    }

    public void setType_name(String type_name) {
        Type_name = type_name;
    }

    public String getBook_name() {
        return Book_name;
    }

    public void setBook_name(String book_name) {
        Book_name = book_name;
    }

    public String getEx_note() {
        return Ex_note;
    }

    public void setEx_note(String ex_note) {
        Ex_note = ex_note;
    }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }
}
