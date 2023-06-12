package com.example.apis;

import com.example.entities.Bale;
import com.example.entities.Debt;
import com.example.entities.RequestsBody;
import com.example.services.Service;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@NoArgsConstructor
public class Controller {

    @Autowired
    private Service appService;

    @PostMapping("calculate_gross")
    double calculateGross(@RequestBody List<Bale> bales)
    {
        return this.appService.calculateGross(bales);
    }

    @PostMapping("process_taxes")
    Double calculateTaxes(@RequestBody List<Bale> bales , @PathVariable double gross)
    {
        return this.appService.calculateNetAfterTaxes(bales, gross);
    }

    @PostMapping("process_debts/{gross}")
    Double[] processDebts(@RequestBody List<Debt> debts , @PathVariable Double gross)
    {
        return this.appService.debtListProcessor(debts, gross);
    }

    @PostMapping("process_sale")
    Map<String, Double> processSale(@RequestBody RequestsBody requestBody)
    {
        return this.appService.processSales(requestBody);
    }

}
