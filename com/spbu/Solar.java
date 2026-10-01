package com.spbu;

public class Solar extends Product {
    private int cetane;

    public Solar(String nama, double harga, int cetane) {
        super(nama, harga);
        this.cetane = cetane;
    }

    public Solar() {
        this("", 0, 0);
    }

    public int getCetane() {
        return cetane;
    }

    public void setCetane(int cetane) {
        this.cetane = cetane;
    }

}
