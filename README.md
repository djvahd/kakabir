# Kakabir: Kasir Kantin Biru

Proyek Praktikum Pemrograman Berorientasi Objek 2026.

**Kelompok BadGenius**

| Nama | NPM |
|---|---|
| Farrel Athariz Saffa | 140810250026 |
| Jovanic Adriel Harjanto | 140810250050 |
| Mochammad Ziyad Zayid Arully | 140810250065 |

**Teknologi:** Java (Maven), Swing, MySQL (Laragon), JDBC + HikariCP.
Materi dibatasi pada slide praktikum minggu 1 sampai 9.

---

## 1. Ide dan Deskripsi

Kantin Biru masih mencatat pesanan dan menghitung uang secara manual, sehingga rawan salah hitung dan stok sulit dipantau. **Kakabir** adalah aplikasi desktop kasir sederhana yang membantu pencatatan menu, transaksi penjualan, dan riwayat penjualan.

**Pengguna aplikasi**

| Peran | Hak akses |
|---|---|
| Admin | Kelola menu (tambah, ubah, hapus), lihat riwayat transaksi |
| Kasir | Melayani transaksi, lihat riwayat transaksi |

## 2. Fungsionalitas

| Kode | Fitur | Deskripsi |
|---|---|---|
| F1 | Login | Masuk dengan username dan password. Gagal login menampilkan pesan dari exception. Setelah masuk, tab yang tampil mengikuti peran. |
| F2 | Kelola menu | Admin menambah, mengubah, menghapus, dan melihat menu (nama, kategori, harga, stok) pada `JTable`. Input divalidasi (harga dan stok tidak boleh negatif, nama wajib diisi). |
| F3 | Transaksi | Kasir memilih menu dan jumlah lalu menambahkannya ke keranjang. Keranjang bisa dihapus per baris. Total dihitung otomatis, kasir mengisi uang bayar, kembalian dihitung otomatis. |
| F4 | Simpan transaksi | Menyimpan transaksi dan detailnya, lalu mengurangi stok menu. Semuanya dalam satu transaksi database (commit atau rollback). Stok tidak cukup atau uang bayar kurang ditolak lewat exception. |
| F5 | Riwayat | Daftar transaksi (kode, tanggal, kasir, total). Memilih satu baris menampilkan detail itemnya. |
| F6 | Logout | Kembali ke layar login. |

## 3. Alur Aplikasi

```mermaid
flowchart TD
    A([Mulai]) --> B[LoginFrame]
    B --> C{Username dan password valid?}
    C -- Tidak --> D[LoginGagalException, tampil pesan] --> B
    C -- Ya --> E[DashboardFrame]
    E --> F{Peran}
    F -- Admin --> G[Tab Menu + Tab Riwayat]
    F -- Kasir --> H[Tab Transaksi + Tab Riwayat]
    G --> I[Tambah / ubah / hapus menu] --> G
    H --> J[Pilih menu dan jumlah] --> K[Tambah ke keranjang]
    K --> L{Tambah item lagi?}
    L -- Ya --> J
    L -- Tidak --> M[Isi uang bayar, klik Bayar]
    M --> N{Stok cukup dan bayar >= total?}
    N -- Tidak --> O[Exception, tampil pesan] --> H
    N -- Ya --> P[Simpan transaksi, kurangi stok, commit] --> Q[Tampil kembalian, kosongkan keranjang] --> H
    G --> R[Logout]
    H --> R
    R --> B
```

**Alur menyimpan transaksi (F4):**

1. `TransaksiPanel` membuat objek `Transaksi` dari `Keranjang`.
2. `TransaksiDaoJdbc.simpan()` membuka koneksi dari pool dan `setAutoCommit(false)`.
3. Insert baris `transaksi`, lalu insert tiap `detail_transaksi`.
4. Untuk tiap item: `UPDATE menu SET stok = stok - ? WHERE id = ? AND stok >= ?`. Jika tidak ada baris terubah, lempar `StokTidakCukupException`.
5. Semua berhasil: `commit()`. Ada kegagalan: `rollback()`. Koneksi dikembalikan ke pool di blok `finally`.

## 4. Rancangan Database

![ERD Kakabir (dbdiagram.io)](img/erd.png)

## 5. Rancangan Class

### 5.1 Struktur paket

```
com.badgenius.kakabir
├── Main.java
├── db         Koneksi                    (HikariCP, singleton)
├── model      Pengguna, Admin, Kasir, Menu, Keranjang, ItemKeranjang,
│              Transaksi, DetailTransaksi, Kategori (enum)
├── dao        CrudDao<T>, MenuDao, PenggunaDao, TransaksiDao
│              + implementasi *DaoJdbc
├── exception  KasirException, LoginGagalException,
│              StokTidakCukupException, DataTidakValidException
└── view       LoginFrame, DashboardFrame, MenuPanel, TransaksiPanel, RiwayatPanel
```

