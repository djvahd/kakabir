package com.badgenius.kakabir.model;

import com.badgenius.kakabir.exception.DataTidakValidException;
import com.badgenius.kakabir.exception.StokTidakCukupException;

import java.util.ArrayList;
import java.util.List;

/**
 * Keranjang belanja kasir. Logikanya murni Java, nggak nyentuh database.
 */
public class Keranjang {

    private final List<ItemKeranjang> items = new ArrayList<>();

    /** Tambah menu sebanyak jumlah. Menu yang sama dijumlahin ke baris yang udah ada. */
    public void tambah(Menu menu, int jumlah) throws DataTidakValidException, StokTidakCukupException {
        if (jumlah <= 0) {
            throw new DataTidakValidException("Jumlah harus lebih dari 0.");
        }
        ItemKeranjang ada = cari(menu);
        int jumlahBaru = jumlah + (ada == null ? 0 : ada.getJumlah());
        if (jumlahBaru > menu.getStok()) {
            throw new StokTidakCukupException("Stok " + menu.getNama() + " tinggal " + menu.getStok() + ".");
        }
        if (ada == null) {
            items.add(new ItemKeranjang(menu, jumlah));
        } else {
            ada.setJumlah(jumlahBaru);
        }
    }

    /** Tambah 1 buah. */
    public void tambah(Menu menu) throws DataTidakValidException, StokTidakCukupException {
        tambah(menu, 1);
    }

    public void hapus(int index) {
        items.remove(index);
    }

    public int getTotal() {
        int total = 0;
        for (ItemKeranjang item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void kosongkan() {
        items.clear();
    }

    public boolean isKosong() {
        return items.isEmpty();
    }

    /** Salinan list, jadi pemanggil nggak bisa ngubah isi keranjang langsung. */
    public List<ItemKeranjang> getItems() {
        return new ArrayList<>(items);
    }

    private ItemKeranjang cari(Menu menu) {
        for (ItemKeranjang item : items) {
            if (item.getMenu().getId() == menu.getId()) {
                return item;
            }
        }
        return null;
    }
}
