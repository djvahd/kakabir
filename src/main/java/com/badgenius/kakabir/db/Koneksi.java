package com.badgenius.kakabir.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Pool koneksi ke database kakabir. Pool dibikin sekali, dipakai semua DAO.
 */
public class Koneksi {

    private static final String URL = "jdbc:mysql://localhost:3306/kakabir";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static HikariDataSource ds;

    private Koneksi() {
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (ds == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(URL);
            config.setUsername(USER);
            config.setPassword(PASSWORD);
            config.setMaximumPoolSize(5);
            ds = new HikariDataSource(config);
        }
        return ds.getConnection();
    }

    public static synchronized void tutup() {
        if (ds != null) {
            ds.close();
            ds = null;
        }
    }
}