### 5.2 Diagram class

```mermaid
classDiagram
    class Pengguna {
        <<abstract>>
        -int id
        -String username
        -String password
        -String nama
        +getRole() String*
        +getters/setters
    }
    class Admin {
        +getRole() String
    }
    class Kasir {
        +getRole() String
    }
    Pengguna <|-- Admin
    Pengguna <|-- Kasir

    class Menu {
        -int id
        -String nama
        -Kategori kategori
        -int harga
        -int stok
        +setHarga(int)
        +setStok(int)
        +getters/setters
    }
    class Kategori {
        <<enumeration>>
        MAKANAN
        MINUMAN
    }
    Menu --> Kategori

    class ItemKeranjang {
        -Menu menu
        -int jumlah
        +getSubtotal() int
    }
    class Keranjang {
        -List~ItemKeranjang~ items
        +tambah(Menu, int)
        +hapus(int index)
        +getTotal() int
        +kosongkan()
    }
    Keranjang "1" o-- "*" ItemKeranjang
    ItemKeranjang --> Menu

    class Transaksi {
        -int id
        -String kode
        -LocalDateTime tanggal
        -int idKasir
        -int total
        -int bayar
        -int kembalian
        -List~DetailTransaksi~ detail
    }
    class DetailTransaksi {
        -int idMenu
        -String namaMenu
        -int harga
        -int jumlah
        -int subtotal
    }
    Transaksi "1" *-- "*" DetailTransaksi

    class CrudDao~T~ {
        <<interface>>
        +findAll() List~T~
        +findById(int) Optional~T~
        +insert(T)
        +update(T)
        +delete(int)
    }
    class MenuDao {
        <<interface>>
    }
    class PenggunaDao {
        <<interface>>
        +findByUsername(String) Optional~Pengguna~
    }
    class TransaksiDao {
        <<interface>>
        +simpan(Transaksi)
        +findDetail(int) List~DetailTransaksi~
    }
    CrudDao <|-- MenuDao
    CrudDao <|-- PenggunaDao
    CrudDao <|-- TransaksiDao
    MenuDao <|.. MenuDaoJdbc
    PenggunaDao <|.. PenggunaDaoJdbc
    TransaksiDao <|.. TransaksiDaoJdbc

    class Koneksi {
        -HikariDataSource ds
        +getConnection() Connection
    }
    MenuDaoJdbc ..> Koneksi
    PenggunaDaoJdbc ..> Koneksi
    TransaksiDaoJdbc ..> Koneksi

    class KasirException
    KasirException <|-- LoginGagalException
    KasirException <|-- StokTidakCukupException
    KasirException <|-- DataTidakValidException

    class LoginFrame
    class DashboardFrame
    class MenuPanel
    class TransaksiPanel
    class RiwayatPanel
    LoginFrame ..> PenggunaDao
    DashboardFrame *-- MenuPanel
    DashboardFrame *-- TransaksiPanel
    DashboardFrame *-- RiwayatPanel
    MenuPanel ..> MenuDao
    TransaksiPanel ..> MenuDao
    TransaksiPanel ..> TransaksiDao
    TransaksiPanel --> Keranjang
    RiwayatPanel ..> TransaksiDao
```

### 5.3 Peran tiap bagian

| Bagian | Tanggung jawab |
|---|---|
| `model` | Menyimpan data dan aturan sederhana (validasi di setter, hitung subtotal dan total). Atribut `private`. |
| `dao` | Satu-satunya tempat yang memakai SQL. Hasil pencarian tunggal dikembalikan sebagai `Optional`. |
| `exception` | Kesalahan aplikasi bertipe checked, pesannya langsung ditampilkan lewat `JOptionPane`. |
| `db.Koneksi` | Membuat pool koneksi sekali, dipakai semua DAO. |
| `view` | Hanya urusan tampilan dan event. Logika data dipanggil lewat DAO dan model. |

## 6. Rencana Pengerjaan

| Tahap | Isi | Issue / PR |
|---|---|---|
| 1 | Scaffolding Maven, `schema.sql`, `Koneksi` (HikariCP), `Main` | Setup proyek |
| 2 | Model, exception, DAO pengguna, `LoginFrame` | F1 |
| 3 | `Menu`, `MenuDao`, `MenuPanel` | F2 |
| 4 | `Keranjang`, `TransaksiPanel`, `TransaksiDao.simpan` | F3, F4 |
| 5 | `RiwayatPanel`, `DashboardFrame` dengan hak akses per peran, logout | F5, F6 |
| 6 | README, tangkapan layar, pengecekan akhir | Dokumentasi |

