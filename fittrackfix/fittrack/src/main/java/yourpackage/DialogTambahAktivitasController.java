package yourpackage;

//PJ Alif

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Map;

public class DialogTambahAktivitasController {

    @FXML private ComboBox<String> jenisAktivitasCombo;
    @FXML private TextField durasiField;
    @FXML private TextArea catatanArea;
    @FXML private Button simpanButton;
    @FXML private Label titleLabel;

    private LocalDate selectedDate;
    private Activity activityToEdit = null;

    private final Map<String, Double> metsValues = Map.of(
            "Lari (sedang)", 8.0,
            "Berenang (santai)", 6.0,
            "Jalan Kaki (cepat)", 4.3,
            "Bersepeda (santai)", 4.0,
            "Latihan Beban", 3.5
    );

    @FXML
    public void initialize() {
        jenisAktivitasCombo.setItems(FXCollections.observableArrayList(metsValues.keySet()));
    }

    public void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
    }

    public void setActivityToEdit(Activity activity) {
        this.activityToEdit = activity;
        this.selectedDate = activity.getActivityDate();

        titleLabel.setText("Edit Aktivitas");
        jenisAktivitasCombo.setValue(activity.getActivityType());
        durasiField.setText(String.valueOf(activity.getDurationMinutes()));
        catatanArea.setText(activity.getNotes());
    }

    @FXML
    void handleSave() {
        if (jenisAktivitasCombo.getValue() == null || durasiField.getText().isEmpty() || catatanArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Semua field harus diisi.");
            return;
        }

        try {
            String jenis = jenisAktivitasCombo.getValue();
            int durasi = Integer.parseInt(durasiField.getText());
            String catatan = catatanArea.getText();

            double beratKg = 65.0;

            double kalori = durasi * metsValues.get(jenis) * 3.5 * beratKg / 200.0;

            if (activityToEdit == null) {
                Activity newActivity = new Activity(jenis, durasi, kalori, selectedDate, catatan);
                DatabaseManager.addActivity(newActivity, SessionManager.getCurrentUserId());
            } else {
                activityToEdit.setActivityType(jenis);
                activityToEdit.setDurationMinutes(durasi);
                activityToEdit.setCaloriesBurned(kalori);
                activityToEdit.setNotes(catatan);
                DatabaseManager.updateActivity(activityToEdit);
            }

            closeStage();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Durasi harus berupa angka.");
        }
    }

    @FXML
    void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) simpanButton.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}