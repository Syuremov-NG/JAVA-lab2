package FX.task5;

import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class task5Controller {
    private final ObservableList<Valute> valutes = FXCollections.observableArrayList();
    private final ObservableList<Metall> metals = FXCollections.observableArrayList();
    public static class Valute{
        private String date;
        private String id;
        private float nominal;
        private float value;
        Valute(String d, String i, float n, float v){
            date = d;
            id = i;
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

    public static class Metall{
        private String date;
        private String id;
        private float value;
        Metall(String d, String i, float v){
            date = d;
            id = i;
            value = v;
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
    private ComboBox<String> nameArea;

    @FXML
    private Button subBtn;

    @FXML
    private LineChart<String, Number> loneChart;

    @FXML
    private CategoryAxis catAxis;

    @FXML
    private NumberAxis numAxis;

    @FXML
    private HBox valGroup;

    @FXML
    private HBox metGroup;

    @FXML
    private ComboBox<String> metArea;

    @FXML
    private RadioButton valRadio;

    @FXML
    private ToggleGroup typeToggle;

    @FXML
    private RadioButton metRadio;

    @FXML
    private TableView<Metall> tableMet;

    @FXML
    void doGET(ActionEvent event) throws Exception {
        table.getColumns().clear();
        tableMet.getColumns().clear();
        valutes.clear();
        metals.clear();
        loneChart.getData().clear();
        URL url = new URL("https://cbr.ru/scripts/XML_val.asp?d=0");

        //Валюты
        URL codesUrl = new URL("https://cbr.ru/scripts/XML_val.asp?d=0");
        DocumentBuilder builder = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document docCodes = null;
        String valCode = nameArea.getValue();
        String valName = nameArea.getValue();
        ObservableList<String> vals = FXCollections.observableArrayList();

        try {
            builder = factory.newDocumentBuilder();
            URLConnection connCodes = codesUrl.openConnection();
            docCodes = builder.parse(connCodes.getInputStream());
            NodeList elementsCodes = docCodes.getDocumentElement().getElementsByTagName("Item");
            for(int j = 0; j < elementsCodes.getLength(); j++){
                vals.add(elementsCodes.item(j).getChildNodes().item(0).getTextContent());
                if(Objects.equals(elementsCodes.item(j).getChildNodes().item(0).getTextContent(), valName)){
                    valCode = elementsCodes.item(j).getChildNodes().item(3).getTextContent();
                }
            }
            nameArea.setItems(vals);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        // Металлы

        vals = FXCollections.observableArrayList("Серебро", "Золото", "Платина", "Палладий");
        try {
            builder = factory.newDocumentBuilder();
            metArea.setItems(vals);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TableColumn<Metall, String> dateColMet = new TableColumn<Metall, String>("Дата");
        dateColMet.setCellValueFactory(new PropertyValueFactory<Metall, String>("date"));
        TableColumn<Metall, String> idColMet = new TableColumn<Metall, String>("ID");
        idColMet.setCellValueFactory(new PropertyValueFactory<Metall, String>("id"));
        TableColumn<Metall, Float> valColMet = new TableColumn<Metall, Float>("Стоимость");
        valColMet.setCellValueFactory(new PropertyValueFactory<Metall, Float>("value"));
        tableMet.setItems(metals);
        tableMet.getColumns().addAll(dateColMet, idColMet, valColMet);


        if(valRadio.isSelected()){

            if(firstArea.getText() == "" && secArea.getText() == "" || nameArea.getValue() == ""){
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
                table.setItems(valutes);
                ser.setData(datas);
                ser.setName(nameArea.getValue());
                loneChart.getData().add(ser);
                loneChart.getYAxis().setAutoRanging(true);
                loneChart.setCreateSymbols(false);
                loneChart.setAnimated(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            if(firstArea.getText() == "" && secArea.getText() == "" || metArea.getValue() == ""){
                System.out.println("Неверные данные!");
                return;
            }
            //http://www.cbr.ru/scripts/xml_metall.asp?date_req1=02/07/2021&date_req2=01/08/2021
            else if(firstArea.getText() != "" && secArea.getText() == ""){
                url = new URL("http://www.cbr.ru/scripts/xml_metall.asp?date_req1="+firstArea.getText()+"&date_req2="+firstArea.getText());
            }
            else if(firstArea.getText() == "" && secArea.getText() != ""){
                url = new URL("http://www.cbr.ru/scripts/xml_metall.asp?date_req1="+secArea.getText()+"&date_req2="+secArea.getText());
            }
            else if(firstArea.getText() != "" && secArea.getText() != ""){
                url = new URL("http://www.cbr.ru/scripts/xml_metall.asp?date_req1="+firstArea.getText()+"&date_req2="+secArea.getText());
            }

            String code = "1";
            switch (metArea.getValue()){
                case "Золото":
                    code = "1";
                    break;
                case "Серебро":
                    code = "2";
                    break;
                case "Платина":
                    code = "3";
                    break;
                case "Палладий":
                    code = "4";
                    break;
            }
            String[] arr = new String[]{"Золото", "Серебро", "Платина", "Палладий"};
            URLConnection conn = url.openConnection();

            try {
                Document doc = builder.parse(conn.getInputStream());
                NodeList elements = doc.getDocumentElement().getElementsByTagName("Record");

                ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
                XYChart.Series ser = new XYChart.Series();

                for (int i = 0; i < elements.getLength(); i++) {
                    if(Objects.equals(elements.item(i).getAttributes().item(0).getTextContent(), code)){
                        StringBuffer val = new StringBuffer();
                        String[] sp = elements.item(i).getChildNodes().item(1).getTextContent().split(",");
                        try {
                            val.append(sp[0] + "." + sp[1].charAt(0) + sp[1].charAt(1));
                        }catch (Exception e){
                            val.append(sp[0] + ".00");
                        }

                        float inf = Float.parseFloat(val.toString());
                        System.out.println("Metals "+inf);
                        datas.add(new XYChart.Data(elements.item(i).getAttributes().item(1).getTextContent(), inf));

                        metals.add(new Metall(elements.item(i).getAttributes().item(1).getTextContent(),
                                arr[Integer.parseInt(code)-1],
                                inf));
                    }
                }
                tableMet.setItems(metals);
                ser.setData(datas);
                ser.setName(metArea.getValue());
                loneChart.getData().add(ser);
                loneChart.getYAxis().setAutoRanging(true);
                loneChart.setCreateSymbols(false);
                loneChart.setAnimated(false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() throws Exception {
        valRadio.setOnAction(event -> {
            valGroup.setVisible(true);
            metGroup.setVisible(false);
            table.setVisible(true);
            tableMet.setVisible(false);
        });
        metRadio.setOnAction(event -> {
            metGroup.setVisible(true);
            valGroup.setVisible(false);
            table.setVisible(false);
            tableMet.setVisible(true);
        });
        doGET(new ActionEvent());
    }
}

