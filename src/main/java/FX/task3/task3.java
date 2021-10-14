package FX.task3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class task3 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/task3FXML.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 682, 400);
        stage.setTitle("Editor");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.requestFocus();
        stage.show();
    }
}
