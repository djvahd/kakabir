package com.badgenius.kakabir.model;

/**
 * Induk buat Admin dan Kasir. Bedanya cuma peran (getRole).
 */
public abstract class Pengguna {

    private int id;
    private String username;
    private String password;
    private String nama;

    public Pengguna() {
    }

    public Pengguna(int id, String username, String password, String nama) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nama = nama;
    }

    /** Peran pengguna, sama kayak nilai kolom role di database (ADMIN / KASIR). */
    public abstract String getRole();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
