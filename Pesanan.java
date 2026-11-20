import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Pesanan {

    private final String namaPelanggan;
    private final ArrayList<MenuItem> daftarPesanan = new ArrayList<>();
    private Diskon diskon;

    public Pesanan(String namaPelanggan) {
        this.namaPelanggan = MenuItem.wajibTeks(namaPelanggan, "Nama pelanggan");
    }

    public void tambahPesanan(MenuItem item, int jumlah) {
        if (item == null || item instanceof Diskon) {
            throw new IllegalArgumentException("Pilih makanan atau minuman untuk dipesan.");
        }
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah pesanan minimal 1.");
        }
        for (int i = 0; i < jumlah; i++) {
            daftarPesanan.add(item);
        }
    }

    public void setDiskon(Diskon diskon) {
        this.diskon = diskon;
    }

    public int getJumlahItem() {
        return daftarPesanan.size();
    }

    public double hitungSubtotal() {
        double subtotal = 0;
        for (MenuItem item : daftarPesanan) {
            subtotal += item.getHarga();
        }
        return subtotal;
    }

    public double hitungPotongan() {
        return diskon == null ? 0 : (hitungSubtotal() * diskon.getPersentase()) / 100;
    }

    public double hitungTotal() {
        return hitungSubtotal() - hitungPotongan();
    }

    public String buatStruk() {
        if (daftarPesanan.isEmpty()) {
            throw new IllegalArgumentException("Pesanan masih kosong.");
        }

        StringBuilder struk = new StringBuilder();
        struk.append("\n========================\n");
        struk.append("         RESTORAN SEDERHANA\n");
        struk.append("========================\n");
        struk.append("Pelanggan : ").append(namaPelanggan).append("\n");
        struk
            .append("Tanggal : ")
            .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
            .append("\n");
        struk.append("------------------------\n");

        Map<MenuItem, Integer> jumlahPerItem = new LinkedHashMap<>();
        for (MenuItem item : daftarPesanan) {
            jumlahPerItem.put(item, jumlahPerItem.getOrDefault(item, 0) + 1);
        }
        for (Map.Entry<MenuItem, Integer> baris : jumlahPerItem.entrySet()) {
            MenuItem item = baris.getKey();
            int jumlah = baris.getValue();
            struk.append(
                String.format(
                    "%-18s %2d x %8.0f = %9.0f%n",
                    item.getNama(),
                    jumlah,
                    item.getHarga(),
                    item.getHarga() * jumlah
                )
            );
        }

        struk.append("------------------------\n");
        struk.append(String.format("%-29s %12.0f%n", "Subtotal", hitungSubtotal()));
        if (diskon != null) {
            struk.append(
                String.format(
                    "Diskon %-17s %12.0f%n",
                    String.format("(%.0f%%)", diskon.getPersentase()),
                    hitungPotongan()
                )
            );
        }
        struk.append("------------------------\n");
        struk.append(String.format("%-29s %12.0f%n", "TOTAL", hitungTotal()));
        struk.append("========================\n");
        struk.append("         Terima kasih!\n");
        return struk.toString();
    }
}
