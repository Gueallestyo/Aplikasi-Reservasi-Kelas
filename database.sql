-- Buat database
CREATE DATABASE IF NOT EXISTS reservasi_kelas;
USE reservasi_kelas;

-- Tabel dosen
CREATE TABLE dosen (
    id_dosen VARCHAR(10) PRIMARY KEY,
    nama_dosen VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);

-- Tabel kelas
CREATE TABLE kelas (
    kode_kelas VARCHAR(10) PRIMARY KEY
);

-- Tabel reservasi
CREATE TABLE reservasi (
    id_reservasi INT AUTO_INCREMENT PRIMARY KEY,
    id_dosen VARCHAR(10),
    kode_kelas VARCHAR(10),
    hari ENUM('Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat'),
    jam_ke INT,
    FOREIGN KEY (id_dosen) REFERENCES dosen(id_dosen),
    FOREIGN KEY (kode_kelas) REFERENCES kelas(kode_kelas)
);

-- Insert data
INSERT INTO dosen VALUES 
('D001', 'Pak Amrin', 'amrin123'),
('D002', 'Bu Maria', 'maria456'),
('D003', 'Pak Ricky', 'ricky789');

INSERT INTO kelas VALUES 
('1KA31'), ('1KA32'), ('1KA33'),
('2KA31'), ('2KA32'), ('2KA33');

-- Contoh reservasi
INSERT INTO reservasi (id_dosen, kode_kelas, hari, jam_ke) VALUES 
('D001', '1KA31', 'Senin', 1),
('D002', '2KA32', 'Senin', 3),
('D003', '1KA33', 'Selasa', 5);