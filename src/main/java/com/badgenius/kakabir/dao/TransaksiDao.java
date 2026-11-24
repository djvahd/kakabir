package com.badgenius.kakabir.dao;

import com.badgenius.kakabir.exception.KasirException;
import com.badgenius.kakabir.model.DetailTransaksi;
import com.badgenius.kakabir.model.Transaksi;

import java.util.List;

/**
 * DAO transaksi. Transaksi yang udah tersimpan nggak boleh diubah atau dihapus,
 * jadi update dan delete selalu ditolak.
 */
public interface TransaksiDao extends CrudDao<Transaksi> {

    /**
     * Simpan transaksi beserta detailnya dan kurangi stok, semuanya dalam satu
     * transaksi database (commit kalau sukses, rollback kalau gagal).
     */
    void simpan(Transaksi transaksi) throws KasirException;

    /** Isi (detail) dari satu transaksi. */
    List<DetailTransaksi> findDetail(int idTransaksi) throws KasirException;
}
