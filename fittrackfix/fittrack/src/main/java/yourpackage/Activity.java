package yourpackage;

// PJ Alif

import java.time.LocalDate;

public class Activity {
    private int id;
    private String activityType;
    private int durationMinutes;
    private double caloriesBurned;
    private LocalDate activityDate;
    private String notes;
    private boolean isCompleted;

    public Activity(int id, String activityType, int durationMinutes, double caloriesBurned, LocalDate activityDate, String notes, boolean isCompleted) {
        this.id = id;
        this.activityType = activityType;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
        this.activityDate = activityDate;
        this.notes = notes;
        this.isCompleted = isCompleted;
    }

    public Activity(String activityType, int durationMinutes, double caloriesBurned, LocalDate activityDate, String notes) {
        this.activityType = activityType;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
        this.activityDate = activityDate;
        this.notes = notes;
        this.isCompleted = false;
    }

    public int getId() { return id; }
    public String getActivityType() { return activityType; }
    public int getDurationMinutes() { return durationMinutes; }
    public double getCaloriesBurned() { return caloriesBurned; }
    public LocalDate getActivityDate() { return activityDate; }
    public String getNotes() { return notes; }
    public boolean isCompleted() { return isCompleted; }

    public void setCompleted(boolean completed) { isCompleted = completed; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public void setCaloriesBurned(double caloriesBurned) { this.caloriesBurned = caloriesBurned; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return notes;
    }
}