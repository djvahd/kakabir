package com.badgenius.kakabir.model;

import com.badgenius.kakabir.exception.DataTidakValidException;

/**
 * Satu menu yang dijual di kantin. Validasi ada di setter.
 */
public class Menu {

    private int id;
    private String nama;
    private Kategori kategori;
    private int harga;
    private int stok;

    public Menu() {
    }

    /** Buat menu baru yang belum punya id (id dikasih database). */
    public Menu(String nama, Kategori kategori, int harga, int stok) throws DataTidakValidException {
        setNama(nama);
        setKategori(kategori);
        setHarga(harga);
        setStok(stok);
    }

    public Menu(int id, String nama, Kategori kategori, int harga, int stok) throws DataTidakValidException {
        this(nama, kategori, harga, stok);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) throws DataTidakValidException {
        if (nama == null || nama.trim().isEmpty()) {
            throw new DataTidakValidException("Nama menu wajib diisi.");
        }
        this.nama = nama.trim();
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) throws DataTidakValidException {
        if (kategori == null) {
            throw new DataTidakValidException("Kategori menu wajib dipilih.");
        }
        this.kategori = kategori;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) throws DataTidakValidException {
        if (harga < 0) {
            throw new DataTidakValidException("Harga nggak boleh minus.");
        }
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) throws DataTidakValidException {
        if (stok < 0) {
            throw new DataTidakValidException("Stok nggak boleh minus.");
        }
        this.stok = stok;
    }

    @Override
    public String toString() {
        return nama;
    }
}
