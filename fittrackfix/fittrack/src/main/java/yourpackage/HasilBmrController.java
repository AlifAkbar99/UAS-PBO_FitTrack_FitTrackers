package yourpackage;

//PJ Abrar

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;

public class HasilBmrController {

    @FXML private Label bmrLabel;
    @FXML private Label tdeeLabel;
    @FXML private Label aktivitasLabel;
    @FXML private BarChart<String, Number> bmrChart;
    @FXML private ImageView backButton;
    @FXML private Button bmiButton;

    public void setBmrResult(double bmr, double tdee, String aktivitas) {
        DecimalFormat df = new DecimalFormat("#");
        bmrLabel.setText(df.format(bmr) + " kcal/hari");
        tdeeLabel.setText(df.format(tdee));
        aktivitasLabel.setText(aktivitas);

        // Mempopulasikan BarChart dengan data BMR
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("BMR", bmr));
        bmrChart.getData().add(series);
    }

    @FXML
    void handleGoBack() throws IOException {
        new SceneManager((Stage) backButton.getScene().getWindow()).switchTo("bmr.fxml");
    }

    @FXML
    void handleGoToBMI() throws IOException {
        new SceneManager((Stage) bmiButton.getScene().getWindow()).switchTo("bmi.fxml");
    }
}