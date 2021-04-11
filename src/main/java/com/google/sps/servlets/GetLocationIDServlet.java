package com.google.sps.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/lid")
public class GetLocationIDServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException, RuntimeException {

    DataSource pool = (DataSource) request.getServletContext().getAttribute("my-pool");
    String gmapsid = request.getParameter("gmapsid");
    String locationId = "test";
    try (Connection conn = pool.getConnection()) {
      String stmt1 = "SELECT locationID FROM locations WHERE gmapsid='" + gmapsid + "' LIMIT 1;";

      try (PreparedStatement incidentStmt = conn.prepareStatement(stmt1)) {
        ResultSet locationResults = incidentStmt.executeQuery();
        if (locationResults.next()) {
          locationId = locationResults.getString("locationID");
        } else {
          response.sendError(404, "Location not in DB.");
        }
      } catch (SQLException ex) {
        throw new ServletException("Error with Query", ex);
      }
    } catch (SQLException ex) {
      throw new ServletException(
          "Unable to successfully connect to the database.", ex);
    }
    // System.out.println(incidents);
    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().print(locationId);
  }

  // public ResultSet returnSpecificLocationID(HttpServletRequest request) throws SQLException, ServletException {
  //     DataSource pool = (DataSource) request.getServletContext().getAttribute("my-pool");
  //     try (Connection conn = pool.getConnection()) {
  //         String stmt1 = "SELECT ID FROM LOCATIONS WHERE LocationID = "+request.locationID+" LIMIT 1;";
  //         try (PreparedStatement getalllocations = conn.prepareStatement(stmt1)) {
  //             ResultSet locations = getalllocations.executeQuery();
  //             System.out.println(locations);
  //             return locations;
  //         }
  //     }catch (SQLException ex) {
  //         throw new ServletException(
  //             "Unable to successfully connect to the database.", ex);
  //     }
  // }

}