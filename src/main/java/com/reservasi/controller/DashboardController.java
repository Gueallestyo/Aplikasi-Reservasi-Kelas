package com.reservasi.controller;

import com.reservasi.util.DatabaseConnector;
import com.reservasi.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML private Label welcomeLabel;
    
    private String idDosen;
    private SceneManager sceneManager;

    public void setIdDosen(String idDosen) {
        this.idDosen = idDosen;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT nama_dosen FROM dosen WHERE id_dosen = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, idDosen);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                welcomeLabel.setText("Selamat datang, " + rs.getString("nama_dosen"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReservasi() {
        try {
            sceneManager.showReservationScene(idDosen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            sceneManager.showLoginScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}