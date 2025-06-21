package yourpackage;

//PJ Albert

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        DatabaseManager.initializeDatabase();

        primaryStage.setTitle("FitTrack - Fitness & Health Assistant");
        primaryStage.setMaximized(true);

        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.loadInitialScene("landing_page.fxml");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


