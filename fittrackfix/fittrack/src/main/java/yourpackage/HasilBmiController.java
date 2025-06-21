package yourpackage;

//PJ Abrar

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;

public class HasilBmiController {

    @FXML private Label tinggiLabel;
    @FXML private Label beratLabel;
    @FXML private Label statusLabel;
    @FXML private Polygon bmiIndicator;
    @FXML private ImageView backButton;

    public void setBmiResult(double bmi, double tinggi, double berat) {
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedBmi = df.format(bmi);

        // Update Labels
        tinggiLabel.setText("Tinggi: " + (int)tinggi + " cm");
        beratLabel.setText("Berat: " + (int)berat + " kg");

        // Tentukan status dan update UI
        String status;
        if (bmi < 18.5) {
            status = "Kurus";
            statusLabel.setStyle("-fx-text-fill: #FBC02D;");
        } else if (bmi < 25) {
            status = "Ideal";
            statusLabel.setStyle("-fx-text-fill: #4CAF50;");
        } else if (bmi < 30) {
            status = "Gemuk";
            statusLabel.setStyle("-fx-text-fill: #F57C00;");
        } else {
            status = "Obesitas";
            statusLabel.setStyle("-fx-text-fill: #d32f2f;");
        }
        statusLabel.setText(status + " (" + formattedBmi + ")");

        // Posisikan indikator gauge secara dinamis
        updateIndicatorPosition(bmi);
    }

    private void updateIndicatorPosition(double bmi) {
        // Range BMI yang ingin kita tampilkan di gauge (misal: 15-40)
        double minBmi = 15.0;
        double maxBmi = 40.0;
        double gaugeWidth = 450.0; // Lebar gauge di FXML

        // Normalisasi nilai BMI ke rentang 0-1
        double normalizedValue = (bmi - minBmi) / (maxBmi - minBmi);

        // Batasi nilai agar indikator tidak keluar dari gauge
        normalizedValue = Math.max(0, Math.min(1, normalizedValue));

        // Hitung posisi X untuk indikator
        double indicatorX = normalizedValue * gaugeWidth;
        bmiIndicator.setTranslateX(indicatorX);
    }

    @FXML
    void handleGoBack() throws IOException {
        new SceneManager((Stage) backButton.getScene().getWindow()).switchTo("bmi.fxml");
    }

//    @FXML
//    void handleGoToBMR() throws IOException {
//        // Navigasi ke Halaman BMR (akan dibuat)
//        System.out.println("Navigasi ke Halaman BMR (akan dibuat)");
//    }
    @FXML
    void handleGoToBMR() throws IOException {
        // Menggunakan backButton sebagai referensi untuk mendapatkan Stage, atau bisa juga bmrButton jika Anda menambahkannya
        new SceneManager((Stage) backButton.getScene().getWindow()).switchTo("bmr.fxml");
    }
}