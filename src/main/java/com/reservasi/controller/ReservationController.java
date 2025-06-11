package com.reservasi.controller;

import com.reservasi.model.Reservasi;
import com.reservasi.util.DatabaseConnector;
import com.reservasi.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {
    @FXML private ComboBox<String> hariComboBox;
    @FXML private ComboBox<Integer> jamComboBox;
    @FXML private ComboBox<String> kelasComboBox;
    @FXML private Button reservasiButton;
    @FXML private Button cekKetersediaanButton;
    @FXML private TextArea hasilTextArea;
    
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
        // Inisialisasi ComboBox
        hariComboBox.setItems(FXCollections.observableArrayList(
            "Senin", "Selasa", "Rabu", "Kamis", "Jumat"
        ));
        
        jamComboBox.setItems(FXCollections.observableArrayList(
            1, 2, 3, 4, 5, 6, 7, 8
        ));
        
        kelasComboBox.setItems(FXCollections.observableArrayList(
            "1KA31", "1KA32", "1KA33", "2KA31", "2KA32", "2KA33"
        ));
    }

    @FXML
    private void handleReservasi() {
        String hari = hariComboBox.getValue();
        Integer jamKe = jamComboBox.getValue();
        String kodeKelas = kelasComboBox.getValue();

        if (hari == null || jamKe == null || kodeKelas == null) {
            showAlert("Error", "Semua field harus diisi!");
            return;
        }

        try (Connection conn = DatabaseConnector.getConnection()) {
            // Cek apakah kelas sudah dipesan di jam tersebut
            String checkQuery = "SELECT * FROM reservasi WHERE kode_kelas = ? AND hari = ? AND jam_ke = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, kodeKelas);
            checkStmt.setString(2, hari);
            checkStmt.setInt(3, jamKe);

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                showAlert("Error", "Kelas sudah dipesan di jam tersebut!");
                return;
            }

            // Buat reservasi baru
            String insertQuery = "INSERT INTO reservasi (id_dosen, kode_kelas, hari, jam_ke) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, idDosen);
            insertStmt.setString(2, kodeKelas);
            insertStmt.setString(3, hari);
            insertStmt.setInt(4, jamKe);

            int affectedRows = insertStmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert("Sukses", "Reservasi berhasil dibuat!");
            } else {
                showAlert("Error", "Gagal membuat reservasi!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Terjadi kesalahan saat mengakses database.");
        }
    }

    @FXML
    private void handleCekKetersediaan() {
        String hari = hariComboBox.getValue();
        Integer jamKe = jamComboBox.getValue();

        if (hari == null || jamKe == null) {
            showAlert("Error", "Pilih hari dan jam terlebih dahulu!");
            return;
        }

        try (Connection conn = DatabaseConnector.getConnection()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Ketersediaan Kelas pada ").append(hari).append(" Jam ke-").append(jamKe).append(":\n\n");
            
            String query = "SELECT k.kode_kelas, IFNULL(d.nama_dosen, 'KOSONG') as status " +
                          "FROM kelas k LEFT JOIN " +
                          "(SELECT r.kode_kelas, d.nama_dosen " +
                          "FROM reservasi r JOIN dosen d ON r.id_dosen = d.id_dosen " +
                          "WHERE r.hari = ? AND r.jam_ke = ?) as t " +
                          "ON k.kode_kelas = t.kode_kelas";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, hari);
            stmt.setInt(2, jamKe);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String kodeKelas = rs.getString("kode_kelas");
                String status = rs.getString("status");
                
                if (status.equals("KOSONG")) {
                    sb.append(kodeKelas).append(": ").append(status).append("\n");
                } else {
                    sb.append(kodeKelas).append(": Sudah dipesan oleh ").append(status).append("\n");
                }
            }
            
            hasilTextArea.setText(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Terjadi kesalahan saat mengakses database.");
        }
    }

    @FXML
    private void handleKembali() {
        try {
            sceneManager.showDashboardScene(idDosen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}