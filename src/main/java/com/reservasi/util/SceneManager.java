package com.reservasi.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.reservasi.controller.DashboardController;
import com.reservasi.controller.LoginController;
import com.reservasi.controller.ReservationController;

public class SceneManager {
    private Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Reservasi Kelas");
    }

    public void showLoginScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/reservasi/view/login.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setSceneManager(this);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showDashboardScene(String idDosen) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/reservasi/view/dashboard.fxml"));
        Parent root = loader.load();
        
        DashboardController controller = loader.getController();
        controller.setIdDosen(idDosen);
         controller.setSceneManager(this);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showReservationScene(String idDosen) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/reservasi/view/reservation.fxml"));
        Parent root = loader.load();
        
        ReservationController controller = loader.getController();
        controller.setIdDosen(idDosen);

        
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}