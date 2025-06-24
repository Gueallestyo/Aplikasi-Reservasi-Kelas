package com.reservasi.model;

public class RuangKelas {
    private String namaRuang;
    private String gedung;
    private String lantai;
    private String region;

    public RuangKelas(String namaRuang, String gedung, String lantai, String region) {
        this.namaRuang = namaRuang;
        this.gedung = gedung;
        this.lantai = lantai;
        this.region = region;
    }

    // Getters
    public String getNamaRuang() { return namaRuang; }
    public String getGedung() { return gedung; }
    public String getLantai() { return lantai; }
    public String getRegion() { return region; }

    @Override
    public String toString() {
        return namaRuang + " (" + gedung + ")";
    }
}