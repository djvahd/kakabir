package com.badgenius.kakabir.dao;

import com.badgenius.kakabir.db.Koneksi;
import com.badgenius.kakabir.exception.KasirException;
import com.badgenius.kakabir.model.Admin;
import com.badgenius.kakabir.model.Kasir;
import com.badgenius.kakabir.model.Pengguna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PenggunaDaoJdbc implements PenggunaDao {

    private static final String KOLOM = "id, username, password, nama, role";

    @Override
    public List<Pengguna> findAll() throws KasirException {
        String sql = "SELECT " + KOLOM + " FROM pengguna ORDER BY username";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Pengguna> hasil = new ArrayList<>();
            while (rs.next()) {
                hasil.add(baris(rs));
            }
            return hasil;
        } catch (SQLException e) {
            throw new KasirException("Gagal ngambil data pengguna: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Pengguna> findById(int id) throws KasirException {
        return cari("id = ?", String.valueOf(id));
    }

    @Override
    public Optional<Pengguna> findByUsername(String username) throws KasirException {
        return cari("username = ?", username);
    }

    @Override
    public void insert(Pengguna p) throws KasirException {
        String sql = "INSERT INTO pengguna (username, password, nama, role) VALUES (?, ?, ?, ?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getUsername());
            ps.setString(2, p.getPassword());
            ps.setString(3, p.getNama());
            ps.setString(4, p.getRole());
            ps.executeUpdate();
            try (ResultSet kunci = ps.getGeneratedKeys()) {
                if (kunci.next()) {
                    p.setId(kunci.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new KasirException("Gagal nambah pengguna: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Pengguna p) throws KasirException {
        String sql = "UPDATE pengguna SET username = ?, password = ?, nama = ?, role = ? WHERE id = ?";
        try (Connection c = Koneksi.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getUsername());
            ps.setString(2, p.getPassword());
            ps.setString(3, p.getNama());
            ps.setString(4, p.getRole());
            ps.setInt(5, p.getId());
            if (ps.executeUpdate() == 0) {
                throw new KasirException("Pengguna nggak ketemu.");
            }
        } catch (SQLException e) {
            throw new KasirException("Gagal ngubah pengguna: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) throws KasirException {
        String sql = "DELETE FROM pengguna WHERE id = ?";
        try (Connection c = Koneksi.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new KasirException("Pengguna nggak ketemu.");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                throw new KasirException("Pengguna ini udah punya transaksi, jadi nggak bisa dihapus.", e);
            }
            throw new KasirException("Gagal ngehapus pengguna: " + e.getMessage(), e);
        }
    }

    private Optional<Pengguna> cari(String kondisi, String nilai) throws KasirException {
        String sql = "SELECT " + KOLOM + " FROM pengguna WHERE " + kondisi;
        try (Connection c = Koneksi.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nilai);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(baris(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new KasirException("Gagal ngambil data pengguna: " + e.getMessage(), e);
        }
    }

    /** Polimorfisme: baris ADMIN jadi objek Admin, selain itu Kasir. */
    private Pengguna baris(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String nama = rs.getString("nama");
        if ("ADMIN".equals(rs.getString("role"))) {
            return new Admin(id, username, password, nama);
        }
        return new Kasir(id, username, password, nama);
    }
}
