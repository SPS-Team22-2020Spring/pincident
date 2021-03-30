package com.google.sps.classes;

import com.mysql.cj.jdbc.Blob;

public final class Locationob {

    private final int locationID; //Location ID
    private final String gmapsid; //GOOGLE MAPS ID
    private final double latitude; //LATITUDE
    private final double longitude; //LONGITUDE
    private final String visualidentifier; //VISUAL IDENTIFIER NOTE
    private Blob image; // BLOB IMAGE
    private int incidentmap; //NUMBER OF INCIDENTS IN THAT LOCATION

    /**
     * Creates a new Location obj with the specified parameters
     * @param locationID Location ID
     * @param gmapsid GOOGLE MAPS ID
     * @param latitude LATITUDE
     * @param longitude LONGITUDE
     * @param visualidentifier VISUAL IDENTIFIER NOTE
     * @param image BLOB IMAGE
     * @param incidentmap NUMBER OF INCIDENTS IN THAT LOCATION
     */
    public Locationob(int locationID, String gmapsid, double latitude, 
        double longitude, String visualidentifier, 
            Blob image, int incidentmap) {
        this.locationID = locationID;
        this.gmapsid = gmapsid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.visualidentifier = visualidentifier;
        this.image = image;
        this.incidentmap = incidentmap;
    }

    /**
     * Gets the location ID
     * @return int location ids
     */
    public final int getLocationID() {
        return locationID;
    }

    /**
     * Gets the google maps id
     * @return String maps id
     */
    public final String getGmapsid() {
        return gmapsid;
    }

    /**
     * Gets the latitude
     * @return Double latitude
     */
    public final double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude
     * @return Double latitude
     */
    public final double getLongitude() {
        return longitude;
    }

    /**
     * Gets the visual identifier note
     * @return String note
     */
    public final String getVisualidentifier() {
        return visualidentifier;
    }

    /**
     * Gets a blob image
     * @return Blob
     */
    public final Blob getImage() {
        return image;
    }

    /**
     * Gets the number of incidents
     * @return integer of the number of incidents
     */
    public final int getIncidentmap() {
        return incidentmap;
    }

    /**
     * Set an image to the location
     * @param image
     */
    public void setImage(Blob image) {
        this.image = image;
    }

    /**
     * Adds 1 incident number to the location
     */
    public void addIncidentmap() {
        incidentmap += 1;
    }
}
