package com.reservasi.model;

public class Reservasi {
    private int idReservasi;
    private String idDosen;
    private String kodeKelas;
    private String hari;
    private int jamKe;

    public Reservasi(String idDosen, String kodeKelas, String hari, int jamKe) {
        this.idDosen = idDosen;
        this.kodeKelas = kodeKelas;
        this.hari = hari;
        this.jamKe = jamKe;
    }

    // Getters and Setters
    public int getIdReservasi() { return idReservasi; }
    public String getIdDosen() { return idDosen; }
    public String getKodeKelas() { return kodeKelas; }
    public String getHari() { return hari; }
    public int getJamKe() { return jamKe; }
}