package yourpackage;

//PJ Albert

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {

    // Inject salah satu elemen FXML (misalnya backButton) untuk mendapatkan akses ke Stage
    @FXML private Node backButton;

    /**
     * Metode pembantu untuk menyederhanakan logika perpindahan scene.
     * @param fxmlFile Nama file FXML tujuan.
     */
    private void switchScene(String fxmlFile) {
        try {
            // Dapatkan Stage saat ini dari Node manapun yang ada di scene
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            SceneManager sceneManager = new SceneManager(currentStage);
            sceneManager.switchTo(fxmlFile);
        } catch (IOException e) {
            System.err.println("Failed to load " + fxmlFile);
            e.printStackTrace();
        }
    }

    /**
     * Kembali ke halaman landing/awal.
     */
    @FXML
    void handleGoToLandingPage() {
        switchScene("landing_page.fxml");
    }

    /**
     * Pindah ke halaman untuk mengatur target mingguan.
     */
    @FXML
    void handleGoToTarget() {
        switchScene("target.fxml");
    }

    /**
     * Pindah ke halaman catatan aktivitas harian.
     */
    @FXML
    void handleGoToCatatan() {
        switchScene("catatan.fxml");
    }

    /**
     * Pindah ke halaman kalkulator BMI dan BMR.
     */
    @FXML
    void handleGoToKalkulator() {
        switchScene("bmi.fxml");
    }

    /**
     * Pindah ke halaman statistik dan visualisasi progres.
     */
    @FXML
    void handleGoToStatistik() {
        switchScene("statistik.fxml");
    }
}