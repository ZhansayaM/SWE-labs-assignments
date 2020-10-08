package kz.edu.nu.cs.se.hw;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/items")
public class ListItemsService {
    
    private List<String> list = new CopyOnWriteArrayList<String>();
    
    public ListItemsService() {
        for (int i = 0; i < 20; i++) {
            Date timestamp = new Date();
            String date = timestamp.toString();
            list.add("Entry " + String.valueOf(i)+ "\t\t |||" + date + "\t\t |||");
        }
        
        Collections.reverse(list);
    }
    
    @GET
    public Response getList() {

        Gson gson = new Gson();
        return Response.ok(gson.toJson(list)).build();
    }
    @GET
    @Path("clear")
    public Response clearListItems() {
        list.clear();
        Gson gson = new Gson();

        return Response.ok(gson.toJson(list)).build();
    }

    @GET
    @Path("remove" + "/{id: [0-9]+}")
    public Response removeListItem(@PathParam("id") String id) {
        int i = Integer.parseInt(id);
        list.remove( - (i + 1) + list.size());

        Gson gson = new Gson();

        return Response.ok(gson.toJson(list)).build();
    }
    
    @GET
    @Path("{id: [0-9]+}")
    public Response getListItem(@PathParam("id") String id) {
        int i = Integer.parseInt(id);
        
        return Response.ok(list.get(i)).build();
    }
    
    @POST
    public Response postListItem(@FormParam("newEntry") String entry) {
        Date timestamp = new Date();
        String date = timestamp.toString();
        list.add(0, entry + "\t\t |||" + date + "\t\t |||");


        return Response.ok().build();
    }
}
