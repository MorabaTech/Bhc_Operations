package com.example.entities;

import lombok.Data;

import java.util.List;

@Data
public class RequestsBody {
   List<Bale> bales;

  List<Rebate> rebates;

   List<Debt> debts;
}
