-- Skema database Kakabir (ngikutin img/erd.png).
-- Jalanin: mysql -uroot < sql/schema.sql  (Laragon harus nyala)
-- Aman dijalanin berulang: tabel dan data awal nggak dobel.

CREATE DATABASE IF NOT EXISTS kakabir;
USE kakabir;

CREATE TABLE IF NOT EXISTS pengguna (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nama     VARCHAR(100) NOT NULL,
    role     ENUM('ADMIN', 'KASIR') NOT NULL
);

CREATE TABLE IF NOT EXISTS menu (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nama     VARCHAR(100) NOT NULL,
    kategori ENUM('MAKANAN', 'MINUMAN') NOT NULL,
    harga    INT NOT NULL,
    stok     INT NOT NULL
);

CREATE TABLE IF NOT EXISTS transaksi (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    kode      VARCHAR(20) NOT NULL UNIQUE,
    tanggal   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_kasir  INT NOT NULL,
    total     INT NOT NULL,
    bayar     INT NOT NULL,
    kembalian INT NOT NULL,
    FOREIGN KEY (id_kasir) REFERENCES pengguna (id)
);

CREATE TABLE IF NOT EXISTS detail_transaksi (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi INT NOT NULL,
    id_menu      INT NOT NULL,
    nama_menu    VARCHAR(100) NOT NULL,
    harga        INT NOT NULL,
    jumlah       INT NOT NULL,
    subtotal     INT NOT NULL,
    FOREIGN KEY (id_transaksi) REFERENCES transaksi (id) ON DELETE CASCADE,
    FOREIGN KEY (id_menu) REFERENCES menu (id)
);

-- Data awal: akun contoh
INSERT IGNORE INTO pengguna (username, password, nama, role) VALUES
    ('admin', 'admin123', 'Admin Kantin', 'ADMIN'),
    ('kasir', 'kasir123', 'Kasir Kantin', 'KASIR');

-- Data awal: menu contoh (cuma diisi kalau tabel menu masih kosong)
INSERT INTO menu (nama, kategori, harga, stok)
SELECT * FROM (VALUES
    ROW('Nasi Goreng', 'MAKANAN', 15000, 20),
    ROW('Mie Ayam',    'MAKANAN', 12000, 20),
    ROW('Ayam Geprek', 'MAKANAN', 14000, 15),
    ROW('Es Teh',      'MINUMAN',  4000, 50),
    ROW('Es Jeruk',    'MINUMAN',  5000, 50),
    ROW('Kopi Hitam',  'MINUMAN',  6000, 30)
) AS awal
WHERE NOT EXISTS (SELECT 1 FROM menu);
