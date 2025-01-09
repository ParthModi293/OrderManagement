package com.example.newproj.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {


   private int userId;
  private List<ProductRequest> products;
  private String paymentType;


}
