package com.example.yx_jo.dublinbikesapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by yx_jo on 30/09/2017.
 */

public class Journey {



    private String ID;
    private String startName;
    private double startLat;
    private double startLong;
    private String endName;
    private double endLat;
    private double endLong;
    private Date date;
    private String imageURL;

    public Journey (){

    }

    public Journey(String ID, String startName, double startLat, double startLong, String endName, double endLat, double endLong, Date date, String imageURL) {
        this.ID = ID;
        this.startName = startName;
        this.startLat = startLat;
        this.startLong = startLong;
        this.endName = endName;
        this.endLat = endLat;
        this.endLong = endLong;
        this.date = date;
        this.imageURL = imageURL;
    }

    public Journey(String ID, String startName, double startLat, double startLong, String endName, double endLat, double endLong, Date date) {
        this.ID = ID;
        this.startName = startName;
        this.startLat = startLat;
        this.startLong = startLong;
        this.endName = endName;
        this.endLat = endLat;
        this.endLong = endLong;
        this.date = date;
    }

    public String getID() {
        return ID;
    }

    public double getStartLat() {
        return startLat;
    }

    public double getStartLong() {
        return startLong;
    }

    public double getEndLat() {
        return endLat;
    }

    public double getEndLong() {
        return endLong;
    }




    public String getStartName() {
        return startName;
    }



    public String getEndName() {
        return endName;
    }

    public Date getDate() {
        return date;
    }

    public String getImageURL() {
        return imageURL;
    }



}
