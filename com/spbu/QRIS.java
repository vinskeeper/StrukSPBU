package com.spbu;

public class QRIS extends MetodePembayaran {
    private String provider;

    public QRIS(double amount, String provider) {
        super(amount);
        this.provider = provider;
    }

    public QRIS() {
        this(0, "QRIS");
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String getRincianPembayaran() {
        return "QRIS " + provider;
    }
}
