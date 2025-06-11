package com.reservasi.controller;

import com.reservasi.model.Dosen;
import com.reservasi.util.DatabaseConnector;
import com.reservasi.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField idDosenField;
    @FXML private PasswordField passwordField;
    
    private Stage stage;
    private SceneManager sceneManager;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    private void handleLogin() {
        String idDosen = idDosenField.getText();
        String password = passwordField.getText();

        if (idDosen.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "ID Dosen dan Password tidak boleh kosong!");
            return;
        }

        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT * FROM dosen WHERE id_dosen = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, idDosen);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Dosen dosen = new Dosen(
                    rs.getString("id_dosen"),
                    rs.getString("nama_dosen"),
                    rs.getString("password")
                );

                sceneManager.showDashboardScene(dosen.getIdDosen());
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "ID Dosen atau Password salah!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat mengakses database.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan saat login.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}