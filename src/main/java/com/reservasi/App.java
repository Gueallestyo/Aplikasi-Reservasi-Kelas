package com.reservasi;

import com.reservasi.database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    private static Stage primaryStage;
    private static String loggedInDosenId; // Untuk menyimpan ID dosen yang login

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        DatabaseManager.initializeDatabase(); // Inisialisasi DB saat aplikasi dimulai
        showLoginView();
    }

    public static void showLoginView() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("src/resources/com/reservasi/Login.fxml")));
        primaryStage.setTitle("Reservasi Ruang Kelas - Login");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

    public static void showDashboardView(String dosenId) throws IOException {
        loggedInDosenId = dosenId; // Simpan ID dosen yang berhasil login
        Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("Dashboard.fxml")));
        primaryStage.setTitle("Reservasi Ruang Kelas - Dashboard");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static String getLoggedInDosenId() {
        return loggedInDosenId;
    }

    public static void main(String[] args) {
        launch();
    }
}