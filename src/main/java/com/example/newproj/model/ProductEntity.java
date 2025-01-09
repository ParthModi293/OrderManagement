package com.example.newproj.model;

import lombok.Data;

@Data

public class ProductEntity {
    private int productId;
    private int quantity;
    private int price;
    private String productName;
    private String sellerName;
    private int sellerId;

}