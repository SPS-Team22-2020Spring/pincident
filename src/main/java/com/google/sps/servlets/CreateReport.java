package main.java.com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.io.PrintWriter;

import com.google.sps.classes.Locationob;

import main.java.com.google.sps.classes.Incident;

@WebServlet("/Cr")
public class CreateReport extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException{
         
        //Data for the new Location object
        String locationID = request.getParameter("locationID"); //We fetch this id when clicking on the pin
        String typeReports = request.getParameter("typeReports");
        String note = request.getParameter("note");
        //Ready to use date for sql query
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  
        
        /*In this part we call the function that executes the query to add a new report 
        to a previous location*/
        //createNewReportTo(locationID, typeReports, note, sqlDate);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print("{Works}");
        out.flush();   
    }
}