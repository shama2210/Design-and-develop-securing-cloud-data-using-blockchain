/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Lenovo
 */
public class sendblock 
{
    String addblock(String user,String operation,String data)
    {
        String reply="FAILED";
       try
       {
           Socket soc=new Socket(utils.ipaddr.blockchainip,utils.ipaddr.blockchainport);
           ObjectOutputStream oos=new ObjectOutputStream(soc.getOutputStream());
           ObjectInputStream oin=new ObjectInputStream(soc.getInputStream());
           
           oos.writeObject("ADDBLOCK");
           oos.writeObject(user);
           oos.writeObject(operation);
           oos.writeObject(data);
           oos.writeObject(new java.util.Date().toString());
           
           reply=(String)oin.readObject();
       }
       catch(Exception e)
       {
           System.out.println(e);
       }
       
       return reply;
    }
    
}
