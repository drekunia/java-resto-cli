public class Diskon extends MenuItem {

    private final double persentase;

    public Diskon(String nama, double persentase) {
        super(nama, 0, "Diskon");
        if (!Double.isFinite(persentase) || persentase <= 0 || persentase > 100) {
            throw new IllegalArgumentException("Diskon harus lebih dari 0 sampai 100 persen.");
        }
        this.persentase = persentase;
    }

    public double getPersentase() {
        return persentase;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("%-20s | %-10s | %-12s | Potongan %.0f%%%n", getNama(), getKategori(), "-", persentase);
    }
}
