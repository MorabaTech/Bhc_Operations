package com.example.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bale {

    private Integer grower;;

    private Integer mass ;

    private double price ;

    private  String grade ;

    private Integer barcode ;

    public Bale(int grower, BigDecimal mergedMass, BigDecimal mergedPrice, String grade, String s) {
    }
}
