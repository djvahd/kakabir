package com.badgenius.kakabir.model;

public class Admin extends Pengguna {

    public Admin() {
    }

    public Admin(int id, String username, String password, String nama) {
        super(id, username, password, nama);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
