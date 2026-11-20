public abstract class MenuItem {

    private final String nama;
    private final double harga;
    private final String kategori;

    public MenuItem(String nama, double harga, String kategori) {
        this.nama = wajibTeks(nama, "Nama");
        if (!Double.isFinite(harga) || harga < 0) {
            throw new IllegalArgumentException("Harga tidak boleh negatif.");
        }
        this.harga = harga;
        this.kategori = wajibTeks(kategori, "Kategori");
    }

    protected static String wajibTeks(String teks, String namaAtribut) {
        if (teks == null || teks.trim().isEmpty() || teks.contains("|")) {
            throw new IllegalArgumentException(namaAtribut + "tidak boleh kosong atau mengandung karakter |.");
        }
        return teks.trim();
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getKategori() {
        return kategori;
    }

    public abstract void tampilMenu();
}
