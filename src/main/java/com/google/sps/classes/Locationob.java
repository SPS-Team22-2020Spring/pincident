package com.google.sps.classes;

import com.mysql.cj.jdbc.Blob;

public class Locationob {
    private int locationID;
    private String gmapsid;
    private double latitude;
    private double longitude;
    private String visualidentifier;
    private Blob image;
    private int incidentmap;

    public Locationob(int locationID, String gmapsid, double latitude, double longitude, String visualidentifier,
            Blob image, int incidentmap) {
        this.locationID = locationID;
        this.gmapsid = gmapsid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.visualidentifier = visualidentifier;
        this.image = image;
        this.incidentmap = incidentmap;

    }

    public int getLocationID() {
        return locationID;
    }

    public String getGmapsid() {
        return gmapsid;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getVisualidentifier() {
        return visualidentifier;
    }

    public Blob getImage() {
        return image;
    }

    public int getIncidentmap() {
        return incidentmap;
    }

    public void setLocationid(int locationID) {
        this.locationID = locationID;
    }

    public void setGmapsid(String gmapsid) {
        this.gmapsid = gmapsid;
    }

    public void setLatitude(double latitude) {
        this.longitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setVisualidentifier(String visualidentifier) {
        this.visualidentifier = visualidentifier;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public void addIncidentmap() {
        incidentmap += 1;
    }
}