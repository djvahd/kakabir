package com.badgenius.kakabir.model;

public class Kasir extends Pengguna {

    public Kasir() {
    }

    public Kasir(int id, String username, String password, String nama) {
        super(id, username, password, nama);
    }

    @Override
    public String getRole() {
        return "KASIR";
    }
}
