package com.reservasi.model;

public class Dosen {
    private String idDosen;
    private String namaDosen;
    private String password;

    public Dosen(String idDosen, String namaDosen, String password) {
        this.idDosen = idDosen;
        this.namaDosen = namaDosen;
        this.password = password;
    }

    // Getters and Setters
    public String getIdDosen() { return idDosen; }
    public String getNamaDosen() { return namaDosen; }
    public String getPassword() { return password; }
}