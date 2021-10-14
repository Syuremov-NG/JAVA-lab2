package FX.task5;

import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class task5Controller {
    private final ObservableList<Valute> valutes = FXCollections.observableArrayList();
    public static class Valute{
        private String date;
        private String id;
        private float nominal;
        private float value;
        Valute(String d, String i, float n, float v){
            date = d;
            id =i;
            nominal = n;
            value = v;
        }

        public float getNominal() {
            return nominal;
        }

        public float getValue() {
            return value;
        }

        public String getDate() {
            return date;
        }

        public String getId() {
            return id;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setNominal(float nominal) {
            this.nominal = nominal;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Valute> table;

    @FXML
    private TextField firstArea;

    @FXML
    private TextField secArea;

    @FXML
    private TextField nameArea;

    @FXML
    private Button subBtn;

    @FXML
    private LineChart<String, Number> loneChart;

    @FXML
    private CategoryAxis catAxis;

    @FXML
    private NumberAxis numAxis;

    @FXML
    void doGET(ActionEvent event) throws Exception {
        boolean chartable = false;
        table.getColumns().clear();
        valutes.clear();
        loneChart.getData().clear();
        URL url = new URL("https://cbr.ru/scripts/XML_val.asp?d=0");
        URL codesUrl = new URL("https://cbr.ru/scripts/XML_val.asp?d=0");
        DocumentBuilder builder = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document docCodes = null;
        String valCode = nameArea.getText();
        String valName = nameArea.getText();
        try {
            builder = factory.newDocumentBuilder();
            URLConnection connCodes = codesUrl.openConnection();
            docCodes = builder.parse(connCodes.getInputStream());
            NodeList elementsCodes = docCodes.getDocumentElement().getElementsByTagName("Item");
//            for(int j = 0; j < elementsCodes.getLength(); j++){
//                if(Objects.equals(elementsCodes.item(j).getChildNodes().item(0).getTextContent(), valName)){
//                    valCode = elementsCodes.item(j).getChildNodes().item(3).getTextContent();
//                }
//            }
            System.out.println(valCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(firstArea.getText() == "" && secArea.getText() == "" || nameArea.getText() == ""){
            System.out.println("Неверные данные!");
            return;
        }
        else if(firstArea.getText() != "" && secArea.getText() == ""){
            url = new URL("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1="+firstArea.getText()+"&date_req2="+firstArea.getText()+"&VAL_NM_RQ="+valCode);
        }
        else if(firstArea.getText() == "" && secArea.getText() != ""){
            url = new URL("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1="+secArea.getText()+"&date_req2="+secArea.getText()+"&VAL_NM_RQ="+valCode);
        }
        else if(firstArea.getText() != "" && secArea.getText() != ""){
            url = new URL("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1="+firstArea.getText()+"&date_req2="+secArea.getText()+"&VAL_NM_RQ="+valCode);
            chartable = true;
        }

        URLConnection conn = url.openConnection();


        try {
            Document doc = builder.parse(conn.getInputStream());
            NodeList elements = doc.getDocumentElement().getElementsByTagName("Record");
            NodeList elementsCodes = docCodes.getDocumentElement().getElementsByTagName("Item");

            ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
            XYChart.Series ser = new XYChart.Series();

            for(int i = 0; i < elements.getLength(); i++){
                StringBuffer val = new StringBuffer();
                String[] sp = elements.item(i).getChildNodes().item(1).getTextContent().split(",");
                val.append(sp[0]+"."+sp[1].charAt(0)+sp[1].charAt(1));
                float inf = Float.parseFloat(val.toString());
                datas.add(new XYChart.Data(elements.item(i).getAttributes().item(0).getTextContent(), inf));
                valutes.add(new Valute(elements.item(i).getAttributes().item(0).getTextContent(),
                        elements.item(i).getAttributes().item(1).getTextContent(),
                        Float.parseFloat(elements.item(i).getChildNodes().item(0).getTextContent()),
                        inf));
            }
            ser.setData(datas);
            ser.setName(nameArea.getText());
            loneChart.getData().add(ser);
            loneChart.getYAxis().setAutoRanging(true);

            TableColumn<Valute, String> dateCol = new TableColumn<Valute, String>("Дата");
            dateCol.setCellValueFactory(new PropertyValueFactory<Valute, String>("date"));
            TableColumn<Valute, String> idCol = new TableColumn<Valute, String>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<Valute, String>("id"));
            TableColumn<Valute, Float> nomCol = new TableColumn<Valute, Float>("Номинал");
            nomCol.setCellValueFactory(new PropertyValueFactory<Valute, Float>("nominal"));
            TableColumn<Valute, Float> valCol = new TableColumn<Valute, Float>("Стоимость");
            valCol.setCellValueFactory(new PropertyValueFactory<Valute, Float>("value"));
            table.setItems(valutes);
            table.getColumns().addAll(dateCol, idCol, nomCol, valCol);

            loneChart.setCreateSymbols(false);
            loneChart.setAnimated(false);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void repaint(XYChart.Series ser){

    }

    @FXML
    void initialize() {
        XYChart.Series ser1 = new XYChart.Series();
    }
}

