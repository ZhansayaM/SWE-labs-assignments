package kz.edu.nu.cs.exercise;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalTime;
import java.time.ZoneId;

@WebServlet(urlPatterns = { "/myservlet" })
public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public MyServlet() {
        super();
    }

    static ArrayList<String> logs = new ArrayList<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        LocalTime time = LocalTime.now(ZoneId.systemDefault());
        logs.add("Time of access: " + time + "\t Host: " + request.getRemoteHost() + "\t Path: " + request.getContextPath() + "\n");
        for (int i = 0; i < logs.size(); i++) {
            out.println(logs.get(i));
        }
        //out.println(logs);
      }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            doGet(request, response);
    }
}
