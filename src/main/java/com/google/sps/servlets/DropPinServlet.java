package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Handles requests sent to the /hello URL. Try running a server and navigating to /hello! */
@WebServlet("/pin")
public class DropPinServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = request.getParameter("searchinput");
    String incidentType = request.getParameter("select-type-incident");
    String comment = request.getParameter("textarea");

    System.out.println("name: " + name);
    System.out.println("incident type: " + incidentType);
    System.out.println("comment: " + comment);

    //will do response later
  }
}