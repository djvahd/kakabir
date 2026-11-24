package com.badgenius.kakabir.dao;

import com.badgenius.kakabir.db.Koneksi;
import com.badgenius.kakabir.exception.KasirException;
import com.badgenius.kakabir.exception.StokTidakCukupException;
import com.badgenius.kakabir.model.DetailTransaksi;
import com.badgenius.kakabir.model.Transaksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransaksiDaoJdbc implements TransaksiDao {

    private static final String KOLOM = "id, kode, tanggal, id_kasir, total, bayar, kembalian";

    @Override
    public List<Transaksi> findAll() throws KasirException {
        String sql = "SELECT " + KOLOM + " FROM transaksi ORDER BY tanggal DESC, id DESC";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Transaksi> hasil = new ArrayList<>();
            while (rs.next()) {
                hasil.add(baris(rs));
            }
            return hasil;
        } catch (SQLException e) {
            throw new KasirException("Gagal ngambil riwayat transaksi: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Transaksi> findById(int id) throws KasirException {
        String sql = "SELECT " + KOLOM + " FROM transaksi WHERE id = ?";
        try (Connection c = Koneksi.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                Transaksi t = baris(rs);
                t.setDetail(findDetail(id));
                return Optional.of(t);
            }
        } catch (SQLException e) {
            throw new KasirException("Gagal ngambil transaksi: " + e.getMessage(), e);
        }
    }

    @Override
    public void insert(Transaksi transaksi) throws KasirException {
        simpan(transaksi);
    }

    @Override
    public void update(Transaksi transaksi) throws KasirException {
        throw new KasirException("Transaksi yang udah tersimpan nggak boleh diubah.");
    }

    @Override
    public void delete(int id) throws KasirException {
        throw new KasirException("Transaksi yang udah tersimpan nggak boleh dihapus.");
    }

    @Override
    public void simpan(Transaksi t) throws KasirException {
        if (t.getTanggal() == null) {
            t.setTanggal(LocalDateTime.now());
        }
        Connection c = null;
        try {
            c = Koneksi.getConnection();
            c.setAutoCommit(false);

            // Kode sementara (unik) dulu, soalnya kode final butuh id dari database.
            int idBaru = insertTransaksi(c, t);
            t.setId(idBaru);
            t.setKode("TRX" + t.getTanggal().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    + "-" + String.format("%04d", idBaru));
            try (PreparedStatement ps = c.prepareStatement("UPDATE transaksi SET kode = ? WHERE id = ?")) {
                ps.setString(1, t.getKode());
                ps.setInt(2, idBaru);
                ps.executeUpdate();
            }

            for (DetailTransaksi d : t.getDetail()) {
                insertDetail(c, idBaru, d);
                kurangiStok(c, d);
            }
            c.commit();
        } catch (KasirException | SQLException e) {
            batal(c);
            t.setId(0);
            t.setKode(null);
            if (e instanceof KasirException) {
                throw (KasirException) e;
            }
            throw new KasirException("Gagal nyimpen transaksi: " + e.getMessage(), e);
        } finally {
            tutup(c);
        }
    }

    @Override
    public List<DetailTransaksi> findDetail(int idTransaksi) throws KasirException {
        String sql = "SELECT id, id_menu, nama_menu, harga, jumlah, subtotal "
                + "FROM detail_transaksi WHERE id_transaksi = ? ORDER BY id";
        try (Connection c = Koneksi.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idTransaksi);
            try (ResultSet rs = ps.executeQuery()) {
                List<DetailTransaksi> hasil = new ArrayList<>();
                while (rs.next()) {
                    hasil.add(new DetailTransaksi(rs.getInt("id"), rs.getInt("id_menu"),
                            rs.getString("nama_menu"), rs.getInt("harga"),
                            rs.getInt("jumlah"), rs.getInt("subtotal")));
                }
                return hasil;
            }
        } catch (SQLException e) {
            throw new KasirException("Gagal ngambil detail transaksi: " + e.getMessage(), e);
        }
    }

    private int insertTransaksi(Connection c, Transaksi t) throws SQLException {
        String sql = "INSERT INTO transaksi (kode, tanggal, id_kasir, total, bayar, kembalian) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, UUID.randomUUID().toString().substring(0, 20));
            ps.setTimestamp(2, Timestamp.valueOf(t.getTanggal() == null ? LocalDateTime.now() : t.getTanggal()));
            ps.setInt(3, t.getIdKasir());
            ps.setInt(4, t.getTotal());
            ps.setInt(5, t.getBayar());
            ps.setInt(6, t.getKembalian());
            ps.executeUpdate();
            try (ResultSet kunci = ps.getGeneratedKeys()) {
                kunci.next();
                return kunci.getInt(1);
            }
        }
    }

    private void insertDetail(Connection c, int idTransaksi, DetailTransaksi d) throws SQLException {
        String sql = "INSERT INTO detail_transaksi (id_transaksi, id_menu, nama_menu, harga, jumlah, subtotal) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idTransaksi);
            ps.setInt(2, d.getIdMenu());
            ps.setString(3, d.getNamaMenu());
            ps.setInt(4, d.getHarga());
            ps.setInt(5, d.getJumlah());
            ps.setInt(6, d.getSubtotal());
            ps.executeUpdate();
        }
    }

    /** Kurangi stok. Kalau nggak ada baris yang berubah, stoknya kurang. */
    private void kurangiStok(Connection c, DetailTransaksi d) throws SQLException, StokTidakCukupException {
        String sql = "UPDATE menu SET stok = stok - ? WHERE id = ? AND stok >= ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, d.getJumlah());
            ps.setInt(2, d.getIdMenu());
            ps.setInt(3, d.getJumlah());
            if (ps.executeUpdate() == 0) {
                throw new StokTidakCukupException("Stok " + d.getNamaMenu() + " nggak cukup, transaksi dibatalin.");
            }
        }
    }

    private Transaksi baris(ResultSet rs) throws SQLException {
        Transaksi t = new Transaksi();
        t.setId(rs.getInt("id"));
        t.setKode(rs.getString("kode"));
        t.setTanggal(rs.getTimestamp("tanggal").toLocalDateTime());
        t.setIdKasir(rs.getInt("id_kasir"));
        t.setTotal(rs.getInt("total"));
        t.setBayar(rs.getInt("bayar"));
        t.setKembalian(rs.getInt("kembalian"));
        return t;
    }

    private void batal(Connection c) {
        if (c != null) {
            try {
                c.rollback();
            } catch (SQLException ignored) {
                // koneksi udah putus, nggak ada yang bisa di-rollback
            }
        }
    }

    private void tutup(Connection c) {
        if (c != null) {
            try {
                c.setAutoCommit(true);
                c.close();
            } catch (SQLException ignored) {
                // nggak ada yang bisa dilakuin kalau nutup koneksi aja gagal
            }
        }
    }
}
