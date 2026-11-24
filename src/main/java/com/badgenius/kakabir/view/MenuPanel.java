package com.badgenius.kakabir.view;

import com.badgenius.kakabir.dao.MenuDao;
import com.badgenius.kakabir.dao.MenuDaoJdbc;
import com.badgenius.kakabir.exception.DataTidakValidException;
import com.badgenius.kakabir.exception.KasirException;
import com.badgenius.kakabir.model.Kategori;
import com.badgenius.kakabir.model.Menu;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Layar admin buat ngatur menu: lihat, tambah, ubah, dan hapus.
 */
public class MenuPanel extends javax.swing.JPanel {

    private final MenuDao menuDao = new MenuDaoJdbc();
    private List<Menu> daftar = new ArrayList<>();

    public MenuPanel() {
        initComponents();
        muatData();
    }

    /** Ambil ulang semua menu dari database ke tabel. */
    public void muatData() {
        try {
            daftar = menuDao.findAll();
        } catch (KasirException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tblMenu.getModel();
        model.setRowCount(0);
        for (Menu m : daftar) {
            model.addRow(new Object[]{m.getNama(), m.getKategori(), m.getHarga(), m.getStok()});
        }
    }

    private void bersihkanForm() {
        txtNama.setText("");
        cmbKategori.setSelectedIndex(0);
        txtHarga.setText("");
        txtStok.setText("");
        tblMenu.clearSelection();
    }

    /** Menu yang lagi dipilih di tabel, atau null kalau belum ada. */
    private Menu menuTerpilih() {
        int baris = tblMenu.getSelectedRow();
        return baris < 0 ? null : daftar.get(baris);
    }

    /** Baca angka dari kolom isian. Bukan angka berarti data nggak valid. */
    private int bacaAngka(String teks, String namaKolom) throws DataTidakValidException {
        try {
            return Integer.parseInt(teks.trim());
        } catch (NumberFormatException e) {
            throw new DataTidakValidException(namaKolom + " harus berupa angka.");
        }
    }

    /** Bikin objek Menu dari isian form. Validasi dilakukan oleh setter di class Menu. */
    private Menu menuDariForm() throws DataTidakValidException {
        Menu m = new Menu();
        m.setNama(txtNama.getText());
        m.setKategori(Kategori.valueOf((String) cmbKategori.getSelectedItem()));
        m.setHarga(bacaAngka(txtHarga.getText(), "Harga"));
        m.setStok(bacaAngka(txtStok.getText(), "Stok"));
        return m;
    }

    private void tampilkanError(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Gagal", JOptionPane.WARNING_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spMenu = new javax.swing.JScrollPane();
        tblMenu = new javax.swing.JTable();
        pnlBawah = new javax.swing.JPanel();
        pnlIsi = new javax.swing.JPanel();
        lblNama = new javax.swing.JLabel();
        lblKategori = new javax.swing.JLabel();
        lblHarga = new javax.swing.JLabel();
        lblStok = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        cmbKategori = new javax.swing.JComboBox<>();
        txtHarga = new javax.swing.JTextField();
        txtStok = new javax.swing.JTextField();
        pnlTombol = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBersihkan = new javax.swing.JButton();

        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new java.awt.BorderLayout(8, 8));

        tblMenu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama", "Kategori", "Harga", "Stok"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMenuMouseClicked(evt);
            }
        });
        spMenu.setViewportView(tblMenu);

        this.add(spMenu, java.awt.BorderLayout.CENTER);

        pnlBawah.setLayout(new java.awt.BorderLayout(8, 8));
        pnlIsi.setLayout(new java.awt.GridLayout(2, 4, 8, 4));
        lblNama.setText("Nama");
        pnlIsi.add(lblNama);

        lblKategori.setText("Kategori");
        pnlIsi.add(lblKategori);

        lblHarga.setText("Harga (Rp)");
        pnlIsi.add(lblHarga);

        lblStok.setText("Stok");
        pnlIsi.add(lblStok);

        pnlIsi.add(txtNama);

        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MAKANAN", "MINUMAN" }));
        pnlIsi.add(cmbKategori);

        pnlIsi.add(txtHarga);

        pnlIsi.add(txtStok);

        pnlBawah.add(pnlIsi, java.awt.BorderLayout.CENTER);

        pnlTombol.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        pnlTombol.add(btnTambah);

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });
        pnlTombol.add(btnUbah);

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        pnlTombol.add(btnHapus);

        btnBersihkan.setText("Bersihkan");
        btnBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBersihkanActionPerformed(evt);
            }
        });
        pnlTombol.add(btnBersihkan);

        pnlBawah.add(pnlTombol, java.awt.BorderLayout.SOUTH);

        this.add(pnlBawah, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void tblMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMenuMouseClicked
        Menu m = menuTerpilih();
        if (m == null) {
            return;
        }
        txtNama.setText(m.getNama());
        cmbKategori.setSelectedItem(m.getKategori().name());
        txtHarga.setText(String.valueOf(m.getHarga()));
        txtStok.setText(String.valueOf(m.getStok()));
    }//GEN-LAST:event_tblMenuMouseClicked

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        try {
            menuDao.insert(menuDariForm());
            muatData();
            bersihkanForm();
        } catch (KasirException e) {
            tampilkanError(e.getMessage());
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        Menu terpilih = menuTerpilih();
        if (terpilih == null) {
            tampilkanError("Pilih dulu menu yang mau diubah di tabel.");
            return;
        }
        try {
            Menu baru = menuDariForm();
            baru.setId(terpilih.getId());
            menuDao.update(baru);
            muatData();
            bersihkanForm();
        } catch (KasirException e) {
            tampilkanError(e.getMessage());
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        Menu terpilih = menuTerpilih();
        if (terpilih == null) {
            tampilkanError("Pilih dulu menu yang mau dihapus di tabel.");
            return;
        }
        int jawab = JOptionPane.showConfirmDialog(this, "Hapus menu " + terpilih.getNama() + "?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            menuDao.delete(terpilih.getId());
            muatData();
            bersihkanForm();
        } catch (KasirException e) {
            tampilkanError(e.getMessage());
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBersihkanActionPerformed
        bersihkanForm();
    }//GEN-LAST:event_btnBersihkanActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBersihkan;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JLabel lblHarga;
    private javax.swing.JLabel lblKategori;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblStok;
    private javax.swing.JPanel pnlBawah;
    private javax.swing.JPanel pnlIsi;
    private javax.swing.JPanel pnlTombol;
    private javax.swing.JScrollPane spMenu;
    private javax.swing.JTable tblMenu;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtStok;
    // End of variables declaration//GEN-END:variables
}
