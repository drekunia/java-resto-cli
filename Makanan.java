public class Makanan extends MenuItem {

    private final String jenisMakanan;

    public Makanan(String nama, double harga, String jenisMakanan) {
        super(nama, harga, "Makanan");
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga makanan harus lebih dari nol.");
        }
        this.jenisMakanan = wajibTeks(jenisMakanan, "Jenis makanan");
    }

    public String getJenisMakanan() {
        return jenisMakanan;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("%-20s | %-10s | Rp%,10.0f | %s%n", getNama(), getKategori(), getHarga(), jenisMakanan);
    }
}
