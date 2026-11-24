package com.badgenius.kakabir.model;

import com.badgenius.kakabir.exception.DataTidakValidException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Satu penjualan: hasil bayar dari isi keranjang.
 */
public class Transaksi {

    private int id;
    private String kode;
    private LocalDateTime tanggal;
    private int idKasir;
    private int total;
    private int bayar;
    private int kembalian;
    private List<DetailTransaksi> detail = new ArrayList<>();

    public Transaksi() {
    }

    /**
     * Ubah keranjang jadi transaksi. Bayar kurang dari total ditolak.
     * Kode dibiarin kosong, nanti DAO yang ngisi.
     */
    public static Transaksi dariKeranjang(Keranjang k, int idKasir, int bayar) throws DataTidakValidException {
        if (k.isKosong()) {
            throw new DataTidakValidException("Keranjang masih kosong.");
        }
        int total = k.getTotal();
        if (bayar < total) {
            throw new DataTidakValidException("Uang bayar kurang " + (total - bayar) + " dari total " + total + ".");
        }
        Transaksi t = new Transaksi();
        t.idKasir = idKasir;
        t.total = total;
        t.bayar = bayar;
        t.kembalian = bayar - total;
        t.tanggal = LocalDateTime.now();
        for (ItemKeranjang item : k.getItems()) {
            Menu m = item.getMenu();
            t.detail.add(new DetailTransaksi(0, m.getId(), m.getNama(), m.getHarga(),
                    item.getJumlah(), item.getSubtotal()));
        }
        return t;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public int getIdKasir() {
        return idKasir;
    }

    public void setIdKasir(int idKasir) {
        this.idKasir = idKasir;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getBayar() {
        return bayar;
    }

    public void setBayar(int bayar) {
        this.bayar = bayar;
    }

    public int getKembalian() {
        return kembalian;
    }

    public void setKembalian(int kembalian) {
        this.kembalian = kembalian;
    }

    public List<DetailTransaksi> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailTransaksi> detail) {
        this.detail = detail;
    }
}
