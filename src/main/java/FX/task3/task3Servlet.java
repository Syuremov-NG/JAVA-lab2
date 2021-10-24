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

    Map<String, Integer> ids = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!ids.containsKey(request.getParameter("id"))){
            ids.put(request.getParameter("id"),1);
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("E:/Programming/TomCAT/bin/users.xml"));
            NodeList userElements = document.getDocumentElement().getElementsByTagName("user");
            PrintWriter out = response.getWriter();
            Date date = new Date();
            ans = false;
            if(ids.get(request.getParameter("id")) < 3) {
                for (int i = 0; i < userElements.getLength(); i++) {
                    Node user = userElements.item(i);
                    if (request.getParameter("login") != null && request.getParameter("login").equals(user.getAttributes().item(0).getTextContent()) &&
                            request.getParameter("password") != null && request.getParameter("password").equals(user.getAttributes().item(1).getTextContent())) {
                        ans = true;
                        break;
                    }
                }
                if(ans){
                    out.println("ans=true;time=" + date +";login="+request.getParameter("login"));
                    logged = true;
                }
                else{
                    out.println("ans=false;tryy=" + ids.get(request.getParameter("id"))+";id="+request.getParameter("id") );
                    int buff = ids.get(request.getParameter("id")) + 1;
                    ids.put(request.getParameter("id"), buff);
                }
            }
            else{
                out.println("ans=false;tryy="+ids.get(request.getParameter("id"))+ ";id="+request.getParameter("id"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        if(resp.containsKey("id")&&!ids.containsKey(resp.get("id"))){
            ids.put(resp.get("id"),1);
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("users.xml"));
            NodeList userElements = document.getDocumentElement().getElementsByTagName("user");
            PrintWriter out = response.getWriter();
            Date date = new Date();

            if(ids.get(resp.get("id")) < 3){

                ans = false;
                for (int i = 0; i < userElements.getLength(); i++) {
                    Node user = userElements.item(i);
                    if (resp.get("login").equals(user.getAttributes().item(0).getTextContent())
                            && resp.get("password").equals(user.getAttributes().item(1).getTextContent()) ) {
                        ans = true;
                        break;
                    }
                }
                if(ans){
                    out.println("ans=true;time=" + date +";login="+resp.get("login"));
                    logged = true;
                }
                else{
                    out.println("ans=false;tryy=" + ids.get(resp.get("id")));
                    int buff = ids.get(resp.get("id")) + 1;
                    ids.put(resp.get("id"), buff);
                }
            }
            else{
                out.println("ans=false;tryy="+ids.get(resp.get("id")));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void test(HttpServletRequest request, NodeList userElements, PrintWriter out, Date date) {
    }

    @Override
    public void destroy() {
        tryy = 0;
    }
}
