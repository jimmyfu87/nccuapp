package com.example.nccumis;

public class Cardtype {
    private int  id;
    private String cardtype_name;
    private int discountMax;
    public Cardtype(int id, String cardtype_name) {
        this.id = id;
        this.cardtype_name = cardtype_name;
        discountMax = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardtype_name() {
        return cardtype_name;
    }

    public void setCardtype_name(String cardtype_name) {
        this.cardtype_name = cardtype_name;
    }

    public void setdiscountmax(int discountParam){
        this.discountMax = discountParam;
    }

    public int getdiscountMax(){
        return discountMax;
    }
}
