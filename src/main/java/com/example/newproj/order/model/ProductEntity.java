package com.example.newproj.order.model;

import lombok.Builder;
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
