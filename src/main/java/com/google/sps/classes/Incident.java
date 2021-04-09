package com.google.sps.classes;

import java.sql.Date;

/**
 * Class Incident
 */
public class Incident {

    private final int locationID; // LOCATION ID
    private final String typeReports; // INCIDENT TYPE
    private final String note; // INCIDENT NOTE
    private final Date dateTime; // INCIDENT DATE

    /**
     * Creates a new Object Incident with 4 variables
     * @param locationID id of the location
     * @param typeReports type of report
     * @param note notes
     * @param dateTime date of the incident
     */
    public Incident(int locationID, String typeReports, String note, 
        Date dateTime) {
        this.locationID = locationID;
        this.typeReports = typeReports;
        this.note = note;
        this.dateTime = dateTime;
    }

    /**
     * Location ID
     * @return te location Id
     */
    public int getLocationID() {
        return locationID;
    }

    /**
     * TypeReports
     * @return incident type
     */
    public String typeReports() {
        return typeReports;
    }

    /**
     * get Notes
     * @return incident notes
     */
    public String getNote() {
        return note;
    }

    /**
     * incident date
     * @return incident date
     */
    public Date getDateTime() {
        return dateTime;
    }
}