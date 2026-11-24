package com.badgenius.kakabir.view;

import com.badgenius.kakabir.dao.PenggunaDao;
import com.badgenius.kakabir.dao.PenggunaDaoJdbc;
import com.badgenius.kakabir.exception.KasirException;
import com.badgenius.kakabir.exception.LoginGagalException;
import com.badgenius.kakabir.model.Pengguna;

import javax.swing.JOptionPane;

/**
 * Layar pertama. Login berhasil buka DashboardFrame, gagal tampil pesan dari exception.
 */
public class LoginFrame extends javax.swing.JFrame {

    private final PenggunaDao penggunaDao = new PenggunaDaoJdbc();

    public LoginFrame() {
        initComponents();
        lblJudul.setFont(lblJudul.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        getRootPane().setDefaultButton(btnMasuk);
        setLocationRelativeTo(null);
    }

    /** Cek username dan password. Dua-duanya salah dibalas pesan yang sama biar nggak bocor mana yang bener. */
    private Pengguna cekLogin(String username, String password) throws KasirException {
        Pengguna p = penggunaDao.findByUsername(username)
                .orElseThrow(() -> new LoginGagalException("Username atau password salah."));
        if (!p.getPassword().equals(password)) {
            throw new LoginGagalException("Username atau password salah.");
        }
        return p;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblJudul = new javax.swing.JLabel();
        pnlForm = new javax.swing.JPanel();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        pwdPassword = new javax.swing.JPasswordField();
        lblKosong = new javax.swing.JLabel();
        btnMasuk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Kakabir - Login");
        this.setPreferredSize(new java.awt.Dimension(380, 240));
        getContentPane().setLayout(new java.awt.BorderLayout(8, 8));

        lblJudul.setText("Kakabir: Kasir Kantin Biru");
        lblJudul.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 10, 0, 10));
        getContentPane().add(lblJudul, java.awt.BorderLayout.NORTH);

        pnlForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 24, 20, 24));
        pnlForm.setLayout(new java.awt.GridLayout(3, 2, 8, 8));
        lblUsername.setText("Username");
        pnlForm.add(lblUsername);

        pnlForm.add(txtUsername);

        lblPassword.setText("Password");
        pnlForm.add(lblPassword);

        pwdPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMasukActionPerformed(evt);
            }
        });
        pnlForm.add(pwdPassword);

        lblKosong.setText("");
        pnlForm.add(lblKosong);

        btnMasuk.setText("Masuk");
        btnMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMasukActionPerformed(evt);
            }
        });
        pnlForm.add(btnMasuk);

        getContentPane().add(pnlForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasukActionPerformed
        try {
            Pengguna p = cekLogin(txtUsername.getText().trim(), new String(pwdPassword.getPassword()));
            new DashboardFrame(p).setVisible(true);
            dispose();
        } catch (KasirException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Login gagal", JOptionPane.WARNING_MESSAGE);
            pwdPassword.setText("");
        }
    }//GEN-LAST:event_btnMasukActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMasuk;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblKosong;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
