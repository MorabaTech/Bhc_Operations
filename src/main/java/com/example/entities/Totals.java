package com.example.entities;

import lombok.Data;

@Data
public class Totals {
    private int numBales;
    private double totalMass;
    private double avgPrice;
    private double totalGross;

    public Totals(int numBales, double totalMass, double avgPrice, double totalGross) {
        this.numBales = numBales;
        this.totalMass = totalMass;
        this.avgPrice = avgPrice;
        this.totalGross = totalGross;
    }
}
