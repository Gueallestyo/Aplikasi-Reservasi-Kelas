package com.reservasi.model;

public class Kelas {
    private String namaKelas;
    private String jurusan;

    public Kelas(String namaKelas, String jurusan) {
        this.namaKelas = namaKelas;
        this.jurusan = jurusan;
    }

    // Getters
    public String getNamaKelas() { return namaKelas; }
    public String getJurusan() { return jurusan; }

    @Override
    public String toString() {
        return namaKelas;
    }
}