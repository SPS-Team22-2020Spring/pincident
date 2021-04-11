package com.google.sps.servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/Cr")
public class CreateReportServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {
    //Data for the new Location object
    System.out.println("Entered post");
    //We fetch this id when clicking on the pin
    int locationID = Integer.valueOf(request.getParameter("locationID"));
    String typeReports = request.getParameter("typeReports");
    String note = request.getParameter("note");
    //Ready to use date for sql query
    java.util.Date utilDate = new java.util.Date();
    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        /*In this part we call the function that executes the query to 
        add a new report to a previous location*/
    Boolean inserted = createNewReportTo(request, locationID, typeReports, note, sqlDate);
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    out.print(inserted);
    out.flush();
  }

  public Boolean createNewReportTo(HttpServletRequest request, int locationID,
      String typeReports, String note, java.sql.Date sqlDate)
      throws ServletException {
    DataSource pool = (DataSource) request.getServletContext().getAttribute("my-pool");
    try (Connection conn = pool.getConnection()) {
      String stmt1 = "INSERT INTO incidents values (?,?,?,?,NULL);";
      try (PreparedStatement repinsertStmt = conn.prepareStatement(stmt1)) {
        repinsertStmt.setInt(1, locationID);
        repinsertStmt.setString(2, typeReports);
        repinsertStmt.setString(3, note);
        repinsertStmt.setDate(4, sqlDate);
        Boolean incidentResults = repinsertStmt.execute();
      }
    } catch (SQLException ex) {
      throw new ServletException(
          "Unable to successfully connect to the database.", ex);
    }
    return false;
  }
}