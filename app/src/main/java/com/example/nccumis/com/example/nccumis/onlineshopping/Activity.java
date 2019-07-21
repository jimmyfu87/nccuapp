package com.example.nccumis.com.example.nccumis.onlineshopping;

public class Activity {
    private int  id;
    private String activity_name;
    private String channel_name;
    private String cardtype_name;
    private int Minimum_pay;
    private double Discount_ratio;
    private int Discount_limit;
    private int Discount_money;
    private String Start_time;
    private String End_time;
    private String Remarks;

    public Activity(int id, String activity_name, String channel_name, String cardtype_name, int minimum_pay, double discount_ratio, int discount_limit, int discount_money, String start_time, String end_time, String remarks) {
        this.id = id;
        this.activity_name = activity_name;
        this.channel_name = channel_name;
        this.cardtype_name = cardtype_name;
        Minimum_pay = minimum_pay;
        Discount_ratio = discount_ratio;
        Discount_limit = discount_limit;
        Discount_money = discount_money;
        Start_time = start_time;
        End_time = end_time;
        Remarks = remarks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getCardtype_name() {
        return cardtype_name;
    }

    public void setCardtype_name(String cardtype_name) {
        this.cardtype_name = cardtype_name;
    }

    public int getMinimum_pay() {
        return Minimum_pay;
    }

    public void setMinimum_pay(int minimum_pay) {
        Minimum_pay = minimum_pay;
    }

    public double getDiscount_ratio() {
        return Discount_ratio;
    }

    public void setDiscount_ratio(double discount_ratio) {
        Discount_ratio = discount_ratio;
    }

    public int getDiscount_limit() {
        return Discount_limit;
    }

    public void setDiscount_limit(int discount_limit) {
        Discount_limit = discount_limit;
    }

    public int getDiscount_money() {
        return Discount_money;
    }

    public void setDiscount_money(int discount_money) {
        Discount_money = discount_money;
    }

    public String getStart_time() {
        return Start_time;
    }

    public void setStart_time(String start_time) {
        Start_time = start_time;
    }

    public String getEnd_time() {
        return End_time;
    }

    public void setEnd_time(String end_time) {
        End_time = end_time;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
