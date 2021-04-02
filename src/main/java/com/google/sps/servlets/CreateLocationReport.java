package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/Cl")
public class CreateLocationReport extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
        //Data for the new Location object
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String visualidentifier = request.getParameter("visualidentifier");
        int incidentmap = 1;
        //Data for the new Incident object
        String locationid = request.getParameter("locationID");
        String typereports = request.getParameter("typeReports");
        String note = request.getParameter("note");
        //Ready to use date for sql query
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  
        System.out.println(sqlDate);
        /*In this part we call the function that executes the query to 
        add a new location and then a report*/
        //createNewLocationReport(longitude, latitude, visualidentifier, incidentmap, 
        //locationid, typereports, note, sqlDate);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print("{Works:True}");
        out.flush();   
    }
    public void createNewLocationReport(double longitude,double latitude,String visualidentifier,int incidentmap){
        
    }
}


