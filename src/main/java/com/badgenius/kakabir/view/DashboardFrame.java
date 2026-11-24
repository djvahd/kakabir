package com.badgenius.kakabir.view;

import com.badgenius.kakabir.model.Admin;
import com.badgenius.kakabir.model.Pengguna;

import java.awt.Component;

/**
 * Jendela utama setelah login. Tab yang tampil ngikutin peran:
 * admin dapat Menu + Riwayat, kasir dapat Transaksi + Riwayat.
 */
public class DashboardFrame extends javax.swing.JFrame {

    private TransaksiPanel transaksiPanel;
    private RiwayatPanel riwayatPanel;

    public DashboardFrame(Pengguna pengguna) {
        initComponents();
        lblPengguna.setText("Masuk sebagai " + pengguna.getNama() + " (" + pengguna.getRole() + ")");

        // Polimorfisme: Admin dan Kasir sama-sama Pengguna, tapi tabnya beda.
        if (pengguna instanceof Admin) {
            tabs.addTab("Menu", new MenuPanel());
        } else {
            transaksiPanel = new TransaksiPanel(pengguna);
            tabs.addTab("Transaksi", transaksiPanel);
        }
        riwayatPanel = new RiwayatPanel();
        tabs.addTab("Riwayat", riwayatPanel);

        // Tiap pindah tab, data tab itu dimuat ulang biar selalu terbaru.
        tabs.addChangeListener(e -> {
            Component aktif = tabs.getSelectedComponent();
            if (aktif == riwayatPanel) {
                riwayatPanel.muatData();
            } else if (aktif == transaksiPanel) {
                transaksiPanel.muatMenu();
            }
        });
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlAtas = new javax.swing.JPanel();
        lblPengguna = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        tabs = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Kakabir - Kantin Biru");
        this.setPreferredSize(new java.awt.Dimension(900, 600));
        getContentPane().setLayout(new java.awt.BorderLayout());

        pnlAtas.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 10, 8, 10));
        pnlAtas.setLayout(new java.awt.BorderLayout(8, 0));
        lblPengguna.setText("Masuk sebagai");
        pnlAtas.add(lblPengguna, java.awt.BorderLayout.CENTER);

        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        pnlAtas.add(btnLogout, java.awt.BorderLayout.EAST);

        getContentPane().add(pnlAtas, java.awt.BorderLayout.NORTH);

        getContentPane().add(tabs, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        dispose();
        new LoginFrame().setVisible(true);
    }//GEN-LAST:event_btnLogoutActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogout;
    private javax.swing.JLabel lblPengguna;
    private javax.swing.JPanel pnlAtas;
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
