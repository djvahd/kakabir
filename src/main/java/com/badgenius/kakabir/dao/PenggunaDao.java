package com.badgenius.kakabir.dao;

import com.badgenius.kakabir.exception.KasirException;
import com.badgenius.kakabir.model.Pengguna;

import java.util.Optional;

public interface PenggunaDao extends CrudDao<Pengguna> {

    /** Cari pengguna lewat username. Kosong kalau nggak ada. */
    Optional<Pengguna> findByUsername(String username) throws KasirException;
}
