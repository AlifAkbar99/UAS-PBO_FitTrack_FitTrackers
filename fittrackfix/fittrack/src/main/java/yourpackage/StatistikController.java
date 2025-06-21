package yourpackage;

//PJ Yajid

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.text.DecimalFormat;

public class StatistikController {

    @FXML private Label headerLabel;
    @FXML private BarChart<String, Number> kaloriChart;
    @FXML private ProgressBar progressKalori;
    @FXML private Label progressLabel;
    @FXML private VBox congratsBox;
    @FXML private ImageView backButton;

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        // Tampilkan statistik untuk minggu ini (Senin - Minggu)
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.with(DayOfWeek.MONDAY);
        LocalDate endDate = today.with(DayOfWeek.SUNDAY);

        // Dapatkan user ID yang sedang login dari SessionManager
        int userId = SessionManager.getCurrentUserId();

        // 1. Ambil data aktivitas dan target dari DB
        // PERUBAHAN DI SINI: Menambahkan 'userId' sebagai argumen ketiga
        Map<LocalDate, Double> summary = DatabaseManager.getCaloriesSummaryForPeriod(startDate, endDate, userId);

        // PERUBAHAN DI SINI: Menambahkan 'userId' sebagai argumen kedua
        Goal currentGoal = DatabaseManager.getGoalForDate(today, userId);

        // 2. Populasi BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Kalori Terbakar");

        double totalCaloriesBurned = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            double calories = summary.getOrDefault(date, 0.0);
            totalCaloriesBurned += calories;
            series.getData().add(new XYChart.Data<>(date.format(formatter), calories));
        }
        kaloriChart.getData().add(series);

        // 3. Update UI Progres
        if (currentGoal != null) {
            double target = currentGoal.getTargetCaloriesWeekly();
            DecimalFormat df = new DecimalFormat("#");

            progressLabel.setText(df.format(totalCaloriesBurned) + " / " + df.format(target) + " kcal");

            double progress = (target > 0) ? (totalCaloriesBurned / target) : 0;
            progressKalori.setProgress(Math.min(1.0, progress));

            if (progress >= 1.0) {
                congratsBox.setVisible(true);
            }
        } else {
            progressLabel.setText("Target mingguan belum diatur!");
        }
    }

    @FXML
    void handleGoBack() throws IOException {
        new SceneManager((Stage) backButton.getScene().getWindow()).switchTo("main_page.fxml");
    }
}