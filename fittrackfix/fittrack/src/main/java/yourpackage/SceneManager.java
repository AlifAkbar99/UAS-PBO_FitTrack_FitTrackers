package yourpackage;

//Pj Arya

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class SceneManager {

    private final Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void switchTo(String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/" + fxmlFile)));
        primaryStage.getScene().setRoot(root);
    }

    public void loadInitialScene(String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/" + fxmlFile)));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
}