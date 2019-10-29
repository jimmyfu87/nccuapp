package com.example.nccumis;

public class Cardtype implements Comparable<Cardtype>{
    private int  id;
    private String cardtype_name;
    private int discountMax;
    private String apply_url;


    public Cardtype(int id, String cardtype_name,String apply_url) {
        this.id = id;
        this.cardtype_name = cardtype_name;
        this.apply_url = apply_url;
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

    public String getApply_url() {
        return apply_url;
    }

    public void setApply_url(String apply_url) {
        this.apply_url = apply_url;
    }

    @Override
    public int compareTo(Cardtype other) {
        return this.discountMax - other.discountMax;
    }
}
