package com.spbu;

public class Debit extends MetodePembayaran {
    private String bank;

    public Debit(double amount, String bank) {
        super(amount);
        this.bank = bank;
    }

    public Debit() {
        this(0, "Bank");
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Override
    public String getRincianPembayaran() {
        return "EDC " + bank;
    }
}
