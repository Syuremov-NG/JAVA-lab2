package FX.task3;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "task3Servlet", value = "/task3Servlet")
public class task3Servlet extends HttpServlet {
    private int tryy = 0;
    boolean ans = false;
    boolean logged = false;
    String loggedUser = "";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("users.xml"));
            NodeList userElements = document.getDocumentElement().getElementsByTagName("user");
            PrintWriter out = response.getWriter();
            Date date = new Date();
            ans = false;
            if(!logged) {
                for (int i = 0; i < userElements.getLength(); i++) {
                    Node user = userElements.item(i);
                    if (request.getParameter("login").equals(user.getAttributes().getNamedItem("login").getNodeValue())
                            && request.getParameter("password").equals(user.getAttributes().getNamedItem("password").getNodeValue()) ) {
                        ans = true;
                        out.println("ans=true;time=" + date +";login="+request.getParameter("login"));
                        logged = true;
                        loggedUser = request.getParameter("login");
                        break;
                    }
                }
                if(!ans){
                    out.println("ans=false;tryy=" + tryy);
                    tryy++;
                }
            }
            else{
                out.println("ans=true;time=" + date +";login="+loggedUser);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("users.xml"));
            NodeList userElements = document.getDocumentElement().getElementsByTagName("user");
            PrintWriter out = response.getWriter();
            Date date = new Date();

            if(!logged){
                String str = null;
                StringBuffer data = new StringBuffer();
                Map<String,String> resp = new HashMap<>();
                BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
                while((str=in.readLine()) != null) data.append(str);
                String[] res = data.toString().split(";");
                for(String item : res){
                    String[] itemS = item.split("=");
                    resp.put(itemS[0], itemS[1]);
                }

                ans = false;
                for (int i = 0; i < userElements.getLength(); i++) {
                    Node user = userElements.item(i);
                    if (resp.get("login").equals(user.getAttributes().getNamedItem("login").getNodeValue())
                            && resp.get("password").equals(user.getAttributes().getNamedItem("password").getNodeValue()) ) {
                        ans = true;
                        out.println("ans=true;time=" + date +";login="+resp.get("login"));
                        logged = true;
                        loggedUser = resp.get("login");
                        break;
                    }
                }
                if(!ans){
                    out.println("ans=false;tryy=" + tryy);
                    tryy++;
                }
            }
            else{
                out.println("ans=true;time=" + date +";login="+loggedUser);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        tryy = 0;
    }
}
