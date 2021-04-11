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
    String incidentsfilter = request.getParameter("incidents");

    if (!incidentsfilter.equals("none")) {
      switch (incidentsfilter) {
        case "all":
          incidentsfilter = ">=1";
          break;
        case "warning":
          incidentsfilter = ">1 AND incidentsmap <=5";
          break;
        case "danger":
          incidentsfilter = ">5 AND incidentsmap <= 10";
          break;
        case "dangerous":
          incidentsfilter = "> 10 AND incidentsmap <= 20";
          break;
        case "verydangerous":
          incidentsfilter = ">20";
          break;
        default:
          //In case someone changes the value in the html no error will appear, instead a list of all locations where incidents > 1
          incidentsfilter = ">=1";
          break;
      }
      try {
        locations = returnAllLocationsWhere(request, incidentsfilter);
      } catch (Exception e) {
        System.out.println(e);
      }
    }
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    out.print(new Gson().toJson(locations));
    out.flush();
  }

  public List<Locationob> returnAllLocationsWhere(HttpServletRequest request, String incidents)
      throws SQLException, ServletException {
    DataSource pool = (DataSource) request.getServletContext().getAttribute("my-pool");
    try (Connection conn = pool.getConnection()){
      String stmt1 = "SELECT * FROM locations WHERE incidentmap " + incidents + ";";
      System.out.println(stmt1);
      try (PreparedStatement getalllocationswhere = conn.prepareStatement(stmt1)) {
        ResultSet locations = getalllocationswhere.executeQuery();
        List<Locationob> loc = rstolocobj(locations);
        return loc;
      }catch (SQLException ex) {
      throw new ServletException(
          "Query failed", ex);
      }
    } catch (SQLException ex) {
      throw new ServletException(
          "Unable to successfully connect to the database.", ex);
    }
  }

  public List<Locationob> returnAllLocations(HttpServletRequest request)
      throws SQLException, ServletException {
    DataSource pool = (DataSource) request.getServletContext().getAttribute("my-pool");
    try (Connection conn = pool.getConnection()) {
      String stmt1 = "SELECT * FROM LOCATIONS";
      try (PreparedStatement getalllocations = conn.prepareStatement(stmt1)) {
        ResultSet locations = getalllocations.executeQuery();
        List<Locationob> loc = rstolocobj(locations);
        return loc;
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
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return locs;
  }
}