package yourpackage;

//PJ Yajid

import java.time.LocalDate;

public class Goal {
    private int id;
    private double targetCaloriesWeekly;
    private int targetDurationWeekly;
    private LocalDate startDate;
    private LocalDate endDate;

    public Goal(int id, double targetCaloriesWeekly, int targetDurationWeekly, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.targetCaloriesWeekly = targetCaloriesWeekly;
        this.targetDurationWeekly = targetDurationWeekly;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Goal(double targetCaloriesWeekly, int targetDurationWeekly, LocalDate startDate, LocalDate endDate) {
        this.targetCaloriesWeekly = targetCaloriesWeekly;
        this.targetDurationWeekly = targetDurationWeekly;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public double getTargetCaloriesWeekly() { return targetCaloriesWeekly; }
    public int getTargetDurationWeekly() { return targetDurationWeekly; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
}