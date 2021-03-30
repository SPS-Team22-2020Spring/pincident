package com.google.sps.servlets;

import java.util.Arrays;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.io.PrintWriter;
import com.google.sps.classes.Locationob;

@WebServlet("/ls")
public class LocationServlet extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException{

        List<Locationob> locations = new LinkedList<Locationob>();
        String incidents = request.getParameter("incidents");
        String typereports = request.getParameter("TypeReports");
        
        System.out.println(incidents+ " " +  typereports);
        if(!incidents.equals("none") || !typereports.equals("none")){
            //If there are  filters for incident but not for typereports
            if(!incidents.equals("none") && typereports.equals("none")){ //IF THIS OPTION IS SELECTED WE DO THE QUERY: "SELECT ... FROM LOCATIONS WHERE incidentsmap =" + incidents; 
                switch(incidents){
                    case "all":
                        incidents = ">=1";
                    break;
                    case "warning":
                        incidents = ">1 AND incidentsmap <=5";
                    break;
                    case "danger":
                        incidents = ">5 AND incidentsmap <= 10";
                    break;
                    case "dangerous":
                        incidents = "> 10 AND incidentsmap <= 20";
                    break;
                    case "verydangerous":
                        incidents = ">20";
                    break;
                    default:
                        //In case someone changes the value in the html no error will appear, instead a list of all locations where incidents > 1
                        incidents = ">=1";
                    break;
                }
                //This option will do the same query with the data sent just for incidents
                System.out.println("returnAllIncidentsWhere("+incidents+")");
            }else{
                if(incidents.equals("none") && !typereports.equals("none")){
                    if(checktypereports(typereports)){
                        System.out.println("returnAllLocationsWhereTypeReport("+typereports+")");
                    }else{
                        //In case someone changes the value in the html no error will appear, instead a list of locations with race
                        System.out.println("returnAllLocationsWhereTypeReport(race)");
                    }
                }else{ //IF THE PAST TWO ARE NOT  TRUE That means we have a double filter query
                    if(checkincidents(incidents) && checktypereports(typereports)){
                        //If the user is not trolling
                        System.out.println("returnAllLocationsWhereIncidentType("+incidents+","+typereports+")");
                        //"Select locationID from incidents where typereports = typereports" 
                        //Once we have all the ids with tah type of report
                        //"Select locationID from incidents where typereports = typereports" 
                    }else{
                        //if the user is trolling return all locations
                        System.out.println("returnAllLocations()");
                    }
                }
            }
        }else{
            //If both filters are None we return every location
            System.out.println("returnAllLocations()");
        }

        /////////////////////In this part we need to fetch the locations via database//////////////////////// 
        Locationob loc1 = new Locationob(1, "Hello", -31, -30, "house1", null, 5);
        Locationob loc2 = new Locationob(2, "Hello1", -32, -30, "house2", null, 6);
        Locationob loc3 = new Locationob(3, "Hello2", -33, -30, "house3", null, 7);
        Locationob loc4 = new Locationob(4, "Hello3", -34, -30, "house4", null, 8);
        Locationob loc5 = new Locationob(5, "Hello4", -35, -30, "house5", null, 9);

        locations.add(loc1);
        locations.add(loc2);
        locations.add(loc3);
        locations.add(loc4);
        locations.add(loc5);
        /////////////////////////////////////////////////////////////////////////  

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(new Gson().toJson(locations));
        out.flush();   
    }
    
    private boolean checkincidents(String tocheck){
        String arr[] = {"all","warning","danger","dangerous", "verydangerous"};
        boolean yes = Arrays.asList(arr).contains(tocheck);
        return yes;
    }

    private boolean checktypereports(String tocheck){
        String arr[] = {"race","nationality","religion","gender","sexuality"};
        boolean yes = Arrays.asList(arr).contains(tocheck);
        return yes;
    }
}

