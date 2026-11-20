public class Minuman extends MenuItem {

    private final String jenisMinuman;

    public Minuman(String nama, double harga, String jenisMinuman) {
        super(nama, harga, "Minuman");
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga minuman harus lebih dari nol.");
        }
        this.jenisMinuman = wajibTeks(jenisMinuman, "Jenis minuman");
    }

    public String getJenisMinuman() {
        return jenisMinuman;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("%-20s | %-10s | Rp%,10.0f | %s%n", getNama(), getKategori(), getHarga(), jenisMinuman);
    }
}
