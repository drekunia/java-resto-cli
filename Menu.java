import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {

    private final ArrayList<MenuItem> daftarMenu = new ArrayList<>();

    public void tambahItem(MenuItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item tidak boleh kosong.");
        }
        daftarMenu.add(item);
    }

    public MenuItem getItem(int nomor) {
        if (nomor < 1 || nomor > daftarMenu.size()) {
            throw new IndexOutOfBoundsException("Nomor menu " + nomor + " tidak ditemukan.");
        }
        return daftarMenu.get(nomor - 1);
    }

    public List<MenuItem> getDaftarMenu() {
        return Collections.unmodifiableList(daftarMenu);
    }

    public void tampilkanMenu() {
        System.out.println("\n-------- MENU RESTORAN --------");
        if (daftarMenu.isEmpty()) {
            System.out.println("Daftar menu masih kosong.");
        }
        for (int i = 0; i < daftarMenu.size(); i++) {
            System.out.printf("%2d. ", i + 1);
            daftarMenu.get(i).tampilMenu();
        }
        System.out.println("------------------------");
    }
}
