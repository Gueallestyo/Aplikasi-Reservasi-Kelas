package com.reservasi.model;

public class Dosen {
    private String idDosen;
    private String nama;
    private String password; 

    public Dosen(String idDosen, String nama, String password) {
        this.idDosen = idDosen;
        this.nama = nama;
        this.password = password;
    }

    // Getters
    public String getIdDosen() { return idDosen; }
    public String getNama() { return nama; }
    public String getPassword() { return password; } 
}