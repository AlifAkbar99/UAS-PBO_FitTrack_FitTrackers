package yourpackage;

//PJ Yajid

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class TargetController {
    @FXML private TextField kaloriField;
    @FXML private TextField durasiField;
    @FXML private DatePicker mulaiPicker;
    @FXML private DatePicker selesaiPicker;
    @FXML private ImageView backButton;

    @FXML
    public void initialize() {
        // Otomatis set periode ke minggu ini (Senin - Minggu)
        LocalDate today = LocalDate.now();
        mulaiPicker.setValue(today.with(DayOfWeek.MONDAY));
        selesaiPicker.setValue(today.with(DayOfWeek.SUNDAY));
    }

    @FXML
    void handleSaveTarget() {
        try {
            double kalori = Double.parseDouble(kaloriField.getText());
            int durasi = Integer.parseInt(durasiField.getText());
            LocalDate mulai = mulaiPicker.getValue();
            LocalDate selesai = selesaiPicker.getValue();

            if (mulai == null || selesai == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Tanggal periode harus diisi.");
                return;
            }

            Goal newGoal = new Goal(kalori, durasi, mulai, selesai);

            // PERUBAHAN DI SINI: Menambahkan argumen kedua yaitu userId dari session
            DatabaseManager.saveOrUpdateGoal(newGoal, SessionManager.getCurrentUserId());

            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Target mingguan berhasil disimpan!");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Input kalori dan durasi harus berupa angka.");
        }
    }

    @FXML
    void handleGoBack() throws IOException {
        new SceneManager((Stage) backButton.getScene().getWindow()).switchTo("main_page.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}