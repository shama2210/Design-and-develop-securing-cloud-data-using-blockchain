/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import utils.dbserver;
import utils.ipaddr;

/**
 *
 * @author Lenovo
 */
public class readreq extends Thread
{
    readreq()
    {
        super();
        start();
    }
    
    public void run()
    {
        try
        {
            ServerSocket ss=new ServerSocket(ipaddr.cloudport);
            
            while (true)
            {
                Socket soc=ss.accept();
                
                ObjectOutputStream oos=new ObjectOutputStream(soc.getOutputStream());
                ObjectInputStream oin=new ObjectInputStream(soc.getInputStream());
                
                String req=(String)oin.readObject();
                System.out.println(req);
                
                if (req.equals("REGISTER"))
                {
                    String uname=(String)oin.readObject();
                    String pass=(String)oin.readObject();
                    String email=(String)oin.readObject();
                    
                    Connection con=new dbserver().dbconnect();
                    
                    
                    
                    if (con==null)
                        oos.writeObject("FAILED");
                    else
                    {
                        PreparedStatement pst=con.prepareStatement("select * from register where uname=?");
                        pst.setString(1,uname);
                        ResultSet rs=pst.executeQuery();
                        if (rs.next())
                        {
                            oos.writeObject("ALREADY");
                        }
                        else
                        {
                            PreparedStatement pst1=con.prepareStatement("insert into register values(?,?,?)");
                            pst1.setString(1,uname);
                            pst1.setString(2,pass);
                            pst1.setString(3,email);
                            pst1.executeUpdate();
                            
                            oos.writeObject("SUCCESS");
                        }
                        
                        
                        con.close();
                    }
                }
                else
                if (req.equals("LOGIN"))
                {
                    String uname=(String)oin.readObject();
                    String pass=(String)oin.readObject();
                                        
                    Connection con=new dbserver().dbconnect();
                                     
                    
                    if (con==null)
                        oos.writeObject("FAILED");
                    else
                    {
                        PreparedStatement pst=con.prepareStatement("select * from register where uname=? and pass=?");
                        pst.setString(1,uname);
                        pst.setString(2,pass);
                        ResultSet rs=pst.executeQuery();
                        if (rs.next())
                        {
                            oos.writeObject("SUCCESS");
                        }
                        else
                        {
                                                        
                            oos.writeObject("INVALID");
                        }
                        
                        con.close();
                    }
                    
                }
                else
                if (req.equals("UPLOAD"))
                {
                    String uname=(String)oin.readObject();
                    String fname=(String)oin.readObject();
                    System.out.println(fname);
                    byte b[]=(byte[])oin.readObject();
                    
                    String reply=checkfile(uname,fname);
                    System.out.println(reply);
                    if (reply.equals("EXIST"))
                    {
                        oos.writeObject(reply);
                    }
                    else
                    {
                        File f=new File("upload/"+uname);
                        if (!f.exists())
                        {
                            f.mkdirs();
                        }
                            
                        b=new DES().encrypt(b);
                            FileOutputStream fout=new FileOutputStream("upload/"+uname+"/"+fname);
                            fout.write(b);
                            fout.close();
                            oos.writeObject("SUCCESS");
                            System.out.println("upload success");
                        
                        
                    }
                }
                else
                if (req.equals("GETFILES"))
                {
                    String uname=(String)oin.readObject();
                    
                    Vector v=getfiles(uname);
                    if (v.size()>0)
                    {
                        oos.writeObject("SUCCESS");
                        oos.writeObject(v);
                    }
                    else
                     oos.writeObject("NOFILES");   
                }
                else
                if (req.equals("GETSHAREDFILES"))
                {
                    String uname=(String)oin.readObject();
                    
                    Vector v=getsharedfiles(uname);
                    if (v.size()>0)
                    {
                        oos.writeObject("SUCCESS");
                        oos.writeObject(v);
                    }
                    else
                     oos.writeObject("NOFILES");   
                }
                else
                if (req.equals("GETUSERS"))
                {
                    String uname=(String)oin.readObject();
                    
                    Vector v=getusers(uname);
                    if (v.size()>0)
                    {
                        oos.writeObject("SUCCESS");
                        oos.writeObject(v);
                    }
                    else
                     oos.writeObject("NOFILES");   
                }
                else
                if (req.equals("EDIT"))
                {
                    String uname=(String)oin.readObject();
                    String fname=(String)oin.readObject();
                    
                    File f=new File("upload/"+uname+"/"+fname);
                     System.out.println(f.getPath());
                    if (f.exists())
                    {
                        System.out.println("FILE EXIST");
                        oos.writeObject("SUCCESS");
                        FileInputStream fin=new FileInputStream(f);
                        byte b[]=new byte[fin.available()];
                        fin.read(b);
                        fin.close();
                        
                        b=new DES().decrypt(b);
                        oos.writeObject(b);
                    }
                    else
                    {
                        oos.writeObject("NOTFOUND");
                    }
                }
                else
                if (req.equals("SAVE")) //save edited file
                {
                    String uname=(String)oin.readObject();
                    String fname=(String)oin.readObject();
                    byte b[]=(byte[])oin.readObject();
                    
                    b=new DES().encrypt(b);
                            FileOutputStream fout=new FileOutputStream("upload/"+uname+"/"+fname);
                            fout.write(b);
                            fout.close();
                            oos.writeObject("SUCCESS");
                            System.out.println("save success");
                    
                }
                else
                if (req.equals("VIEWSHARE"))
                {
                    String uname=(String)oin.readObject();
                    String fname=(String)oin.readObject();
                    
                    Connection con=new dbserver().dbconnect();
                    PreparedStatement pst=con.prepareStatement("select * from share where rname=? and fname=?");
                        pst.setString(1,uname);
                        pst.setString(2,fname);
                    
           
                     ResultSet rs=pst.executeQuery();
                     
                     rs.next();
                     
                     String sname=rs.getString(1).trim();
                    
                    File f=new File("upload/"+sname+"/"+fname);
                     System.out.println(f.getPath());
                    if (f.exists())
                    {
                        System.out.println("FILE EXIST");
                        oos.writeObject("SUCCESS");
                        FileInputStream fin=new FileInputStream(f);
                        byte b[]=new byte[fin.available()];
                        fin.read(b);
                        fin.close();
                        
                        b=new DES().decrypt(b);
                        oos.writeObject(b);
                    }
                    else
                    {
                        oos.writeObject("NOTFOUND");
                    }
                }
                else
                if (req.equals("DELETE"))
                {
                    String uname=(String)oin.readObject();
                    String fname=(String)oin.readObject();
                    
                    File f=new File("upload/"+uname+"/"+fname);
                     System.out.println(f.getPath());
                    if (f.exists())
                    {
                        System.out.println("FILE EXIST");
                         f.delete();
                        oos.writeObject("SUCCESS");
                                                                    
                        
                    }
                    else
                    {
                        oos.writeObject("NOTFOUND");
                    }
                    
                }
                else
                if (req.equals("SHARE"))
                {
                   String uname=(String)oin.readObject();
                    String fname=(String)oin.readObject();
                    String rname=(String)oin.readObject();
                    
                    Connection con=new dbserver().dbconnect();
                    PreparedStatement pst=con.prepareStatement("select * from share where uname=? and rname=? and fname=?");
                    pst.setString(1,uname);
                    pst.setString(2,rname);
                    pst.setString(3,fname);
                    ResultSet rs=pst.executeQuery();
                    if (rs.next())
                    {
                        oos.writeObject("ALREADY");
                    }
                    else
                    {
                       PreparedStatement pst2=con.prepareStatement("insert into share values(?,?,?)");
                       pst2.setString(1,uname);
                       pst2.setString(2,rname);
                       pst2.setString(3,fname);
                       pst2.executeUpdate();
                       
                       oos.writeObject("SUCCESS");
                    }
                    
                    
                    con.close();
                }
                else
                if (req.equals("DOWNLOAD"))
                {
                    
                }
               
                
                
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    
   Vector getfiles(String uname)
   {
       Vector v=new Vector();
       try
       {
           File f=new File("upload/"+uname);
           if (f.exists())
           {
               String files[]=f.list();
               for (int i=0;i<files.length;i++)
                   v.add(files[i]);
           }
           
               
       }
       catch(Exception e)
       {
           System.out.println(e);
           e.printStackTrace();
       }
       
       return v;
       
   }
   
   Vector getsharedfiles(String uname)
   {
       Vector v=new Vector();
       try
       {
           Connection con=new dbserver().dbconnect();
           PreparedStatement pst=con.prepareStatement("select * from share where rname=?");
           pst.setString(1,uname);
           
           ResultSet rs=pst.executeQuery();
           
           while (rs.next())
           {
               v.add(rs.getString(3));
           }
           
           con.close();
               
       }
       catch(Exception e)
       {
           System.out.println(e);
           e.printStackTrace();
       }
       
       return v;
       
   }
   
   Vector getusers(String uname)
   {
       Vector v=new Vector();
       try
       {
            Connection con=new dbserver().dbconnect();
                                     
                    
                        PreparedStatement pst=con.prepareStatement("select * from register where uname<>?");
                        pst.setString(1,uname);
                       
                        ResultSet rs=pst.executeQuery();
                        if (rs.next())
                        {
                           v.add(rs.getString(1).trim());
                        }
                       
                        con.close();
                    
               
       }
       catch(Exception e)
       {
           System.out.println(e);
           e.printStackTrace();
       }
       
       return v;
       
   }
    
    
    String checkfile(String uname,String fname)
    {
        String reply="NOTEXIST";
        try
        {
            File f=new File("upload/"+uname+"/"+fname);
            if (f.exists())
                reply="EXIST";
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return reply;
    }
}
