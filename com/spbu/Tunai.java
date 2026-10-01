package com.spbu;

public class Tunai extends MetodePembayaran {
    private double uangDiterima;

    public Tunai(double amount, double uangDiterima) {
        super(amount);
        this.uangDiterima = uangDiterima;
    }

    public Tunai() {
        this(0, 0);
    }

    public double getUangDiterima() {
        return uangDiterima;
    }

    public void setUangDiterima(double uangDiterima) {
        this.uangDiterima = uangDiterima;
    }

    @Override
    public String getRincianPembayaran() {
        return "TUNAI";
    }
}
