package yourpackage;

//PJ Arya

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpViewController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    void handleSignUp() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validasi Input
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username dan password tidak boleh kosong.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password dan konfirmasi password tidak cocok.");
            return;
        }
        if (DatabaseManager.checkUserExists(username)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username sudah digunakan. Silakan pilih yang lain.");
            return;
        }

        // Jika semua validasi lolos, tambahkan user baru
        DatabaseManager.addUser(username, password);
        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pendaftaran berhasil! Silakan login dengan akun baru Anda.");

        // Kembali ke halaman login
        handleGoToLogin();
    }

    @FXML
    void handleGoToLogin() throws IOException {
        Stage currentStage = (Stage) usernameField.getScene().getWindow();
        new SceneManager(currentStage).switchTo("login_view.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}