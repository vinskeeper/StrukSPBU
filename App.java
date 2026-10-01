import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import com.spbu.Bensin;
import com.spbu.Debit;
import com.spbu.MetodePembayaran;
import com.spbu.Pengisian;
import com.spbu.Product;
import com.spbu.QRIS;
import com.spbu.Solar;
import com.spbu.Tunai;

public class App {
    public static void main(String[] args) {
        ArrayList<Product> daftarProduk;
        try {
            daftarProduk = bacaProduk("data/produk.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File data/produk.txt tidak ditemukan.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Produk gagal dibaca: " + e.getMessage());
            return;
        }

        if (daftarProduk.isEmpty()) {
            System.out.println("Belum ada produk di data/produk.txt.");
            return;
        }

        Scanner input = new Scanner(System.in);
            System.out.print("Invoice number: ");
            String invoiceNumber = input.nextLine();
            System.out.print("Tanggal: ");
            String tanggal = input.nextLine();
            System.out.print("Attendant: ");
            String attendant = input.nextLine();
            System.out.print("Nomor kendaraan: ");
            String nomorKendaraan = input.nextLine();

            System.out.println("Pilih Produk:");
            for (int i = 0; i < daftarProduk.size(); i++) {
                Product product = daftarProduk.get(i);
                System.out.print((i + 1) + ". " + product.getNama()
                        + " - Rp " + (long) product.getHarga());
                if (product instanceof Bensin) {
                    System.out.print(" (Oktan " + ((Bensin) product).getOktan() + ")");
                } else if (product instanceof Solar) {
                    System.out.print(" (Cetane " + ((Solar) product).getCetane() + ")");
                }
                System.out.println();
            }

            System.out.print("Pilihan produk: ");
            if (!input.hasNextInt()) {
                System.out.println("Pilihan harus berupa angka.");
                return;
            }
            int pilihanProduk = input.nextInt();
            input.nextLine();
            if (pilihanProduk < 1 || pilihanProduk > daftarProduk.size()) {
                System.out.println("Pilihan produk tidak tersedia.");
                return;
            }
            Product product = daftarProduk.get(pilihanProduk - 1);

            System.out.print("Jumlah liter: ");
            if (!input.hasNextDouble()) {
                System.out.println("Jumlah liter harus berupa angka.");
                return;
            }
            double quantity = input.nextDouble();
            input.nextLine();
            if (quantity <= 0) {
                System.out.println("Jumlah liter harus lebih dari 0.");
                return;
            }
            double total = product.hitungHarga(quantity);

            System.out.println("Pilih Metode Pembayaran:");
            System.out.println("1. Tunai");
            System.out.println("2. QRIS");
            System.out.println("3. Debit");
            System.out.print("Pilihan metode: ");
            if (!input.hasNextInt()) {
                System.out.println("Pilihan harus berupa angka.");
                return;
            }
            int pilihanMetode = input.nextInt();
            input.nextLine();

            double nominal = total;
            MetodePembayaran metode;
            if (pilihanMetode == 1) {
                System.out.print("Uang diterima: Rp ");
                if (!input.hasNextDouble()) {
                    System.out.println("Nominal harus berupa angka.");
                    return;
                }
                nominal = input.nextDouble();
                input.nextLine();
                if (nominal < total) {
                    System.out.println("Uang yang diterima kurang.");
                    return;
                }
                metode = new Tunai(total, nominal);
            } else if (pilihanMetode == 2) {
                System.out.print("Provider QRIS: ");
                String provider = input.nextLine();
                metode = new QRIS(total, provider);
            } else if (pilihanMetode == 3) {
                System.out.print("Bank: ");
                String bank = input.nextLine();
                metode = new Debit(total, bank);
            } else {
                System.out.println("Metode pembayaran tidak tersedia.");
                return;
            }

            Pengisian pengisian = new Pengisian(invoiceNumber, tanggal,
                    attendant, nomorKendaraan, product, quantity, nominal, metode);
            pengisian.getDaftarProduk().addAll(daftarProduk);
            pengisian.cetak();

            try {
                simpanPengisian(pengisian);
                System.out.println("Transaksi disimpan ke data/data_struk.txt");
            } catch (IOException e) {
                System.out.println("Transaksi gagal disimpan: " + e.getMessage());
            }
        input.close();
    }

    private static ArrayList<Product> bacaProduk(String namaFile) throws FileNotFoundException {
        ArrayList<Product> daftarProduk = new ArrayList<Product>();
        int nomorBaris = 0;
        Scanner fileReader = new Scanner(new File(namaFile));

        while (fileReader.hasNextLine()) {
            String baris = fileReader.nextLine();
            nomorBaris++;
            if (baris.isEmpty()) {
                continue;
            }

            String[] data = baris.split(",");
            if (data.length != 4) {
                throw new IllegalArgumentException("Format baris " + nomorBaris
                        + " harus: nama,harga,BENSIN/SOLAR,nilai");
            }

            String nama = data[0];
            double harga;
            int nilai;
            try {
                harga = Double.parseDouble(data[1]);
                nilai = Integer.parseInt(data[3]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Harga/nilai tidak valid pada baris "
                        + nomorBaris);
            }
            if (nama.isEmpty() || harga < 0) {
                throw new IllegalArgumentException("Nama atau harga tidak valid pada baris "
                        + nomorBaris);
            }

            String jenis = data[2].toUpperCase();
            if (jenis.equals("BENSIN")) {
                daftarProduk.add(new Bensin(nama, harga, nilai));
            } else if (jenis.equals("SOLAR")) {
                daftarProduk.add(new Solar(nama, harga, nilai));
            } else {
                throw new IllegalArgumentException("Jenis produk tidak dikenal pada baris "
                        + nomorBaris + ": " + jenis);
            }
        }

        fileReader.close();
        return daftarProduk;
    }

    private static void simpanPengisian(Pengisian pengisian) throws IOException {
        String detailPembayaran = "";
        MetodePembayaran metode = pengisian.getMetode();
        if (metode instanceof Tunai) {
            detailPembayaran = String.valueOf(((Tunai) metode).getUangDiterima());
        } else if (metode instanceof QRIS) {
            detailPembayaran = ((QRIS) metode).getProvider();
        } else if (metode instanceof Debit) {
            detailPembayaran = ((Debit) metode).getBank();
        }

        Product product = pengisian.getProduct();
        String baris = pengisian.getInvoiceNumber() + "|"
                + pengisian.getTanggal() + "|"
                + pengisian.getAttendant() + "|"
                + pengisian.getNomorKendaraan() + "|"
                + product.getNama() + "|"
                + pengisian.getQuantity() + "|"
                + product.getHarga() + "|"
                + metode.getAmount() + "|"
                + metode.getClass().getSimpleName() + "|"
                + detailPembayaran + "\n";

        FileWriter fileWriter = new FileWriter("data/data_struk.txt", true);
        fileWriter.write(baris);
        fileWriter.close();
    }

}
