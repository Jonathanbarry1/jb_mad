package com.example.yx_jo.dublinbikesapp;

import com.google.android.gms.maps.model.LatLng;



public class BikeStation {

    public String name;
    public String address;
    public double latitude;
    public double longitude;
    public String number;
    public String spotsAvail;
    public String bikesAvail;
    public LatLng position;

    public BikeStation(String name, String address, double latitude, double longitude, String number, String spotsAvail,
                       String bikesAvail){
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.number = number;
        this.spotsAvail = spotsAvail;
        this.bikesAvail = bikesAvail;
    }

    public BikeStation(String name, LatLng position){
        this.name = name;
        this.position = position;
    }

    public LatLng getPosition() {return position; }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    public String getNumber() {
        return number;
    }

    public String getSpotsAvail() {
        return spotsAvail;
    }

    public String getBikesAvail() {
        return bikesAvail;
    }
}
