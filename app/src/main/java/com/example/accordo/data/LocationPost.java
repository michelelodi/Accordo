package com.example.accordo.data;

public class LocationPost extends Post{

    private Double lat, lon;

    public LocationPost(int pid, User author, String ctitle, Double lat, Double lon) {
        super(pid, author, ctitle);
        this.lat = lat;
        this.lon = lon;
    }
}
