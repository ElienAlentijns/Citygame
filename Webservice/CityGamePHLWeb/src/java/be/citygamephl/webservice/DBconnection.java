/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.citygamephl.webservice;
import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.postgresql.geometric.PGpoint;
import org.postgresql.util.Base64;

/**
 *
 * @author vincent
 */
public abstract class DBconnection {
    private static Connection connection = null;
    
    public static void connect() {
        
            try {
            if (connection == null) {
                String host = "192.168.45.6";
                String database = "webservice";
                String username = "webserver";
                String password = "N1c0745";
                String url = "jdbc:postgresql://" + host + "/" + database;
                String driverJDBC = "org.postgresql.Driver";
                Class.forName(driverJDBC);
                connection = DriverManager.getConnection(url, username,
                        password); //line firing the class not found exception

            } else if (connection.isClosed()) {
                connection = null;
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        
        
    }
    
    
    public static void disconnect() {
    if (connection != null) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        }
    }
    
    
    public static Connection getConnection() {

        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            } else {
                connect();
                return connection;
            }
        } catch (SQLException e) {
             e.printStackTrace(System.err);
            return null;
        }
    }
    
    
    public static Object test(int gameID, int playerID){
    
        Connection con = null;
        PreparedStatement stmt = null;
        String bericht = null;

        try {

            con = getConnection();
            
            stmt = con.prepareStatement("SELECT \"sp_SelectLastLocation\"(?)");
            stmt.setInt(1,gameID);
            ResultSet result = stmt.executeQuery();
            result.next();
            bericht = result.getString(1);
            return bericht;
        } catch (SQLException e){
            
            
        }finally {

            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
              
            }
        }
        return bericht;
    }
    
    //functie voor het oproepen van de stored procedure voor het inserten van de coördinaten van een speler
    public static String setPlayerCoördinate(int playerID, int gameID, PGpoint location){
    
        Connection con = null;
        PreparedStatement stmt = null;    
        boolean succes = false;
        try {


            con = getConnection();
            
            stmt = con.prepareStatement("SELECT * FROM public.\"sp_InsertRoute\"(?, ?, ?)");
            stmt.setInt(1,playerID);
            stmt.setInt(2,gameID);
            stmt.setObject(3, location);
            ResultSet result = stmt.executeQuery();
            succes = true;

        } catch (SQLException e){
            
            System.out.println(e);
        }finally {

            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
              
            }
            
            Gson convert = new Gson();
            return convert.toJson(succes);
        }

    }
    
    public static String lastPositions(int gameID, int playerID, String type){
    
        Connection con = null;
        PreparedStatement stmt = null;
        String bericht = null;
        ArrayList<Map> list = new ArrayList();
        
        try {

            con = getConnection();
            
            stmt = con.prepareStatement("SELECT * FROM public.\"sp_SelectLastLocation\"(?, ?, ?)");
            stmt.setInt(1,gameID);
            stmt.setInt(2,playerID);
            stmt.setString(3, type);
            ResultSet result = stmt.executeQuery();
                       
            while(result.next()){
                Map<String,String> info = new HashMap<String,String>();
                    info.put("player",result.getString(1));
                    
                    PGpoint point =(PGpoint) result.getObject(2);                    
                    
                    info.put("latitude",String.valueOf(point.x));
                    info.put("longitude", String.valueOf(point.y));
                    list.add(info);
            
            }

        } catch (SQLException e){
            
            System.out.println(e);
        }finally {

            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
              
            }
            
            Gson convert = new Gson();
            return convert.toJson(list);
        }

    }
    
    public static String login(String username, String password){
        Connection con = null;
        PreparedStatement stmt = null;
        String bericht = null;
        Map<String,String> info = new HashMap<String,String>();
        try {

            con = getConnection();
            
            stmt = con.prepareStatement("SELECT * FROM public.\"sp_Login\"(?, ?)");
            stmt.setString(1,username);
            stmt.setString(2,password);
            ResultSet result = stmt.executeQuery();
            
            while(result.next()){ 
                
                    info.put("gameID", String.valueOf(result.getInt(2)));
                    info.put("playerID",String.valueOf(result.getInt(1)));
                    info.put("intervalPrey", String.valueOf(result.getInt(3)));
                    info.put("intervalHunter", String.valueOf(result.getInt(4)));
                    info.put("role", result.getString(5));
            }
            
        } catch (SQLException e){
            
            System.out.println(e);
        }finally {
            
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
              
            }
            
            Gson convert = new Gson();
            return convert.toJson(info);
        }
    }
    //Functie voor het doorsturen van een foto
    public static String sendPhoto(int taskID, String photo,int playerID){
        Connection con = null;
        PreparedStatement stmt = null;
        String data;
        try {

            con = getConnection();
            
            stmt = con.prepareStatement("SELECT * FROM public.\"sp_InsertPhoto\"(?, ?, ?)");
            stmt.setInt(1,taskID);
            stmt.setString(2,photo);
            stmt.setInt(3,playerID);
            ResultSet result = stmt.executeQuery();
            
        } catch (SQLException e){
           
        }finally {
            
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
                          System.out.println(e);
            }
            
            Gson convert = new Gson();
            return "";
        }
    }
    
    public static String getPhoto(){
        Connection con = null;
        PreparedStatement stmt = null;
        String bericht = null;
        ArrayList<Map> list = new ArrayList();
        
        try {

            con = getConnection();
            
            stmt = con.prepareStatement("SELECT * FROM public.\"sp_SelectPhoto\"()");

            ResultSet result = stmt.executeQuery();
                       
            while(result.next()){
                Map<String,String> info = new HashMap<String,String>();
                    info.put("description",result.getString(1));
                    info.put("photo",result.getString(2));
                    list.add(info);
            }

        } catch (SQLException e){
            
            System.out.println(e);
        }finally {

            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
              
            }
            
            Gson convert = new Gson();
            return convert.toJson(list);
        }

    }
    
     //Functie voor het doorsturen van een foto
    public static String getPhoto(int taskID, String photo,int playerID){
        Connection con = null;
        PreparedStatement stmt = null;

        try {

            con = getConnection();
            
            stmt = con.prepareStatement("SELECT * FROM public.\"sp_InsertPhoto\"(?, ?, ?)");
            stmt.setInt(1,taskID);
            stmt.setString(2,photo);
            stmt.setInt(3,playerID);
            ResultSet result = stmt.executeQuery();
            
        } catch (SQLException e){
            System.out.println(e);
        }finally {
            
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
              
            }
            
            Gson convert = new Gson();
            return "";
        }
    }
    
    //Functie voor het doorsturen van een foto
    public static String getLatestTask(int gameID, String roleName){
        Connection con = null;
        PreparedStatement stmt = null;
        String bericht = null;

        Map<String,String> info = new HashMap<String,String>();
        try {

            con = getConnection();
            
            stmt = con.prepareStatement("SELECT * FROM public.\"sp_SelectNextTask\"(?, ?)");
            stmt.setInt(1,gameID);
            stmt.setString(2,roleName);
            ResultSet result = stmt.executeQuery();
            
            while(result.next()){ 
                info.put("description", result.getString(1));
                info.put("location",result.getString(2));
                PGpoint point =(PGpoint) result.getObject(3);                    
                    
                info.put("latitude",String.valueOf(point.x));
                info.put("longitude", String.valueOf(point.y));
            }
            
        } catch (SQLException e){
            System.out.println(e);
        }finally {
            
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
              
            }
            
            Gson convert = new Gson();
            return convert.toJson(info);
        }
    }
    
    //Functie voor het doorsturen van een chatbericht
    public static String sendMessage(int gameID, int playerID, String message, String chatbox){
        Connection con = null;
        PreparedStatement stmt = null;
        String bericht = null;

        Map<String,String> info = new HashMap<String,String>();
        try {

            con = getConnection();
            
            stmt = con.prepareStatement("SELECT * FROM public.\"sp_InsertMessage\"(?, ?, ? ,?)");
            stmt.setInt(1,gameID);
            stmt.setString(2,message);
            stmt.setString(3,chatbox);
            stmt.setInt(4,playerID);
            ResultSet result = stmt.executeQuery();
            
        } catch (SQLException e){
            System.out.println(e);
        }finally {
            
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
              
            }
            
            Gson convert = new Gson();
            return "";
        }
    }
    
        //Functie voor het opvragen van de chatberichten
    public static String getMessages(int gameID, String role, int lastMessageId){
    
        Connection con = null;
        PreparedStatement stmt = null;
        String bericht = null;
        ArrayList<Map> list = new ArrayList();
        
        try {

            con = getConnection();
            
            stmt = con.prepareStatement("SELECT * FROM public.\"sp_SelectMessages\"(?, ?, ?)");
            stmt.setString(1,role);
            stmt.setInt(2,gameID);
            stmt.setInt(3,lastMessageId);
            ResultSet result = stmt.executeQuery();
                       
            while(result.next()){
                Map<String,String> info = new HashMap<String,String>();
                    info.put("message_ID", String.valueOf(result.getInt(1)));
                    info.put("chatbox", result.getString(2));
                    info.put("message",result.getString(3));
                    info.put("time", String.valueOf(result.getTime(4)));
                    info.put("player", result.getString(5));
                    list.add(info);
            }
            
        } catch (SQLException e){
            System.out.println(e);
        }finally {
            
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
              
            }
            
            Gson convert = new Gson();
            return convert.toJson(list);
        }
    }
    
    public static String getRoleDescription(String role){
    
        Connection con = null;
        PreparedStatement stmt = null;
        String bericht = null;
        ArrayList<Map> list = new ArrayList();
        
        try {

            con = getConnection();
            
            stmt = con.prepareStatement("SELECT * FROM public.\"sp_SelectRoleDescription\"(?)");
            stmt.setString(1,role);
            ResultSet result = stmt.executeQuery();
                       
            while(result.next()){
                Map<String,String> info = new HashMap<String,String>();
                    info.put("roleDescription", result.getString(1));
                    list.add(info);
            }
            
        } catch (SQLException e){
            System.out.println(e);
        }finally {
            
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e){
              
            }
            
            Gson convert = new Gson();
            return convert.toJson(list);
        }
    }
}
