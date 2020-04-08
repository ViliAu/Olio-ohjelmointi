package com.example.bottledispenser;

import java.util.ArrayList;

public class Bottle {

    private String name = "Pepsi Max";
    private String manufacturer = "Pepsi";
    private double total_energy = 0.3d;
    final public float size;
    final public float price;

    public Bottle() {
        size = 0.5f;
        price = 1.80f;
    }
    public Bottle(String n, String manuf, double totE, float size, float price) {
        name = n;
        manufacturer = manuf;
        total_energy = totE;
        this.size = size;
        this.price = price;
    }

    public String getName(){
        return name;
    }
    public String getManufacturer(){
        return manufacturer;
    }
    public double getEnergy(){
        return total_energy;
    }

    @Override
    public String toString() {
        String spinnerText = "";
        spinnerText = String.format("%s - %.2fl - %.2f€", name, size, price);
        return spinnerText;
    }

}