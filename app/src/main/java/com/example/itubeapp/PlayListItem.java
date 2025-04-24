package com.example.itubeapp;

public class PlayListItem {
    private long id;
    private String url;

    public PlayListItem(long id, String url) {
        this.id = id;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
