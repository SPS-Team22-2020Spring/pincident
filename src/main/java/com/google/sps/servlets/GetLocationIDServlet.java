package com.google.sps.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.sps.classes.Locationob;
import java.sql.Blob;

@WebServlet("/lid")
public class GetLocationIDServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
	        throws ServletException, IOException, RuntimeException{

        DataSource pool = (DataSource) request.getServletContext().getAttribute("my-pool");
        String gmapsid = request.getParameter("gmapsid");
        try (Connection conn = pool.getConnection()) {
            String stmt1 = "SELECT locationID FROM locations WHERE gmapsid = "+gmapsid+" LIMIT 1;";
            try (PreparedStatement incidentStmt = conn.prepareStatement(stmt1)) {
            ResultSet locationResults = incidentStmt.executeQuery();
            while (locationResults.next()) {
                // Grabs the second column in the response
                System.out.println(locationResults);
            }
            }
        } catch (SQLException ex) {
            throw new ServletException(
                "Unable to successfully connect to the database.", ex);
        }
        // System.out.println(incidents);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");  
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