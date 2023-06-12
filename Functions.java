package com.example.services;


import com.example.entities.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;
import java.math.RoundingMode;


@org.springframework.stereotype.Service
@Slf4j
public class Service {

    /*..............................................PART A.........................................................*/

    /* The first part of this class contains the PART A of the Assessment
    *
    * All the functions are there as requested
    *
    * Some functions are there as helper functions and there are comments for each and every function on what it does
    *
    *  */


    // Question 1
    public  double calculateGross(List<Bale> bales) {
        double gross = 0.0;
        for (Bale bale : bales) {
            gross += bale.getMass() * bale.getPrice();
        }
        return gross;
    }

    // Question 2
    public double calculateNetAfterTaxes(List<Bale> bales, double gross) {
        double tax1 = gross * 0.003;
        double tax2 = gross * 0.015 + bales.stream().mapToDouble(bale -> bale.getMass() * 0.02).sum();
        double tax3 = bales.size() * 5.0;
        double totalTax = tax1 + tax2 + tax3;
        double net = gross - totalTax;
        return net;
    }


    // Question 3
    public double debtProcessor(Debt debt , double gross)
    {
        double totalDebt = debt.getAmount() +  debt.getAmount() * debt.getInterestRate();

        double commission =  totalDebt * 0.05;

        return  gross - (totalDebt + commission);
    }


    // Question 4
    public Double [] debtListProcessor(List<Debt> debtItems , double gross)
    {
        double commission = 0.00;

        double remainingGross = gross;

        Comparator<Debt> priorityComparator = Comparator.comparingInt(Debt::getPriority);
        // Sort the list
        Collections.sort(debtItems, priorityComparator);

        for(Debt debt : debtItems)
        {
            double net = this.debtProcessor(debt , remainingGross);
            double totalDebt = debt.getAmount() + (debt.getAmount() * debt.getInterestRate());
            double debtCommission = totalDebt * 0.005;
            commission = commission + debtCommission;
            remainingGross = remainingGross -  (totalDebt + debtCommission);
        }

        Double [] result = {commission, remainingGross};
        return result;
    }

    // Question 5
    public  double ApplyRebate(List<Bale> bales ,  Rebate rebate , double gross) {

        double rebateAmount = 0.00;

        if (rebate.getType().equals("REBATE_1")) {

          rebateAmount = rebateOne(bales);

        } else if (rebate.getType().equals("REBATE_2")) {

            rebateAmount = rebateTwo(bales);

        } else if (rebate.getType().equals("BOTH")) {

           rebateAmount = rebateOne(bales) + rebateTwo(bales);

        }

        return gross + rebateAmount;

    }


    //This is a helper function
    private double rebateOne(List<Bale> bales)
    {
        double totalMass = 0 ;
        for(Bale bale : bales)
        {
            totalMass += bale.getMass();
        }

        return totalMass * 0.05;
    }

    //This one is also a helper function
    private double rebateTwo(List<Bale> bales)
    {
        return  10 + 0.02 * calculateGross(bales);
    }


    // QUESTION 6
    public  Map<String, Double> processSale(List<Bale> bales, List<Debt> debts, List<Rebate> rebates) {

        //Calculate Gross using the already existing function
        double gross = calculateGross(bales);
        System.out.println("Gross value: $" + String.format("%.2f", gross));
        //Get the Net after Taxes here use the function 2
        double netAfterTaxes = calculateNetAfterTaxes(bales, gross);
        System.out.println("Net after Taxes: $" + String.format("%.2f", netAfterTaxes));

        Double[] netAfterDebts = debtListProcessor(debts,netAfterTaxes);

        System.out.println("Net after Debt Deductions: $" + String.format("%.2f", netAfterDebts[1] ));

        System.out.println("Commission on debts: $" + String.format("%.2f", netAfterDebts[0]));


        for( Rebate rebate : rebates)
        {
            netAfterDebts[1] +=  ApplyRebate(bales , rebate , gross);
        }

        System.out.println("Net proceeds due to grower: $" + String.format("%.2f",  netAfterDebts[1]));

        Map<String, Double> result = new HashMap<>();
        result.put("netProceeds",  netAfterDebts[1]);
        result.put("commissionTotal",  netAfterDebts[0]);
        return result;
    }

