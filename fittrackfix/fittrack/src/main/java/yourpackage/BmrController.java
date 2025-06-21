package yourpackage;

//PJ Abrar

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

public class BmrController {

    @FXML private DatePicker tanggalLahirPicker;
    @FXML private ComboBox<String> jenisKelaminCombo;
    @FXML private TextField tinggiField;
    @FXML private TextField beratField;
    @FXML private ComboBox<String> aktivitasCombo;
    @FXML private Button hitungButton;
    @FXML private ImageView backButton;

    // Faktor pengali TDEE
    private final Map<String, Double> activityFactors = Map.of(
            "Jarang/Tidak pernah olahraga", 1.2,
            "Olahraga ringan (1-3 hari/minggu)", 1.375,
            "Olahraga sedang (3-5 hari/minggu)", 1.55,
            "Olahraga berat (6-7 hari/minggu)", 1.725,
            "Sangat berat (atlet/pekerja fisik)", 1.9
    );

    @FXML
    public void initialize() {
        // Mengisi pilihan pada ComboBox saat halaman dimuat
        jenisKelaminCombo.setItems(FXCollections.observableArrayList("Pria", "Wanita"));
        aktivitasCombo.setItems(FXCollections.observableArrayList(activityFactors.keySet()));
    }

    @FXML
    void handleCalculateBMR() {
        // Validasi semua input
        if (tanggalLahirPicker.getValue() == null || jenisKelaminCombo.getValue() == null || aktivitasCombo.getValue() == null ||
                tinggiField.getText().isEmpty() || beratField.getText().isEmpty()) {
            showAlert("Input Error", "Semua field harus diisi.");
            return;
        }

        try {
            double tinggiCm = Double.parseDouble(tinggiField.getText());
            double beratKg = Double.parseDouble(beratField.getText());
            LocalDate birthDate = tanggalLahirPicker.getValue();
            int umur = Period.between(birthDate, LocalDate.now()).getYears();
            String jenisKelamin = jenisKelaminCombo.getValue();
            String aktivitas = aktivitasCombo.getValue();

            // Hitung BMR menggunakan rumus Harris-Benedict
            double bmr;
            if ("Pria".equals(jenisKelamin)) {
                bmr = 88.362 + (13.397 * beratKg) + (4.799 * tinggiCm) - (5.677 * umur);
            } else { // Wanita
                bmr = 447.593 + (9.247 * beratKg) + (3.098 * tinggiCm) - (4.330 * umur);
            }

            // Hitung TDEE
            double tdee = bmr * activityFactors.get(aktivitas);

            // Navigasi ke halaman hasil dan kirim data
            switchToHasilBmr(bmr, tdee, aktivitas);

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Mohon masukkan angka yang valid untuk tinggi dan berat.");
        }
    }

    private void switchToHasilBmr(double bmr, double tdee, String aktivitas) {
        try {
            Stage currentStage = (Stage) hitungButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hasil_bmr.fxml"));
            Parent root = loader.load();

            HasilBmrController hasilController = loader.getController();
            hasilController.setBmrResult(bmr, tdee, aktivitas);

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
    void handleGoToBMI() throws IOException {
        new SceneManager((Stage) hitungButton.getScene().getWindow()).switchTo("bmi.fxml");
    }

    @FXML
    void handleGoToBMR() throws IOException {
        new SceneManager((Stage) backButton.getScene().getWindow()).switchTo("bmr.fxml");
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}