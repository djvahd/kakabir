package com.badgenius.kakabir.exception;

/**
 * Data yang diisi nggak valid (kosong, minus, dan sebagainya).
 */
public class DataTidakValidException extends KasirException {

    public DataTidakValidException(String pesan) {
        super(pesan);
    }
}
