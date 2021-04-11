package com.google.sps.classes;

import java.sql.Blob;

public final class Locationob {

  private final int locationID; //Location ID
  private final String gmapsid; //GOOGLE MAPS ID
  private final double latitude; //LATITUDE
  private final double longitude; //LONGITUDE
  private final String visualidentifier; //VISUAL IDENTIFIER NOTE
  private final int incidentmap; //NUMBER OF INCIDENTS IN THAT LOCATION
  private Blob image; // BLOB IMAGE

  /**
   * Creates a new Location obj with the specified parameters
   *
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
   *
   * @return int location ids
   */
  public int getLocationID() {
    return locationID;
  }

  /**
   * Gets the google maps id
   *
   * @return String maps id
   */
  public String getGmapsid() {
    return gmapsid;
  }

  /**
   * Gets the latitude
   *
   * @return Double latitude
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * Gets the longitude
   *
   * @return Double latitude
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * Gets the visual identifier note
   *
   * @return String note
   */
  public String getVisualidentifier() {
    return visualidentifier;
  }

  /**
   * Gets a blob image
   *
   * @return Blob
   */
  public Blob getImage() {
    return image;
  }

  /**
   * Set an image to the location
   */
  public void setImage(Blob image) {
    this.image = image;
  }

  /**
   * Gets the number of incidents
   *
   * @return integer of the number of incidents
   */
  public int getIncidentmap() {
    return incidentmap;
  }
}