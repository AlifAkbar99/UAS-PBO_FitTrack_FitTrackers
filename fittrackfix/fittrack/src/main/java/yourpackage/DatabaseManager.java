package yourpackage;

//PJ Arya

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:fittrack.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void initializeDatabase() {
        String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "gender TEXT," +
                "birth_date TEXT," +
                "height REAL," +
                "weight REAL" +
                ");";

        String createActivitiesTable = "CREATE TABLE IF NOT EXISTS activities (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "activity_type TEXT NOT NULL," +
                "duration_minutes INTEGER NOT NULL," +
                "calories_burned REAL NOT NULL," +
                "activity_date TEXT NOT NULL," +
                "notes TEXT," +
                "is_completed BOOLEAN DEFAULT 0," +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ");";

        String createGoalsTable = "CREATE TABLE IF NOT EXISTS goals (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "target_calories_weekly REAL," +
                "target_duration_weekly INTEGER," +
                "start_date TEXT NOT NULL," +
                "end_date TEXT NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ");";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createUserTable);
            stmt.execute(createActivitiesTable);
            stmt.execute(createGoalsTable);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static int validateLogin(String username, String password) {
        System.out.println("Mencoba validasi login untuk user: " + username);
        String sql = "SELECT id FROM users WHERE username = ? COLLATE NOCASE AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login berhasil untuk user: " + username);
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Login gagal untuk user: " + username);
        return 0;
    }

    public static boolean checkUserExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ? COLLATE NOCASE";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void addUser(String username, String password) {
        System.out.println("Mencoba menambahkan user baru: " + username);
        String sql = "INSERT INTO users(username, password) VALUES(?,?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User " + username + " berhasil ditambahkan ke database.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan user: " + e.getMessage());
        }
    }

    public static List<Activity> getActivitiesForDate(LocalDate date, int userId) {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE activity_date = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date.toString());
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                activities.add(new Activity(
                        rs.getInt("id"),
                        rs.getString("activity_type"),
                        rs.getInt("duration_minutes"),
                        rs.getDouble("calories_burned"),
                        LocalDate.parse(rs.getString("activity_date")),
                        rs.getString("notes"),
                        rs.getBoolean("is_completed")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return activities;
    }

    public static void addActivity(Activity activity, int userId) {
        String sql = "INSERT INTO activities(activity_type, duration_minutes, calories_burned, activity_date, notes, user_id) VALUES(?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, activity.getActivityType());
            pstmt.setInt(2, activity.getDurationMinutes());
            pstmt.setDouble(3, activity.getCaloriesBurned());
            pstmt.setString(4, activity.getActivityDate().toString());
            pstmt.setString(5, activity.getNotes());
            pstmt.setInt(6, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateActivity(Activity activity) {
        String sql = "UPDATE activities SET activity_type = ?, duration_minutes = ?, calories_burned = ?, notes = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, activity.getActivityType());
            pstmt.setInt(2, activity.getDurationMinutes());
            pstmt.setDouble(3, activity.getCaloriesBurned());
            pstmt.setString(4, activity.getNotes());
            pstmt.setInt(5, activity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteActivity(int activityId, int userId) {
        String sql = "DELETE FROM activities WHERE id = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, activityId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateActivityStatus(int id, boolean isCompleted) {
        String sql = "UPDATE activities SET is_completed = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, isCompleted);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void saveOrUpdateGoal(Goal goal, int userId) {
        String deleteSql = "DELETE FROM goals WHERE start_date = ? AND end_date = ? AND user_id = ?";
        String insertSql = "INSERT INTO goals(target_calories_weekly, target_duration_weekly, start_date, end_date, user_id) VALUES(?,?,?,?,?)";
        try (Connection conn = getConnection()) {
            try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql)) {
                pstmtDelete.setString(1, goal.getStartDate().toString());
                pstmtDelete.setString(2, goal.getEndDate().toString());
                pstmtDelete.setInt(3, userId);
                pstmtDelete.executeUpdate();
            }
            try (PreparedStatement pstmtInsert = conn.prepareStatement(insertSql)) {
                pstmtInsert.setDouble(1, goal.getTargetCaloriesWeekly());
                pstmtInsert.setInt(2, goal.getTargetDurationWeekly());
                pstmtInsert.setString(3, goal.getStartDate().toString());
                pstmtInsert.setString(4, goal.getEndDate().toString());
                pstmtInsert.setInt(5, userId);
                pstmtInsert.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Goal getGoalForDate(LocalDate date, int userId) {
        String sql = "SELECT * FROM goals WHERE ? BETWEEN start_date AND end_date AND user_id = ? LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date.toString());
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Goal(
                        rs.getInt("id"),
                        rs.getDouble("target_calories_weekly"),
                        rs.getInt("target_duration_weekly"),
                        LocalDate.parse(rs.getString("start_date")),
                        LocalDate.parse(rs.getString("end_date"))
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Map<LocalDate, Double> getCaloriesSummaryForPeriod(LocalDate startDate, LocalDate endDate, int userId) {
        Map<LocalDate, Double> summary = new LinkedHashMap<>();
        String sql = "SELECT activity_date, SUM(calories_burned) as total_calories " +
                "FROM activities WHERE activity_date BETWEEN ? AND ? AND user_id = ? " +
                "GROUP BY activity_date ORDER BY activity_date ASC";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());
            pstmt.setInt(3, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                summary.put(LocalDate.parse(rs.getString("activity_date")), rs.getDouble("total_calories"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return summary;
    }
}