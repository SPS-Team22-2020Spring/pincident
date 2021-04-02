package com.google.sps;

import java.sql.*;  

public class MysqlConnector{  
    public Connection startConnection() throws SQLException{
        try{  
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3366/pincident_database","root","");
        System.out.println("Connection ready");
        return connection;
        }catch(Exception e){ 
            System.out.println("Connection fail");
            System.out.println(e);
        }  
        Connection ll = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3366/pincident_database","root","");
        return ll;
    } 
}  