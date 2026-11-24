package com.badgenius.kakabir.dao;

import com.badgenius.kakabir.db.Koneksi;
import com.badgenius.kakabir.exception.KasirException;
import com.badgenius.kakabir.model.Kategori;
import com.badgenius.kakabir.model.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuDaoJdbc implements MenuDao {

    @Override
    public List<Menu> findAll() throws KasirException {
        String sql = "SELECT id, nama, kategori, harga, stok FROM menu ORDER BY nama";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Menu> hasil = new ArrayList<>();
            while (rs.next()) {
                hasil.add(baris(rs));
            }
            return hasil;
        } catch (SQLException e) {
            throw new KasirException("Gagal ngambil data menu: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Menu> findById(int id) throws KasirException {
        String sql = "SELECT id, nama, kategori, harga, stok FROM menu WHERE id = ?";
        try (Connection c = Koneksi.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(baris(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new KasirException("Gagal ngambil data menu: " + e.getMessage(), e);
        }
    }

    @Override
    public void insert(Menu menu) throws KasirException {
        String sql = "INSERT INTO menu (nama, kategori, harga, stok) VALUES (?, ?, ?, ?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, menu.getNama());
            ps.setString(2, menu.getKategori().name());
            ps.setInt(3, menu.getHarga());
            ps.setInt(4, menu.getStok());
            ps.executeUpdate();
            try (ResultSet kunci = ps.getGeneratedKeys()) {
                if (kunci.next()) {
                    menu.setId(kunci.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new KasirException("Gagal nambah menu: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Menu menu) throws KasirException {
        String sql = "UPDATE menu SET nama = ?, kategori = ?, harga = ?, stok = ? WHERE id = ?";
        try (Connection c = Koneksi.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, menu.getNama());
            ps.setString(2, menu.getKategori().name());
            ps.setInt(3, menu.getHarga());
            ps.setInt(4, menu.getStok());
            ps.setInt(5, menu.getId());
            if (ps.executeUpdate() == 0) {
                throw new KasirException("Menu nggak ketemu, mungkin udah dihapus.");
            }
        } catch (SQLException e) {
            throw new KasirException("Gagal ngubah menu: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) throws KasirException {
        String sql = "DELETE FROM menu WHERE id = ?";
        try (Connection c = Koneksi.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new KasirException("Menu nggak ketemu, mungkin udah dihapus.");
            }
        } catch (SQLException e) {
            // 1451 = masih dipakai foreign key (menu udah pernah kejual)
            if (e.getErrorCode() == 1451) {
                throw new KasirException("Menu ini udah pernah kejual, jadi nggak bisa dihapus.", e);
            }
            throw new KasirException("Gagal ngehapus menu: " + e.getMessage(), e);
        }
    }

    private Menu baris(ResultSet rs) throws SQLException, KasirException {
        return new Menu(rs.getInt("id"), rs.getString("nama"),
                Kategori.valueOf(rs.getString("kategori")), rs.getInt("harga"), rs.getInt("stok"));
    }
}
