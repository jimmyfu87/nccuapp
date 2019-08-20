package com.example.nccumis.com.example.nccumis.onlineshopping;

public class Product {
    private int  id;
    private String product_name;
    private String product_price;
    private String product_url;
    private String member_id;
    private String channel_name;
    private String upload_time;

    public Product(int id, String product_name, String product_price, String product_url, String member_id, String channel_name,String upload_time) {
        this.id = id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_url = product_url;
        this.member_id = member_id;
        this.channel_name = channel_name;
        this.upload_time = upload_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }
}
