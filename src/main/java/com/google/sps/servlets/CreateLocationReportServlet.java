package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/Cl")
public class CreateLocationReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
	        throws ServletException, IOException {
        //Data for the new Location object
        Double longitude = Double.parseDouble(request.getParameter("longitude"));
        Double latitude = Double.parseDouble(request.getParameter("latitude"));
        String visualidentifier = request.getParameter("visualidentifier");
        String gmapsid = request.getParameter("gmapsid");
        int incidentmap = 1;
        //Data for the new Incident object
        int locationid = Integer.valueOf(request.getParameter("locationID"));
        String typereports = request.getParameter("typeReports");
        String note = request.getParameter("note");
        //Ready to use date for sql query
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  
        /*In this part we call the function that executes the query to 
        add a new location and then a report*/
        Boolean inserted = createNewLocationReport(request, longitude, latitude, visualidentifier, gmapsid, incidentmap, locationid, typereports, note, sqlDate);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(inserted);
        out.flush();   
    }
    public Boolean createNewLocationReport(HttpServletRequest request, double longitude,
        double latitude,String visualidentifier, String gmapsid, int incidentmap, 
            int locationID, String typereports, String note, java.sql.Date sqlDate) 
                throws ServletException {
        Boolean returnBoolean = false;
        DataSource pool = (DataSource) request.getServletContext().getAttribute("my-pool");
        try (Connection conn = pool.getConnection()) {
            String stmt1 = "INSERT INTO locations values (NULL,?,?,?,?,NULL,?);";
            String stmt2 = "INSERT INTO incidents values (?,?,?,?,NULL);";
            try (PreparedStatement locinsertStmt = conn.prepareStatement(stmt1)) {
                locinsertStmt.setString(1, gmapsid);
                locinsertStmt.setDouble(2, latitude);
                locinsertStmt.setDouble(3, longitude);
                locinsertStmt.setString(4, visualidentifier);
                locinsertStmt.setInt(5, incidentmap);
                ResultSet incidentResults = locinsertStmt.executeQuery();
                System.out.println(incidentResults);
            }
            try (PreparedStatement repinsertStmt = conn.prepareStatement(stmt2)) {
                ResultSet incidentResults = repinsertStmt.executeQuery();
                System.out.println(incidentResults);
            }
            returnBoolean = true;
        }catch (SQLException ex) {
            returnBoolean = false;
            throw new ServletException(
                "Unable to successfully connect to the database.", ex);
        }
        return returnBoolean;
    }
}