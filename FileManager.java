import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FileManager {

    private static final Path FILE_MENU = Path.of("data", "menu.txt");
    private static final Path FOLDER_STRUK = Path.of("data", "struk");

    public static boolean menuTersimpan() {
        return Files.exists(FILE_MENU);
    }

    public static void simpanMenu(Menu menu) throws IOException {
        List<String> baris = new ArrayList<>();

        for (MenuItem item : menu.getDaftarMenu()) {
            if (item instanceof Makanan) {
                Makanan makanan = (Makanan) item;
                baris.add(
                    String.join(
                        "|",
                        "Makanan",
                        makanan.getNama(),
                        Double.toString(makanan.getHarga()),
                        makanan.getKategori(),
                        makanan.getJenisMakanan()
                    )
                );
            } else if (item instanceof Minuman) {
                Minuman minuman = (Minuman) item;
                baris.add(
                    String.join(
                        "|",
                        "Minuman",
                        minuman.getNama(),
                        Double.toString(minuman.getHarga()),
                        minuman.getKategori(),
                        minuman.getJenisMinuman()
                    )
                );
            } else if (item instanceof Diskon) {
                Diskon diskon = (Diskon) item;
                baris.add(
                    String.join(
                        "|",
                        "Diskon",
                        diskon.getNama(),
                        "0",
                        diskon.getKategori(),
                        Double.toString(diskon.getPersentase())
                    )
                );
            }
        }

        Files.createDirectories(FILE_MENU.getParent());
        Files.write(
            FILE_MENU,
            baris,
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    public static Menu muatMenu() throws IOException {
        Menu menu = new Menu();
        List<String> baris = Files.readAllLines(FILE_MENU, StandardCharsets.UTF_8);

        for (int i = 0; i < baris.size(); i++) {
            String teks = baris.get(i).trim();
            if (teks.isEmpty()) {
                continue;
            }
            String[] kolom = teks.split("\\|", -1);
            try {
                if (kolom.length != 5) {
                    throw new IllegalArgumentException("Jumlah kolom tidak sesuai.");
                }
                String tipe = kolom[0];
                String nama = kolom[1];
                double harga = Double.parseDouble(kolom[2]);
                String kategori = kolom[3];
                String detail = kolom[4];

                switch (tipe) {
                    case "Makanan":
                        if (!kategori.equals("Makanan")) {
                            throw new IllegalArgumentException("Kategori makanan tidak sesuai.");
                        }
                        menu.tambahItem(new Makanan(nama, harga, detail));
                        break;
                    case "Minuman":
                        if (!kategori.equals("Minuman")) {
                            throw new IllegalArgumentException("Kategori minuman tidak sesuai.");
                        }
                        menu.tambahItem(new Minuman(nama, harga, detail));
                        break;
                    case "Diskon":
                        if (!kategori.equals("Diskon") || harga != 0) {
                            throw new IllegalArgumentException("Data diskon tidak sesuai");
                        }
                        menu.tambahItem(new Diskon(nama, Double.parseDouble(detail)));
                        break;
                    default:
                        throw new IllegalArgumentException("Tipe item tidak dikenal.");
                }
            } catch (IllegalArgumentException e) {
                throw new IOException("Data tidak valid pada menu.txt baris " + (i + 1) + ": " + e.getMessage(), e);
            }
        }
        return menu;
    }

    public static Path simpanStruk(Pesanan pesanan) throws IOException {
        Files.createDirectories(FOLDER_STRUK);
        String waktu = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String kode = UUID.randomUUID().toString().substring(0, 6);
        Path tujuan = FOLDER_STRUK.resolve("struk-" + waktu + "-" + kode + ".txt");
        Files.writeString(tujuan, pesanan.buatStruk(), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        return tujuan;
    }

    public static List<String> daftarStruk() throws IOException {
        List<String> hasil = new ArrayList<>();
        if (!Files.isDirectory(FOLDER_STRUK)) {
            return hasil;
        }
        try (DirectoryStream<Path> berkas = Files.newDirectoryStream(FOLDER_STRUK, "*.txt")) {
            for (Path berkasStruk : berkas) {
                hasil.add(berkasStruk.getFileName().toString());
            }
        }
        Collections.sort(hasil);
        return hasil;
    }

    public static String bacaStruk(String namaBerkas) throws IOException {
        Path target = FOLDER_STRUK.resolve(namaBerkas).normalize();
        if (!target.startsWith(FOLDER_STRUK)) {
            throw new IOException("Nama berkas tidak valid.");
        }
        return Files.readString(target, StandardCharsets.UTF_8);
    }
}
