package com.badgenius.kakabir.exception;

/**
 * Induk semua error aplikasi. Pesannya langsung ditampilin ke kasir.
 */
public class KasirException extends Exception {

    public KasirException(String pesan) {
        super(pesan);
    }

    public KasirException(String pesan, Throwable penyebab) {
        super(pesan, penyebab);
    }
}
