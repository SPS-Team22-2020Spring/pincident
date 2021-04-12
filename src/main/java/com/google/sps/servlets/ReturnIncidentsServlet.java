package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.classes.Incident;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/Ris")
public class ReturnIncidentsServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException, RuntimeException {
    List<Incident> incidents = new LinkedList<Incident>();
    String locationid = request.getParameter("id");
    try {
        incidents = returnAllIncidentsWhere(request, locationid);
    } catch (Exception e) {
        System.out.println(e);
    }
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    out.print(new Gson().toJson(incidents));
    out.flush();
  }

  public List<Incident> returnAllIncidentsWhere(HttpServletRequest request, String incidents)
      throws SQLException, ServletException {
    DataSource pool = (DataSource) request.getServletContext().getAttribute("my-pool");
    try (Connection conn = pool.getConnection()){
      String stmt1 = "SELECT * FROM incidents WHERE locationID = " + incidents + ";";
      try (PreparedStatement getalllocationswhere = conn.prepareStatement(stmt1)) {
        ResultSet incidents_ = getalllocationswhere.executeQuery();
        List<Incident> inc = rstoincobj(incidents_);
        return inc;
      }catch (SQLException ex) {
      throw new ServletException(
          "Query failed", ex);
      }
    } catch (SQLException ex) {
      throw new ServletException(
          "Unable to successfully connect to the database.", ex);
    }
  }

  public List<Incident> rstoincobj(ResultSet rs) {
    List<Incident> incs = new LinkedList<Incident>();
    try {
      while (rs.next()) {
        int locationID = rs.getInt(1);
        String typeReports = rs.getString(2);
        String note = rs.getString(3);
        Date sqlDate = rs.getDate(4);
        Incident inc = new Incident(locationID, typeReports, note, sqlDate);
        incs.add(inc);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return incs;
  }
}