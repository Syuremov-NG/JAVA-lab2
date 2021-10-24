package com.demo;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    Map<String, Integer> ids = new HashMap<>();
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=Windows-1251");
        HttpSession session = request.getSession();
        if(!ids.containsKey(session.getId())){
            ids.put(session.getId(),1);
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("users.xml"));
            NodeList userElements = document.getDocumentElement().getElementsByTagName("user");
            PrintWriter out = response.getWriter();
            Date date = new Date();

            if(ids.get(session.getId()) < 3) {
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
                    int buff = ids.get(session.getId()) + 1;
                    ids.put(session.getId(), buff);
                    request.setAttribute("message", (4-ids.get(session.getId())+" attempts " + session.getId()));
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
            }
            else {
                out.println("<html><body>");
                out.println("<h2>" + "Attempts ended! No entry!" + "</h2>");
                out.println("<html><body>");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}