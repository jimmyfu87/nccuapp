package com.example.nccumis;

public class Income {
    private  int   In_id;
    private  int    In_price;
    private  String In_date;
    private  String Type_name;
    private  String Book_name;
    private  String In_note;
    private  int User_id;

    public int getIn_id() {
        return In_id;
    }

    public void setIn_id(int in_id) {
        In_id = in_id;
    }

    public int getIn_price() {
        return In_price;
    }

    public void setIn_price(int in_price) {
        In_price = in_price;
    }

    public String getIn_date() {
        return In_date;
    }

    public void setIn_date(String in_date) {
        In_date = in_date;
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

    public String getIn_note() {
        return In_note;
    }

    public void setIn_note(String in_note) {
        In_note = in_note;
    }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }
}
