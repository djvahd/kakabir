package com.badgenius.kakabir;

import com.badgenius.kakabir.db.Koneksi;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        // Sementara: cuma ngetes koneksi ke database. Nanti diganti buka LoginFrame.
        try (Connection c = Koneksi.getConnection()) {
            System.out.println("Konek ke database: " + c.getCatalog());
        } catch (Exception e) {
            System.out.println("Gagal konek: " + e.getMessage());
        } finally {
            Koneksi.tutup();
        }
    }
}
