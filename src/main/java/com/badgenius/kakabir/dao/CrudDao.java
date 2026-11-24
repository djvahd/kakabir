package com.badgenius.kakabir.dao;

import com.badgenius.kakabir.exception.KasirException;

import java.util.List;
import java.util.Optional;

/**
 * Operasi dasar ke database. Error SQL dibungkus jadi KasirException,
 * jadi view cukup nangkep satu jenis exception.
 */
public interface CrudDao<T> {

    List<T> findAll() throws KasirException;

    Optional<T> findById(int id) throws KasirException;

    void insert(T data) throws KasirException;

    void update(T data) throws KasirException;

    void delete(int id) throws KasirException;
}
