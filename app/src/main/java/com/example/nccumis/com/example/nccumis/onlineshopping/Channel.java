package com.example.nccumis.com.example.nccumis.onlineshopping;

public class Channel {
    private int  id;
    private String channel_name;
    private String channel_url;

    public Channel(int id, String channel_name, String channel_url) {
        this.id = id;
        this.channel_name = channel_name;
        this.channel_url = channel_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getChannel_url() {
        return channel_url;
    }

    public void setChannel_url(String channel_url) {
        this.channel_url = channel_url;
    }
}
