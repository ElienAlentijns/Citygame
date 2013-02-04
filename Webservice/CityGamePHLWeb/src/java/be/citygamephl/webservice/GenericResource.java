/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.citygamephl.webservice;

import com.google.gson.Gson;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.postgresql.geometric.PGpoint;
import org.postgresql.util.Base64;

/**
 * REST Web Service
 *
 * @author vincent
 */
@Path("generic")
public class GenericResource {
   
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    /**
     * Retrieves representation of an instance of be.citygamephl.webservice.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/getLocations/{gameID}/{playerID}/{type}")
    @Produces("application/json")
    public String getLocations(@PathParam("gameID") int gameID,@PathParam("playerID") int playerID,@PathParam("type") String type) {
        //TODO return proper representation object
        
        return DBconnection.lastPositions(gameID,playerID,type);
    }

    
    @POST
    @Path("/saveLocations")
    @Produces("application/json")
    public String saveLocations(@FormParam ("gameID") int gameID, @FormParam ("playerID") int playerID, @FormParam ("locations") String locations) {
        //TODO return proper representation object
        
         Gson converter = new Gson();
         ArrayList<Map> info = converter.fromJson(locations,ArrayList.class);
       
        tekst = locations +" " + info;
        return locations +" " + info;
    }
    
    @GET
    @Path("/saveLocations/{gameID}/{playerID}/{posistion}")
    @Produces("application/json")
    public String saveLocations2(@PathParam ("gameID") int gameID, @PathParam ("playerID") String role, @PathParam ("posistion") String position){
        
         Gson converter = new Gson();
         ArrayList<Map> info = converter.fromJson(position,ArrayList.class);
       
        tekst = position +" " + info;

        return position;
    }
    
    public static String tekst;
    @GET
    @Path("/test")
    @Produces("application/json")
    public String getTest() {
         
     return tekst;
     }
    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
   
    @POST 
    @Consumes("application/json")
    @Path("/create")
    public void create(final String photo) {
        System.out.println("param1 = " + photo);
        tekst = photo;

    }
    
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/plain")
    @Path("/message")
    public String updateMessage(@FormParam ("name") String name, @FormParam ("city") String city){
            System.out.println("Name:" + name + " and City:" + city);
            // Forward details to service layer.
            Gson converter = new Gson();
            return converter.toJson("Success:true");
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/plain")
    @Path("/sendPhoto")
    public String sendPhoto(@FormParam ("playerID") int playerID, @FormParam ("taskID") int taskID, @FormParam ("photo") String photo){

            return DBconnection.sendPhoto(taskID, photo,playerID);
    }
    
    @GET
    @Path("/getPhoto/")
    @Produces("application/json")
    public String getPhoto() {
        //TODO return proper representation object        
        return DBconnection.getPhoto();
    }

    @GET
    @Path("/login/{username}/{password}")
    @Produces("application/json")
    public String login(@PathParam("username") String username,@PathParam("password") String password) {
        //TODO return proper representation object        
        return DBconnection.login(username,password);
    }
    
    @POST
    @Path("/loginPost")
    @Produces("application/json")
    public String login2(@FormParam ("username") String username, @FormParam ("password") String password) {
        //TODO return proper representation object
       
        return DBconnection.login(username,password);
    }
    
    @GET
    @Path("/getLatestTask/{gameID}/{role}")
    @Produces("application/json")
    public String getLatestTask(@PathParam("gameID") int gameID,@PathParam("role") String role) {
        //TODO return proper representation object
        
        return DBconnection.getLatestTask(gameID,role);
    }
    
    /*@POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/plain")
    @Path("/sendMessage")*/
    @POST
    @Path("/sendMessage")
    @Produces("application/json")
    public String sendMessage(@FormParam ("gameID") int gameID,@FormParam ("playerID")  int playerID,@FormParam ("message") String message, @FormParam ("chatbox") String chatbox){
        return DBconnection.sendMessage(gameID, playerID, message, chatbox);
    }
    
    /*@POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/plain")
    @Path("/getMessages")*/
    @GET
    @Path("/getMessages/{gameID}/{role}/{lastMessageId}")
    @Produces("application/json")
    public String getMessages(@PathParam ("gameID") int gameID, @PathParam ("role") String role, @PathParam ("lastMessageId") int lastMessageId){
        return DBconnection.getMessages(gameID, role, lastMessageId);
    }
    
    
    
    @GET
    @Path("/getRoleDescription/{role}")
    @Produces("application/json")
    public String getRoleDescription(@PathParam("role") String role) {
        //TODO return proper representation object        
        return DBconnection.getRoleDescription(role);
    }
     
    @POST
    @Path("/setPlayerCoordinate")
    @Produces("application/json")
    public String setPlayerCoordinate(@FormParam("playerID") int playerID,@FormParam("gameID") int gameID,@FormParam("latitude") double latitude,@FormParam("longitude") double longitude) {
        //TODO return proper representation object        
        PGpoint location = new PGpoint(latitude, longitude);
        return DBconnection.setPlayerCoördinate(playerID, gameID, location);
    }
}
