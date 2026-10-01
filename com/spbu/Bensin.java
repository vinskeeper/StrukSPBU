package com.spbu;

public class Bensin extends Product {
    private int oktan;

    public Bensin(String nama, double harga, int oktan) {
        super(nama, harga);
        this.oktan = oktan;
    }

    public Bensin() {
        this("", 0, 0);
    }

    public int getOktan() {
        return oktan;
    }

    public void setOktan(int oktan) {
        this.oktan = oktan;
    }

}
