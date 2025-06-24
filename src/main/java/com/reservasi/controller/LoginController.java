package com.reservasi.controller;

import com.reservasi.App;
import com.reservasi.database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    @FXML
    private void handleLoginButton(ActionEvent event) {
        checkLogin();
    }

    private void checkLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validasi Form
        if (username.isEmpty() && password.isEmpty()) {
            messageLabel.setText("Masukkan ID/Username dan Password wajib diisi");
            return;
        } else if (username.isEmpty()) {
            messageLabel.setText("Masukkan ID/Username");
            return;
        } else if (password.isEmpty()) {
            messageLabel.setText("Password wajib diisi");
            return;
        }

        // Logika Cek Login
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT id_dosen, password FROM dosen WHERE id_dosen = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    messageLabel.setText("Login berhasil!");
                    // Load Dashboard
                    App.showDashboardView(username); // Melewatkan ID dosen yang login
                } else {
                    messageLabel.setText("Password salah");
                    passwordField.clear(); // Kosongkan kolom password
                }
            } else {
                messageLabel.setText("ID/Username tidak ditemukan");
                passwordField.clear(); // Kosongkan kolom password
            }

        } catch (SQLException e) {
            messageLabel.setText("Error database: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            messageLabel.setText("Error memuat dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}