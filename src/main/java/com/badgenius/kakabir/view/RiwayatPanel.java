package com.badgenius.kakabir.view;

import com.badgenius.kakabir.dao.TransaksiDao;
import com.badgenius.kakabir.dao.TransaksiDaoJdbc;
import com.badgenius.kakabir.exception.KasirException;
import com.badgenius.kakabir.model.DetailTransaksi;
import com.badgenius.kakabir.model.Transaksi;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Riwayat penjualan: daftar transaksi di atas, isi transaksi yang dipilih di bawah.
 */
public class RiwayatPanel extends javax.swing.JPanel {

    private static final DateTimeFormatter FORMAT_TANGGAL = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private final TransaksiDao transaksiDao = new TransaksiDaoJdbc();
    private List<Transaksi> daftar = new ArrayList<>();

    public RiwayatPanel() {
        initComponents();
        // Pilih baris lewat mouse atau tombol panah, detailnya ikut berubah.
        tblRiwayat.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tampilkanDetail();
            }
        });
        muatData();
    }

    /** Ambil ulang daftar transaksi dari database. */
    public void muatData() {
        try {
            daftar = transaksiDao.findAll();
        } catch (KasirException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tblRiwayat.getModel();
        model.setRowCount(0);
        for (Transaksi t : daftar) {
            model.addRow(new Object[]{t.getKode(), t.getTanggal().format(FORMAT_TANGGAL),
                rupiah(t.getTotal()), rupiah(t.getBayar()), rupiah(t.getKembalian())});
        }
        ((DefaultTableModel) tblDetail.getModel()).setRowCount(0);
    }

    private void tampilkanDetail() {
        DefaultTableModel model = (DefaultTableModel) tblDetail.getModel();
        model.setRowCount(0);
        int baris = tblRiwayat.getSelectedRow();
        if (baris < 0 || baris >= daftar.size()) {
            return;
        }
        try {
            for (DetailTransaksi d : transaksiDao.findDetail(daftar.get(baris).getId())) {
                model.addRow(new Object[]{d.getNamaMenu(), rupiah(d.getHarga()), d.getJumlah(), rupiah(d.getSubtotal())});
            }
        } catch (KasirException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String rupiah(int angka) {
        return String.format(Locale.forLanguageTag("id-ID"), "Rp %,d", angka);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlAtas = new javax.swing.JPanel();
        btnMuat = new javax.swing.JButton();
        pnlTabel = new javax.swing.JPanel();
        pnlRiwayat = new javax.swing.JPanel();
        lblRiwayat = new javax.swing.JLabel();
        spRiwayat = new javax.swing.JScrollPane();
        tblRiwayat = new javax.swing.JTable();
        pnlDetail = new javax.swing.JPanel();
        lblDetail = new javax.swing.JLabel();
        spDetail = new javax.swing.JScrollPane();
        tblDetail = new javax.swing.JTable();

        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new java.awt.BorderLayout(8, 8));

        pnlAtas.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        btnMuat.setText("Muat Ulang");
        btnMuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMuatActionPerformed(evt);
            }
        });
        pnlAtas.add(btnMuat);

        this.add(pnlAtas, java.awt.BorderLayout.NORTH);

        pnlTabel.setLayout(new java.awt.GridLayout(2, 1, 0, 8));
        pnlRiwayat.setLayout(new java.awt.BorderLayout(0, 4));
        lblRiwayat.setText("Daftar transaksi (yang terbaru di atas)");
        pnlRiwayat.add(lblRiwayat, java.awt.BorderLayout.NORTH);

        tblRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Tanggal", "Total", "Bayar", "Kembalian"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spRiwayat.setViewportView(tblRiwayat);

        pnlRiwayat.add(spRiwayat, java.awt.BorderLayout.CENTER);

        pnlTabel.add(pnlRiwayat);

        pnlDetail.setLayout(new java.awt.BorderLayout(0, 4));
        lblDetail.setText("Detail transaksi yang dipilih");
        pnlDetail.add(lblDetail, java.awt.BorderLayout.NORTH);

        tblDetail.setModel(new javax.swing.table.DefaultTableModel(
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
        spDetail.setViewportView(tblDetail);

        pnlDetail.add(spDetail, java.awt.BorderLayout.CENTER);

        pnlTabel.add(pnlDetail);

        this.add(pnlTabel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMuatActionPerformed
        muatData();
    }//GEN-LAST:event_btnMuatActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMuat;
    private javax.swing.JLabel lblDetail;
    private javax.swing.JLabel lblRiwayat;
    private javax.swing.JPanel pnlAtas;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlRiwayat;
    private javax.swing.JPanel pnlTabel;
    private javax.swing.JScrollPane spDetail;
    private javax.swing.JScrollPane spRiwayat;
    private javax.swing.JTable tblDetail;
    private javax.swing.JTable tblRiwayat;
    // End of variables declaration//GEN-END:variables
}
