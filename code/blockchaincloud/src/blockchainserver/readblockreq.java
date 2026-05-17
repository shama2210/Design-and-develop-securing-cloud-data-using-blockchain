/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainserver;

import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Lenovo
 */
public class readblockreq extends Thread
{
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;
    public static String previousHash="0";
    
    readblockreq()
    {
        super();
        start();
    }
    
    public void run()
    {
        try
        {
            ServerSocket ss=new ServerSocket(3000);
            
            while (true)
            {
                Socket soc=ss.accept();
                ObjectOutputStream oos=new ObjectOutputStream(soc.getOutputStream());
                ObjectInputStream oin=new ObjectInputStream(soc.getInputStream());
                
                String req=(String)oin.readObject();
                
                if (req.equals("ADDBLOCK"))
                {
                   String user=(String)oin.readObject();
                   String opr1=(String)oin.readObject();
                   String opr2=(String)oin.readObject();
                   String opr3=(String)oin.readObject();
                   String opr=opr1+"-->"+opr2+"-->"+opr3;
                   
                   blockcserver.jTextArea1.append("Operation "+opr+" from "+user+"\n");
                   
                   Block b=new Block(user,opr,previousHash);
                   blockchain.add(b);
                   blockcserver.jTextArea1.append("Block Successfully added!\n");
                   blockchain.get(blockchain.size()-1).mineBlock(difficulty);
                   
                   oos.writeObject("SUCCESS");
                }
                else
                if (req.equals("VIEWLOG"))
                {
                    String user=(String)oin.readObject();
                    System.out.println(user);
                    String log=getlog(user);
                    
                    oos.writeObject(log);
                }
                
                
                oos.close();
                oin.close();
                soc.close();
                
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }    
    
    
    String getlog(String user)
    {
        Vector<Block> v=new Vector<Block>();
        String reply="";
        try
        {
            for (int i=0;i<blockchain.size();i++)
            {
                Block b=blockchain.get(i);
                
                if (b.user.equals(user))
                {
                    v.add(b);
                }
            }
            
            for (int j=0;j<v.size();j++)
            {
                reply+=v.get(j).operation+"$";
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return reply;
    }
    
    
    void writelogs()
    {
        try
        {
            if (blockchain.size()>0)
            {
                JSONArray blockList = new JSONArray();
                
                for (int i=0;i<blockchain.size();i++)
                {
                    Block b=(Block)blockchain.get(i);
                    JSONObject blockdetails = new JSONObject();
                    blockdetails.put("user", b.user);
                    blockdetails.put("operation", b.operation);
                    blockdetails.put("previoushash", b.previousHash);
                    blockdetails.put("timestamp", b.timeStamp);
                    blockdetails.put("hash", b.hash);
                    
                    JSONObject blockObject = new JSONObject(); 
                    blockObject.put("block", blockdetails);
                    
                    blockList.add(blockObject);
                }
                
                FileWriter file = new FileWriter("userlogs.json");
                 file.write(blockList.toJSONString()); 
                 file.flush();
                 file.close();
                
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
        
}
