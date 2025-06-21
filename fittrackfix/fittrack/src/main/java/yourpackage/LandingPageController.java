package yourpackage;

//PJ Albert

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingPageController {

    @FXML
    private ImageView nextButton;

    @FXML
    void handleNextPage() {
        try {
            // Dapatkan stage saat ini dari komponen manapun di scene
            Stage currentStage = (Stage) nextButton.getScene().getWindow();

            // Gunakan SceneManager untuk beralih ke halaman login
            SceneManager sceneManager = new SceneManager(currentStage);

            // PERUBAHAN UTAMA ADA DI BARIS INI:
            // Tujuan navigasi diubah ke "login_view.fxml"
            sceneManager.switchTo("login_view.fxml");

        } catch (IOException e) {
            System.err.println("Failed to load login_view.fxml");
            e.printStackTrace();
        }
    }
}