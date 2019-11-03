package com.example.nccumis.com.example.nccumis.onlineshopping;


import android.os.Parcel;
import android.os.Parcelable;

public class Activity implements Parcelable {
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
    public Activity(){

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(activity_name);
        dest.writeString(channel_name);
        dest.writeString(cardtype_name);
        dest.writeInt(Minimum_pay);
        dest.writeDouble(Discount_ratio);
        dest.writeInt(Discount_limit);
        dest.writeInt(Discount_money);
        dest.writeString(Start_time);
        dest.writeString(End_time);
        dest.writeString(Remarks);

    }
    public static final Parcelable.Creator<Activity> CREATOR  = new Creator<Activity>() {
        //实现从source中创建出类的实例的功能
        @Override
        public Activity createFromParcel(Parcel source) {
            Activity activity  = new Activity();
            activity.id=source.readInt();
            activity.activity_name=source.readString();
            activity.channel_name=source.readString();
            activity.cardtype_name=source.readString();
            activity.Minimum_pay=source.readInt();
            activity.Discount_ratio=source.readDouble();
            activity.Discount_limit=source.readInt();
            activity.Discount_money=source.readInt();
            activity.Start_time=source.readString();
            activity.End_time=source.readString();
            activity.Remarks=source.readString();

            return activity;
        }
        //创建一个类型为T，长度为size的数组
        @Override
        public Activity[] newArray(int size) {
            return new Activity[size];
        }
    };

}
