/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Lenovo
 */
public class dbserver {
    
    public dbserver()
    {
        
    }
    
    Connection con=null;
    
    public Connection dbconnect()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
           con=DriverManager.getConnection("jdbc:mysql://localhost:3306/blockchaincloud","root","root");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return con;
    }
    
}
