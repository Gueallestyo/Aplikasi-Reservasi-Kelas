package com.reservasi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:reservasi_kelas.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Enable foreign keys
            stmt.execute("PRAGMA foreign_keys = ON;");

            // Create dosen table
            stmt.execute("CREATE TABLE IF NOT EXISTS dosen (" +
                         "id_dosen TEXT PRIMARY KEY," +
                         "nama TEXT NOT NULL," +
                         "password TEXT NOT NULL" +
                         ");");

            // Create mata_kuliah table
            stmt.execute("CREATE TABLE IF NOT EXISTS mata_kuliah (" +
                         "kode_mk TEXT PRIMARY KEY," +
                         "nama_mk TEXT NOT NULL," +
                         "jurusan TEXT" +
                         ");");

            // Create jam_kuliah table
            stmt.execute("CREATE TABLE IF NOT EXISTS jam_kuliah (" +
                         "jam_ke INTEGER PRIMARY KEY," +
                         "waktu_mulai TEXT NOT NULL," +
                         "waktu_selesai TEXT NOT NULL" +
                         "Waktu_ke TEXT" +
                         ");");

            // Create kelas table
            stmt.execute("CREATE TABLE IF NOT EXISTS kelas (" +
                         "nama_kelas TEXT PRIMARY KEY," +
                         "jurusan TEXT" +
                         ");");

            // Create ruang_kelas table
            stmt.execute("CREATE TABLE IF NOT EXISTS ruang_kelas (" +
                         "nama_ruang TEXT PRIMARY KEY," +
                         "gedung TEXT," +
                         "lantai TEXT," +
                         "region TEXT" +
                         ");");

            // Create jadwal_reservasi table
            stmt.execute("CREATE TABLE IF NOT EXISTS jadwal_reservasi (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "id_dosen TEXT NOT NULL," +
                         "kode_mk TEXT NOT NULL," +
                         "nama_kelas TEXT NOT NULL," +
                         "jam_mulai INTEGER NOT NULL," + // Refers to jam_ke
                         "jam_akhir INTEGER NOT NULL," +  // Refers to jam_ke
                         "tanggal TEXT NOT NULL," +       // YYYY-MM-DD
                         "nama_ruang TEXT NOT NULL," +
                         "FOREIGN KEY (id_dosen) REFERENCES dosen(id_dosen)," +
                         "FOREIGN KEY (kode_mk) REFERENCES mata_kuliah(kode_mk)," +
                         "FOREIGN KEY (jam_mulai) REFERENCES jam_kuliah(jam_ke)," +
                         "FOREIGN KEY (jam_akhir) REFERENCES jam_kuliah(jam_ke)," +
                         "FOREIGN KEY (nama_ruang) REFERENCES ruang_kelas(nama_ruang)," +
                         "UNIQUE(tanggal, jam_mulai, jam_akhir, nama_ruang)" + // Prevent conflicts
                         ");");

            // Insert initial data (contoh)
            insertInitialData(conn);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    private static void insertInitialData(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Dosen
            stmt.execute("INSERT OR IGNORE INTO dosen (id_dosen, nama, password) VALUES ('D001', 'Prof. Budi', 'pass123');");
            stmt.execute("INSERT OR IGNORE INTO dosen (id_dosen, nama, password) VALUES ('D002', 'Dr. Ani', 'pass123');");

            // Mata Kuliah
            stmt.execute("INSERT OR IGNORE INTO mata_kuliah (kode_mk, nama_mk, jurusan) VALUES ('MK001', 'Pemrograman Lanjut', 'Informatika');");
            stmt.execute("INSERT OR IGNORE INTO mata_kuliah (kode_mk, nama_mk, jurusan) VALUES ('MK002', 'Struktur Data', 'Informatika');");
            stmt.execute("INSERT OR IGNORE INTO mata_kuliah (kode_mk, nama_mk, jurusan) VALUES ('MK003', 'Fisika Dasar', 'Teknik Elektro');");

            // Jam Kuliah
            stmt.execute("INSERT OR IGNORE INTO jam_kuliah (jam_ke, waktu_mulai, waktu_selesai) VALUES (1, '07:30', '08:30');");
            stmt.execute("INSERT OR IGNORE INTO jam_kuliah (jam_ke, waktu_mulai, waktu_selesai) VALUES (2, '08:30', '09:30');");
            stmt.execute("INSERT OR IGNORE INTO jam_kuliah (jam_ke, waktu_mulai, waktu_selesai) VALUES (3, '09:30', '10:30');");
            stmt.execute("INSERT OR IGNORE INTO jam_kuliah (jam_ke, waktu_mulai, waktu_selesai) VALUES (4, '10:30', '11:30');");
            stmt.execute("INSERT OR IGNORE INTO jam_kuliah (jam_ke, waktu_mulai, waktu_selesai) VALUES (5, '11:30', '12:30');");
            stmt.execute("INSERT OR IGNORE INTO jam_kuliah (jam_ke, waktu_mulai, waktu_selesai) VALUES (6, '13:00', '14:00');");
            stmt.execute("INSERT OR IGNORE INTO jam_kuliah (jam_ke, waktu_mulai, waktu_selesai) VALUES (7, '14:00', '15:00');");
            stmt.execute("INSERT OR IGNORE INTO jam_kuliah (jam_ke, waktu_ke, waktu_selesai) VALUES (8, '15:00', '16:00');");
            stmt.execute("INSERT OR IGNORE INTO jam_kuliah (jam_ke, waktu_mulai, waktu_selesai) VALUES (9, '16:00', '17:00');");
            stmt.execute("INSERT OR IGNORE INTO jam_kuliah (jam_ke, waktu_mulai, waktu_selesai) VALUES (10, '17:00', '18:00');");


            // Kelas
            stmt.execute("INSERT OR IGNORE INTO kelas (nama_kelas, jurusan) VALUES ('A', 'Informatika');");
            stmt.execute("INSERT OR IGNORE INTO kelas (nama_kelas, jurusan) VALUES ('B', 'Informatika');");
            stmt.execute("INSERT OR IGNORE INTO kelas (nama_kelas, jurusan) VALUES ('C', 'Teknik Elektro');");

            // Ruang Kelas
            stmt.execute("INSERT OR IGNORE INTO ruang_kelas (nama_ruang, gedung, lantai, region) VALUES ('R.101', 'Gedung A', 'Lantai 1', 'Kampus Utama');");
            stmt.execute("INSERT OR IGNORE INTO ruang_kelas (nama_ruang, gedung, lantai, region) VALUES ('R.102', 'Gedung A', 'Lantai 1', 'Kampus Utama');");
            stmt.execute("INSERT OR IGNORE INTO ruang_kelas (nama_ruang, gedung, lantai, region) VALUES ('R.201', 'Gedung A', 'Lantai 2', 'Kampus Utama');");
            stmt.execute("INSERT OR IGNORE INTO ruang_kelas (nama_ruang, gedung, lantai, region) VALUES ('R.B01', 'Gedung B', 'Lantai Basement', 'Kampus Cabang');");

        } catch (SQLException e) {
            System.err.println("Error inserting initial data: " + e.getMessage());
        }
    }
}