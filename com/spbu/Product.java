package com.spbu;

public class Product {
    private String nama;
    private double harga;

    public Product(String nama, double harga) {
        this.nama = nama;
        this.harga = harga;
    }

    public Product() {
        this("", 0);
    }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public double hitungHarga(double quantity) {
        return harga * quantity;
    }
}
