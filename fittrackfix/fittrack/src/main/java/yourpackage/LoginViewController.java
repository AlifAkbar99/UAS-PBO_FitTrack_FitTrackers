package yourpackage;

//PJ Arya

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton; // Digunakan untuk mendapatkan stage

    @FXML
    void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username dan password tidak boleh kosong.");
            return;
        }

        int userId = DatabaseManager.validateLogin(username, password);
        if (userId > 0) {
            // Jika login berhasil, simpan sesi
            SessionManager.setCurrentUser(userId, username);

            // Pindah ke halaman utama
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            new SceneManager(currentStage).switchTo("main_page.fxml");
        } else {
            // Jika login gagal
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username atau password salah.");
        }
    }

    @FXML
    void handleGoToSignUp() throws IOException {
        Stage currentStage = (Stage) usernameField.getScene().getWindow();
        new SceneManager(currentStage).switchTo("signup_view.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}