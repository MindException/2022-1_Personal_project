package com.lastproject.used_item_market;

public class University {

    public String university;    //대학 이름
    public String latitude;      //위도
    public String longtitude;    //경도
    public double distance;      //거리

    public University(){}
    public University(String university, String latitude, String longtitude){

        this.university = university;
        this.latitude = latitude;
        this.longtitude = longtitude;


    }

    public University(String university, double distance){

        this.university = university;
        this.distance = distance;


    }



}
