package main.java.com.google.sps.classes;

import java.sql.Date;

public class Incident{

    private int locationID;
    private String typeReports;
    private String note;
    private Date dateTime;

    public Incident(int locationID, String typeReports, String note, Date dateTime) {
        this.locationID = locationID;
        this.typeReports = typeReports;
        this.note = note;
        this.dateTime = dateTime;
    }

    public int getLocationID() {
        return locationID;
    }

    public String typeReports() {
        return typeReports;
    }

    public String getNote() {
        return note;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setLocationid(int locationID) {
        this.locationID = locationID;
    }

    public void setTypeReports(String typeReports) {
        this.typeReports = typeReports;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(Date date) {
        this.dateTime = date;
    }
}