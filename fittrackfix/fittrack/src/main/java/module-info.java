module com.fittrack {
    // Modul yang dibutuhkan oleh aplikasi
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Diperlukan untuk koneksi database (JDBC)

    // Buka package controller agar FXML dapat mengaksesnya
    opens yourpackage to javafx.fxml;

    // Ekspor package utama
    exports yourpackage;
}