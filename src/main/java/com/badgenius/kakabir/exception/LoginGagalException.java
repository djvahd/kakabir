package com.badgenius.kakabir.exception;

/**
 * Username atau password salah.
 */
public class LoginGagalException extends KasirException {

    public LoginGagalException(String pesan) {
        super(pesan);
    }
}