    public static List<Bale> reworks(List<Bale> bales) {
        List<Bale> mergedBales = new ArrayList<>();
        List<Bale> rejectedBales = new ArrayList<>();
        BigDecimal totalGross = BigDecimal.ZERO;
        BigDecimal totalMass = BigDecimal.ZERO;
        int numMerged = 0;
        for (int i = 0; i < bales.size(); i++) {
            Bale bale1 = bales.get(i);
            if (mergedBales.contains(bale1) || rejectedBales.contains(bale1)) {
                continue;
            }
            for (int j = i + 1; j < bales.size(); j++) {
                Bale bale2 = bales.get(j);
                if (mergedBales.contains(bale2) || rejectedBales.contains(bale2)) {
                    continue;
                }
                if (bale1.getGrade().equals(bale2.getGrade())) {
                    double totalMassCheck = bale1.getMass() + bale2.getMass();
                    if (totalMassCheck  <= 120) {
                        BigDecimal mergedGross = BigDecimal.valueOf((bale1.getMass() * bale1.getPrice())
                                + (bale2.getMass() * (bale2.getPrice())));
                        BigDecimal mergedMass = BigDecimal.valueOf(bale1.getMass() + (bale2.getMass()));
                        BigDecimal mergedPrice = mergedGross.divide(mergedMass, 2, RoundingMode.HALF_UP);
                        Bale mergedBale = new Bale(11222223 ,mergedMass, mergedPrice, bale1.getGrade(), "M" + (++numMerged));
                        mergedBales.add(bale1);
                        mergedBales.add(bale2);
                        mergedBales.add(mergedBale);
                        totalGross = totalMass.add(mergedGross);
                        totalMass = totalMass.add(mergedMass);
                        break;
                    } else {
                        rejectedBales.add(bale1);
                        rejectedBales.add(bale2);
                    }
                } else {
                    rejectedBales.add(bale1);
                    rejectedBales.add(bale2);
                }
            }
        }
        BigDecimal newAveragePrice = totalGross.divide(totalMass, 2, RoundingMode.HALF_UP);
        System.out.println("Rejected bales:");
        for (Bale bale : rejectedBales) {
            System.out.println(bale);
        }
        System.out.println("New average price for merged bales: " + newAveragePrice);
        return mergedBales;
    }


    /*..............................................PART B..........................................................*/

    /* The second part of this class contains the PART B of the Assessment
     *
     * All the functions are there as requested
     *
     * There are comments for each and every function on what it does
     *
     *  */


   // Question 4 A
    public List<Bale> selectBale(List<Bale> bales, double minPrice, int numBales, String excludedGrade) {
        List<Bale> qualifiedBales = new ArrayList<>();
        int count = 0;

        for (Bale bale : bales) {
            if (bale.getPrice() >= minPrice && !bale.getGrade().contains(excludedGrade)) {
                qualifiedBales.add(bale);
                count++;

                if (count == numBales) {
                    break;
                }
            }
        }

        return qualifiedBales;
    }


    // Question 4 B
    public Totals getTotals(List<Bale> selectedBales) {
        int numBales = selectedBales.size();
        double totalMass = 0.0;
        double totalPrice = 0.0;
        double totalGross = 0.0;

        for (Bale bale : selectedBales) {
            totalMass += bale.getMass();
            totalPrice += bale.getPrice();
            totalGross += bale.getMass() * bale.getPrice();
        }

        double avgPrice = totalPrice / numBales;

        return new Totals(numBales, totalMass, avgPrice, totalGross);
    }

    // Question 4 C
    public void runProcess(List<Bale> bales, double minPrice, int numBales) {
        List<Bale> selectedBales = selectBale(bales, minPrice, numBales, "TMOX");
        Totals totals = getTotals(selectedBales);

        System.out.println("Selected bales:");
        for (Bale bale : selectedBales) {
            System.out.println("- " + bale.toString());
        }

        System.out.println("\nTotals:");
        System.out.println("- Number of bales: " + totals.getNumBales());
        System.out.println("- Total mass: " + totals.getTotalMass());
        System.out.println("- Average price: " + totals.getAvgPrice());
        System.out.println("- Total gross: " + totals.getTotalGross());
    }

    public Map<String, Double> processSales(RequestsBody requestBody) {

        return  this.processSale(requestBody.getBales() , requestBody.getDebts() , requestBody.getRebates());
    }
}
