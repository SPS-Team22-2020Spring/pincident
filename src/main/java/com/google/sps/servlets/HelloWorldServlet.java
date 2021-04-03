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

/** Handles requests sent to the /hello URL. Try running a server and navigating to /hello! */
@WebServlet("/hello")
public final class HelloWorldServlet extends HttpServlet {

  static final long serialVersionUID = 0;

  @Override
  public void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException, ServletException {

    DataSource pool = (DataSource) request.getServletContext().getAttribute("my-pool");
    try (Connection conn = pool.getConnection()) {
      String stmt1 = "SELECT * FROM incidents";
      try (PreparedStatement incidentStmt = conn.prepareStatement(stmt1)) {
        ResultSet incidentResults = incidentStmt.executeQuery();
        while (incidentResults.next()) {
          // Grabs the second column in the response
          System.out.println(incidentResults.getString(2));
        }
      }
    } catch (SQLException ex) {
      throw new ServletException(
          "Unable to successfully connect to the database.", ex);
    }

    response.setContentType("text/html;");
    response.getWriter().println("<h1>Hello world!</h1>");
  }
}
