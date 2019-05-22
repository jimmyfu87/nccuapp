package com.example.nccumis;

public class Type {
    private int id;
    private int price;
    private String typeName;

    public Type(int id, int price, String typeName){
        this.id = id;
        this.price = price;
        this.typeName = typeName;
    }

    public Type(int price, String typeName){
        this.price = price;
        this.typeName = typeName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
