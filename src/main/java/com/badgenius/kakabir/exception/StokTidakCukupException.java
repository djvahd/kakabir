package com.badgenius.kakabir.exception;

/**
 * Stok menu kurang dari jumlah yang diminta.
 */
public class StokTidakCukupException extends KasirException {

    public StokTidakCukupException(String pesan) {
        super(pesan);
    }
}
