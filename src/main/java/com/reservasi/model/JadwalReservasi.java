package com.reservasi.model;

public class JadwalReservasi {
    private int id;
    private String idDosen;
    private String kodeMk;
    private String namaKelas;
    private int jamMulai;
    private int jamAkhir;
    private String tanggal;
    private String namaRuang;

    public JadwalReservasi(int id, String idDosen, String kodeMk, String namaKelas, int jamMulai, int jamAkhir, String tanggal, String namaRuang) {
        this.id = id;
        this.idDosen = idDosen;
        this.kodeMk = kodeMk;
        this.namaKelas = namaKelas;
        this.jamMulai = jamMulai;
        this.jamAkhir = jamAkhir;
        this.tanggal = tanggal;
        this.namaRuang = namaRuang;
    }

    // Getters
    public int getId() { return id; }
    public String getIdDosen() { return idDosen; }
    public String getKodeMk() { return kodeMk; }
    public String getNamaKelas() { return namaKelas; }
    public int getJamMulai() { return jamMulai; }
    public int getJamAkhir() { return jamAkhir; }
    public String getTanggal() { return tanggal; }
    public String getNamaRuang() { return namaRuang; }
}