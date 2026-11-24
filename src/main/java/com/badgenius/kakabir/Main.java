package com.badgenius.kakabir;

import com.badgenius.kakabir.db.Koneksi;
import com.badgenius.kakabir.view.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // nggak masalah, pakai tampilan bawaan Swing
        }
        // Pool koneksi ditutup waktu aplikasi keluar.
        Runtime.getRuntime().addShutdownHook(new Thread(Koneksi::tutup));
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
