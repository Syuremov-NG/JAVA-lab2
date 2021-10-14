package html.task4;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

@WebServlet(name = "task4Servlet", value = "/task4Servlet")
public class task4Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=Windows-1251");
        URL url = new URL("http://localhost:8080/task4Servlet");
        URL codesUrl = new URL("https://cbr.ru/scripts/XML_val.asp?d=0");
        DocumentBuilder builder = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        PrintWriter out = response.getWriter();
        Document docCodes = null;
        String valCode = "";
        String valName = request.getParameter("name");

        try {
            builder = factory.newDocumentBuilder();
            URLConnection connCodes = codesUrl.openConnection();
            docCodes = builder.parse(connCodes.getInputStream());
            NodeList elementsCodes = docCodes.getDocumentElement().getElementsByTagName("Item");
            for(int j = 0; j < elementsCodes.getLength(); j++){
                if(Objects.equals(elementsCodes.item(j).getChildNodes().item(0).getTextContent(), valName)){
                    valCode = elementsCodes.item(j).getChildNodes().item(3).getTextContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(request.getParameter("firstDate") == "" && request.getParameter("secDate") == "" || request.getParameter("name") == ""){
            response.sendRedirect("task4.jsp");
        }
        else if(request.getParameter("firstDate") != "" && request.getParameter("secDate") == ""){
            url = new URL("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1="+request.getParameter("firstDate")+"&date_req2="+request.getParameter("firstDate")+"&VAL_NM_RQ="+valCode);
        }
        else if(request.getParameter("firstDate") == "" && request.getParameter("secDate") != ""){
            url = new URL("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1="+request.getParameter("secDate")+"&date_req2="+request.getParameter("secDate")+"&VAL_NM_RQ="+valCode);
        }
        else if(request.getParameter("firstDate") != "" && request.getParameter("secDate") != ""){
            url = new URL("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1="+request.getParameter("firstDate")+"&date_req2="+request.getParameter("secDate")+"&VAL_NM_RQ="+valCode);
        }

        URLConnection conn = url.openConnection();

        try {
            Document doc = builder.parse(conn.getInputStream());
            NodeList elements = doc.getDocumentElement().getElementsByTagName("Record");
            NodeList elementsCodes = docCodes.getDocumentElement().getElementsByTagName("Item");

            out.println("ID выбранной купюры: "+valCode);

            out.println("<table><tr><th>Date</th><th>ID</th><th>Nominal</th><th>Value</th></tr>");
            if(elements.getLength() == 0){
                out.println("<p>Информации нет</p>");
            }
            else{
                for(int i = 0; i < elements.getLength(); i++){
                    out.println("<tr><td>"+elements.item(i).getAttributes().item(0).getTextContent()+"</td>"+
                            "<td>"+valName+"</td>"+
                            "<td>"+elements.item(i).getChildNodes().item(0).getTextContent()+"</td>"+
                            "<td>"+elements.item(i).getChildNodes().item(1).getTextContent()+"</td></tr>");
                }
            }
            out.println("</table>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
