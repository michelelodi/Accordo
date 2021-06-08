package com.example.accordo.data;

public class LocationPost implements Post{

    private String pid,ctitle;
    private User author;
    private Double lat, lon;

    public LocationPost(String pid, User author, String ctitle, Double lat, Double lon) {

        this.pid = pid;
        this.author = author;
        this.ctitle = ctitle;
        this.lat = lat;
        this.lon = lon;
    }
}
