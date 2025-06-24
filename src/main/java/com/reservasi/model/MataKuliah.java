package com.reservasi.model;

public class MataKuliah {
    private String kodeMk;
    private String namaMk;
    private String jurusan;

    public MataKuliah(String kodeMk, String namaMk, String jurusan) {
        this.kodeMk = kodeMk;
        this.namaMk = namaMk;
        this.jurusan = jurusan;
    }

    // Getters
    public String getKodeMk() { return kodeMk; }
    public String getNamaMk() { return namaMk; }
    public String getJurusan() { return jurusan; }

    @Override
    public String toString() {
        return namaMk + " (" + kodeMk + ")";
    }
}