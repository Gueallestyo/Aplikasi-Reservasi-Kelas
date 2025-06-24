package com.reservasi.model;

public class JamKuliah {
    private int jamKe;
    private String waktuMulai;
    private String waktuSelesai;

    public JamKuliah(int jamKe, String waktuMulai, String waktuSelesai) {
        this.jamKe = jamKe;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
    }

    // Getters
    public int getJamKe() { return jamKe; }
    public String getWaktuMulai() { return waktuMulai; }
    public String getWaktuSelesai() { return waktuSelesai; }

    @Override
    public String toString() {
        return waktuMulai + " - " + waktuSelesai;
    }
}