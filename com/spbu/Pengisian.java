package com.spbu;

import java.util.ArrayList;

public class Pengisian implements CetakStruk {
    private String invoiceNumber;
    private String tanggal;
    private String attendant;
    private String nomorKendaraan;
    private Product product;
    private ArrayList<Product> daftarProduk;
    private double quantity;
    private double nominal;
    private double total;
    private MetodePembayaran metode;

    public Pengisian(String invoiceNumber, String tanggal, String attendant,
            String nomorKendaraan, Product product, double quantity,
            double nominal, MetodePembayaran metode) {
        this.invoiceNumber = invoiceNumber;
        this.tanggal = tanggal;
        this.attendant = attendant;
        this.nomorKendaraan = nomorKendaraan;
        this.product = product;
        this.quantity = quantity;
        this.nominal = nominal;
        this.metode = metode;
        this.daftarProduk = new ArrayList<>();
        hitungTotal();
    }

    public Pengisian() {
        this("", "", "", "", null, 0, 0, null);
    }

    public double hitungTotal() {
        total = product == null ? 0 : product.hitungHarga(quantity);
        if (metode != null) {
            metode.setAmount(total);
        }
        return total;
    }

    @Override
    public void cetak() {
        String garis = "----------------------------------------------------------------------";
        System.out.println("                       SPBU");
        System.out.println("                 Salinan Pelanggan");
        System.out.println(garis);
        System.out.println("Invoice No     : " + invoiceNumber);
        System.out.println("Date           : " + tanggal);
        System.out.println("Attendant      : " + attendant);
        System.out.println("No. Kendaraan  : " + nomorKendaraan);
        System.out.println(garis);
        System.out.printf("%-29s %12s %12s %14s%n", "Product", "Qty", "Price", "Amount");
        System.out.printf("%-29s %12s %12s %14s%n",
                (product == null ? "-" : product.getNama()), quantity + " L",
                "Rp " + Math.round(product == null ? 0 : product.getHarga()),
                "Rp " + Math.round(total));
        System.out.println(garis);
        System.out.printf("%-55s %14s%n", "Total", "Rp " + Math.round(total));

        if (metode == null) {
            System.out.println("Metode pembayaran belum dipilih.");
        } else if (metode instanceof Tunai) {
            Tunai tunai = (Tunai) metode;
            System.out.printf("%-55s %14s%n", metode.getRincianPembayaran(),
                    "Rp " + Math.round(metode.getAmount()));
            System.out.printf("%-55s %14s%n", "Uang diterima",
                    "Rp " + Math.round(tunai.getUangDiterima()));
            System.out.printf("%-55s %14s%n", "Kembalian",
                    "Rp " + Math.round(tunai.getUangDiterima() - total));
        } else {
            System.out.printf("%-55s %14s%n", metode.getRincianPembayaran(),
                    "Rp " + Math.round(metode.getAmount()));
        }

        System.out.println(garis);
        System.out.println("              Terima kasih telah berkunjung!");
        System.out.println("============================================================");
    }

    public ArrayList<Product> getDaftarProduk() { return daftarProduk; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public String getAttendant() { return attendant; }
    public void setAttendant(String attendant) { this.attendant = attendant; }
    public String getNomorKendaraan() { return nomorKendaraan; }
    public void setNomorKendaraan(String nomorKendaraan) { this.nomorKendaraan = nomorKendaraan; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public double getNominal() { return nominal; }
    public void setNominal(double nominal) { this.nominal = nominal; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public MetodePembayaran getMetode() { return metode; }
    public void setMetode(MetodePembayaran metode) { this.metode = metode; }
}