## 7. Cara Menjalankan

1. Nyalakan **Laragon** (Start All), pastikan MySQL jalan di port 3306 (user `root`, tanpa password).
2. Impor skema dan data awal. Aman diulang, data tidak dobel:
   ```
   mysql -uroot < sql/schema.sql
   ```
3. Compile dan jalankan:
   ```
   mvn compile exec:java
   ```
   Atau buka folder proyek di NetBeans (File > Open Project), lalu Run pada `Main`.
4. Akun contoh: `admin` / `admin123` (Admin), `kasir` / `kasir123` (Kasir).

Semua layar (`LoginFrame`, `DashboardFrame`, `MenuPanel`, `TransaksiPanel`, `RiwayatPanel`) dibuat sebagai **form NetBeans**: tiap kelas punya pasangan `.java` dan `.form` di paket `view`, jadi bisa dibuka di tab **Design** dan komponennya bisa digeser atau diubah propertinya. Jangan edit isi blok `initComponents` lewat tab Source, ubah lewat Design.

## 8. Tangkapan Layar

| Login | Menu (Admin) |
|---|---|
| ![Login](img/login.png) | ![Menu](img/menu.png) |

| Transaksi (Kasir) | Riwayat |
|---|---|
| ![Transaksi](img/transaksi.png) | ![Riwayat](img/riwayat.png) |

## 9. Pengujian

Dites ke MySQL 8.4 sungguhan lewat objek Swing asli (tombol diklik lewat kode, dialog `JOptionPane` dijawab otomatis, pesannya dicatat). Hasil sebenarnya sama dengan yang diharapkan di semua skenario.

| No | Skenario | Langkah | Hasil yang diharapkan | Hasil sebenarnya |
|---|---|---|---|---|
| 1 | Login password salah | Username `kasir`, password `salah`, klik Masuk | Pesan "Username atau password salah." | Sesuai |
| 2 | Login kasir benar | `kasir` / `kasir123` | Dashboard dengan tab Transaksi dan Riwayat | Sesuai |
| 3 | Login admin benar | `admin` / `admin123` | Dashboard dengan tab Menu dan Riwayat | Sesuai |
| 4 | Tambah menu, harga bukan angka | Harga `abc`, klik Tambah | Pesan "Harga harus berupa angka." | Sesuai |
| 5 | Tambah menu, harga minus | Harga `-5` | Pesan "Harga nggak boleh minus." | Sesuai |
| 6 | Tambah menu, nama kosong | Nama berisi spasi | Pesan "Nama menu wajib diisi." | Sesuai |
| 7 | Tambah menu valid | Nama `Tes UI`, harga `5000`, stok `5` | Baris baru muncul di tabel | Sesuai |
| 8 | Ubah menu | Klik baris, harga jadi `7000`, klik Ubah | Harga di tabel jadi 7000 | Sesuai |
| 9 | Hapus menu | Pilih baris, klik Hapus, jawab Yes | Baris hilang dari tabel | Sesuai |
| 10 | Hapus menu yang sudah terjual | Pilih Es Teh, klik Hapus | Pesan "Menu ini udah pernah kejual, jadi nggak bisa dihapus." | Sesuai |
| 11 | Transaksi: tambah ke keranjang | Es Teh jumlah 2 | 1 baris, total Rp 8.000 | Sesuai |
| 12 | Transaksi: stok kurang | Jumlah 999 | Pesan stok tinggal sekian, keranjang tidak berubah | Sesuai |
| 13 | Transaksi: jumlah bukan angka | Jumlah `abc` | Pesan "Jumlah harus berupa angka." | Sesuai |
| 14 | Transaksi: bayar kurang | Bayar `1000` untuk total 8.000 | Pesan uang bayar kurang, keranjang tetap ada | Sesuai |
| 15 | Transaksi normal | Bayar `10000` | Pesan tersimpan dengan kembalian Rp 2.000, keranjang kosong, stok berkurang | Sesuai |
| 16 | Rollback | Item pertama cukup, stok item kedua habis saat bayar | Transaksi dibatalkan, stok item pertama dan jumlah transaksi tidak berubah | Sesuai |
| 17 | Riwayat | Buka tab Riwayat, pilih baris pertama | Transaksi tampil, detail berisi Es Teh | Sesuai |
| 18 | Logout | Klik Logout | Kembali ke LoginFrame, dashboard tertutup | Sesuai |

Belum dites: tampilan di Design view NetBeans dan jalannya aplikasi di layar komputer lain (hanya dites di satu mesin).
