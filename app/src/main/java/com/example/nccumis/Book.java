package com.example.nccumis;

public class Book {
    private int  id;
    private String name;
    private int Amount_start;
    private int Amount_remain;
    private String Currency_type;
    private String Start_date;
    private String End_date;

    public Book(int id, String name, int amount_start, int amount_remain, String currency_type,String start_date,String end_date) {
        this.id = id;
        this.name = name;
        Amount_start = amount_start;
        Amount_remain = amount_remain;
        Currency_type = currency_type;
        Start_date=start_date;
        End_date=end_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount_start() {
        return Amount_start;
    }

    public void setAmount_start(int amount_start) {
        Amount_start = amount_start;
    }

    public int getAmount_remain() {
        return Amount_remain;
    }

    public void setAmount_remain(int amount_remain) {
        Amount_remain = amount_remain;
    }

    public String getCurrency_type() {
        return Currency_type;
    }

    public void setCurrency_type(String currency_type) {
        Currency_type = currency_type;
    }

    public String getStart_date() {
        return Start_date;
    }

    public void setStart_date(String start_date) {
        Start_date = start_date;
    }

    public String getEnd_date() {
        return End_date;
    }

    public void setEnd_date(String end_date) {
        End_date = end_date;
    }
}

