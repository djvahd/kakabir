package com.badgenius.kakabir.view;

import com.badgenius.kakabir.dao.MenuDao;
import com.badgenius.kakabir.dao.MenuDaoJdbc;
import com.badgenius.kakabir.dao.TransaksiDao;
import com.badgenius.kakabir.dao.TransaksiDaoJdbc;
import com.badgenius.kakabir.exception.DataTidakValidException;
import com.badgenius.kakabir.exception.KasirException;
import com.badgenius.kakabir.model.ItemKeranjang;
import com.badgenius.kakabir.model.Keranjang;
import com.badgenius.kakabir.model.Menu;
import com.badgenius.kakabir.model.Pengguna;
import com.badgenius.kakabir.model.Transaksi;

import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Layar kasir: pilih menu, isi keranjang, terima bayaran, simpan transaksi.
 */
public class TransaksiPanel extends javax.swing.JPanel {

    private final MenuDao menuDao = new MenuDaoJdbc();
    private final TransaksiDao transaksiDao = new TransaksiDaoJdbc();
    private final Keranjang keranjang = new Keranjang();
    private final Pengguna kasir;

    public TransaksiPanel(Pengguna kasir) {
        this.kasir = kasir;
        initComponents();
        lblTotal.setFont(lblTotal.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        lblKembalian.setFont(lblKembalian.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        muatMenu();
    }

    /** Ambil ulang daftar menu (stok bisa berubah) ke pilihan menu. */
    public void muatMenu() {
        try {
            Menu[] semua = menuDao.findAll().toArray(new Menu[0]);
            cmbMenu.setModel(new DefaultComboBoxModel<>(semua));
        } catch (KasirException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Gambar ulang tabel keranjang, total, dan kembalian. */
    private void segarkan() {
        DefaultTableModel model = (DefaultTableModel) tblKeranjang.getModel();
        model.setRowCount(0);
        for (ItemKeranjang item : keranjang.getItems()) {
            model.addRow(new Object[]{item.getMenu().getNama(), rupiah(item.getMenu().getHarga()),
                item.getJumlah(), rupiah(item.getSubtotal())});
        }
        lblTotal.setText(rupiah(keranjang.getTotal()));
        hitungKembalian();
    }

    /** Kembalian dihitung otomatis tiap uang bayar diketik. */
    private void hitungKembalian() {
        try {
            int kembalian = Integer.parseInt(txtBayar.getText().trim()) - keranjang.getTotal();
            lblKembalian.setText(kembalian < 0 ? "Kurang " + rupiah(-kembalian) : rupiah(kembalian));
        } catch (NumberFormatException e) {
            lblKembalian.setText("Rp 0");
        }
    }

    private int bacaAngka(String teks, String namaKolom) throws DataTidakValidException {
        try {
            return Integer.parseInt(teks.trim());
        } catch (NumberFormatException e) {
            throw new DataTidakValidException(namaKolom + " harus berupa angka.");
        }
    }

    private void tampilkanError(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Gagal", JOptionPane.WARNING_MESSAGE);
    }

    private static String rupiah(int angka) {
        return String.format(Locale.forLanguageTag("id-ID"), "Rp %,d", angka);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPilih = new javax.swing.JPanel();
        lblMenu = new javax.swing.JLabel();
        cmbMenu = new javax.swing.JComboBox<>();
        lblJumlah = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        spKeranjang = new javax.swing.JScrollPane();
        tblKeranjang = new javax.swing.JTable();
        pnlBawah = new javax.swing.JPanel();
        pnlBayar = new javax.swing.JPanel();
        lblTotalTeks = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblBayarTeks = new javax.swing.JLabel();
        txtBayar = new javax.swing.JTextField();
        lblKembalianTeks = new javax.swing.JLabel();
        lblKembalian = new javax.swing.JLabel();
        pnlTombol = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        btnBayar = new javax.swing.JButton();

        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new java.awt.BorderLayout(8, 8));

        pnlPilih.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
        lblMenu.setText("Menu");
        pnlPilih.add(lblMenu);

        cmbMenu.setPreferredSize(new java.awt.Dimension(220, 26));
        pnlPilih.add(cmbMenu);

        lblJumlah.setText("Jumlah");
        pnlPilih.add(lblJumlah);

        txtJumlah.setText("1");
        txtJumlah.setColumns(5);
        pnlPilih.add(txtJumlah);

        btnTambah.setText("Tambah ke Keranjang");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        pnlPilih.add(btnTambah);

        this.add(pnlPilih, java.awt.BorderLayout.NORTH);

        tblKeranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Menu", "Harga", "Jumlah", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spKeranjang.setViewportView(tblKeranjang);

        this.add(spKeranjang, java.awt.BorderLayout.CENTER);

        pnlBawah.setLayout(new java.awt.BorderLayout(8, 8));
        pnlBayar.setLayout(new java.awt.GridLayout(3, 2, 8, 4));
        lblTotalTeks.setText("Total");
        pnlBayar.add(lblTotalTeks);

        lblTotal.setText("Rp 0");
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pnlBayar.add(lblTotal);

        lblBayarTeks.setText("Uang bayar (Rp)");
        pnlBayar.add(lblBayarTeks);

        txtBayar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBayarKeyReleased(evt);
            }
        });
        pnlBayar.add(txtBayar);

        lblKembalianTeks.setText("Kembalian");
        pnlBayar.add(lblKembalianTeks);

        lblKembalian.setText("Rp 0");
        lblKembalian.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pnlBayar.add(lblKembalian);

        pnlBawah.add(pnlBayar, java.awt.BorderLayout.CENTER);

        pnlTombol.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        btnHapus.setText("Hapus Baris");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        pnlTombol.add(btnHapus);

        btnBayar.setText("Bayar");
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });
        pnlTombol.add(btnBayar);

        pnlBawah.add(pnlTombol, java.awt.BorderLayout.SOUTH);

        this.add(pnlBawah, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        Menu menu = (Menu) cmbMenu.getSelectedItem();
        if (menu == null) {
            tampilkanError("Belum ada menu yang bisa dipilih.");
            return;
        }
        try {
            keranjang.tambah(menu, bacaAngka(txtJumlah.getText(), "Jumlah"));
            txtJumlah.setText("1");
            segarkan();
        } catch (KasirException e) {
            tampilkanError(e.getMessage());
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void txtBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyReleased
        hitungKembalian();
    }//GEN-LAST:event_txtBayarKeyReleased

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        int baris = tblKeranjang.getSelectedRow();
        if (baris < 0) {
            tampilkanError("Pilih dulu baris keranjang yang mau dihapus.");
            return;
        }
        keranjang.hapus(baris);
        segarkan();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
        try {
            int bayar = bacaAngka(txtBayar.getText(), "Uang bayar");
            Transaksi t = Transaksi.dariKeranjang(keranjang, kasir.getId(), bayar);
            transaksiDao.simpan(t);
            JOptionPane.showMessageDialog(this, "Transaksi " + t.getKode() + " tersimpan.\nKembalian: "
                    + rupiah(t.getKembalian()), "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            keranjang.kosongkan();
            txtBayar.setText("");
            muatMenu();
            segarkan();
        } catch (KasirException e) {
            tampilkanError(e.getMessage());
        }
    }//GEN-LAST:event_btnBayarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<com.badgenius.kakabir.model.Menu> cmbMenu;
    private javax.swing.JLabel lblBayarTeks;
    private javax.swing.JLabel lblJumlah;
    private javax.swing.JLabel lblKembalian;
    private javax.swing.JLabel lblKembalianTeks;
    private javax.swing.JLabel lblMenu;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalTeks;
    private javax.swing.JPanel pnlBawah;
    private javax.swing.JPanel pnlBayar;
    private javax.swing.JPanel pnlPilih;
    private javax.swing.JPanel pnlTombol;
    private javax.swing.JScrollPane spKeranjang;
    private javax.swing.JTable tblKeranjang;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtJumlah;
    // End of variables declaration//GEN-END:variables
}
