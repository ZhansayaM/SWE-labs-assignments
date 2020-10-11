package kz.edu.nu.cs.exercise;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/items")
public class PagingService {

    private List<String> list = new CopyOnWriteArrayList<String>();

    public PagingService() {
        for (int i = 0; i < 100; i++) {
            list.add("Entry " + String.valueOf(i));
        }
    }

    @GET
    public Response getMyList(@QueryParam("page") int size) {

        int start = size * 10;

        Gson gson = new Gson();
        String json;

        PagedHelper p = new PagedHelper();
        p.setList(list.subList(start,
        Math.min(start + 10, list.size())));

        p.setPrev("" + (size-1 >= 0 ? size-1: null));
        p.setNext("" + (list.size() > (size+1)*10 ? size+1 : null));

        json = gson.toJson(p, PagedHelper.class);

        return Response.ok(json).build();
    }
}
