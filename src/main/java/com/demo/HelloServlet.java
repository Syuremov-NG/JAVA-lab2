package com.demo;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.Date;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private int tryy = 0;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        System.out.println(tryy);
//        boolean  contains = false;
//        int tryy = 0;
//        for(Cookie item : request.getCookies()){
//            if(item.getName().equals("tryes")){
//                contains = true;
//                tryy = Integer.parseInt(item.getValue());
//            }
//        }
//        if(!contains){
//        Cookie newCookie = new Cookie("tryes", "0");
//        newCookie.setMaxAge(-1);
//            response.addCookie(newCookie);
//        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("users.xml"));
            NodeList userElements = document.getDocumentElement().getElementsByTagName("user");
            PrintWriter out = response.getWriter();
            Date date = new Date();

            if(tryy < 3) {
                boolean ans = false;
                for (int i = 0; i < userElements.getLength(); i++) {
                    Node user = userElements.item(i);
                    if (request.getParameter("login").equals(user.getAttributes().getNamedItem("login").getNodeValue())
                            && request.getParameter("password").equals(user.getAttributes().getNamedItem("password").getNodeValue()) ) {
                        ans = true;
                        System.out.println("найден");
                        break;
                    }
                }

                if(ans){
                    out.println("<html><body>");
                    out.println("<h1>" + "Hello " + request.getParameter("login") + "</h1>");
                    out.println("<h2>" + "Now " + date + "</h2>");
                    out.println("<html><body>");
                }
                else{
//                    response.addCookie(new Cookie("tryes", String.valueOf(tryy+1)));
                    tryy++;
                    response.sendRedirect("index.jsp");
                }
            }
            else {
                out.println("<html><body>");
                out.println("<h2>" + "Attemps ended! No entry!" + "</h2>");
                out.println("<html><body>");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}