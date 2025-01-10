package com.example.newproj.order.model;

import lombok.Data;

@Data
public class ProductDto {



    private int productId;
    private int price;
    private String productName;
    private String sellerName;
}
