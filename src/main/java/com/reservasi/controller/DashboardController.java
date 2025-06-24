package com.reservasi.controller;

import com.reservasi.App;
import com.reservasi.database.DatabaseManager;
import com.reservasi.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private ComboBox<MataKuliah> mataKuliahComboBox;
    @FXML private ComboBox<Kelas> kelasComboBox;
    @FXML private ComboBox<Integer> jamMulaiComboBox;
    @FXML private ComboBox<Integer> jamAkhirComboBox;
    @FXML private ComboBox<RuangKelas> ruangKelasComboBox;
    @FXML private DatePicker tanggalDatePicker;
    @FXML private Label statusLabel;
    @FXML private TableView<JadwalReservasi> reservasiTableView;
    @FXML private TableColumn<JadwalReservasi, String> mkCol;
    @FXML private TableColumn<JadwalReservasi, String> kelasCol;
    @FXML private TableColumn<JadwalReservasi, String> jamCol;
    @FXML private TableColumn<JadwalReservasi, String> ruangCol;
    @FXML private TableColumn<JadwalReservasi, String> tanggalCol;

    private String loggedInDosenId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedInDosenId = App.getLoggedInDosenId();
        loadFormData();
        configureDatePicker();
        configureTableView();
        loadUserReservations();
    }

    private void loadFormData() {
        try (Connection conn = DatabaseManager.getConnection()) {
            // Load Mata Kuliah
            List<MataKuliah> mataKuliahList = new ArrayList<>();
            ResultSet rsMk = conn.createStatement().executeQuery("SELECT * FROM mata_kuliah");
            while (rsMk.next()) {
                mataKuliahList.add(new MataKuliah(rsMk.getString("kode_mk"), rsMk.getString("nama_mk"), rsMk.getString("jurusan")));
            }
            mataKuliahComboBox.setItems(FXCollections.observableArrayList(mataKuliahList));

            // Load Kelas
            List<Kelas> kelasList = new ArrayList<>();
            ResultSet rsKelas = conn.createStatement().executeQuery("SELECT * FROM kelas");
            while (rsKelas.next()) {
                kelasList.add(new Kelas(rsKelas.getString("nama_kelas"), rsKelas.getString("jurusan")));
            }
            kelasComboBox.setItems(FXCollections.observableArrayList(kelasList));

            // Load Jam Perkuliahan (hanya jam_ke untuk combobox)
            for (int i = 1; i <= 9; i++) { // Jam Mulai 1-9
                jamMulaiComboBox.getItems().add(i);
            }
            for (int i = 2; i <= 10; i++) { // Jam Akhir 2-10
                jamAkhirComboBox.getItems().add(i);
            }

            // Load Ruang Kelas
            List<RuangKelas> ruangKelasList = new ArrayList<>();
            ResultSet rsRuang = conn.createStatement().executeQuery("SELECT * FROM ruang_kelas");
            while (rsRuang.next()) {
                ruangKelasList.add(new RuangKelas(rsRuang.getString("nama_ruang"), rsRuang.getString("gedung"), rsRuang.getString("lantai"), rsRuang.getString("region")));
            }
            ruangKelasComboBox.setItems(FXCollections.observableArrayList(ruangKelasList));

        } catch (SQLException e) {
            statusLabel.setText("Error memuat data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configureDatePicker() {
        tanggalDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                // Tidak bisa memilih hari sebelumnya atau hari Minggu, maksimal 5 hari ke depan
                setDisable(empty || date.compareTo(today) < 0 || date.getDayOfWeek() == DayOfWeek.SUNDAY || date.compareTo(today.plusDays(5)) > 0);
            }
        });
    }

    private void configureTableView() {
        mkCol.setCellValueFactory(cellData -> {
            String kodeMk = cellData.getValue().getKodeMk();
            try (Connection conn = DatabaseManager.getConnection()) {
                PreparedStatement pstmt = conn.prepareStatement("SELECT nama_mk FROM mata_kuliah WHERE kode_mk = ?");
                pstmt.setString(1, kodeMk);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new SimpleStringProperty(rs.getString("nama_mk"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty(kodeMk); // Fallback
        });
        kelasCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamaKelas()));
        jamCol.setCellValueFactory(cellData -> {
            int jamMulaiKe = cellData.getValue().getJamMulai();
            int jamAkhirKe = cellData.getValue().getJamAkhir();
            try (Connection conn = DatabaseManager.getConnection()) {
                PreparedStatement pstmtMulai = conn.prepareStatement("SELECT waktu_mulai FROM jam_kuliah WHERE jam_ke = ?");
                pstmtMulai.setInt(1, jamMulaiKe);
                ResultSet rsMulai = pstmtMulai.executeQuery();

                PreparedStatement pstmtAkhir = conn.prepareStatement("SELECT waktu_selesai FROM jam_kuliah WHERE jam_ke = ?");
                pstmtAkhir.setInt(1, jamAkhirKe);
                ResultSet rsAkhir = pstmtAkhir.executeQuery();

                if (rsMulai.next() && rsAkhir.next()) {
                    return new SimpleStringProperty(rsMulai.getString("waktu_mulai") + " - " + rsAkhir.getString("waktu_selesai"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty(jamMulaiKe + " - " + jamAkhirKe); // Fallback
        });
        ruangCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamaRuang()));
        tanggalCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggal()));
    }

    private void loadUserReservations() {
        ObservableList<JadwalReservasi> reservasiList = FXCollections.observableArrayList();
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM jadwal_reservasi WHERE id_dosen = ? ORDER BY tanggal DESC, jam_mulai ASC";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, loggedInDosenId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reservasiList.add(new JadwalReservasi(
                        rs.getInt("id"),
                        rs.getString("id_dosen"),
                        rs.getString("kode_mk"),
                        rs.getString("nama_kelas"),
                        rs.getInt("jam_mulai"),
                        rs.getInt("jam_akhir"),
                        rs.getString("tanggal"),
                        rs.getString("nama_ruang")
                ));
            }
            reservasiTableView.setItems(reservasiList);
        } catch (SQLException e) {
            statusLabel.setText("Error memuat reservasi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePesanKelasButton(ActionEvent event) {
        pesanKelas();
    }

    private void pesanKelas() {
        MataKuliah selectedMk = mataKuliahComboBox.getValue();
        Kelas selectedKelas = kelasComboBox.getValue();
        Integer selectedJamMulai = jamMulaiComboBox.getValue();
        Integer selectedJamAkhir = jamAkhirComboBox.getValue();
        RuangKelas selectedRuang = ruangKelasComboBox.getValue();
        LocalDate selectedTanggal = tanggalDatePicker.getValue();

        // Validasi Semua Field Wajib Diisi
        if (selectedMk == null || selectedKelas == null || selectedJamMulai == null ||
            selectedJamAkhir == null || selectedRuang == null || selectedTanggal == null) {
            statusLabel.setText("Semua field wajib diisi!");
            return;
        }

        // Validasi jam_akhir > jam_awal
        if (selectedJamAkhir <= selectedJamMulai) {
            statusLabel.setText("Jam akhir harus lebih besar dari jam mulai.");
            return;
        }

        String tanggalStr = selectedTanggal.format(DateTimeFormatter.ISO_LOCAL_DATE);

        try (Connection conn = DatabaseManager.getConnection()) {
            // Cek ketersediaan ruangan (validasi kombinasi tanggal, jam, dan ruang harus unik)
            String checkAvailabilityQuery = "SELECT COUNT(*) FROM jadwal_reservasi " +
                                            "WHERE nama_ruang = ? AND tanggal = ? AND (" +
                                            " (jam_mulai < ? AND jam_akhir > ?) OR " + // Overlap existing
                                            " (jam_mulai >= ? AND jam_mulai < ?) OR " + // New start in existing
                                            " (jam_akhir > ? AND jam_akhir <= ?) OR " + // New end in existing
                                            " (jam_mulai <= ? AND jam_akhir >= ?) )";   // Existing within new
            PreparedStatement pstmtCheck = conn.prepareStatement(checkAvailabilityQuery);
            pstmtCheck.setString(1, selectedRuang.getNamaRuang());
            pstmtCheck.setString(2, tanggalStr);
            pstmtCheck.setInt(3, selectedJamAkhir);
            pstmtCheck.setInt(4, selectedJamMulai);
            pstmtCheck.setInt(5, selectedJamMulai);
            pstmtCheck.setInt(6, selectedJamAkhir);
            pstmtCheck.setInt(7, selectedJamMulai);
            pstmtCheck.setInt(8, selectedJamAkhir);
            pstmtCheck.setInt(9, selectedJamMulai);
            pstmtCheck.setInt(10, selectedJamAkhir);

            ResultSet rsCheck = pstmtCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                statusLabel.setText("Ruangan " + selectedRuang.getNamaRuang() + " tidak tersedia pada jam " +
                                   getJamDisplay(selectedJamMulai, selectedJamAkhir) + " tanggal " + tanggalStr);
                return;
            }

            // Jika valid, simpan ke database
            String insertQuery = "INSERT INTO jadwal_reservasi (id_dosen, kode_mk, nama_kelas, jam_mulai, jam_akhir, tanggal, nama_ruang) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmtInsert = conn.prepareStatement(insertQuery);
            pstmtInsert.setString(1, loggedInDosenId);
            pstmtInsert.setString(2, selectedMk.getKodeMk());
            pstmtInsert.setString(3, selectedKelas.getNamaKelas());
            pstmtInsert.setInt(4, selectedJamMulai);
            pstmtInsert.setInt(5, selectedJamAkhir);
            pstmtInsert.setString(6, tanggalStr);
            pstmtInsert.setString(7, selectedRuang.getNamaRuang());
            pstmtInsert.executeUpdate();

            statusLabel.setText("Reservasi berhasil!");
            clearForm();
            loadUserReservations(); // Refresh tabel setelah reservasi
        } catch (SQLException e) {
            statusLabel.setText("Error saat menyimpan reservasi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        mataKuliahComboBox.getSelectionModel().clearSelection();
        kelasComboBox.getSelectionModel().clearSelection();
        jamMulaiComboBox.getSelectionModel().clearSelection();
        jamAkhirComboBox.getSelectionModel().clearSelection();
        ruangKelasComboBox.getSelectionModel().clearSelection();
        tanggalDatePicker.setValue(null);
    }

    private String getJamDisplay(int jamMulaiKe, int jamAkhirKe) {
        String display = "";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement pstmtMulai = conn.prepareStatement("SELECT waktu_mulai FROM jam_kuliah WHERE jam_ke = ?");
            pstmtMulai.setInt(1, jamMulaiKe);
            ResultSet rsMulai = pstmtMulai.executeQuery();

            PreparedStatement pstmtAkhir = conn.prepareStatement("SELECT waktu_selesai FROM jam_kuliah WHERE jam_ke = ?");
            pstmtAkhir.setInt(1, jamAkhirKe);
            ResultSet rsAkhir = pstmtAkhir.executeQuery();

            if (rsMulai.next() && rsAkhir.next()) {
                display = rsMulai.getString("waktu_mulai") + " - " + rsAkhir.getString("waktu_selesai");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return display;
    }


    @FXML
    private void handleHomeButton(ActionEvent event) {
        statusLabel.setText("Anda berada di halaman utama.");
        loadUserReservations(); // Refresh reservasi jika diperlukan
    }

    @FXML
    private void handleLogoutButton(ActionEvent event) {
        try {
            App.showLoginView();
        } catch (IOException e) {
            statusLabel.setText("Error logout: " + e.getMessage());
            e.printStackTrace();
        }
    }
}