import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static Menu menu = new Menu();
    private static Pesanan pesananAktif;

    public static void main(String[] args) {
        Locale.setDefault(Locale.forLanguageTag("id-ID"));
        siapkanMenuAwal();

        boolean berjalan = true;
        while (berjalan) {
            System.out.println("\n========================");
            System.out.println("         MANAJEMEN RESTORAN");
            System.out.println("========================");
            System.out.println("1. Tambah Item Menu");
            System.out.println("2. Tampilkan Menu Restoran");
            System.out.println("3. Buat Pesanan");
            System.out.println("4. Hitung Total Pesanan");
            System.out.println("5. Cetak dan Simpan Struk");
            System.out.println("6. Simpan Daftar Menu");
            System.out.println("7. Muat Daftar Menu");
            System.out.println("8. Baca Struk Tersimpan");
            System.out.println("0. Keluar");

            int pilihan = bacaInt("Pilihan: ");
            switch (pilihan) {
                case 1:
                    tambahMenu();
                    break;
                case 2:
                    menu.tampilkanMenu();
                    break;
                case 3:
                    buatPesanan();
                    break;
                case 4:
                    hitungTotal();
                    break;
                case 5:
                    cetakStruk();
                    break;
                case 6:
                    simpanMenu();
                    break;
                case 7:
                    muatMenu();
                    break;
                case 8:
                    bacaStruk();
                    break;
                case 0:
                    System.out.println("Terima kasih. Program selesai.");
                    berjalan = false;
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia.");
            }
        }
        scanner.close();
    }

    private static void siapkanMenuAwal() {
        if (FileManager.menuTersimpan()) {
            try {
                menu = FileManager.muatMenu();
                System.out.println("Daftar menu berhasil dimuat dari data/menu.txt.");
            } catch (IOException e) {
                System.out.println("Gagal memuat menu awal: " + e.getMessage());
                System.out.println("Program dimulai dengan menu kosong");
            }
            return;
        }

        menu.tambahItem(new Makanan("Nasi Goreng", 20000, "Makanan Berat"));
        menu.tambahItem(new Makanan("Mie Goreng", 18000, "Makanan Berat"));
        menu.tambahItem(new Minuman("Es Teh", 5000, "Minuman Dingin"));
        menu.tambahItem(new Minuman("Kopi", 10000, "Minuman Panas"));
        menu.tambahItem(new Diskon("Promo Hemat", 10));
        System.out.println("Menu contoh tersedia. Gunakan pilihan 6 untuk menyimpan");
    }

    private static int bacaInt(String pesan) {
        while (true) {
            System.out.print(pesan);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka bulat.");
            }
        }
    }

    private static double bacaDouble(String pesan) {
        while (true) {
            System.out.println(pesan);
            try {
                double hasil = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                if (!Double.isFinite(hasil)) {
                    throw new NumberFormatException("Bukan bilangan terbatas.");
                }
                return hasil;
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka yang valid.");
            }
        }
    }

    private static String bacaTeks(String pesan) {
        while (true) {
            System.out.println(pesan);
            String teks = scanner.nextLine().trim();
            if (!teks.isEmpty() && !teks.contains("|")) {
                return teks;
            }
            System.out.println("Teks tidak boleh kosong atau mengandung |.");
        }
    }

    private static void tambahMenu() {
        System.out.println("\n1. Tambah Makanan");
        System.out.println("2. Tambah Minuman");
        System.out.println("3. Tambah Diskon");
        System.out.println("0. Kembali");
        int pilihan = bacaInt("Pilihan: ");
        if (pilihan == 0) {
            return;
        }
        if (pilihan < 1 || pilihan > 3) {
            System.out.println("Pilihan tidak tesedia.");
            return;
        }

        String nama = bacaTeks("Nama item: ");
        try {
            if (pilihan == 1) {
                double harga = bacaDouble("Harga makanan: ");
                String jenis = bacaTeks("Jenis makanan: ");
                menu.tambahItem(new Makanan(nama, harga, jenis));
            } else if (pilihan == 2) {
                double harga = bacaDouble("Harga minuman: ");
                String jenis = bacaTeks("Jenis minuman: ");
                menu.tambahItem(new Minuman(nama, harga, jenis));
            } else {
                double persen = bacaDouble("Persentase diskon (1-100): ");
                menu.tambahItem(new Diskon(nama, persen));
            }
            System.out.println("Item berhasil ditambahkan.");
        } catch (IllegalArgumentException e) {
            System.out.println("Gagal menambahkan item: " + e.getMessage());
        }
     }

     private static void buatPesanan() {
         String namaPelanggan = bacaTeks("Nama pelanggan: ");
         Pesanan pesananBaru = new Pesanan(namaPelanggan);
         System.out.println("Pilih nomor makanan/minuman atau diskon. Ketik 0 untuk selesai.");

         while (true) {
             menu.tampilkanMenu();
             int nomor = bacaInt("Nomor item (0 = selesai): ");
             if (nomor == 0) {
                 break;
             }

             try {
                 MenuItem item = menu.getItem(nomor);
                 if (item instanceof Diskon) {
                     pesananBaru.setDiskon((Diskon) item);
                     System.out.println("Diskon diterapkan: " + item.getNama());
                 } else {
                     int jumlah = bacaInt("Jumlah: ");
                     pesananBaru.tambahPesanan(item, jumlah);
                     System.out.println("Pesanan berhasil dicatat.");
                 }
             } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                 System.out.println("Kesalahan: " + e.getMessage());
             }
         }

         if (pesananBaru.getJumlahItem() > 0) {
             pesananAktif = pesananBaru;
             System.out.println("Pesanan disimpan sementara. Jumlah item: " + pesananAktif.getJumlahItem());
         } else {
             System.out.println("Belum ada makanan/minunam. Pesanan tidak dibuat.");
         }
     }

     private static boolean adaPesanan() {
         if (pesananAktif == null || pesananAktif.getJumlahItem() == 0) {
             System.out.println("Belum ada pesanan aktif. Gunakan pilihan 3 dahulu.");
             return false;
         }
         return true;
     }

     private static void hitungTotal() {
         if (!adaPesanan()) {
             return;
         }
         System.out.printf("Subtotal: Rp%,.0f%n", pesananAktif.hitungSubtotal());
         System.out.printf("Potongan: Rp%,.0f%n", pesananAktif.hitungPotongan());
         System.out.printf("Total   : Rp%,.0f%n", pesananAktif.hitungTotal());
     }

     private static void cetakStruk() {
         if (!adaPesanan()) {
             return;
         }
         System.out.println(pesananAktif.buatStruk());
         try {
             Path lokasi = FileManager.simpanStruk(pesananAktif);
             System.out.println("Struk disimpan di: " + lokasi);
         } catch (IOException e) {
             System.out.println("Gagal menyimpan struk: " + e.getMessage());
         }
     }

     private static void simpanMenu() {
         try {
             FileManager.simpanMenu(menu);
             System.out.println("Daftar menu disimpan di data/menu.txt.");
         } catch (IOException e) {
             System.out.println("Gagal menyimpan menu: " + e.getMessage());
         }
     }

     private static void muatMenu() {
         try {
             menu = FileManager.muatMenu();
             System.out.println("Berhasil memuat " + menu.getDaftarMenu().size() + " item.");
             System.out.println("Pesanan aktif yang sudah dibuat tidak berubah.");
         } catch (IOException e) {
             System.out.println("Gagal memuat menu: " + e.getMessage());
         }
     }

     private static void bacaStruk() {
         try {
             List<String> daftar = FileManager.daftarStruk();
             if (daftar.isEmpty()) {
                 System.out.println("Belum ada file struk tersipan.");
             }
             System.out.println("\nDaftar struk tersimpan:");
             for (int i = 0; i < daftar.size(); i++) {
                 System.out.println((i + 1) + ". " + daftar.get(i));
             }
             int nomor = bacaInt("Nomor struk (0 = kembali): ");
             if (nomor == 0) {
                 return;
             }
             if (nomor < 1 || nomor > daftar.size()) {
                 throw new IndexOutOfBoundsException("Nomor struk tidak ada.");
             }
             System.out.println(FileManager.bacaStruk(daftar.get(nomor - 1)));
         } catch (IOException | IndexOutOfBoundsException e) {
             System.out.println("Gagal membaca struk: " + e.getMessage());
         }
     }
}
