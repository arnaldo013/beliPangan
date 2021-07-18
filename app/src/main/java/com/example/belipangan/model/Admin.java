package com.example.belipangan.model;

public class Admin {
    private String nama, email, noTelpon, role;

    public Admin(String nama, String email, String noTelpon, String role) {
        this.nama = nama;
        this.email = email;
        this.noTelpon = noTelpon;
        this.role = role;
    }

    public Admin() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoTelpon() {
        return noTelpon;
    }

    public void setNoTelpon(String noTelpon) {
        this.noTelpon = noTelpon;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
