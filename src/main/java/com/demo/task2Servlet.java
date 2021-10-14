package com.demo;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "task2Servlet", value = "/task2Servlet")
public class task2Servlet extends HttpServlet {
    int reqCount;

    @Override
    public void init() throws ServletException {
        reqCount = 0;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        this.reqCount++;

        switch (request.getParameter("message")){
            case "one":
                request.setAttribute("message", ("Session id: " + session.getId() + "\n"
                        + "Session start in: " + new Date(session.getCreationTime())));
                break;
            case "two":
                request.setAttribute("message", ("Time: " + new Date()));
                break;
            case "three":
                request.setAttribute("message", ("Session requests count: " + reqCount));
        }
        request.getRequestDispatcher("task2.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
