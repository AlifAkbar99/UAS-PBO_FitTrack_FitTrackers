package yourpackage;

//PJ Abrar

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class BmiController {

    @FXML private TextField tinggiField;
    @FXML private TextField beratField;
    @FXML private ImageView backButton;
    @FXML private Button bmrButton;
    @FXML private Button hitungButton;

    @FXML
    void handleCalculateBMI() {
        String tinggiText = tinggiField.getText();
        String beratText = beratField.getText();

        // Validasi input
        if (tinggiText.isEmpty() || beratText.isEmpty()) {
            showAlert("Input Error", "Tinggi dan Berat badan tidak boleh kosong.");
            return;
        }

        try {
            double tinggiCm = Double.parseDouble(tinggiText);
            double beratKg = Double.parseDouble(beratText);

            if (tinggiCm <= 0 || beratKg <= 0) {
                showAlert("Input Error", "Tinggi dan Berat badan harus bernilai positif.");
                return;
            }

            // Hitung BMI
            double tinggiM = tinggiCm / 100.0;
            double bmi = beratKg / (tinggiM * tinggiM);

            // Navigasi ke halaman hasil dan kirim data BMI
            switchToHasilBmi(bmi, tinggiCm, beratKg);

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Mohon masukkan angka yang valid untuk tinggi dan berat.");
        }
    }

    private void switchToHasilBmi(double bmi, double tinggi, double berat) {
        try {
            Stage currentStage = (Stage) hitungButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hasil_bmi.fxml"));
            Parent root = loader.load();

            // Dapatkan controller dari halaman hasil SEBELUM menampilkannya
            HasilBmiController hasilController = loader.getController();

            // Panggil method di controller tujuan untuk mengirim data
            hasilController.setBmiResult(bmi, tinggi, berat);

            // Ganti scene
            currentStage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleGoBack() throws IOException {
        new SceneManager((Stage) backButton.getScene().getWindow()).switchTo("main_page.fxml");
    }

    @FXML
    void handleGoToBMR() throws IOException {
         new SceneManager((Stage) bmrButton.getScene().getWindow()).switchTo("bmr.fxml");
//        System.out.println("Navigasi ke Halaman BMR (akan dibuat)");
    }

//    @FXML
//    void handleGoToBMR() throws IOException {
//        new SceneManager((Stage) bmrButton.getScene().getWindow()).switchTo("bmr.fxml");
//    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}