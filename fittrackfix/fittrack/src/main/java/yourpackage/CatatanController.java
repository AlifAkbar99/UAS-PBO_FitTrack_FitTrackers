package yourpackage;

// PJ Alif

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class CatatanController {

    @FXML private DatePicker datePicker;
    @FXML private ListView<Activity> activityListView;
    @FXML private ImageView backButton;
    @FXML private Button addButton;

    @FXML
    public void initialize() {
        datePicker.setValue(LocalDate.now());
        setupListView();
        loadActivitiesForDate(datePicker.getValue());
    }

    /**
     * Mengatur bagaimana setiap item (Activity) akan ditampilkan di dalam ListView.
     * Termasuk menambahkan CheckBox, tombol Edit, dan tombol Hapus.
     */
    private void setupListView() {
        activityListView.setCellFactory(param -> new ListCell<>() {
            private final CheckBox checkBox = new CheckBox();
            private final Label label = new Label();
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Hapus");
            private final HBox hbox = new HBox(10, checkBox, label, editButton, deleteButton);

            {
                // Mengatur agar label bisa melebar memenuhi ruang
                HBox.setHgrow(label, Priority.ALWAYS);
                label.setMaxWidth(Double.MAX_VALUE);

                // Aksi untuk CheckBox
                checkBox.setOnAction(event -> {
                    if (getItem() != null) {
                        getItem().setCompleted(checkBox.isSelected());
                        DatabaseManager.updateActivityStatus(getItem().getId(), checkBox.isSelected());
                        updateItem(getItem(), isEmpty()); // Refresh tampilan cell untuk efek coret
                    }
                });

                // Aksi untuk tombol Edit
                editButton.setOnAction(event -> {
                    Activity item = getItem();
                    if (item != null) {
                        openActivityDialog(item);
                    }
                });

                // Aksi untuk tombol Hapus
                deleteButton.setOnAction(event -> {
                    Activity item = getItem();
                    if (item != null) {
                        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmation.setTitle("Konfirmasi Hapus");
                        confirmation.setHeaderText("Anda yakin ingin menghapus aktivitas ini?");
                        confirmation.setContentText('"' + item.getNotes() + '"');

                        Optional<ButtonType> result = confirmation.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            DatabaseManager.deleteActivity(item.getId(), SessionManager.getCurrentUserId());
                            loadActivitiesForDate(datePicker.getValue()); // Refresh list setelah hapus
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Activity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    label.setText(item.toString());
                    checkBox.setSelected(item.isCompleted());

                    // Tambahkan efek coret jika sudah selesai
                    if (item.isCompleted()) {
                        label.setStyle("-fx-strikethrough: true; -fx-opacity: 0.6;");
                    } else {
                        label.setStyle("-fx-strikethrough: false; -fx-opacity: 1.0;");
                    }
                    setGraphic(hbox);
                }
            }
        });
    }

    /**
     * Memuat aktivitas dari database untuk tanggal dan user tertentu, lalu menampilkannya di ListView.
     * @param date Tanggal yang dipilih.
     */
    private void loadActivitiesForDate(LocalDate date) {
        ObservableList<Activity> activities = FXCollections.observableArrayList(
                DatabaseManager.getActivitiesForDate(date, SessionManager.getCurrentUserId())
        );
        activityListView.setItems(activities);
    }

    @FXML
    void handleDateChange() {
        loadActivitiesForDate(datePicker.getValue());
    }

    @FXML
    void handleAddActivity() {
        openActivityDialog(null); // Panggil dialog dengan item null untuk mode "Tambah"
    }

    /**
     * Membuka dialog untuk menambah atau mengedit aktivitas.
     * @param activity null jika menambah baru, atau objek Activity jika mengedit.
     */
    private void openActivityDialog(Activity activity) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialog_tambah_aktivitas.fxml"));
            Parent root = loader.load();

            DialogTambahAktivitasController controller = loader.getController();
            if (activity == null) {
                // Mode Tambah
                controller.setSelectedDate(datePicker.getValue());
            } else {
                // Mode Edit
                controller.setActivityToEdit(activity);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(activity == null ? "Tambah Aktivitas Baru" : "Edit Aktivitas");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner((Stage) addButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            dialogStage.showAndWait(); // Tunggu sampai dialog ditutup

            loadActivitiesForDate(datePicker.getValue()); // Selalu refresh list setelah dialog ditutup

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleGoBack() throws IOException {
        new SceneManager((Stage) backButton.getScene().getWindow()).switchTo("main_page.fxml");
    }
}