package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.classes.Locationob;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/ls")
public class LocationsServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException, RuntimeException {

    List<Locationob> locations = new LinkedList<Locationob>();
    String incidents = request.getParameter("incidents");

    if (!incidents.equals("none")) {
      switch (incidents) {
        case "all":
          incidents = ">=1";
          break;
        case "warning":
          incidents = ">1 AND incidentsmap <=5";
          break;
        case "danger":
          incidents = ">5 AND incidentsmap <= 10";
          break;
        case "dangerous":
          incidents = "> 10 AND incidentsmap <= 20";
          break;
        case "verydangerous":
          incidents = ">20";
          break;
        default:
          //In case someone changes the value in the html no error will appear, instead a list of all locations where incidents > 1
          incidents = ">=1";
          break;
      }
      try {
        //I don't know wich is correct I'll check once the html is ready
        locations = rstolocobj(returnAllLocationsWhere(request, incidents));
        //List<Locationob> locations_copy = new LinkedList<Locationob>(rstolocobj(returnAllLocationsWhere(request, incidents)));
      } catch (Exception e) {
        System.out.println(e);
      }
    } else {
      //If both filters are None we return every location
      try {
        //I don't know wich is correct I'll check once the html is ready
        locations = rstolocobj(returnAllLocations(request));
        //List<Locationob> locations_copy = new LinkedList<Locationob>(rstolocobj(returnAllLocations(request)));
      } catch (Exception e) {
        System.out.println(e);
      }
    }
    System.out.println(incidents);
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    out.print(new Gson().toJson(locations));
    out.flush();
  }

  public ResultSet returnAllLocationsWhere(HttpServletRequest request, String incidents)
      throws SQLException, ServletException {
    DataSource pool = (DataSource) request.getServletContext().getAttribute("my-pool");
    try (Connection conn = pool.getConnection()) {
      String stmt1 = "SELECT * FROM LOCATIONS WHERE incidentsmap" + incidents + ";";
      try (PreparedStatement getalllocationswhere = conn.prepareStatement(stmt1)) {
        ResultSet locations = getalllocationswhere.executeQuery();
        System.out.println(locations);
        return locations;
      }
    } catch (SQLException ex) {
      throw new ServletException(
          "Unable to successfully connect to the database.", ex);
    }
  }

  public ResultSet returnAllLocations(HttpServletRequest request)
      throws SQLException, ServletException {
    DataSource pool = (DataSource) request.getServletContext().getAttribute("my-pool");
    try (Connection conn = pool.getConnection()) {
      String stmt1 = "SELECT * FROM LOCATIONS";
      try (PreparedStatement getalllocations = conn.prepareStatement(stmt1)) {
        ResultSet locations = getalllocations.executeQuery();
        System.out.println(locations);
        return locations;
      }
    } catch (SQLException ex) {
      throw new ServletException(
          "Unable to successfully connect to the database.", ex);
    }
  }

  public List<Locationob> rstolocobj(ResultSet rs) {
    List<Locationob> locs = new LinkedList<Locationob>();
    try {
      while (rs.next()) {
        int LocationID = rs.getInt(1);
        String GmapsID = rs.getString(2);
        Double Latitude = rs.getDouble(3);
        Double Longitude = rs.getDouble(4);
        String Visualidentifier = rs.getString(5);
        Blob Image = rs.getBlob(6);
        int Incidentsmap = rs.getInt(7);
        Locationob loc = new Locationob(LocationID, GmapsID, Latitude, Longitude, Visualidentifier,
            Image,
            Incidentsmap);
        locs.add(loc);
        System.out
            .println("LocationID: " + LocationID + " GmapsID " + GmapsID + " Latitude: " + Latitude
                + " Longitude: " + Longitude + " Visualidentifier: " + Visualidentifier + " Image: "
                + Image
                + " " + Incidentsmap);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return locs;
  }
}