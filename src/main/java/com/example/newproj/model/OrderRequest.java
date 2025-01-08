package com.example.newproj.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {


    int userId;
   List<ProductRequest> products;






}
