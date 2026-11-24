package com.badgenius.kakabir.model;

/**
 * Satu baris isi transaksi. Nama dan harga menu disalin saat transaksi dibuat,
 * jadi riwayat tetap bener walau menunya diubah atau harganya naik.
 */
public class DetailTransaksi {

    private int id;
    private int idMenu;
    private String namaMenu;
    private int harga;
    private int jumlah;
    private int subtotal;

    public DetailTransaksi(int id, int idMenu, String namaMenu, int harga, int jumlah, int subtotal) {
        this.id = id;
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.harga = harga;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
}
