package com.spbu;

public abstract class MetodePembayaran {
    private double amount;

    public MetodePembayaran(double amount) {
        this.amount = amount;
    }

    public MetodePembayaran() {
        this(0);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public abstract String getRincianPembayaran();
}
