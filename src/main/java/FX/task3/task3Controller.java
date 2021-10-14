package FX.task3;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javax.servlet.http.HttpServletRequest;

public class task3Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    private Button getBtn;

    @FXML
    private Button postBtn;

    @FXML
    private Label ans;

    @FXML
    private AnchorPane logArea;

    private void getResp(HttpURLConnection con){
        int status = 0;
        String str = null;
        StringBuffer data = new StringBuffer();
        Map<String,String> resp = new HashMap<>();
        try {
            status = con.getResponseCode();
            if(status > 299) {
                return;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((str=in.readLine()) != null) data.append(str);
            String[] res = data.toString().split(";");

            for(String item : res){
                String[] itemS = item.split("=");
                resp.put(itemS[0], itemS[1]);
            }
            if(resp.get("ans").equals("true")){
                this.logArea.setVisible(false);
                ans.setText("Hello! " + resp.get("login") +
                        "\nNow "+ resp.get("time"));
            }
            else if(resp.get("ans").equals("false") && resp.get("tryy").equals("3")){
                this.logArea.setVisible(false);
                ans.setText("Attempts ended!");
            }
            else if(resp.get("ans").equals("false")){
                ans.setText("Attempts left "+(3-Integer.parseInt(resp.get("tryy"))));
            }

            in.close();
            con.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void get(String inf){
        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL("http://localhost:8080/task3Servlet?" + inf);
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "text/spline");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

        } catch(Exception e) {
            System.out.println("Подключение не удалось!");
        }

        getResp(con);
    }

    private void post(String inf){
        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL("http://localhost:8080/task3Servlet");
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty(
                    "User-Agent", "Java client");
            con.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            byte[] data = inf.getBytes(StandardCharsets.UTF_8);
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(data);
            }
        } catch(Exception e) {
          e.printStackTrace();
        }
        getResp(con);
    }

    @FXML
    void doGET(ActionEvent event) {
        get("login=" + login.getText() + "&password=" + password.getText());
    }

    @FXML
    void doPOST(ActionEvent event) {
        if(login.getText().length() != 0){
            post("login=" + login.getText() + ";password=" + password.getText());
        }
        else {
            post("login=none;password=none");
        }
    }

    @FXML
    void initialize() throws IOException {
        post("login=none;password=none");
    }
}
