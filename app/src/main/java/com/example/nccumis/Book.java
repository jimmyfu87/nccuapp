package com.example.nccumis;

public class Book {
    private int  id;
    private String name;
    private int Amount_start;
    private int Amount_remain;
    private String Currency_type;

    public Book(int id, String name, int amount_start, int amount_remain, String currency_type) {
        this.id = id;
        this.name = name;
        Amount_start = amount_start;
        Amount_remain = amount_remain;
        Currency_type = currency_type;
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

}

